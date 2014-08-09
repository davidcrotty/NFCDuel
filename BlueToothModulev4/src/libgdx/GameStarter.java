package libgdx;

import org.json.JSONException;
import org.json.JSONObject;

import nfc.NdefReader;
import json.JsonBuilder;
import singleton.ListenerHolder;
import singleton.SessionData;
import interfaces.IDataProcessor;
import interfaces.NFCCompleted;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import bluetoothcomms.BluetoothWrite;

import com.androidactivities.BattleOverview;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GameStarter extends AndroidApplication implements IDataProcessor,
		NFCCompleted {

	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "GameStarter";
	private MyGdxGame game;
	private ListenerHolder holder;
	private NfcAdapter mNfcAdapter;
	private NFCCompleted nfcCallback;

	public static boolean gameFinished;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;

		gameFinished = false;

		holder.setActivity(this);
		holder.setContext(getApplicationContext());
		testNfc();
		nfcCallback = this;

		game = new MyGdxGame();
		initialize(game, cfg);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (gameFinished) {
			BluetoothDevice device;
			BluetoothAdapter localDevice = BluetoothAdapter.getDefaultAdapter();

			device = localDevice.getRemoteDevice(SessionData.getMacAddress());
			// write lose
			JSONObject lose = new JSONObject();
			try {

				lose.put("MESSAGETYPE", "6"); // 5 means win or lose
				lose.put("LOSEFLAG", "closed");
			} catch (JSONException e) {

			}
			BluetoothWrite writer = new BluetoothWrite(lose.toString(), device);
			writer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "hello");
		}
	}

	@Override
	public void processStringData(String data, Context cont) {
		// TODO Auto-generated method stub

		try {
			game.determineAction(data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void testNfc() {
		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if (mNfcAdapter == null) {
			// Device doesn't support NFC, throw error
		} else {
			if (!mNfcAdapter.isEnabled()) {
				mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
				// NFC not enabled, enable
			} else {
				// NFC works
			}
		}

		//
	}

	@Override
	protected void onResume() {
		super.onResume();

		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		setupForegroundDispatch(this, mNfcAdapter);
	}

	@Override
	protected void onPause() {
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */
		stopForegroundDispatch(this, mNfcAdapter);

		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		/**
		 * This method gets called, when a new Intent gets associated with the
		 * current activity instance. Instead of creating a new activity,
		 * onNewIntent will be called.
		 * 
		 * In our case this method gets called, when the user attaches a Tag to
		 * the device.
		 */
		handleIntent(intent);
	}

	public static void setupForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(
				activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}

		adapter.enableForegroundDispatch(activity, pendingIntent, filters,
				techList);
	}

	public static void stopForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReader(this, nfcCallback).execute(tag);

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
					new NdefReader(this, nfcCallback).execute(tag);
					break;
				}
			}
		}

	}

	@Override
	public void taskComplete(String text) throws JSONException {
		// TODO Auto-generated method stub
		game.determineAction(text);
	}

}