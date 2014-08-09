package com.androidactivities;

import singleton.LoginData;

import com.example.bluetoothmodulev4.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Profile extends Activity{
	
	private boolean animationPlayed;
	private LinearLayout faderView, mainView, breakdownView;
	private TextView victoryStatus, winTxt, lossTxt, expTxt, rankTxt,lvlTxt;
	private Button quit;
	
	//variables
	private TextView winsOne, lossesOne, expOne, lvlOne,  rankOne;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileview);
		
		faderView = (LinearLayout)findViewById(R.id.faderView);
		mainView = (LinearLayout)findViewById(R.id.mainView);
		breakdownView = (LinearLayout)findViewById(R.id.breakDownView);
		breakdownView.setAlpha(0.0f);
		victoryStatus = (TextView)findViewById(R.id.victoryStatus);
		winTxt = (TextView)findViewById(R.id.winsTxt);
		lossTxt = (TextView)findViewById(R.id.losses);
		expTxt = (TextView)findViewById(R.id.exp);
		lvlTxt = (TextView)findViewById(R.id.level);
		rankTxt = (TextView)findViewById(R.id.rank);
		quit = (Button)findViewById(R.id.continueButton);
		winTxt.setAlpha(0.0f);
		lossTxt.setAlpha(0.0f);
		expTxt.setAlpha(0.0f);
		rankTxt.setAlpha(0.0f);
		lvlTxt.setAlpha(0.0f);
		
		//variables
		winsOne = (TextView)findViewById(R.id.winsTxt2);
		lossesOne = (TextView)findViewById(R.id.lossesTxt);
		expOne = (TextView)findViewById(R.id.expTxt);
		lvlOne = (TextView)findViewById(R.id.levelTxt);
		rankOne = (TextView)findViewById(R.id.rankTxt);
		winsOne.setAlpha(0.0f);
		lossesOne.setAlpha(0.0f);
		expOne.setAlpha(0.0f);
		lvlOne.setAlpha(0.0f);
		rankOne.setAlpha(0.0f);
		
		
		animationPlayed = false;
		
		quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(i);
				
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(LoginData.getUsername().compareTo("")==0)
		{
			setTitle("Guest");
		}else
		{
			setTitle(LoginData.getUsername());
		}
		
		if(!animationPlayed)
		{
			//animate
			
					// TODO Auto-generated method stub
					
					Animation fadeanimation = AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.fadein);
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
						
//							winsOne.setAlpha(0.0f);
//							lossesOne.setAlpha(0.0f);
//							expOne.setAlpha(0.0f);
//							lvlOne.setAlpha(0.0f);
//							rankOne.setAlpha(0.0f);
							
							Animation slideUpA = AnimationUtils.loadAnimation(getApplicationContext(),
									R.anim.slide_up);
							lvlTxt.startAnimation(slideUpA);
							lvlOne.startAnimation(slideUpA);
							slideUpA.setStartOffset(50);
							lvlTxt.setAlpha(1.0f);
							lvlOne.setAlpha(1.0f);
							
							Animation slideUp1 = AnimationUtils.loadAnimation(getApplicationContext(),
									R.anim.slide_up);
							slideUp1.setStartOffset(100);
							expTxt.startAnimation(slideUp1);
							expOne.startAnimation(slideUp1);
							expTxt.setAlpha(1.0f);
							expOne.setAlpha(1.0f);
							
							
							
							
							Animation slideUp2 = AnimationUtils.loadAnimation(getApplicationContext(),
									R.anim.slide_up);
							slideUp2.setStartOffset(200);
							winTxt.startAnimation(slideUp2);
							winsOne.startAnimation(slideUp2);
							winTxt.setAlpha(1.0f);
							winsOne.setAlpha(1.0f);
							
							Animation slideUp3 = AnimationUtils.loadAnimation(getApplicationContext(),
									R.anim.slide_up);
							slideUp3.setStartOffset(300);
							lossTxt.startAnimation(slideUp3);
							lossesOne.startAnimation(slideUp3);
							lossTxt.setAlpha(1.0f);
							lossesOne.setAlpha(1.0f);
							
							
							Animation slideUp4 = AnimationUtils.loadAnimation(getApplicationContext(),
									R.anim.slide_up);
							slideUp4.setStartOffset(400);
							rankTxt.startAnimation(slideUp4);
							rankOne.startAnimation(slideUp4);
							rankTxt.setAlpha(1.0f);
							rankOne.setAlpha(1.0f);
						}
					});
					
					
				}
			
		}
	}


