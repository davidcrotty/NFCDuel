package libgdx;


import interfaces.ReturnOverview;

import java.io.Writer;
import java.text.Normalizer;

import org.json.JSONException;
import org.json.JSONObject;

import gameloader.EffectsLoader;
import gameloader.Loader;
import json.JsonBuilder;
import singleton.ListenerHolder;
import singleton.LoginData;
import singleton.SessionData;
import tweenAccessors.ImageButtonAccessor;
import tweenAccessors.IntegerAccessor;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import bluetoothcomms.BluetoothWrite;

import com.androidactivities.BattleOverview;
import com.androidactivities.CharacterSelectActivity;
import com.androidactivities.LoginActivity;
import com.androidactivities.MainActivity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class Splash implements Screen {

	//private ReturnOverview returnOverview;//for win callback
	
	private Sound hit, fireballSound, waterballSound, magicshieldSound, defenseSound;
	
	private int magicMAXOpponent, magicMAXMe;

	public static final String NAME = "NFCDuel", VERSION = "0.0";
	private Sprite background, playerOneBackground, playerTwoBackground;
	private SpriteBatch batch;
	private TweenManager tweenManager;
	// END Background variables

	private Skin skin; // look and feel
	private Stage stage;
	private Table table, healthOneTable, healthTwoTable, timerTable,
			statsTable, nameStatsTable, magicRemainTablePlayer1,
			magicRemainTablePlayer2, characterOne, characterTwo, statusTable,
			playerInformationTable;
	private BitmapFont whiteNonGlow, infoFont, mainFont, damageFont; // for text
	private Label slash, slash2, slash3, slash4; // display
	// health hack
	private Table healthOneYellowTable, healthOneRedTable,
			healthTwoYellowTable, healthTwoRedTable, magicTable;
	private Image healthOneYellowImage, healthOneRedImage,
			healthTwoYellowImage, healthTwoRedImage;

	private TextureAtlas atlas;

	// private Label playerOnename, playerTwoName, playerOneLevel,
	// playerTwoLevel;
	// playerOneHealth, playerTwohealth, playerOneMagic, playerTwoMagic;
	// used for displaying Player names, hp and magic
	private Image emptyHealthPlayerOne, emptyHealthPlayerTwo; // Empty boxes
	private Image healthStatusPlayerOne, healthStatusPlayerTwo;
	private Image magicStatusPlayerOne, magicStatusPlayerTwo;

	private Label playerOneLevel, playerTwoLevel, paddingLabel, timerLabel;
	private Label playerOneHealthLabel, playerTwoHealthLabel,
			playerOneHealthLabelMAX, playerTwoHealthLabelMAX;
	private Label damageControlLabelP1, damageControlLabelP2,
			playerInformationLabel;

	// Names and mana remaining
	private Label playerOneName, playerTwoName, playerOneMagic, playerTwoMagic,
			playerOneMagicMAX, playerTwoMagicMAX;

	// Buttons
	private ImageButton attackButton, nfcButton;

	// Characters
	private Image playerOneSprite, playerTwoSprite;

	private Texture playerOneHealthStatus;

	// animations
	private TextureRegion currentFrameP1, currentFrameP2;
	private TextureRegion effectsP1, effectsP2;
	private float stateTime;

	private boolean justAttackedP1, justAttackedP2; // record if attacked or not
													// for renderloop
	private int animatePosP1, animatePosP2;

	private String modifierP1, modifierP2; // used to record damage intake
	public static Float fireballPosX;
	private boolean fireballVisibleP1;
	
	//win or lose bool
	public static boolean winOrLose;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tweenManager.update(delta); // update each render

		stateTime += Gdx.graphics.getDeltaTime();
		calculateWhichAnim(); // which animation to use

		batch.begin();
		background.draw(batch);

		batch.draw(currentFrameP1, animatePosP1, 90);

		batch.draw(currentFrameP2, animatePosP2, 90);

		if (fireballVisibleP1) {
			batch.draw(effectsP1, fireballPosX, 90);
		}
		if (fireballVisibleP2) {
			batch.draw(effectsP2, fireballPosX, 90);
		}
		if(waterballVisibleP1)
		{
			batch.draw(effectsP1, waterballPosX, 90);
		}
		if(waterballVisibleP2)
		{
			try{
			batch.draw(effectsP2, waterballPosX, 90);
			} catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		
		if(magicShieldVisibleP1)
		{
			batch.draw(effectsP1, 90,90);
		}
		if(magicShieldVisibleP2)
		{
			batch.draw(effectsP2, Gdx.graphics.getWidth() - 400,90);
		}
		if(defenseVisibleP1)
		{
			batch.draw(effectsP1, 90,90);
		}
		if(defenseVisibleP2)
		{
			batch.draw(effectsP2, Gdx.graphics.getWidth() - 400,90);
		}
		
		batch.end();

		// END BACKGROUND LOAD

		stage.act(delta);
		stage.draw();

	}

	private void calculateWhichAnim() {
		if (!justAttackedP1) {
			currentFrameP1 = Loader.getAnimation("P1WALK").getKeyFrame(
					stateTime, true);
		} else {
			currentFrameP1 = Loader.getAnimation("P1ATTACK").getKeyFrame(
					stateTime, true);
		}

		if (!justAttackedP2) {
			currentFrameP2 = Loader.getAnimation("P2WALK").getKeyFrame(
					stateTime, true);
		} else {
			currentFrameP2 = Loader.getAnimation("P2ATTACK").getKeyFrame(
					stateTime, true);
		}

		if (fireballVisibleP1) {
			effectsP1 = EffectsLoader.getAnimation("P1FIREBALL").getKeyFrame(
					stateTime, true);
		}
		if (fireballVisibleP2) {
			effectsP2 = EffectsLoader.getAnimation("P2FIREBALL").getKeyFrame(
					stateTime, true);
		}
		
		if(waterballVisibleP1)
		{
			effectsP1 = EffectsLoader.getAnimation("P1WATERBALL").getKeyFrame(
					stateTime, true);
		}
		if(waterballVisibleP2)
		{
			effectsP2 = EffectsLoader.getAnimation("P2WATERBALL").getKeyFrame(
					stateTime, true);
		}
		if(magicShieldVisibleP1)
		{
			effectsP1 = EffectsLoader.getAnimation("MAGICDEFUP").getKeyFrame(
					stateTime, true);
		}
		if(magicShieldVisibleP2)
		{
			effectsP2 = EffectsLoader.getAnimation("MAGICDEFUP").getKeyFrame(
					stateTime, true);
		}
		if(defenseVisibleP1)
		{
			effectsP1 = EffectsLoader.getAnimation("DEFUP").getKeyFrame(
					stateTime, true);
		}
		if(defenseVisibleP2)
		{
			effectsP2 = EffectsLoader.getAnimation("DEFUP").getKeyFrame(
					stateTime, true);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(640, 480, false);
		stage.getCamera().position.set(640 / 2, 480 / 2, 0);

	}

	@Override
	public void show() {

		//returnOverview = (ReturnOverview);
		magicMAXMe = SessionData.getMyStats()[2];
		magicMAXOpponent = SessionData.getOpponentStats()[2];
		
		// visibility
		fireballVisibleP1 = false;
		fireballVisibleP2 = false;
		justCastP1 = false;
		magicShieldVisibleP1 = false;
		magicShieldVisibleP2 = false;
		defenseVisibleP1 = false;
		defenseVisibleP2 = false;
		
		animatePosP1 = 90;
		animatePosP2 = Gdx.graphics.getWidth() - 300;
		justAttackedP1 = false;
		justAttackedP2 = false;

		hit = Gdx.audio.newSound(Gdx.files.internal("Sound/hit.mp3"));

		stage = new Stage();
		table = new Table(); // skin may be needed, which uses texture atlas
		healthOneTable = new Table();
		timerTable = new Table();
		statsTable = new Table();
		nameStatsTable = new Table();
		magicRemainTablePlayer1 = new Table();
		magicRemainTablePlayer2 = new Table();
		characterOne = new Table();
		characterTwo = new Table();
		statusTable = new Table();
		playerInformationTable = new Table();
		healthTwoTable = new Table();
		healthOneYellowTable = new Table();
		healthOneRedTable = new Table();
		healthTwoYellowTable = new Table();
		healthTwoRedTable = new Table();
		magicTable = new Table();

		Gdx.input.setInputProcessor(stage);

		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		healthOneTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		timerTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		statsTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		nameStatsTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		magicRemainTablePlayer1.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		magicRemainTablePlayer2.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		characterOne.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		characterTwo.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		statusTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		playerInformationTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		healthTwoTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		healthOneYellowTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		healthOneRedTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		healthTwoYellowTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		healthTwoRedTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		magicTable.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		batch = new SpriteBatch();
		tweenManager = new TweenManager(); // for animations
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(ImageButton.class, new ImageButtonAccessor());
		Tween.registerAccessor(Image.class, new HealthBarAccessor());
		Tween.registerAccessor(Label.class, new TextAccessor());
		Tween.registerAccessor(Float.class, new IntegerAccessor());

		Texture backgroundText = new Texture("data/back2.gif");
		background = new Sprite(backgroundText);
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Tween.set(background, SpriteAccessor.ALPHA).target(0)
				.start(tweenManager);
		Tween.to(background, SpriteAccessor.ALPHA, 2).target(1)
				.start(tweenManager);
		// END BACKGROUND LOAD

		damageFont = new BitmapFont(Gdx.files.internal("font/timerFont.fnt"),
				false);
		damageFont.setScale(2.5f);
		whiteNonGlow = new BitmapFont(Gdx.files.internal("font/timerFont.fnt"),
				false);
		whiteNonGlow.setScale(0.6f);
		infoFont = new BitmapFont(Gdx.files.internal("font/timerFont.fnt"),
				false);
		infoFont.setScale((float) 0.4);
		mainFont = new BitmapFont(Gdx.files.internal("font/timerFont.fnt"),
				false);
		mainFont.setScale((float) 0.6);
		// Importing fonts
		setLabels();

		// Empty healthbars

		setUpHealthBars();
		setupButtons();

		magicTable.setFillParent(true);
		magicTable.top();
		playerInformationTable.setFillParent(true);
		playerInformationTable.bottom();
		statusTable.setFillParent(true);
		statusTable.bottom();
		table.setFillParent(true);
		table.top();
		healthOneTable.setFillParent(true);
		healthOneTable.top();
		timerTable.setFillParent(true);
		timerTable.top();
		statsTable.setFillParent(true);
		statsTable.top();
		nameStatsTable.setFillParent(true);
		nameStatsTable.top();
		magicRemainTablePlayer1.setFillParent(true);
		magicRemainTablePlayer1.top().left();
		magicRemainTablePlayer2.setFillParent(true);
		magicRemainTablePlayer2.top().right();
		characterOne.setFillParent(true);
		characterOne.bottom().left();
		characterTwo.setFillParent(true);
		characterTwo.bottom().right();
		nameStatsTable.toFront(); // bring to front
		healthTwoTable.setFillParent(true);
		healthTwoTable.top();
		healthOneYellowTable.setFillParent(true);
		healthOneYellowTable.top();
		healthOneRedTable.setFillParent(true);
		healthOneRedTable.top();
		healthTwoYellowTable.setFillParent(true);
		healthTwoYellowTable.top();
		healthTwoRedTable.setFillParent(true);
		healthTwoRedTable.top();

		loadCharacters();
		loadSoundEffects();

		playerInformationTable.add(playerInformationLabel).padBottom(110);

		// characterOne.add(playerOneSprite).padBottom(10);
		// characterTwo.add(playerTwoSprite).padBottom(10);

		magicRemainTablePlayer1.add(magicStatusPlayerOne).padTop(80)
				.padRight(5);
		magicRemainTablePlayer1.debug();

		magicRemainTablePlayer2.add(magicStatusPlayerTwo).padTop(80).padLeft(5);
		magicRemainTablePlayer2.debug();

		table.add(emptyHealthPlayerTwo).width(300).padTop(5).height(70);
		table.add(paddingLabel);
		table.add(emptyHealthPlayerOne).width(300).padTop(5).height(70);
		table.debug();

		healthOneTable.add(healthStatusPlayerOne).width(265).padTop(20)
				.height(20).padLeft(220);
		healthOneYellowTable.add(healthOneYellowImage).width(265).padTop(20)
				.height(20).padLeft(220);
		healthOneRedTable.add(healthOneRedImage).width(265).padTop(20)
				.height(20).padLeft(220);

		healthTwoTable.add(healthStatusPlayerTwo).width(265).padTop(20)
				.height(20).padLeft(310);
		healthTwoYellowTable.add(healthTwoYellowImage).width(265).padTop(20)
				.height(20).padLeft(310);
		healthTwoRedTable.add(healthTwoRedImage).width(265).padTop(20)
				.height(20).padLeft(310);
		// add two in

		timerTable.add(timerLabel).padTop(10);
		timerTable.debug();

		statsTable.add(playerOneLevel).padTop(50).padRight(10);
		statsTable.add(playerOneMagicMAX).padTop(50);
		statsTable.add(slash3).padTop(50);
		statsTable.add(playerOneMagic).padTop(50).padRight(390);
		statsTable.add(playerTwoMagicMAX).padTop(50);
		statsTable.add(slash4).padTop(50);
		statsTable.add(playerTwoMagic).padTop(50).padRight(10);
		statsTable.add(playerTwoLevel).padTop(50);
		statsTable.debug();

		nameStatsTable.add(playerOneName).padTop(55).padRight(50);
		nameStatsTable.add(playerTwoName).padTop(55);
		// nameStatsTable.row();
		// nameStatsTable.add(playerOneMagicMAX);
		// nameStatsTable.add(slash);
		// nameStatsTable.add(playerOneMagic).padRight(50);
		// nameStatsTable.add(playerTwoMagic);
		// nameStatsTable.add(slash);
		// nameStatsTable.add(playerTwoMagicMAX);
		nameStatsTable.row();
		nameStatsTable.add(attackButton).padTop(65);
		nameStatsTable.add(nfcButton).padTop(65);
		nameStatsTable.debug();

		magicTable.add(playerOneHealthLabelMAX).padTop(80);
		magicTable.add(slash).padTop(80);
		magicTable.add(playerOneHealthLabel).padTop(80).padRight(30);

		magicTable.add(playerTwoHealthLabelMAX).padTop(80);
		magicTable.add(slash2).padTop(80);
		magicTable.add(playerTwoHealthLabel).padTop(80);

		statusTable.add(damageControlLabelP1).padBottom(10).padRight(170);
		statusTable.add(damageControlLabelP2).padBottom(10).padLeft(170);
		statusTable.debug();

		stage.addActor(table);
		stage.addActor(healthOneTable);
		stage.addActor(timerTable);
		stage.addActor(statsTable);
		stage.addActor(nameStatsTable);
		stage.addActor(magicRemainTablePlayer1);
		stage.addActor(magicRemainTablePlayer2);
		stage.addActor(characterOne);
		stage.addActor(characterTwo);
		stage.addActor(statusTable);
		stage.addActor(playerInformationTable);
		stage.addActor(healthTwoTable);
		stage.addActor(healthOneYellowTable);
		stage.addActor(healthOneRedTable);
		stage.addActor(healthTwoRedTable);
		stage.addActor(healthTwoYellowTable);
		stage.addActor(magicTable);
		// initial disable
		attackButton.setVisible(false);
		nfcButton.setVisible(false);
		healthOneYellowTable.setVisible(false);
		healthOneRedTable.setVisible(false);
		healthTwoYellowTable.setVisible(false);
		healthTwoRedTable.setVisible(false);

		if (SessionData.isFirstTurn()) {
			// is this users first turn
			attackButton.setVisible(true);
			nfcButton.setVisible(true);
			playerInformationLabel.setText("Your turn");
			Tween.set(attackButton, SpriteAccessor.ALPHA).target(0)
					.start(tweenManager);
			Tween.to(attackButton, SpriteAccessor.ALPHA, 5).target(1)
					.start(tweenManager);
			Tween.set(nfcButton, SpriteAccessor.ALPHA).target(0)
					.start(tweenManager);
			Tween.to(nfcButton, SpriteAccessor.ALPHA, 5).target(1)
					.start(tweenManager);
		} else {
			playerInformationLabel.setText("Waiting for opponent...");
		}

		// playerInformationLabel.setText(""+healthStatusPlayerOne.getScaleY());
		// damageControlLabelP1.setText(""+paddingLabel.getX());

	}

	private void loadSoundEffects() {
		fireballSound = Gdx.audio.newSound(Gdx.files
				.internal("Sound/fireball.mp3"));
		waterballSound = Gdx.audio.newSound(Gdx.files
				.internal("Sound/waterspell.mp3"));
		magicshieldSound= Gdx.audio.newSound(Gdx.files
				.internal("Sound/magicshield.mp3"));
		defenseSound = Gdx.audio.newSound(Gdx.files
				.internal("Sound/defshield.mp3"));
		
		
	}

	private void loadCharacters() {
		FileHandle P1Walk = null;
		FileHandle P1Attack = null;
		FileHandle P2Walk = null;
		FileHandle P2Attack = null;

		Texture playerOneText = null;
		Texture playerTwoText = null;
		// if statement to determine class and attributes later
		if (SessionData.getMyClass().compareTo("Warrior") == 0) {

			Loader.FRAME_COLSP1ATTACK = 4;
			Loader.FRAME_ROWSP1ATTACK = 1;
			Loader.FRAME_COLSP1WALK = 5;
			Loader.FRAME_ROWSP1WALK = 3;
			P1Walk = Gdx.files.internal("spritesheet/warrior/idleP1.png");
			P1Attack = Gdx.files.internal("spritesheet/warrior/attackP1.png");
		} else if (SessionData.getMyClass().compareTo("Paladin") == 0) {
			Loader.FRAME_COLSP1ATTACK = 7;
			Loader.FRAME_ROWSP1ATTACK = 2;
			Loader.FRAME_COLSP1WALK = 2;
			Loader.FRAME_ROWSP1WALK = 3;
			P1Walk = Gdx.files.internal("spritesheet/paladin/idleP1.png");
			P1Attack = Gdx.files.internal("spritesheet/paladin/attackP1.png");
		} else if (SessionData.getMyClass().compareTo("Wizard") == 0) {
			Loader.FRAME_COLSP1ATTACK = 4;
			Loader.FRAME_ROWSP1ATTACK = 1;
			Loader.FRAME_COLSP1WALK = 4;
			Loader.FRAME_ROWSP1WALK = 1;
			P1Walk = Gdx.files.internal("spritesheet/wizard/idleP1.png");
			P1Attack = Gdx.files.internal("spritesheet/wizard/attackP1.png");
		}

		// playerOneSprite = new Image(playerOneText);
		playerOneMagic.setText("" + SessionData.getMyStats()[2]);
		playerOneMagicMAX.setText("" + SessionData.getMyStats()[2]);

		playerOneHealthLabel.setText("" + SessionData.getMyStats()[0]);
		playerOneHealthLabelMAX.setText("" + SessionData.getMyStats()[0]);

		if (SessionData.getOpponentClass().compareTo("Warrior") == 0) {
			Loader.FRAME_COLSP2ATTACK = 4;
			Loader.FRAME_ROWSP2ATTACK = 1;
			Loader.FRAME_COLSP2WALK = 5;
			Loader.FRAME_ROWSP2WALK = 3;
			P2Walk = Gdx.files.internal("spritesheet/warrior/idleP2.png");
			P2Attack = Gdx.files.internal("spritesheet/warrior/attackP2.png");

		} else if (SessionData.getOpponentClass().compareTo("Paladin") == 0) {
			Loader.FRAME_COLSP2ATTACK = 7;
			Loader.FRAME_ROWSP2ATTACK = 2;
			Loader.FRAME_COLSP2WALK = 2;
			Loader.FRAME_ROWSP2WALK = 3;
			P2Walk = Gdx.files.internal("spritesheet/paladin/idleP2.png");
			P2Attack = Gdx.files.internal("spritesheet/paladin/attackP2.png");
		} else if (SessionData.getOpponentClass().compareTo("Wizard") == 0) {
			Loader.FRAME_COLSP2ATTACK = 4;
			Loader.FRAME_ROWSP2ATTACK = 1;
			Loader.FRAME_COLSP2WALK = 4;
			Loader.FRAME_ROWSP2WALK = 1;
			P2Walk = Gdx.files.internal("spritesheet/wizard/idleP2.png");
			P2Attack = Gdx.files.internal("spritesheet/wizard/attackP2.png");
		}

		// use loader
		Loader.setTextures(P1Walk, P1Attack, P2Walk, P2Attack);

		// playerTwoSprite = new Image(playerTwoText);
		playerTwoMagic.setText("" + SessionData.getOpponentStats()[2]);
		playerTwoMagicMAX.setText("" + SessionData.getOpponentStats()[2]);
		playerTwoHealthLabel.setText("" + SessionData.getOpponentStats()[0]);
		playerTwoHealthLabelMAX.setText("" + SessionData.getOpponentStats()[0]);
	}

	private void setupButtons() {
		skin = new Skin();
		skin.add("attackDown", new Texture("HUD/swordicondown.png"));
		skin.add("attackUp", new Texture("HUD/swordiconup.png"));
		skin.add("nfcDown", new Texture("HUD/nfcswipeicondown.png"));
		skin.add("nfcUp", new Texture("HUD/nfcswipeicon.png"));

		ImageButtonStyle attackStyle = new ImageButtonStyle();
		attackStyle.up = skin.getDrawable("attackDown");
		attackStyle.down = skin.getDrawable("attackUp");
		attackStyle.pressedOffsetX = -1;
		attackStyle.pressedOffsetY = -1;

		ImageButtonStyle nfcStyle = new ImageButtonStyle();
		nfcStyle.up = skin.getDrawable("nfcDown");
		nfcStyle.down = skin.getDrawable("nfcUp");
		nfcStyle.pressedOffsetX = -1;
		nfcStyle.pressedOffsetY = -1;

		// load in effects for cards
		EffectsLoader.loadEffects();

		attackButton = new ImageButton(attackStyle);
		nfcButton = new ImageButton(nfcStyle);

		attackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				BluetoothDevice device;
				BluetoothAdapter localDevice = BluetoothAdapter
						.getDefaultAdapter();

				device = localDevice.getRemoteDevice(SessionData
						.getMacAddress());

				// get strength attribute
				attackButton.setVisible(false);
				nfcButton.setVisible(false);
				playerInformationLabel.setText("Waiting on opponent...");

				// calc opponents armour/defense here before sending damage

				String damage = calculateTotalDamage(SessionData.getMyStats()[3],SessionData.getOpponentStats()[1], 20);
				
				BluetoothWrite writer = new BluetoothWrite(JsonBuilder
						.createDamage(damage, "ATTACK"), device);
				writer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						"hello");

				animateOpponent(damage);
			}

		});

		nfcButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

			}

		});

	}
	
	private String calculateTotalMagicDamage(int baseDamage)
	{
		int totalDamage = baseDamage+magicMAXMe/10;
		totalDamage = totalDamage - magicMAXOpponent/10;
		
		return totalDamage+"";
	}
	
	private String calculateTotalDamage(int ap, int meleeDef, int baseDamage)
	{
		int totalDamage = baseDamage - (meleeDef/10);
		totalDamage = totalDamage + (ap/10);
		
		
		return totalDamage+"";
	}

	private void setLabels() {
		LabelStyle padding = new LabelStyle(whiteNonGlow, Color.WHITE);
		LabelStyle timeFont = new LabelStyle(whiteNonGlow, Color.RED); // use as
																		// damage
																		// too
		LabelStyle infoStyle = new LabelStyle(infoFont, Color.WHITE);
		LabelStyle mainStyle = new LabelStyle(mainFont, Color.BLACK);

		LabelStyle healFont = new LabelStyle(whiteNonGlow, Color.GREEN);

		paddingLabel = new Label("   ", padding);
		timerLabel = new Label("30", timeFont);

		playerOneLevel = new Label("Lvl " + "2", infoStyle); // match data
																// stores values
		playerTwoLevel = new Label("Lvl " + "1", infoStyle);

		playerOneHealthLabel = new Label("HP", mainStyle);
		playerTwoHealthLabel = new Label("HP", mainStyle);
		playerOneHealthLabelMAX = new Label("", mainStyle);
		playerTwoHealthLabelMAX = new Label("", mainStyle);

		playerOneName = new Label("Player 1", mainStyle);
		playerTwoName = new Label("Player 2", mainStyle);
		playerOneMagic = new Label("", infoStyle);
		playerTwoMagic = new Label("", infoStyle);
		playerOneMagicMAX = new Label("", infoStyle);
		playerTwoMagicMAX = new Label("", infoStyle);

		damageControlLabelP1 = new Label("", healFont);
		damageControlLabelP2 = new Label("", healFont);

		playerInformationLabel = new Label("Player information", mainStyle);
		slash = new Label("/", mainStyle);
		slash2 = new Label("/", mainStyle);
		slash3 = new Label("/", mainStyle);
		slash4 = new Label("/", mainStyle);

	}

	private void setUpHealthBars() {
		Texture playerOneHealthText = new Texture("HUD/emptyBarRight.png");
		emptyHealthPlayerOne = new Image(playerOneHealthText);

		Texture playerTwoHealthText = new Texture("HUD/emptyBarLeft.png");
		emptyHealthPlayerTwo = new Image(playerTwoHealthText);

		playerOneHealthStatus = new Texture("HUD/vectorHealth.png");

		healthStatusPlayerOne = new Image(playerOneHealthStatus);
		healthStatusPlayerOne.setScaleX(-1);

		Texture playerTwoHealthStatus = new Texture("HUD/vectorHealth.png");
		healthStatusPlayerTwo = new Image(playerTwoHealthStatus);

		Texture playerOneMagicStatus = new Texture("HUD/manaiconsmaller.png");
		magicStatusPlayerOne = new Image(playerOneMagicStatus);

		Texture playerTwoMagicStatus = new Texture("HUD/manaiconsmaller.png");
		magicStatusPlayerTwo = new Image(playerTwoMagicStatus);

		// health part
		Texture healthOneYellowText = new Texture("HUD/vectorhealth3.png");
		healthOneYellowImage = new Image(healthOneYellowText);
		healthOneYellowImage.setScaleX(-1);

		Texture healthOneRedText = new Texture("HUD/vectorhealth4.png");
		healthOneRedImage = new Image(healthOneRedText);
		healthOneRedImage.setScaleX(-1);

		healthTwoYellowImage = new Image(healthOneYellowText);
		healthTwoRedImage = new Image(healthOneRedText);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		hit.dispose();
		fireballSound.dispose();
		waterballSound.dispose();
		batch.dispose();
		magicshieldSound.dispose();
		background.getTexture().dispose();
		// END BACKGROUND
		stage.dispose();

	}

	public void updateUi() {
		playerOneName.setText("BOB");

	}

	//player 2
	private void finishEffectsP1() {
		int modifierInt = Integer.parseInt(modifierP2);
		int original = Integer.parseInt(playerTwoHealthLabel.getText()
				.toString());

		original = original - modifierInt;

		playerTwoHealthLabel.setText("" + original);

		float max = Float.parseFloat(playerTwoHealthLabelMAX.getText()
				.toString());
		// float damage = ((healthStatusPlayerOne.getScaleX()/max *
		// modifierInt));
		float toMinus = (modifierInt / max);

		// to avoid over flipping if at 0
		hit.play();
		if (original <= 1) {
			original = 0;
			toMinus = healthStatusPlayerTwo.getScaleX();
			playerTwoHealthLabel.setText("0");
		}

		if ((original >= ((int) max / 2)) && (original <= ((int) max / 4))) {
			// set here
			healthStatusPlayerTwo.setVisible(false);
			healthTwoYellowTable.setVisible(true);
			healthTwoRedTable.setVisible(false);
		} else if (original <= max / 4) {
			healthStatusPlayerTwo.setVisible(false);
			healthTwoYellowTable.setVisible(false);
			healthTwoRedTable.setVisible(true);
		} else if (original >= max / 2) {
			healthStatusPlayerTwo.setVisible(true);
			healthTwoYellowTable.setVisible(false);
			healthTwoRedTable.setVisible(false);
		}

		Tween.set(healthStatusPlayerTwo, HealthBarAccessor.SIZE)
				.target(healthStatusPlayerTwo.getScaleX()).start(tweenManager);
		Tween.to(healthStatusPlayerTwo, HealthBarAccessor.SIZE, 1)
				.target(healthStatusPlayerTwo.getScaleX() - toMinus)
				.start(tweenManager);
		Tween.set(healthTwoYellowImage, HealthBarAccessor.SIZE)
				.target(healthTwoYellowImage.getScaleX()).start(tweenManager);
		Tween.to(healthTwoYellowImage, HealthBarAccessor.SIZE, 1)
				.target(healthTwoYellowImage.getScaleX() - toMinus)
				.start(tweenManager);
		Tween.set(healthTwoRedImage, HealthBarAccessor.SIZE)
				.target(healthTwoRedImage.getScaleX()).start(tweenManager);
		Tween.to(healthTwoRedImage, HealthBarAccessor.SIZE, 1)
				.target(healthTwoRedImage.getScaleX() - toMinus)
				.start(tweenManager);

		// send damage to opponent too

		// update damage text...
		damageControlLabelP2.setStyle(new LabelStyle(damageFont, Color.RED));
		damageControlLabelP2.setText("-" + modifierP2);

		Tween.set(damageControlLabelP2, TextAccessor.SIZE)
				.target(damageControlLabelP2.getY()).start(tweenManager);
		Tween.to(damageControlLabelP2, TextAccessor.SIZE, 2).target(150)
				.start(tweenManager);
		Tween.set(damageControlLabelP2, TextAccessor.ALPHA).target(1)
				.start(tweenManager);
		Tween.to(damageControlLabelP2, TextAccessor.ALPHA, 2).target(0)
				.start(tweenManager);
	}

	// note this is called on OUR attack
	private void animateOpponent(String damage) {

		justCastP1 = true;
		this.modifierP2 = damage;

		animatePosP1 = Gdx.graphics.getWidth() - 400;
		justAttackedP1 = true;
		Tween.set(damageControlLabelP1, TextAccessor.SIZE)
				.target(damageControlLabelP1.getY()).start(tweenManager);
		Tween.to(damageControlLabelP1, TextAccessor.SIZE, 3)
				.target(damageControlLabelP1.getY()).start(tweenManager)
				.setCallback(cbp2).setCallbackTriggers(TweenCallback.COMPLETE);

	}

	/**
	 * Callback set after player moves
	 */
	private TweenCallback cbp1 = new TweenCallback() {

		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			justAttackedP2 = false;
			animatePosP2 = Gdx.graphics.getWidth() - 400;
			finishEffects();
		}
	};

	/**
	 * Callback set after player moves
	 */
	private TweenCallback cbp2 = new TweenCallback() {

		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			justAttackedP1 = false;
			animatePosP1 = 90;
			finishEffectsP1();
		}
	};

	private boolean fireballVisibleP2;

	// note this is called on opponent attack
	public void animateEffects(String modifier) {
		// tween here
		this.modifierP1 = modifier;

		animatePosP2 = 90;
		justAttackedP2 = true;
		Tween.set(damageControlLabelP1, TextAccessor.SIZE)
				.target(damageControlLabelP1.getY()).start(tweenManager);
		Tween.to(damageControlLabelP1, TextAccessor.SIZE, 3)
				.target(damageControlLabelP1.getY()).start(tweenManager)
				.setCallback(cbp1).setCallbackTriggers(TweenCallback.COMPLETE);

	}

	public void animateIncomingFireball(String damage) {
		
		modifierP1 = damage;
		fireballSound.play();
		fireballPosX = (float) (Gdx.graphics.getWidth() - 400);
		fireballVisibleP2 = true;
		// disable buttons
		attackButton.setVisible(true);
		nfcButton.setVisible(true);
		playerInformationLabel.setText("Your turn");

		// damage can be calculated here because we know that a fireball has
		// come in
		// Tween.set(effectsP1,
		// IntegerAccessor.MOVERIGHT).target(90).start(tweenManager);
		Tween.set(fireballPosX, IntegerAccessor.MOVERIGHT)
				.target(Gdx.graphics.getWidth() - 400).start(tweenManager);
		Tween.to(fireballPosX, IntegerAccessor.MOVERIGHT, 2).target(90.0f)
				.start(tweenManager).setCallback(fireballCallbackP2);
	}
	
	private boolean waterballVisibleP2;
	
	private TweenCallback fireballCallbackP2 = new TweenCallback() {

		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			fireballVisibleP2 = false;
			waterballVisibleP2 = false;
			finishEffects();
		}
	};

	
	//player one
	private void finishEffects() {

		int modifierInt = Integer.parseInt(modifierP1);
		int original = Integer.parseInt(playerOneHealthLabel.getText()
				.toString());

		original = original - modifierInt;

		playerOneHealthLabel.setText("" + original);

		float max = Float.parseFloat(playerOneHealthLabelMAX.getText()
				.toString());
		// float damage = ((healthStatusPlayerOne.getScaleX()/max *
		// modifierInt));
		float toMinus = (modifierInt / max);

		// to avoid over flipping if at 0
		hit.play();
		if (original <= 1) {
			original = 0;
			toMinus = healthStatusPlayerOne.getScaleX();
			playerOneHealthLabel.setText("0");
		}

		if ((original <= max / 2) && (original >= max / 4)) {
			// set here
			healthStatusPlayerOne.setVisible(false);
			healthOneYellowTable.setVisible(true);
			healthOneRedTable.setVisible(false);
		} else if (original <= max / 4) {
			healthStatusPlayerOne.setVisible(false);
			healthOneYellowTable.setVisible(false);
			healthOneRedTable.setVisible(true);
		} else if (original >= max / 2) {
			healthStatusPlayerOne.setVisible(true);
			healthOneYellowTable.setVisible(false);
			healthOneRedTable.setVisible(false);
		}

		Tween.set(healthStatusPlayerOne, HealthBarAccessor.SIZE)
				.target(healthStatusPlayerOne.getScaleX()).start(tweenManager);
		Tween.to(healthStatusPlayerOne, HealthBarAccessor.SIZE, 1)
				.target(healthStatusPlayerOne.getScaleX() + toMinus)
				.start(tweenManager);
		Tween.set(healthOneYellowImage, HealthBarAccessor.SIZE)
				.target(healthOneYellowImage.getScaleX()).start(tweenManager);
		Tween.to(healthOneYellowImage, HealthBarAccessor.SIZE, 1)
				.target(healthOneYellowImage.getScaleX() + toMinus)
				.start(tweenManager);
		Tween.set(healthOneRedImage, HealthBarAccessor.SIZE)
				.target(healthOneRedImage.getScaleX()).start(tweenManager);
		Tween.to(healthOneRedImage, HealthBarAccessor.SIZE, 1)
				.target(healthOneRedImage.getScaleX() + toMinus)
				.start(tweenManager);

		// send damage to opponent too

		// update damage text...
		damageControlLabelP1.setStyle(new LabelStyle(damageFont, Color.RED));
		damageControlLabelP1.setText("-" + modifierP1);

		Tween.set(damageControlLabelP1, TextAccessor.SIZE)
				.target(damageControlLabelP1.getY()).start(tweenManager);
		Tween.to(damageControlLabelP1, TextAccessor.SIZE, 2).target(150)
				.start(tweenManager);
		Tween.set(damageControlLabelP1, TextAccessor.ALPHA).target(1)
				.start(tweenManager);
		Tween.to(damageControlLabelP1, TextAccessor.ALPHA, 2).target(0)
				.start(tweenManager);

		// send message to update opponents UI with their effect - DON't
		// actually faster to update same side, less network transit/delay
		checkLoseCondition(original);
	}
	
	public void winScenario(String text)
	{
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ListenerHolder.getActivity());
        builder.setMessage(text).setCancelable(false)
               .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       
                	   GameStarter.gameFinished = true;
                	   //fire interface off to the main class
                	   Intent i = new Intent(ListenerHolder.getContext(),BattleOverview.class);
                	   i.putExtra("WINCOND", winOrLose);
                	  ListenerHolder.getActivity().startActivity(i);
                	   
                   }
               });
               
        // Create the AlertDialog object and return it
        builder.create().show();
	}
	
	public void disconnectedDialog()
	{
		ListenerHolder.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			
				AlertDialog.Builder builder = new AlertDialog.Builder(ListenerHolder.getActivity());
		        builder.setMessage("Opponent disconnected").setCancelable(false)
		               .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       
		                	   GameStarter.gameFinished = true;
		                	   //fire interface off to the main class
		                	   Intent i = new Intent(ListenerHolder.getContext(),MainActivity.class);
		                	   
		                	  ListenerHolder.getActivity().startActivity(i);
		                	   
		                   }
		               });	
				
			}
		});
	}
	
	private void checkLoseCondition(int original)
	{
		if(original == 0 || original < 0)
		{
			BluetoothDevice device;
			BluetoothAdapter localDevice = BluetoothAdapter.getDefaultAdapter();

			device = localDevice.getRemoteDevice(SessionData.getMacAddress());
			//write lose
			JSONObject lose = new JSONObject();
			try{
			
			lose.put("MESSAGETYPE", "5"); //5 means win or lose
			lose.put("LOSEFLAG", "lost");
			} catch(JSONException e)
			{
				
			}
			BluetoothWrite writer = new BluetoothWrite(lose.toString(), device);
			writer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "hello");
			winOrLose = false;
			
			//winScenario("Oh no! You have been defeated!");
			loseScenario();
			
		}
	}
	
	private void loseScenario()
	{
		ListenerHolder.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(ListenerHolder.getActivity());
		        builder.setMessage("Oh no you have been defeated!").setCancelable(false)
		               .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       
		                	   GameStarter.gameFinished = true;
		                	   //fire interface off to the main class
		                	   Intent i = new Intent(ListenerHolder.getContext(),BattleOverview.class);
		                	   i.putExtra("WINCOND", winOrLose);
		                	  ListenerHolder.getActivity().startActivity(i);
		                	   
		                   }
		               });
		               
		        // Create the AlertDialog object and return it
		        builder.create().show();
			}
		});
		
			
	}

	public void passTurn() {
		attackButton.setVisible(true);
		nfcButton.setVisible(true);
		Tween.set(attackButton, SpriteAccessor.ALPHA).target(0)
				.start(tweenManager);
		Tween.to(attackButton, SpriteAccessor.ALPHA, 3).target(1)
				.start(tweenManager);
		Tween.set(nfcButton, SpriteAccessor.ALPHA).target(0)
				.start(tweenManager);
		Tween.to(nfcButton, SpriteAccessor.ALPHA, 3).target(1)
				.start(tweenManager);

		playerInformationLabel.setText("Your turn");
		justCastP1 = false;
	}

	private TweenCallback fireballCallbackP1 = new TweenCallback() {

		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			fireballVisibleP1 = false;
			waterballVisibleP1 = false;
			finishEffectsP1();

		}
	};

	private boolean justCastP1;

	public static Float waterballPosX;

	private boolean waterballVisibleP1;

	private boolean magicShieldVisibleP1;

	private boolean magicShieldVisibleP2;

	// fireball animation
	public void fireBallEffect() {

		if (!justCastP1) {
			modifierP2 = calculateTotalMagicDamage(30);
			fireballSound.play();
			fireballPosX = 90.0f;
			fireballVisibleP1 = true;
			// disable buttons
			attackButton.setVisible(false);
			nfcButton.setVisible(false);
			playerInformationLabel.setText("Waiting on opponent...");

			// damage can be calculated here because we know that a fireball has
			// come in
			// Tween.set(effectsP1,
			// IntegerAccessor.MOVERIGHT).target(90).start(tweenManager);
			Tween.set(fireballPosX, IntegerAccessor.MOVERIGHT).target(90.0f)
					.start(tweenManager);
			Tween.to(fireballPosX, IntegerAccessor.MOVERIGHT, 2)
					.target(Float.valueOf(Gdx.graphics.getWidth() - 400))
					.start(tweenManager).setCallback(fireballCallbackP1);
			writeData(JsonBuilder.createDamage(calculateTotalMagicDamage(30), "FIREBALL"));
		}
		justCastP1 = true;
		// Tween.to(effectsP1, IntegerAccessor.MOVERIGHT,
		// 2).target(Integer.valueOf(Gdx.graphics.getWidth() -
		// 400)).start(tweenManager);

		// double value
		
	}

	private void writeData(String data) {
		BluetoothDevice device;
		BluetoothAdapter localDevice = BluetoothAdapter.getDefaultAdapter();

		device = localDevice.getRemoteDevice(SessionData.getMacAddress());

		// get strength attribute
		attackButton.setVisible(false);
		nfcButton.setVisible(false);
		playerInformationLabel.setText("Waiting on opponent...");

		// calc opponents armour/defense here before sending damage

		BluetoothWrite writer = new BluetoothWrite(data, device);
		writer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "hello");
	}

	public void magicDefenseEffect() {
		
		if(!justCastP1){
		magicshieldSound.play();
		magicShieldVisibleP1 = true;
		// magicSoundPosX = 90;
		// magicSoundVisibleP1 = true;

		 //a tween of two seconds to allow for callback
		 Tween.set(waterballPosX, IntegerAccessor.WATER).target(90.0f)
			.start(tweenManager);
		 Tween.to(waterballPosX, IntegerAccessor.WATER, 2)
			.target(Float.valueOf(Gdx.graphics.getWidth() - 400))
			.start(tweenManager).setCallback(magicDefenseEffectCallbackP1);
		 
		 writeData(JsonBuilder.createDamage("", "MAGICDEFENSEUP"));
		}
		 justCastP1 = true;
	}
	
	public void opponentMagicDefenseEffect()
	{
		magicshieldSound.play();
		magicShieldVisibleP2 = true;
		// magicSoundPosX = 90;
		// magicSoundVisibleP1 = true;

		 //a tween of two seconds to allow for callback
		 Tween.set(waterballPosX, IntegerAccessor.WATER).target(90.0f)
			.start(tweenManager);
		 Tween.to(waterballPosX, IntegerAccessor.WATER, 2)
			.target(Float.valueOf(Gdx.graphics.getWidth() - 400))
			.start(tweenManager).setCallback(magicDefenseEffectCallbackP2);
		 
		 
	}
	
	private TweenCallback magicDefenseEffectCallbackP2 = new TweenCallback() {
		
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			damageControlLabelP2.setStyle(new LabelStyle(mainFont, Color.BLUE));
			damageControlLabelP2.setText("Mdef +10");

			//set magic def
			
			Tween.set(damageControlLabelP2, TextAccessor.SIZE)
					.target(damageControlLabelP2.getY()).start(tweenManager);
			Tween.to(damageControlLabelP2, TextAccessor.SIZE, 2).target(150)
					.start(tweenManager);
			Tween.set(damageControlLabelP2, TextAccessor.ALPHA).target(1)
					.start(tweenManager);
			Tween.to(damageControlLabelP2, TextAccessor.ALPHA, 2).target(0)
					.start(tweenManager);
			
			magicShieldVisibleP2 = false;
		}
	};

	private TweenCallback magicDefenseEffectCallbackP1 = new TweenCallback() {
		
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			damageControlLabelP1.setStyle(new LabelStyle(mainFont, Color.BLUE));
			damageControlLabelP1.setText("Mdef +10");

			//set magic def
			
			Tween.set(damageControlLabelP1, TextAccessor.SIZE)
					.target(damageControlLabelP1.getY()).start(tweenManager);
			Tween.to(damageControlLabelP1, TextAccessor.SIZE, 2).target(150)
					.start(tweenManager);
			Tween.set(damageControlLabelP1, TextAccessor.ALPHA).target(1)
					.start(tweenManager);
			Tween.to(damageControlLabelP1, TextAccessor.ALPHA, 2).target(0)
					.start(tweenManager);
			
			magicShieldVisibleP1 = false;
		}
	};

	private int defensePosX;

	private boolean defenseVisibleP1;
	
	public void defenseEffect() {
		
		if(!justCastP1){
		 defenseSound.play();
		 defensePosX = 90;
		 defenseVisibleP1 = true;
		 Tween.set(waterballPosX, IntegerAccessor.WATER).target(90.0f)
			.start(tweenManager);
		 Tween.to(waterballPosX, IntegerAccessor.WATER, 2)
			.target(Float.valueOf(Gdx.graphics.getWidth() - 400))
			.start(tweenManager).setCallback(defenseEffectCallbackP1);
		 
		 writeData(JsonBuilder.createDamage("", "DEFENSEUP"));
		}
		 justCastP1 = true;
	}
	
	private TweenCallback defenseEffectCallbackP1 = new TweenCallback() {
		
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			damageControlLabelP1.setStyle(new LabelStyle(mainFont, Color.ORANGE));
			damageControlLabelP1.setText("DEF +10");

			//set magic def
			
			Tween.set(damageControlLabelP1, TextAccessor.SIZE)
					.target(damageControlLabelP1.getY()).start(tweenManager);
			Tween.to(damageControlLabelP1, TextAccessor.SIZE, 2).target(150)
					.start(tweenManager);
			Tween.set(damageControlLabelP1, TextAccessor.ALPHA).target(1)
					.start(tweenManager);
			Tween.to(damageControlLabelP1, TextAccessor.ALPHA, 2).target(0)
					.start(tweenManager);
			
			defenseVisibleP1 = false;
		}
	};

	private boolean defenseVisibleP2;

	public void waterballEffect() {
		
		if (!justCastP1) {
		 modifierP2 = calculateTotalMagicDamage(25);
		 waterballSound.play();
		 waterballPosX = 90.0f;
		 waterballVisibleP1 = true;
		 
		 attackButton.setVisible(false);
		 nfcButton.setVisible(false);
		 playerInformationLabel.setText("Waiting on opponent...");

			// damage can be calculated here because we know that a fireball has
			// come in
			// Tween.set(effectsP1,
			// IntegerAccessor.MOVERIGHT).target(90).start(tweenManager);
			Tween.set(waterballPosX, IntegerAccessor.WATER).target(90.0f)
					.start(tweenManager);
			Tween.to(waterballPosX, IntegerAccessor.WATER, 2)
					.target(Float.valueOf(Gdx.graphics.getWidth() - 400))
					.start(tweenManager).setCallback(fireballCallbackP1);
			
			
			writeData(JsonBuilder.createDamage(calculateTotalMagicDamage(25), "WATERBALL"));
		}
		justCastP1 = true;
	}
	
	private TweenCallback defenseEffectCallbackP2 = new TweenCallback() {
		
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// TODO Auto-generated method stub
			damageControlLabelP2.setStyle(new LabelStyle(mainFont, Color.ORANGE));
			damageControlLabelP2.setText("DEF +10");

			//set magic def
			
			Tween.set(damageControlLabelP2, TextAccessor.SIZE)
					.target(damageControlLabelP2.getY()).start(tweenManager);
			Tween.to(damageControlLabelP2, TextAccessor.SIZE, 2).target(150)
					.start(tweenManager);
			Tween.set(damageControlLabelP2, TextAccessor.ALPHA).target(1)
					.start(tweenManager);
			Tween.to(damageControlLabelP2, TextAccessor.ALPHA, 2).target(0)
					.start(tweenManager);
			
			defenseVisibleP2 = false;
		}
	};
	
	public void opponentDefenseEffect()
	{
		defenseSound.play();
		defenseVisibleP2 = true;
		defensePosX = Gdx.graphics.getWidth() - 400;
		// magicSoundPosX = 90;
		// magicSoundVisibleP1 = true;

		 //a tween of two seconds to allow for callback
		 Tween.set(waterballPosX, IntegerAccessor.WATER).target(90.0f)
			.start(tweenManager);
		 Tween.to(waterballPosX, IntegerAccessor.WATER, 2)
			.target(Float.valueOf(Gdx.graphics.getWidth() - 400))
			.start(tweenManager).setCallback(defenseEffectCallbackP2);
	}
	
	//incoming
	public void animateIncomingWaterball(String damage)
	{
		modifierP1 = damage;
		waterballSound.play();
		waterballPosX = (float) (Gdx.graphics.getWidth() - 400);
		waterballVisibleP2 = true;
		// disable buttons
		attackButton.setVisible(true);
		nfcButton.setVisible(true);
		playerInformationLabel.setText("Your turn");

		// damage can be calculated here because we know that a fireball has
		// come in
		// Tween.set(effectsP1,
		// IntegerAccessor.MOVERIGHT).target(90).start(tweenManager);
		Tween.set(waterballPosX, IntegerAccessor.WATER)
				.target(Gdx.graphics.getWidth() - 400).start(tweenManager);
		Tween.to(waterballPosX, IntegerAccessor.WATER, 2).target(90.0f)
				.start(tweenManager).setCallback(fireballCallbackP2);
	}

}
