package com.androidactivities;

import org.json.JSONException;

import singleton.ListenerHolder;
import singleton.LoginData;
import singleton.SessionData;

import json.JsonBuilder;
import libgdx.GameStarter;
import interfaces.IDataProcessor;

import com.example.bluetoothmodulev4.R;

import fragmentinterfaces.FragmentSelect;
import fragmentinterfaces.TabletOrPhone;
import fragments.CharacterStats;
import fragments.ImageListFragment;
import fragments.ImageSelectorFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.ViewGroup;

public class CharacterSelectActivity extends FragmentActivity implements
		FragmentSelect, TabletOrPhone, IDataProcessor {

	private ViewGroup mImageSelectorLayout, mImageRotatorLayout;
	ImageSelectorFragment imageSelectorFragment;

	public static String accessData = "";
	private ListenerHolder holder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.root);

		if (LoginData.getUsername().compareTo("") == 0) {
			setTitle("Guest");
		} else {
			setTitle(LoginData.getUsername());
		}

		holder.setActivity(this);
		holder.setContext(getApplicationContext());
		// accessData = "";

		if (savedInstanceState != null) {

		} else {
			mImageSelectorLayout = (ViewGroup) findViewById(R.id.activity_main_image_selector_container);
			if (mImageSelectorLayout != null) {
				imageSelectorFragment = new ImageSelectorFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(mImageSelectorLayout.getId(),
						imageSelectorFragment,
						ImageSelectorFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}

			mImageRotatorLayout = (ViewGroup) findViewById(R.id.activity_main_image_rotate_container);
			if (mImageRotatorLayout != null) {
				CharacterStats imageRotatorFragment = new CharacterStats();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(mImageRotatorLayout.getId(),
						imageRotatorFragment, CharacterStats.class.getName());
				//
				// // Commit the transaction
				fragmentTransaction.commit();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		overridePendingTransition(anim.fade_in, anim.fade_out);

		if (LoginData.getUsername().compareTo("") == 0) {
			setTitle("Guest");
		} else {
			setTitle(LoginData.getUsername());
		}
	}

	@Override
	public void selectedItem(int pos, String text) {
		// TODO Auto-generated method stub

		FragmentManager fragmentManager = getSupportFragmentManager();
		CharacterStats imageRotatorFragment = (CharacterStats) fragmentManager
				.findFragmentByTag(CharacterStats.class.getName());

		if (imageRotatorFragment != null) {
			imageRotatorFragment.updateText(text);
		}

		// test if two layouts are on one page, we need to recreate the
		// character select fragment
		// if(mImageRotatorLayout != null)
		// {
		// //trigger interface call
		// //public method expose instead of interface call
		// imageSelectorFragment.replaceFragment();
		// }

	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub

		super.onResumeFragments();
		overridePendingTransition(anim.fade_in, anim.fade_out);
	}



	@Override
	public void gridSelected(String text) {
		// TODO Auto-generated method stub

		mImageRotatorLayout = (ViewGroup) findViewById(R.id.activity_main_image_rotate_container);
		if (mImageRotatorLayout != null) {
			// We're in 2 pane layout, do nothing

		} else {
			mImageSelectorLayout = (ViewGroup) findViewById(R.id.activity_main_image_selector_container);
			CharacterStats imageRotatorFragment = new CharacterStats();

			setSelectionData(text);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(mImageSelectorLayout.getId(),
					imageRotatorFragment);
			fragmentTransaction.addToBackStack(null);

			// // Commit the transaction
			fragmentTransaction.commit();
		}

	}

	private void setSelectionData(String text) {

		accessData = text;
	}

	@Override
	public void processStringData(String data, Context cont) {

		Intent game;
		boolean startGame = false;
		// TODO Auto-generated method stub
		if (data.compareTo("") == 0) {

		} else {
			try {
				if (JsonBuilder.getObject(data).get("CLASS").toString()
						.compareTo("Warrior") == 0) {
					SessionData.setOpponentClass(JsonBuilder.getObject(data)
							.get("CLASS").toString());
					SessionData.setOpponentChosen(true);
					SessionData.setOpponentStats(JsonBuilder.getObject(data)
							.getString("HEALTH"), JsonBuilder.getObject(data)
							.getString("ARMOUR"), JsonBuilder.getObject(data)
							.getString("MAGIC"));
					if (SessionData.getChosen()
							&& SessionData.getOpponentChosen()) {
						startGame = true;
					}

				} else if (JsonBuilder.getObject(data).get("CLASS").toString()
						.compareTo("Paladin") == 0) {
					SessionData.setOpponentClass(JsonBuilder.getObject(data)
							.get("CLASS").toString());
					SessionData.setOpponentChosen(true);
					SessionData.setOpponentStats(JsonBuilder.getObject(data)
							.getString("HEALTH"), JsonBuilder.getObject(data)
							.getString("ARMOUR"), JsonBuilder.getObject(data)
							.getString("MAGIC"));
					if (SessionData.getChosen()
							&& SessionData.getOpponentChosen()) {
						startGame = true;
					}

				} else if (JsonBuilder.getObject(data).get("CLASS").toString()
						.compareTo("Wizard") == 0) {
					SessionData.setOpponentClass(JsonBuilder.getObject(data)
							.get("CLASS").toString());
					SessionData.setOpponentChosen(true);
					SessionData.setOpponentStats(JsonBuilder.getObject(data)
							.getString("HEALTH"), JsonBuilder.getObject(data)
							.getString("ARMOUR"), JsonBuilder.getObject(data)
							.getString("MAGIC"));
					if (SessionData.getChosen()
							&& SessionData.getOpponentChosen()) {
						startGame = true;
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (startGame) {
			game = new Intent(getApplicationContext(), GameStarter.class);
			startActivity(game);
		}
	}

}
