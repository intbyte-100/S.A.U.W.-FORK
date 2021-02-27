package com.kgc.sauw.screen;

import com.badlogic.gdx.Screen;
import com.kgc.sauw.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;
import java.util.*;
import com.kgc.sauw.InterfaceAPI.Button;
import com.kgc.sauw.InterfaceAPI.Interface;
import java.io.File;
import java.io.FileReader;
import org.json.JSONObject;
import java.io.FileWriter;
import com.kgc.sauw.InterfaceAPI.InterfaceEvents;
import com.kgc.sauw.InterfaceAPI.EditText;
import com.badlogic.gdx.files.FileHandle;

public class MenuScreen implements Screen {
	boolean gameStart = false;
	MainGame game;
	Random random = new Random();
	int xC, yC;
	int w, h;
	SpriteBatch b;
	Camera2D camera;
	int WIDTH;
	Button startButton;
	Button settingsButton;
	Button modsButton;
	Button exitButton;
	
	
	Button closeButton;
	World world;
	Blocks blocks;
	Textures t;
	Timer timer = new Timer();
	float tmr;
	int camX, camY;
	boolean StartGameMenu = false;
	int SAUW_coins = 0;
	Button sel_0;
	Button sel_1;
	Button sel_2;
	Button createNewWorld;
	Button up;
	Button down;
	private String result = "";
	Interface createWorldInterface;
	JSONObject data;
	Settings settings;
	Langs langs;
	private Music music;
	public SettingsScreen SettingsScreen;
	public ModsScreen ModsScreen;
	BitmapFont bf = new BitmapFont(Gdx.files.internal("ttf.fnt"));

	private int worldSelIndex = 0;
	String[] worldNames;
	public MenuScreen(final MainGame game) {
		WIDTH = Gdx.graphics.getWidth();
		FileHandle settings = Gdx.files.external("S.A.U.W./User/settings.json");
		if (!settings.exists()) {
            try {
				settings.mkdirs();
				settings.writeString(Gdx.files.internal("json/settings.json").readString(), false);
			} catch (Exception e) {

			}
		}
		this.settings = new Settings();
		langs = new Langs(this.settings);
		try {
			FileHandle data = Gdx.files.external("S.A.U.W./User/data.json");
			result = data.readString();
			this.data = new JSONObject(result);

			SAUW_coins = this.data.getInt("SAUW_Coins");
		} catch (Exception e) {
			Gdx.app.log("error", e.toString());
		}
		this.game = game;
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		t = new Textures();
		Items items = new Items(t, langs);
		t.load();
		timer.schedule(new TimerTask(){
				@Override
				public void run() {
					xC = random.nextInt(11) - 5;
					yC = random.nextInt(11) - 5;
				}
			}, 0, 5000);
		camera = new Camera2D();
		b = new SpriteBatch();
		SettingsScreen = new SettingsScreen(game, t, this);
		ModsScreen = new ModsScreen(game, t, this);
		startButton = new Button("", w / 16 * 5, h - w / 16 * 5 + w / 128, w / 16 * 6, w / 16);
		//startButton.setTextColor(Color.BLACK);
		startButton.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					StartGameMenu = true;
				}
			});
		settingsButton = new Button("", w / 16 * 5, h - w / 16 * 6, w / 16 * 6, w / 16);
		//settingsButton.setTextColor(Color.BLACK);
		settingsButton.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					game.setScreen(SettingsScreen);
				}
			});
		modsButton = new Button("", w / 16 * 5, h - w / 16 * 7 - w / 128, w / 16 * 6, w / 16);
		//exitButton.setTextColor(Color.BLACK);
		modsButton.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					game.setScreen(ModsScreen);
				}
			});
		exitButton = new Button("", w / 16 * 5, h - w / 16 * 8 - w / 128 * 2, w / 16 * 6, w / 16);
		//exitButton.setTextColor(Color.BLACK);
		exitButton.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					Gdx.app.exit();
				}
			});

		FileHandle worldsFolder = Gdx.files.external("S.A.U.W./Worlds/");
		FileHandle[] files = worldsFolder.list();
		int i = 0;
		for (FileHandle file : files) {
			if (file.isDirectory()) i++;
		}

			worldNames = new String[i];
			int ii = 0;
			for (int j = 0; j < files.length; j++) {
				if (files[j].isDirectory()) worldNames[ii] = files[j].name();
				ii++;
			}

		sel_0 = new Button("", w / 16 * 5, h - w / 16 * 5 + w / 128, w / 16 * 6, w / 16);
		//sel_0.setTextColor(Color.BLACK);
		sel_0.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
			        loadGame(worldNames[worldSelIndex]);
				}
			});
		sel_1 = new Button("", w / 16 * 5, h - w / 16 * 6, w / 16 * 6, w / 16);
		//sel_1.setTextColor(Color.BLACK);
        sel_1.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
			        loadGame(worldNames[worldSelIndex + 1]);
				}
			});
		sel_2 = new Button("", w / 16 * 5, h - w / 16 * 7 - w / 128, w / 16 * 6, w / 16);
        //sel_2.setTextColor(Color.BLACK);
		sel_2.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
			        loadGame(worldNames[worldSelIndex + 2]);
				}
			});
		closeButton = new Button("", 0, (h - w / 16), w / 16, w / 16, t.button_left_0, t.button_left_1);
		closeButton.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					StartGameMenu = false;
				}
			});
		createWorldInterface = new Interface(Interface.InterfaceSizes.FULL, t, b, camera, items, null);
		createWorldInterface.setHeaderText(langs.getString("createNewWorld"));
		createWorldInterface.setInterfaceEvents(new InterfaceEvents(){
                EditText worldName;
				Button create;
				BitmapFont bf = new BitmapFont(Gdx.files.internal("ttf.fnt"));
				@Override
				public void initialize() {
					worldName = new EditText((int)(Interface.x + WIDTH / 16), (int)(Interface.y + Interface.heigth - WIDTH / 16 * 3), WIDTH / 16 * 9, WIDTH / 16, camera);
					bf.setScale(WIDTH / 16 / 2 / bf.getCapHeight());
					bf.setColor(Color.BLACK);
				    create = new Button("create", WIDTH / 32, WIDTH / 32, WIDTH / 16 * 3, WIDTH / 16);
					create.setText(langs.getString("create"));
					//create.setTextColor(Color.BLACK);
					create.setEventListener(new Button.EventListener(){
							@Override
							public void onClick() {
								for (int i = 0; i < worldNames.length; i++) {
									if (worldName.input.equals(worldNames[i])) return;
								}
								loadGame(worldName.input);
							}
						});
					Interface.buttons.add(create);
				}

				@Override
				public void tick() {
					worldName.update();
				}

				@Override
				public void onOpen() {
					worldName.input = langs.getString("newWorld");
				}

				@Override
				public void renderBefore() {
				}

				@Override
				public void render() {
					bf.draw(b, langs.getString("WorldName"), worldName.X + camera.X, worldName.Y + worldName.h + WIDTH / 16 + camera.Y);
					worldName.render(b);
				}
			});
		createNewWorld = new Button("", WIDTH / 32, WIDTH / 32, WIDTH / 16 * 6, WIDTH / 16);
		//createNewWorld.setTextColor(Color.BLACK);
		createNewWorld.setText(langs.getString("createNewWorld"));
        createNewWorld.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					createWorldInterface.open();
				}
			});
		HideButtonsIfNeed();
		setSelectButtonsText();
		up = new Button("", w / 32 * 23, sel_0.Y, w / 16, w / 16, t.button_up_0, t.button_up_1);
		up.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					worldSelIndex--;
					if (worldSelIndex < 0) worldSelIndex = 0;
					HideButtonsIfNeed();
					setSelectButtonsText();
				}
			});
		down = new Button("", w / 32 * 23, sel_2.Y, w / 16, w / 16, t.button_down_0, t.button_down_1);
		down.setEventListener(new Button.EventListener(){
				@Override
				public void onClick() {
					worldSelIndex++;
					if (worldSelIndex >= worldNames.length) worldSelIndex = worldNames.length - 1;
					HideButtonsIfNeed();
					setSelectButtonsText();
				}
			});
		blocks = new Blocks(t, langs);
		world = new World(b, t, items, camera, blocks);
		String lastWorld = null;
		try {
			JSONObject data = new JSONObject(Gdx.files.external("S.A.U.W./User/data.json").readString());
			lastWorld = data.getString("lastWorld");
		} catch (Exception e) {

		}
		if (lastWorld != null) {
			if (Gdx.files.external("S.A.U.W./Worlds/" + lastWorld).exists())
				world.load(lastWorld);
			else world.createNewWorld();
		} else {
			world.createNewWorld();
		}
		startButton.setText(langs.getString("startGame"));
		settingsButton.setText(langs.getString("settings"));
		modsButton.setText(langs.getString("mods"));
		exitButton.setText(langs.getString("exit"));
		
		
		music = new Music(this.settings, null);
	}
	public void loadGame(String worldName) {
		SettingsScreen.dispose();
		dispose();
		game.setScreen(new MyGdxGame(game, t, b, music, worldName));
	}
	public void setSelectButtonsText() {
		if (!sel_0.isHided() && worldSelIndex < worldNames.length) sel_0.setText(worldNames[worldSelIndex]);
		if (!sel_1.isHided() && worldSelIndex + 1 < worldNames.length) sel_1.setText(worldNames[worldSelIndex + 1]);
		if (!sel_2.isHided() && worldSelIndex + 2 < worldNames.length) sel_2.setText(worldNames[worldSelIndex + 2]);
	}
	public void HideButtonsIfNeed() {
		if (worldSelIndex >= worldNames.length) sel_0.hide(true);
		else sel_0.hide(false);
		if (worldSelIndex + 1 >= worldNames.length) sel_1.hide(true);
		else sel_1.hide(false);
		if (worldSelIndex + 2 >= worldNames.length) sel_2.hide(true);
		else sel_2.hide(false);
	}
	@Override
    public void show() {

    }
    @Override
    public void render(float delta) {
		music.update(true);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camX += xC;
		camY += yC;
		if (camX < WIDTH / 16) camX = (int)WIDTH / 16;
		if (camY < WIDTH / 16) camY = (int)WIDTH / 16;
		if (camX + camera.W > (world.maps.map0[0].length - 1) * (WIDTH / 16)) camX = (int)((world.maps.map0[0].length - 1) * (WIDTH / 16) - camera.W);
		if (camY + camera.H > (world.maps.map0.length - 1) * (WIDTH / 16)) camY = (int)((world.maps.map0.length - 1) * (WIDTH / 16) - camera.H);
		camera.lookAt(camX, camY);
		camera.update(b);
		//startButton.setText("" + cam.X + " " + cam.Y);
		b.begin();
		b.setColor(0.6f, 0.6f, 0.6f, 1);
		world.renderLowLayer();
		world.renderHighLayer();
		b.setColor(1, 1, 1, 1);
		b.draw(t.logo, camera.X + w / 16 * 5, camera.Y + h - w / 16 * 4, w / 16 * 6, w / 16 * 3);
		if (!StartGameMenu) {
			startButton.update(camera);
			settingsButton.update(camera);
			modsButton.update(camera);
			exitButton.update(camera);
			startButton.render(b, camera);
			settingsButton.render(b, camera);
			modsButton.render(b, camera);
			exitButton.render(b, camera);
			b.draw(t.SAUWCoin, camera.X + w / 32, camera.Y + h - w / 16, w / 32, w / 32);
			bf.setScale(w / 768);
			bf.draw(b, SAUW_coins + "", camera.X + w / 16 + w / 64, camera.Y + h - w / 32);
		} else {
			if (!createWorldInterface.isOpen) {
				sel_0.update(camera);
				sel_1.update(camera);
				sel_2.update(camera);
				closeButton.update(camera);
				createNewWorld.update(camera);
				up.update(camera);
				down.update(camera);
			}
			createWorldInterface.update(null, camera);

			if (!createWorldInterface.isOpen) {
				sel_0.render(b, camera);
				sel_1.render(b, camera);
				sel_2.render(b, camera);
				closeButton.render(b, camera);
				createNewWorld.render(b, camera);
				up.render(b, camera);
				down.render(b, camera);
			}
			createWorldInterface.render(null, camera);
		}
		b.end();
    }
    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void hide() {

    }
    @Override
    public void dispose() {
    }

}