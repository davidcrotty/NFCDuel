package com.androidactivities;

import interfaces.CustomListEvents;
import interfaces.ErrorHandler;
import interfaces.IDataProcessor;
import interfaces.OnTaskCompleted;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import json.JsonBuilder;
import networkcomms.WebRequester;
import singleton.ListenerHolder;
import singleton.LoginData;
import singleton.SessionData;
import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import bluetoothcomms.BluetoothWrite;
import android.support.*;
import android.support.v4.app.NavUtils;

import com.example.bluetoothmodulev4.R;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends Activity implements IDataProcessor,
		CustomListEvents, OnTaskCompleted, ErrorHandler {

	private static final String PREF = "pref";
	private OnTaskCompleted listener;
	private BluetoothAdapter mBluetoothAdapter;
	private ListenerHolder holder;
	private BluetoothWrite writer;
	private ImageView banner;
	private ListView loginList;
	private CustomListEvents event;
	private SharedPreferences settings;
	private ErrorHandler errorCallback;

	// loginbox
	private LinearLayout loginBox;
	private Button confirmButton;
	private EditText usernameField, passwordField;

	private Crouton alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);

		event = (CustomListEvents) this;
		listener = this;
		errorCallback = this;

		banner = (ImageView) findViewById(R.id.loginView);
		loginBox = (LinearLayout) findViewById(R.id.loginTable);
		confirmButton = (Button) findViewById(R.id.confirmButton);
		usernameField = (EditText) findViewById(R.id.usernameField);
		passwordField = (EditText) findViewById(R.id.passwordField);
		// reference xml layout

		holder.setActivity(this);
		holder.setContext(getApplicationContext());

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		animator();
		enableBluetooth();
		setPreferences();

		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (usernameField.length() < 5) {
					Crouton.showText(LoginActivity.this,
							"Username must be > 4", Style.ALERT);

				}
				if (passwordField.length() < 5) {
					Crouton.showText(LoginActivity.this,
							"Password must be > 4", Style.ALERT);
				}

				if (passwordField.length() > 4 && usernameField.length() > 4) {
					// if here to set default sessiondata
					if (SessionData.getWebServiceIp().compareTo("") == 0) {
						SessionData.setWebServiceIp("http://192.168.43.76");
					}

					WebRequester webReq = new WebRequester(null, "", listener,
							errorCallback, 0, LoginActivity.this); // 0
																	// indicates
																	// we're
																	// making a
																	// check on
																	// username/pwd
					URI myUri = URI.create("http://"
							+ SessionData.getWebServiceIp()
							+ ":20000/Home/login?username="
							+ usernameField.getText().toString().trim()
							+ "&password="
							+ passwordField.getText().toString().trim());

					webReq.execute(myUri);
				}
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			// dialog
			ipConfDialog();
			return true;
		case R.id.action_exit:
			// quit dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);
			builder.setMessage("Are you sure you want to logout?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									LoginData.setUsername("");
									LoginData.setFirstname("");
									LoginData.setLastname("");
									LoginData.setExp(0);
									LoginData.setLosses(0);
									LoginData.setWins(0);
									LoginData.setRank("");
									LoginData.setEmailAddress("");
									onResume();

								}
							});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

						}
					});
			// Create the AlertDialog object and return it
			builder.create().show();

			return true;
		case R.id.action_profile:
			// bring up profile
			profileAct();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void ipConfDialog() {

		// Get the layout inflater
		final View layout = View.inflate(this, R.layout.ipdialog, null);
		final EditText ipAddr = (EditText) layout.findViewById(R.id.ipAddr);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(0);

		builder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SessionData.setWebServiceIp(ipAddr.getText().toString()
								.trim());
					}
				});
		builder.setView(layout);
		builder.create().show();

	}

	private void profileAct() {

		if (LoginData.getUsername().compareTo("") == 0) {
			// not possible
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);
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

	private void setPreferences() {
		settings = getSharedPreferences(PREF, 0);
		String name = settings.getString("USERDATA", "");
		String password = settings.getString("USERPWD", "");
		if (name.compareTo("") == 0) {

		} else {

			// access username and password from listview, and checkbox then gg!
			// LoginAdapter adapter = new LoginAdapter(this, event, name,
			// password, true);
			// loginList.setAdapter(adapter);
			// adapter.notifyDataSetChanged();
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		animator();
	}

	/**
	 * Options action bar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void animator() {
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.slide_up);
		banner.startAnimation(animation);

		Animation animation1 = AnimationUtils.loadAnimation(this,
				R.anim.slide_up);
		animation1.setStartOffset(100);
		loginBox.startAnimation(animation1);

	}

	private void enableBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// throw message device does not support bluetooth
			Toast.makeText(getApplicationContext(),
					"Your device does not support bluetooth",
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.enable();

			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(anim.fade_in, anim.fade_out);

		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		overridePendingTransition(anim.fade_in, anim.fade_out);
		holder.setActivity(this);
		holder.setContext(getApplicationContext());

		if (LoginData.getUsername().compareTo("") == 0) {

			setTitle("Guest");
		} else {
			setTitle(LoginData.getUsername().toString());
		}
	}

	@Override
	public void processStringData(String data, Context cont) {
		// TODO Auto-generated method stub

		alert = Crouton.makeText(holder.getActivity(),
				" \n XYZ has challenged you to a battle! Tap to accept \n ",
				Style.ALERT).setConfiguration(
				new Configuration.Builder().setDuration(
						Configuration.DURATION_INFINITE).build());
		alert.show();

		alert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BluetoothDevice device;
				device = mBluetoothAdapter.getRemoteDevice(SessionData
						.getMacAddress());

				writer = new BluetoothWrite(
						JsonBuilder.createConfirm("ACCEPT"), device);
				writer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						"hello");
				Intent characterSelect = new Intent(getApplicationContext(),
						com.androidactivities.CharacterSelectActivity.class);
				startActivity(characterSelect);
				alert.cancel();
			}
		});

	}

	@Override
	public void isChecked(boolean check, int id) {
		// TODO Auto-generated method stub

		if (id == 0) {
			if (!check) {
				// settings = getSharedPreferences("USERDATA",
				// Context.MODE_PRIVATE);
				settings = getSharedPreferences(PREF, Context.MODE_PRIVATE);
				settings.edit().clear().commit();
			} else {
				try {
					View view = loginList.getChildAt(0);
					EditText usernameText = (EditText) view
							.findViewById(R.id.usernameField);
					EditText passwordText = (EditText) view
							.findViewById(R.id.passwordField);

					settings.edit()
							.putString("USERDATA",
									usernameText.getText().toString())
							.putString("USERPWD",
									passwordText.getText().toString()).commit();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onTaskCompleted(String result) {
		// TODO Auto-generated method stub
		// filter based on JSON string

		try {
			JSONObject jsonObj = new JSONObject(result);
			LoginData.setUsername(jsonObj.getString("_Username").toString());
			LoginData.setFirstname(jsonObj.getString("_FirstName").toString());
			LoginData.setLastname(jsonObj.getString("_LastName").toString());
			LoginData.setEmailAddress(jsonObj.getString("_EmailAddress")
					.toString());
			LoginData.setWins(Integer.parseInt(jsonObj.getString("_Wins")
					.toString()));
			LoginData.setLosses(Integer.parseInt(jsonObj.getString("_Losses")
					.toString()));
			LoginData.setRank(jsonObj.getString("_Rank").toString());
			LoginData.setExp(Integer.parseInt(jsonObj.getString("_Experience")
					.toString()));

		} catch (JSONException e) {

		}
		// dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LoginActivity.this);
		builder.setMessage(
				"Welcome: " + LoginData.getFirstname() + " "
						+ LoginData.getLastname()).setPositiveButton(
				"Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Intent main = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(main);

					}
				});

		// Create the AlertDialog object and return it
		builder.create().show();

		// should only handle an incoming profile
		// Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSent(boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void catchError() {
		// TODO Auto-generated method stub

		// allows for graceful timeout
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Crouton.showText(
						LoginActivity.this,
						"\n Error: Could not connect to server, check your network settings \n",
						Style.ALERT);
			}
		});

	}

	@Override
	public void onProtocolCodeReceive(String result) {
		// TODO Auto-generated method stub

		String temp = "";
		try {
			JSONObject jsonObj = new JSONObject(result);
			temp = jsonObj.getString("_Error");

			if (temp.compareTo("INVALID") == 0) {
				Crouton.showText(LoginActivity.this,
						"Invalid username or password", Style.ALERT);
			} else {
				if (SessionData.getWebServiceIp().compareTo("") == 0) {
					SessionData.setWebServiceIp("http://192.168.43.76");
				} else {

				}

				Crouton.showText(LoginActivity.this, "Success!", Style.CONFIRM);
				// fire off new web request
				WebRequester webReq = new WebRequester(null, "", listener,
						errorCallback, 1, LoginActivity.this); // 0 indicates
																// we're making
																// a check on
																// username/pwd
				URI myUri = URI.create("http://"
						+ SessionData.getWebServiceIp()
						+ ":20000/Home/getProfileView?username="
						+ usernameField.getText().toString().trim());

				webReq.execute(myUri);
			}

		} catch (JSONException e) {

		}
	}

}
