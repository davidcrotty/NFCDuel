package com.androidactivities;

import java.net.URI;

import interfaces.ErrorHandler;
import interfaces.OnTaskCompleted;
import networkcomms.WebRequester;
import singleton.LoginData;
import singleton.SessionData;

import com.example.bluetoothmodulev4.R;

import datasets.StatCalculator;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BattleOverview extends Activity implements OnTaskCompleted,
		ErrorHandler {

	private boolean animationPlayed;
	private LinearLayout faderView, mainView, breakdownView;
	private TextView victoryStatus, winTxt, lossTxt, expTxt, rankTxt, lvlTxt;
	private Button quit, cont;

	private TextView winOne, winTwo, lossOne, lossTwo, expOne, expTwo, rankOne,
			rankTwo, lvlOne, lvlTwo;

	private OnTaskCompleted finishedTask;
	private ErrorHandler onError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finishscreen);

		finishedTask = (OnTaskCompleted) this;
		onError = (ErrorHandler) this;

		boolean winStatus = false;

		// get win or loss
		try {
			Intent i = getIntent();
			winStatus = i.getBooleanExtra("WINCOND", false);
		} catch (Exception e) {

		}

		faderView = (LinearLayout) findViewById(R.id.faderView);
		mainView = (LinearLayout) findViewById(R.id.mainView);
		breakdownView = (LinearLayout) findViewById(R.id.breakDownView);
		breakdownView.setAlpha(0.0f);
		victoryStatus = (TextView) findViewById(R.id.victoryStatus);
		winTxt = (TextView) findViewById(R.id.wins);
		lossTxt = (TextView) findViewById(R.id.losses);
		expTxt = (TextView) findViewById(R.id.exp);
		lvlTxt = (TextView) findViewById(R.id.level);
		rankTxt = (TextView) findViewById(R.id.rank);
		quit = (Button) findViewById(R.id.quitButton);
		winTxt.setAlpha(0.0f);
		lossTxt.setAlpha(0.0f);
		expTxt.setAlpha(0.0f);
		rankTxt.setAlpha(0.0f);
		lvlTxt.setAlpha(0.0f);

		if (winStatus) {
			victoryStatus.setText("VICTORY");
			victoryStatus.setTextColor(Color.parseColor("#19FF37"));
		} else {
			victoryStatus.setText("DEFEAT");
			victoryStatus.setTextColor(Color.parseColor("#FF0000"));
		}

		winOne = (TextView) findViewById(R.id.winsTxt);
		lossOne = (TextView) findViewById(R.id.lossesTxt);
		expOne = (TextView) findViewById(R.id.expTxt);
		lvlOne = (TextView) findViewById(R.id.levelTxt);
		rankOne = (TextView) findViewById(R.id.rankTxt);

		winOne.setAlpha(0.0f);
		lossOne.setAlpha(0.0f);
		expOne.setAlpha(0.0f);
		lvlOne.setAlpha(0.0f);
		rankOne.setAlpha(0.0f);

		winTwo = (TextView) findViewById(R.id.winsChange);
		lossTwo = (TextView) findViewById(R.id.lossesChange);
		expTwo = (TextView) findViewById(R.id.expChange);
		lvlTwo = (TextView) findViewById(R.id.levelChange);
		rankTwo = (TextView) findViewById(R.id.rankChange);

		winTwo.setAlpha(0.0f);
		lossTwo.setAlpha(0.0f);
		expTwo.setAlpha(0.0f);
		lvlTwo.setAlpha(0.0f);
		rankTwo.setAlpha(0.0f);

		cont = (Button)findViewById(R.id.continueButton);
		quit = (Button)findViewById(R.id.quitButton);
		
		setStats(winStatus);

		animationPlayed = false;

		uploadStats();

		cont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(i);
			}
		});

		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//finish();
				System.exit(0);

			}
		});
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
	}

	private void uploadStats() {
		// http://localhost:25730/Home/update?username=thomasrichards&wins=5&losses=5&rank=beginner&exp=70&level=9

		WebRequester statSubmitter = new WebRequester(null, "", finishedTask,
				onError, 2, BattleOverview.this);
		URI myUri = URI.create("http://" + SessionData.getWebServiceIp()
				+ ":20000/Home/update?username=" + LoginData.getUsername()
				+ "&wins=" + LoginData.getWins() + "&losses="
				+ LoginData.getLosses() + "&rank=" + LoginData.getRank()
				+ "&exp=" + LoginData.getExp() + "&level="
				+ LoginData.getLevel());
		
		statSubmitter.execute(myUri);

	}

	private void setStats(boolean data) {
		winOne.setText(""+LoginData.getWins());
		lossOne.setText(""+LoginData.getLosses());
		expOne.setText(""+LoginData.getExp());
		lvlOne.setText(""+LoginData.getLevel());
		rankOne.setText(""+LoginData.getRank());

		// add changes
		expTwo.setText(""
				+ StatCalculator.calcExpgain(SessionData.getOpponentLevel(),
						data));
		if (data) {
			winTwo.setText("1");
			winTwo.setTextColor(Color.parseColor("#19FF37"));
			LoginData.setWins(LoginData.getWins()
					+ Integer.parseInt(winTwo.getText().toString()));
			lossTwo.setText("0");
		} else {
			winTwo.setText("0");
			lossTwo.setText("1");
			LoginData.setLosses(LoginData.getLosses()
					+ Integer.parseInt(lossTwo.getText().toString()));
			lossTwo.setTextColor(Color.parseColor("#FF0000"));
		}

		if (StatCalculator.calcLevelUp(
				Integer.parseInt(expTwo.getText().toString())
						+ Integer.parseInt(expOne.getText().toString()),
				LoginData.getLevel())) {
			lvlTwo.setText("1");
			lvlTwo.setTextColor(Color.parseColor("#19FF37"));
			LoginData.setLevel(LoginData.getLevel()
					+ Integer.parseInt(lvlTwo.getText().toString()));
		} else {
			lvlTwo.setText("0");

		}

		// get level here
		String temp = LoginData.getRank();
		LoginData.setRank(StatCalculator.calcRank(LoginData.getLevel()));

		if (temp.compareTo(LoginData.getRank()) == 0) {

		} else {
			rankTwo.setText(LoginData.getRank());
			rankTwo.setTextColor(Color.parseColor("#19FF37"));
		}

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.action_exit:
			// quit dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(
					BattleOverview.this);
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
									Intent i = new Intent(
											getApplicationContext(),
											MainActivity.class);
									startActivity(i);

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

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (LoginData.getUsername().compareTo("") == 0) {
			setTitle("Guest");
		} else {
			setTitle(LoginData.getUsername());
		}

		if (!animationPlayed) {
			// animate
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.fadeoutfinish);
			faderView.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					faderView.setVisibility(View.GONE);
					Animation fadeanimation = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.fadein);
					breakdownView.startAnimation(fadeanimation);
					breakdownView.setAlpha(1.0f);
					fadeanimation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub

							Animation slideUpA = AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.slide_up);
							lvlTxt.startAnimation(slideUpA);
							lvlOne.startAnimation(slideUpA);
							lvlTwo.startAnimation(slideUpA);

							slideUpA.setStartOffset(50);
							lvlTxt.setAlpha(1.0f);
							lvlOne.setAlpha(1.0f);
							lvlTwo.setAlpha(1.0f);

							Animation slideUp1 = AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.slide_up);
							slideUp1.setStartOffset(100);
							expTxt.startAnimation(slideUp1);
							expOne.startAnimation(slideUp1);
							expTwo.startAnimation(slideUp1);
							expTxt.setAlpha(1.0f);
							expOne.setAlpha(1.0f);
							expTwo.setAlpha(1.0f);

							Animation slideUp2 = AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.slide_up);
							slideUp2.setStartOffset(200);
							winTxt.startAnimation(slideUp2);
							winOne.startAnimation(slideUp2);
							winTwo.startAnimation(slideUp2);
							winTxt.setAlpha(1.0f);
							winOne.setAlpha(1.0f);
							winTwo.setAlpha(1.0f);

							Animation slideUp3 = AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.slide_up);
							slideUp3.setStartOffset(300);
							lossTxt.startAnimation(slideUp3);
							lossOne.startAnimation(slideUp3);
							lossTwo.startAnimation(slideUp3);
							lossTxt.setAlpha(1.0f);
							lossOne.setAlpha(1.0f);
							lossTwo.setAlpha(1.0f);

							Animation slideUp4 = AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.slide_up);
							slideUp4.setStartOffset(400);
							rankTxt.startAnimation(slideUp4);
							rankOne.startAnimation(slideUp4);
							rankTwo.startAnimation(slideUp4);
							rankTxt.setAlpha(1.0f);
							rankOne.setAlpha(1.0f);
							rankTwo.setAlpha(1.0f);
						}
					});

				}
			});
		}
	}

	@Override
	public void catchError() {
		// TODO Auto-generated method stub
		Crouton.makeText(this, "Error uploading stats, please check your connection then click continue", Style.ALERT);
	}

	@Override
	public void onTaskCompleted(String result) {
		// TODO Auto-generated method stub
		Crouton.makeText(this, "Your stats have been uploaded", Style.CONFIRM);
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
