package libgdx;


import json.JsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class MyGdxGame extends Game {

	private Music mp3Sound;
	private Splash splash;

	@Override
	public void create() {	
		mp3Sound = Gdx.audio.newMusic(Gdx.files.internal("Sound/theme1.mp3"));
		mp3Sound.setLooping(true);
		mp3Sound.setVolume(0.5f);
		mp3Sound.play();
		
		splash = new Splash();
		setScreen(splash);
		
		
	}

	@Override
	public void dispose() {
		mp3Sound.dispose();
		super.dispose();
	}

	@Override
	public void render() {	
		super.render();
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	public void determineAction(String text) throws JSONException
	{
		//magic effects
		if(text.compareTo("Fireball")==0)
		{
			splash.fireBallEffect();
			
		} else if (text.compareTo("Waterball")==0)
		{
			splash.waterballEffect();
		} else if (text.compareTo("Magicdefense")==0)
		{
			splash.magicDefenseEffect();
		} else if (text.compareTo("Defense")==0)
		{
			splash.defenseEffect();
		} else if (text.compareTo("Health")==0)
		{
			
		}
		
		JSONObject topData = JsonBuilder.getObject(text);
		
		if(topData.getString("MESSAGETYPE").compareTo("4")==0)
		{
			//TODO modifiers, armour etc.
			if(topData.getString("DAMAGETYPE").compareTo("ATTACK")==0)
			{
				splash.animateEffects(topData.getString("NUMBER"));
			} else if (topData.getString("DAMAGETYPE").compareTo("FIREBALL")==0)
			{
				splash.animateIncomingFireball(topData.getString("NUMBER"));
			} else if(topData.getString("DAMAGETYPE").compareTo("WATERBALL")==0)
			{
				splash.animateIncomingWaterball(topData.getString("NUMBER"));
			} else if (topData.getString("DAMAGETYPE").compareTo("MAGICDEFENSEUP")==0)
			{
				
				splash.opponentMagicDefenseEffect();
			} else if (topData.getString("DAMAGETYPE").compareTo("DEFENSEUP")==0)
			{
				splash.opponentDefenseEffect();
			}
			
			splash.passTurn();
		} else if (topData.getString("MESSAGETYPE").compareTo("5")==0)
		{
			Splash.winOrLose = true;
			splash.winScenario("Congratulations, you are victorious!");
		} else if(topData.getString("MESSAGETYPE").compareTo("5")==0)
		{
			splash.disconnectedDialog();
		}
		
		
		
		//splash.updateUi();
	}
}
