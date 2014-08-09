package com.androidactivities;

import com.example.bluetoothmodulev4.R;

import fragmentinterfaces.TabletOrPhone;
import fragmentinterfaces.ViewEnabler;
import fragments.CharacterStats;
import fragments.CredentialsFragment;
import fragments.SecurityFragment;
import interfaces.IDataProcessor;
import singleton.ListenerHolder;
import singleton.LoginData;
import singleton.RegistrationData;
import singleton.SessionData;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class RegisterActivity extends FragmentActivity implements IDataProcessor,TabletOrPhone{

	private static ListenerHolder holder;
	private ViewGroup credentialsLayout, securitylayout;
	private CredentialsFragment credentialFragment;
	
	//private SecurityFragment securityFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerview);
		
			
		
		
		holder.setActivity(this);
		holder.setContext(getApplicationContext());
		
		if(savedInstanceState != null)
		{
			
		} else
		{
			credentialsLayout = (ViewGroup) findViewById(R.id.registercredentialslayout);
			if(credentialsLayout != null)
			{
				credentialFragment = new CredentialsFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(credentialsLayout.getId(),
						credentialFragment,
						CredentialsFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}
			
			securitylayout = (ViewGroup) findViewById(R.id.registersecuritylayout);
			if(securitylayout != null)
			{
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
				
				SecurityFragment securityFragment = new SecurityFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(securitylayout.getId(),
						securityFragment,
						SecurityFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}
		}
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
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
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
	        builder.setMessage("Are you sure you want to logout?")
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
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(LoginData.getUsername().compareTo("")==0)
		{
			setTitle("Guest");
		}else
		{
			setTitle(LoginData.getUsername());
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_profile).setVisible(false);
		return true;
	}

	@Override
	public void processStringData(String data, Context cont) {
		// TODO Auto-generated method stub
		
	}


	
	public void gridSelected(String text) {
		
		
		
		// TODO Auto-generated method stub
		securitylayout = (ViewGroup) findViewById(R.id.registersecuritylayout);
		if (securitylayout != null) {
			// We're in 2 pane layout, do nothing
			//signal to securityLayout that it needs to enable the last view section
			SecurityFragment.enableLayout();
		} else {
			RegistrationData.setWasFragment(true);
			
			credentialsLayout = (ViewGroup) findViewById(R.id.registercredentialslayout);
			SecurityFragment securityFrag = new SecurityFragment();

			
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(credentialsLayout.getId(),
					securityFrag);
			fragmentTransaction.addToBackStack(null);

			// // Commit the transaction
			fragmentTransaction.commit();
			
			//static
		}
	}


	
}
