package com.androidactivities;

import interfaces.DataCall;
import interfaces.IDataProcessor;
import interfaces.NFCCompleted;
import interfaces.OnTaskCompleted;

import java.io.IOException;
import java.net.Socket;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.bluetoothmodulev4.R;


import json.JsonBuilder;

import singleton.ListenerHolder;
import singleton.LoginData;
import singleton.SessionData;

import nfc.NdefReader;

import bluetoothcomms.BluetoothListen;
import bluetoothcomms.BluetoothWrite;
import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.tech.Ndef;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChallengeActivity extends Activity implements
		CreateNdefMessageCallback, IDataProcessor {

	public static final String MIME_TEXT_PLAIN = "text/plain";
	private final static String TAG = "CHALLENGEACTIVITY";
	private Long mLastPausedMillis = 0L;
	private BluetoothSocket socket;
	private BluetoothServerSocket serveSock;
	private BluetoothListen listenTask;
	private BluetoothWrite writer;
	private BluetoothDevice device;
	private BluetoothAdapter adapter;
	private ListenerHolder holder;

	// NFC
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private IntentFilter[] mNdefExchangeFilters;
	
	private AlertDialog builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incoming_challenge);
		setTitle("Not logged in");

		holder.setActivity(this);
		holder.setContext(getApplicationContext());
		
		
		
		
		adapter = BluetoothAdapter.getDefaultAdapter();
		//device = adapter.getRemoteDevice("10:68:3F:51:7A:0E");
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		nfcHandler();
		overridePendingTransition(anim.fade_in, anim.fade_out);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	
	private void profileAct() {

		if (LoginData.getUsername().compareTo("") == 0) {
			// not possible
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ChallengeActivity.this);
			builder.setMessage("You must be logged in to use this feature")
					.setPositiveButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			builder.create().show();
		} else {
			Intent i = new Intent(this, Profile.class);
			startActivity(i);
		}

	}
	
	private void ipConfDialog()
	{
		
	    // Get the layout inflater
		final View layout = View.inflate(this, R.layout.ipdialog, null);
	    final EditText ipAddr = (EditText)layout.findViewById(R.id.ipAddr);
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(0);
		
		builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SessionData.setWebServiceIp(ipAddr.getText().toString().trim());
			}
		});
		builder.setView(layout);
	    builder.create().show();
	    
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			//dialog
			ipConfDialog();
			return true;
		case R.id.action_exit:
			//quit dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(ChallengeActivity.this);
	        builder.setMessage("Are you sure you want to Logout?")
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       
	                	  
	                	   LoginData.setUsername("");
	                	   LoginData.setFirstname("");
	                	   LoginData.setLastname("");
	                	   LoginData.setExp(0);
	                	   LoginData.setLosses(0);
	                	   LoginData.setWins(0);
	                	   LoginData.setRank("");
	                	   LoginData.setEmailAddress("");
	                	   Intent i = new Intent(getApplicationContext(),MainActivity.class);
	                	   startActivity(i);
	                	   
	                   }
	               });
	        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       
	                	   
	                	   
	                   }
	               });
	        // Create the AlertDialog object and return it
	        builder.create().show();
			
			return true;
		case R.id.action_profile:
			//bring up profile
			profileAct();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	/**
	 * Options action bar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_profile).setVisible(false);
		menu.findItem(R.id.action_settings).setVisible(false);
		return true;
	}
	
	private void nfcHandler() {
		mNfcAdapter.setNdefPushMessageCallback(this, this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		// intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);

		// onResume gets called after this to handle the intent
		holder.setActivity(this);
		holder.setContext(getApplicationContext());
		setIntent(intent);
		// above would be called on the new device
		// if the intent !=

	}
	

	
	

	/**
	 * Processes the NDEF message
	 * 
	 * @param intent
	 */
	private void processIntent(Intent intent) {

		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		// record 0 contains the MIME type, record 1 is the AAR, if present
		Toast.makeText(this, new String(msg.getRecords()[0].getPayload()),
				Toast.LENGTH_SHORT).show();

	}

	private void handleIntent(Intent intent) {
		NdefReader reader = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

				NFCCompleted a = null;
				reader = new NdefReader(getApplicationContext(),a);
				reader.execute(tag);
			} else {
				Log.d(TAG, "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();
			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					NFCCompleted a = null;
					reader = new NdefReader(getApplicationContext(),a);
					reader.execute(tag);
					break;
				}
			}
		}
	}

	public static void stopForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		overridePendingTransition(anim.fade_in, anim.fade_out);
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
			// message bit
		}
		
		if(LoginData.getUsername().compareTo("")==0)
		{
			setTitle("Guest");
		}else
		{
			setTitle(LoginData.getUsername());
		}
	}

	@Override
	protected void onPause() {
		stopForegroundDispatch(this, mNfcAdapter);
		super.onPause();
	}



	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		
		String challengerID = "";
		
		if(LoginData.getUsername().compareTo("")==0)
		{
			LoginData.setUsername("Guest");
		}
		
		JSONObject p2pMessage = new JSONObject();
		
		try{
		
		p2pMessage.put("MACADDRESS", adapter.getAddress().toString());
		p2pMessage.put("USERNAME", LoginData.getUsername());
		} catch(JSONException e)
		{
			
		}
		//String text = ("" + adapter.getAddress().toString()); //local mac address!
		//String text = JsonBuilder.createGame(adapter.getAddress().toString());
		// creates the message for NFC
		NdefRecord[] records = { new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(), new byte[0], p2pMessage.toString().getBytes()) };

		NdefMessage msg = new NdefMessage(records);

		SessionData.setFirstTurn(true);
		
		return msg;
	}
	
	//timer
	

	@Override
	public void processStringData(String data, Context cont) {
		// TODO Auto-generated method stub
		
		//parse
		//if json type mac address do the else
		//if json type is GAMEACK
		
		Log.d("DATA VAR: ", "VALUE:"+data);
		if(data.compareTo("")==0)
		{
			//test for success or not
			
		}else
		{
		//move into nested if
			if(JsonBuilder.parseGame(data).compareTo("1")==0)
			{
				device = adapter.getRemoteDevice(JsonBuilder.getMacAddress(data));
				SessionData.setMacAddress(device.getAddress());
				BluetoothWrite write = new BluetoothWrite("", device);
				write.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						"hello");
				
				
				builder = new AlertDialog.Builder(ChallengeActivity.this).create();
				builder.setTitle("Waiting for opponent");  
				builder.setMessage("00:10");
				builder.setCancelable(false);
				builder.show();
				
				
				CountDownTimer start = new CountDownTimer(30000, 1000) {
				    @Override
				    public void onTick(long millisUntilFinished) {
				    	builder.setMessage(""+ (millisUntilFinished/1000));
				    }

				    @Override
				    public void onFinish() {
				        builder.dismiss();
				    }
				}.start();
			} else if(JsonBuilder.parseGame(data).compareTo("2")==0)
			{
				Intent characterSelect = new Intent(getApplicationContext(),
						CharacterSelectActivity.class);
				startActivity(characterSelect);
			}
			//SessionData.setMacAddress(JsonBuilder.parseGameAck(data));
			

		
		}
		//wait window now
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(anim.fade_in, anim.fade_out);
	}

}
