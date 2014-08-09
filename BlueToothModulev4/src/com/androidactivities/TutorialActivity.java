package com.androidactivities;

import java.util.concurrent.Executor;

import com.example.bluetoothmodulev4.R;

import bluetoothcomms.BluetoothWrite;
import interfaces.IDataProcessor;
import singleton.ListenerHolder;
import singleton.LoginData;
import singleton.SessionData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class TutorialActivity extends Activity implements IDataProcessor {

	private ListenerHolder holder;
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private IntentFilter[] mNdefExchangeFilters;

	// bluetooth testing
	private TextView textV;
	private Button send;
	private BluetoothAdapter mBluetoothAdapter;
	private String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_layout);

		textV = (TextView) findViewById(R.id.textView1);
		send = (Button) findViewById(R.id.sendButton);

		holder.setActivity(this);
		holder.setContext(getApplicationContext());

		// mNfcAdapter.disableForegroundDispatch(this.getParent());
//		initNfc();
//		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		// testing
//		send.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				BluetoothDevice device;
//				device = mBluetoothAdapter.getRemoteDevice(SessionData
//						.getMacAddress());
//
//				BluetoothWrite writer = new BluetoothWrite(textV.getText()
//						.toString(), device);
//				writer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//			}
//		});
	}

//	@Override
//	public void onBackPressed() {
//
//		super.onBackPressed();
//
//		Intent i = new Intent(this, MainActivity.class);
//		startActivity(i);
//	}

	private void initNfc() {
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Intent filters for exchanging over p2p.
		IntentFilter ndefDetected = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefDetected.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) {
		}
		mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		if (LoginData.getUsername().compareTo("") == 0) {
//			setTitle("Guest");
//		} else {
//			setTitle(LoginData.getUsername());
//		}
//
//		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
//				mNdefExchangeFilters, null);
	}

	@Override
	public void processStringData(String data, Context cont) {
		// TODO Auto-generated method stub

//		this.data = data;
//		this.data = this.data + "ABC";
//		textV.setText(this.data);

		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// TutorialActivity.this);
		// builder.setMessage("XYZ has challenged you to a battle!").setTitle(
		// "Incoming match");
		// builder.setPositiveButton("Accept",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// // TODO Auto-generated method stub
		// Intent charSelect = new Intent(getApplicationContext(),
		// CharacterSelect.class);
		// startActivity(charSelect);
		// // also signal to other end that you have accepted
		// }
		// });
		// builder.setNegativeButton("Decline",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// dialog.cancel();
		//
		// }
		// });
		// AlertDialog dialog = builder.create();
		// dialog.show();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

//		processIntent(intent);

	}

	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

		NdefMessage msg = (NdefMessage) rawMsgs[0];
		// record 0 contains the MIME type, record 1 is the AAR, if present
		Toast.makeText(this, new String(msg.getRecords()[0].getPayload()),
				Toast.LENGTH_SHORT).show();
	}

}
