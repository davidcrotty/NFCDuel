package com.androidactivities;

import interfaces.ErrorHandler;
import interfaces.IDataProcessor;
import interfaces.OnTaskCompleted;

import com.example.bluetoothmodulev4.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import networkcomms.WebRequester;

import json.JsonBuilder;
import singleton.ListenerHolder;
import singleton.LoginData;
import singleton.SessionData;
import bluetoothcomms.BluetoothWrite;
import datasets.UserProfile;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import adapters.Item;
import adapters.RankAdapter;
import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileViewerActivity extends FragmentActivity implements
		IDataProcessor, OnTaskCompleted, ErrorHandler {

	private Crouton alert;
	private ListenerHolder holder;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothWrite writer;
	private OnTaskCompleted listener;
	private ErrorHandler errorCallback;
	private GridView view;
	private ArrayList<UserProfile> profileList;

	private SearchView searchView;
	
	//adapter needs to be global and swapped
	private RankAdapter globalAdapter;
	private View dialogView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profilerbrowser);

		listener = (OnTaskCompleted) this;
		errorCallback = (ErrorHandler) this;
		view = (GridView) findViewById(R.id.gridView);

		holder.setActivity(this);
		holder.setContext(getApplicationContext());
		enableBluetooth();
		// generateData(); //testing method
		fetchWebData();
		handleIntent(getIntent());

		dialogView = this.getLayoutInflater().inflate(R.layout.profilechecker, null);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		
			return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onNewIntent(Intent intent) {

		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			// use the query to search your data somehow

			Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void fetchWebData() {
		WebRequester web = new WebRequester(null, "", listener, errorCallback,
				1, ProfileViewerActivity.this);
		URI myUri = URI.create("http://" + SessionData.getWebServiceIp()
				+ ":20000/Home/getallprofiles");
		web.execute(myUri);

	}

	private ArrayList<UserProfile> addDrawables(ArrayList<UserProfile> p) {
		for (int i = 0; i < p.size(); i++) {
			p.get(i).setDrawable(p.get(i).getRank());
		}

		return p;
	}

	private void generateData(ArrayList<UserProfile> p) {

		// if p.getrank .compare to .setdrawable
		// ArrayList<Item> data = new ArrayList<Item>();

		// set profile drawables
		profileList = new ArrayList<UserProfile>();

		profileList = addDrawables(p);

		globalAdapter = new RankAdapter(this, profileList, true);
		view.setAdapter(globalAdapter);
		
		
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				//View layout = ProfileViewerActivity.this.getLayoutInflater().inflate(R.layout.profilechecker, null);
				
				
				
				TextView username = (TextView) dialogView.findViewById(R.id.profileName);
				ImageView imView = (ImageView) dialogView.findViewById(R.id.rankIcon);
				TextView firstName = (TextView) dialogView.findViewById(R.id.firstName);
				TextView lastName = (TextView) dialogView.findViewById(R.id.lastName);
				TextView rank = (TextView) dialogView.findViewById(R.id.rankName);
				TextView level = (TextView) dialogView.findViewById(R.id.level);
				
				username.setText(globalAdapter.getItem(arg2).getUsername().toString());
				imView.setImageResource(globalAdapter.getItem(arg2).getDrawable());
				firstName.setText(globalAdapter.getItem(arg2).getFirstName().toString());
				lastName.setText(globalAdapter.getItem(arg2).getLastName().toString());
				rank.setText(""+globalAdapter.getItem(arg2).getRank());
				level.setText(""+ globalAdapter.getItem(arg2).getLevel());
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ProfileViewerActivity.this);
				builder.setIcon(0);

				builder.setPositiveButton("Confirm",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								((ViewGroup)dialogView.getParent()).removeView(dialogView);
							}
						});
				
				builder.setView(dialogView);
				builder.create().show();
				
				
				
			}
		});

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
			setTitle(LoginData.getUsername());
		}
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

	/**
	 * Options action bar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// filter view here

				// global gridview
				// store 'Dataset' globaly
				// search within this local dataset
				// set adapter based on results

				String comparison = "" + searchView.getQuery();

				ArrayList<UserProfile> localList = new ArrayList<UserProfile>();

				if (comparison.isEmpty()) {
					RankAdapter adapter = new RankAdapter(
							getApplicationContext(), profileList, false);
					view.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < profileList.size(); i++) {
						String a = profileList.get(i).getUsername();
						a.toLowerCase();
						if (a.contains(comparison.toLowerCase())) {
							localList.add(profileList.get(i));
						}
					}

					
					globalAdapter = new RankAdapter(
							getApplicationContext(), localList, false);
					view.setAdapter(globalAdapter);
					globalAdapter.notifyDataSetChanged();
				}
				return false;
			}
		});

		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(anim.fade_in, anim.fade_out);
		
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
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
	public void catchError() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Crouton.showText(
						ProfileViewerActivity.this,
						"\n Error: Could not connect to server, check your network settings \n",
						Style.ALERT);
			}
		});
	}

	@Override
	public void onTaskCompleted(String result) {

		ArrayList<UserProfile> list = new ArrayList<UserProfile>();

		try {
			JSONArray someJson = new JSONArray(result);

			for (int i = 0; i < someJson.length(); i++) {
				UserProfile p = new UserProfile(someJson.getJSONObject(i)
						.getString("_Username"), someJson.getJSONObject(i)
						.getString("_FirstName"), someJson.getJSONObject(i)
						.getString("_LastName"), Integer.parseInt(someJson
						.getJSONObject(i).getString("_Wins")),
						Integer.parseInt(someJson.getJSONObject(i).getString(
								"_Losses")), Integer.parseInt(someJson
								.getJSONObject(i).getString("_Level")),
						someJson.getJSONObject(i).getString("_Rank"), someJson
								.getJSONObject(i).getString("_EmailAddress"));
				list.add(p);
				Log.d("JSON Entry", someJson.getString(i));

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final ArrayList<UserProfile> passList = list;

		// use GSon?

		// callback here make sure on UI thread
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// ui actions here
				generateData(passList);
			}
		});

	}

	@Override
	public void onProtocolCodeReceive(String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSent(boolean ok) {
		// TODO Auto-generated method stub

	}

}
