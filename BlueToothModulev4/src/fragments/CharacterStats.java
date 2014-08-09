package fragments;



import java.util.ArrayList;

import json.JsonBuilder;
import libgdx.GameStarter;

import singleton.SessionData;

import bluetoothcomms.BluetoothWrite;

import com.androidactivities.CharacterSelectActivity;
import com.androidactivities.TutorialActivity;
import com.example.bluetoothmodulev4.R;

import fragmentinterfaces.TabletOrPhone;
import gamedata.ClassValues;
import gamedata.Stats;
import adapters.StatAdapt;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CharacterStats extends Fragment {

	private TextView mImageView;
	private boolean mInitialCreate;
	
	//adapter handling
	ListView characterStatistics;
	StatAdapt statAdaptor;
	private ArrayList<Stats> stats;
	private Button confirmButton;
	private BluetoothAdapter mBluetoothAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (savedInstanceState != null) {
			mInitialCreate = false;
		} else
		{
			mInitialCreate = true;
		}
		
		stats = new ArrayList<Stats>();
		Resources res= getResources();
		statAdaptor = new StatAdapt(getActivity(),stats,res,getActivity().getApplicationContext());
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the fragment layout into the container provided
				View v = inflater.inflate(R.layout.character_stats, container,false);

				// Setup views
				mImageView = (TextView) v.findViewById(R.id.textView1);
				//stats = new ArrayList<Stats>();
				//populateData(); // will get called each time!!
		
				//maybe global?
				characterStatistics = (ListView)v.findViewById(R.id.statsView);
				confirmButton = (Button)v.findViewById(R.id.confirmButton);
				confirmButton.setVisibility(View.INVISIBLE);
				//see if we have data from main activity to use
				if(CharacterSelectActivity.accessData.compareTo("")==0)
				{
					//confirmButton.setVisibility(View.INVISIBLE);
					
				} else
				{
					confirmButton.setVisibility(View.VISIBLE);
					updateText(CharacterSelectActivity.accessData);
					characterStatistics.setAdapter(statAdaptor);
					
				}
		//create adaptor
		
		//characterStatistics.setAdapter(statAdaptor);
		addListeners();
		// TODO Auto-generated method stub
		return v;
	}
	
	private void addListeners()
	{
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//create JSON containing class name
				
				//ACTIVITY should handle interface callback
				BluetoothDevice device;
				device = mBluetoothAdapter.getRemoteDevice(SessionData.getMacAddress().toString());
				
				BluetoothWrite writer = new BluetoothWrite(JsonBuilder.createCharacter(mImageView.getText().toString(),ClassValues.getStatsBasedOnClass(mImageView.getText().toString())), device);
				writer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
				
				SessionData.setChosen(true);
				SessionData.setMyClass(mImageView.getText().toString());
				SessionData.setMyStats(ClassValues.getStatsBasedOnClass(mImageView.getText().toString())[0], ClassValues.getStatsBasedOnClass(mImageView.getText().toString())[1], ClassValues.getStatsBasedOnClass(mImageView.getText().toString())[2],ClassValues.getStatsBasedOnClass(mImageView.getText().toString())[3]);
				
				
				if(SessionData.getChosen() && SessionData.getOpponentChosen())
				{
					Intent game = new Intent(getActivity().getApplicationContext(),GameStarter.class);
					startActivity(game);
				}else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Waiting for opponent");
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				
				
			}
		});
	}
	
	/**
	 * Sets default data if unselected
	 */
	private void populateData(String text)
	{
		//take in param type
		//use static class based on class fed in to return stats to stats class.
		
		//Could hide data when nothing is selected
		Stats tempStatHealth = new Stats("Health", "healthicon", ClassValues.getValues(text)[0], ClassValues.getValues(text)[0]);
		Stats tempStatArmour = new Stats("Armour", "armouricon",ClassValues.getValues(text)[1],ClassValues.getValues(text)[1]);
		Stats tempStatMagic = new Stats("Magic", "manaicon",ClassValues.getValues(text)[2],ClassValues.getValues(text)[2]);
		Stats tempAttackPower = new Stats("Attack power", "attackpowericon",ClassValues.getValues(text)[3],ClassValues.getValues(text)[3]);
		
		stats.add(tempStatHealth);
		stats.add(tempStatArmour);
		stats.add(tempStatMagic);
		stats.add(tempAttackPower);
		
		
	}

	//tapping listview brings up dialog of stat purpose
	//Method needs to be public to be accessable to the interface
	public void updateText(String text)
	{
		//animations done here
		//change listview here
		mImageView.setText(text);
		if(text.compareTo("Warrior")==0)
		{
			mImageView.setText("Warrior");
			//icon of warrior
			stats.clear();
			populateData(text);
			characterStatistics.setAdapter(statAdaptor);
			CharacterSelectActivity.accessData = "Warrior";
			
			//access listview adapter
		}
		else if (text.compareTo("Paladin")==0)
		{
			mImageView.setText("Paladin");
			stats.clear();
			populateData(text);
			characterStatistics.setAdapter(statAdaptor);
			CharacterSelectActivity.accessData = "Paladin";
		}
		else if (text.compareTo("Wizard")==0)
		{
			mImageView.setText("Wizard");
			stats.clear();
			populateData(text);
			characterStatistics.setAdapter(statAdaptor);
			CharacterSelectActivity.accessData = "Wizard";
		}
		
		else{
			mImageView.setText("Error");
		}
		
		confirmButton.setVisibility(View.VISIBLE);
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(CharacterSelectActivity.accessData.compareTo("")==0)
		{
			confirmButton.setVisibility(View.INVISIBLE);
		}else
		{
		confirmButton.setVisibility(View.VISIBLE);
		updateText(CharacterSelectActivity.accessData);
		characterStatistics.setAdapter(statAdaptor);
		}
	}
	
	

	
}
