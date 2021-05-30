package com.kgc.sauw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.kgc.sauw.game.MainGame;
import com.kgc.sauw.graphic.Graphic;
import com.kgc.sauw.gui.elements.Button;
import com.kgc.sauw.gui.elements.Checkbox;
import com.kgc.sauw.gui.elements.Slider;
import com.kgc.sauw.resource.TextureGenerator;
import org.json.JSONObject;

import java.util.Iterator;

import static com.kgc.sauw.config.Settings.SETTINGS;
import static com.kgc.sauw.graphic.Graphic.*;
import static com.kgc.sauw.utils.Languages.LANGUAGES;

public class SettingsScreen implements Screen {
    private int width = Gdx.graphics.getWidth();
    private int height = Gdx.graphics.getHeight();
    MenuScreen MenuScreen;

    Button closeButton;

    Button general;
    Button Interface;
    Button gameSettings;
    Button sound;

    private Button nextLang;
    private Button prevLang;
    private Checkbox debug;
    private Checkbox debugRenderer;
    private Checkbox AIPU;
    private Checkbox useConsole;

    private Slider musicVolume;

    public int currentSettingCot = 0;

    Texture background0 = TextureGenerator.generateTexture(15, (height - width / 16 * 2) / (width / 16), false);
    Texture background1 = TextureGenerator.generateTexture(3, 1, true);

    BitmapFont bf = new BitmapFont(Gdx.files.internal("ttf.fnt"));

    JSONObject availableLangs;

    //private Notification Notification;
    public SettingsScreen(MenuScreen ms) {
        this.MenuScreen = ms;
        try {
            availableLangs = new JSONObject(Gdx.files.internal("json/availableLanguages.json").readString("UTF-8"));
        } catch (Exception e) {

        }
        //Notification = new Notification(Textures.generateBackground(8, 4));
        debug = new Checkbox(TEXTURES.switch_0, TEXTURES.switch_1);
        debug.setSize(width / 16, width / 16);
        debug.setPosition(MENU_CAMERA.X + width / 16 * 6 + width / 64, MENU_CAMERA.Y + height - width / 16 * 6);
        debug.setChecked(SETTINGS.debugMode);
        debug.setEventListener(new Checkbox.EventListener() {
            @Override
            public void onClick(boolean isChecked) {
                SETTINGS.debugMode = isChecked;
                SETTINGS.saveSettings();
            }
        });
        debugRenderer = new Checkbox(TEXTURES.switch_0, TEXTURES.switch_1);
        debugRenderer.setSize(width / 16, width / 16);
        debugRenderer.setPosition(MENU_CAMERA.X + width / 16 * 6 + width / 64, MENU_CAMERA.Y + height - width / 16 * 7);
        debugRenderer.setChecked(SETTINGS.debugRenderer);
        debugRenderer.setEventListener(new Checkbox.EventListener() {
            @Override
            public void onClick(boolean isChecked) {
                SETTINGS.debugRenderer = isChecked;
                SETTINGS.saveSettings();
            }
        });
        AIPU = new Checkbox(TEXTURES.switch_0, TEXTURES.switch_1);
        AIPU.setSize(width / 16, width / 16);
        AIPU.setPosition(MENU_CAMERA.X + width / 16 * 9, MENU_CAMERA.Y + height - width / 16 * 3);
        AIPU.setChecked(SETTINGS.autopickup);
        AIPU.setEventListener(new Checkbox.EventListener() {
            @Override
            public void onClick(boolean isChecked) {
                SETTINGS.autopickup = isChecked;
                SETTINGS.saveSettings();
            }
        });
        useConsole = new Checkbox(TEXTURES.switch_0, TEXTURES.switch_1);
        useConsole.setSize(width / 16, width / 16);
        useConsole.setPosition(MENU_CAMERA.X + width / 16 * 9, MENU_CAMERA.Y + height - width / 16 * 4);
        useConsole.setChecked(SETTINGS.useConsole);
        useConsole.setEventListener(new Checkbox.EventListener() {
            @Override
            public void onClick(boolean isChecked) {
                SETTINGS.useConsole = isChecked;
                SETTINGS.saveSettings();
                if (isChecked) {
						/*Notification.hideOnClick(true);
						Notification.show(width / 16 * 4, (height - width / 16 * 4) / 2, width / 16 * 8, width / 16 * 4, LANGUAGES.getString("useConsole"), LANGUAGES.getString("useConsoleNotification"), 10);*/
                }
            }
        });
        closeButton = new Button("SETTINGS_SCREEN_CLOSE_BUTTON", width - width / 16, height - width / 16, width / 32, width / 32, TEXTURES.closeButton, TEXTURES.closeButton);
        closeButton.addEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                MainGame.getGame().setScreen(MenuScreen);
            }
        });
        prevLang = new Button("SETTINGS_SCREEN_PREVIOUS_LANGUAGE_BUTTON", MENU_CAMERA.X + width / 16 * 4 + width / 64, MENU_CAMERA.Y + height - width / 32 * 7, width / 16, width / 16);
        prevLang.setIcon(Graphic.TEXTURES.icon_up);
        nextLang = new Button("SETTINGS_SCREEN_NEXT_LANGUAGE_BUTTON", MENU_CAMERA.X + width / 16 * 4 + width / 64, MENU_CAMERA.Y + height - width / 32 * 9, width / 16, width / 16);
        nextLang.setIcon(Graphic.TEXTURES.icon_down);
        nextLang.addEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                Iterator i = availableLangs.keys();
                int ii = 0;
                while (i.hasNext()) {
                    ii++;
                    String l = (String) i.next();
                    if (l.equals(SETTINGS.lang)) {
                        if (ii + 1 <= availableLangs.length()) {
                            SETTINGS.lang = (String) i.next();
                        } else {
                            SETTINGS.lang = (String) availableLangs.keys().next();
                        }
                        SETTINGS.saveSettings();
                        break;
                    }
                }
            }
        });
        prevLang.addEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                Iterator i = availableLangs.keys();
                int prevLang = 0;
                int ii = 0;
                while (i.hasNext()) {
                    String l = (String) i.next();
                    if (l.equals(SETTINGS.lang)) {
                        prevLang = ii - 1;
                        if (prevLang < 0) prevLang = availableLangs.length() - 1;
                        ii = 0;
                    }
                    ii++;
                }
                i = availableLangs.keys();
                while (i.hasNext()) {
                    String langg = (String) i.next();
                    if (ii == prevLang) {
                        SETTINGS.lang = langg;
                        SETTINGS.saveSettings();
                        break;
                    }
                    ii++;
                }
            }
        });
        bf.getData().setScale(width / 16 / 2 / bf.getCapHeight());
        bf.setColor(Color.BLACK);
        general = new Button("SETTINGS_SCREEN_GENERAL_BUTTON", width / 16, height - width / 32 * 3, width / 32 * 5, width / 16);
        //general.setTextColor(Color.BLACK);
        general.setText(LANGUAGES.getString("general"));
        general.addEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                currentSettingCot = 0;
                general.lock(true);
            }
        });
        Interface = new Button("SETTINGS_SCREEN_INTERFACE_BUTTON", general.x + general.width + width / 128, height - width / 32 * 3, width / 16 * 4, width / 16);
        // Interface.setTextColor(Color.BLACK);
        Interface.setText(LANGUAGES.getString("interface"));
        Interface.addEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                currentSettingCot = 1;
                Interface.lock(true);
            }
        });
        gameSettings = new Button("SETTINGS_SCREEN_GAME_BUTTON", Interface.x + Interface.width + width / 128, height - width / 32 * 3, width / 16 * 3, width / 16);
        // gameSettings.setTextColor(Color.BLACK);
        gameSettings.setText(LANGUAGES.getString("game"));
        gameSettings.addEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                currentSettingCot = 2;
                gameSettings.lock(true);
            }
        });
        sound = new Button("SETTINGS_SCREEN_SOUND_BUTTON", gameSettings.x + gameSettings.width + width / 128, height - width / 32 * 3, width / 16 * 2, width / 16);
        //sound.setTextColor(Color.BLACK);
        sound.setText(LANGUAGES.getString("sound"));
        sound.addEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                currentSettingCot = 3;
                sound.lock(true);
            }
        });
        musicVolume = new Slider(width / 16 * 4, height - width / 16 * 2 - width / 64 * 3, width / 16 * 4, width / 32);
        musicVolume.setEventListener(new Slider.EventListener() {
            @Override
            public void onValueChange(int v) {
                SETTINGS.musicVolume = v;
                SETTINGS.saveSettings();
            }
        });
        musicVolume.setValue(SETTINGS.musicVolume);
    }

    @Override
    public void render(float p1) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        general.lock(false);
        Interface.lock(false);
        gameSettings.lock(false);
        sound.lock(false);

        if (currentSettingCot == 0) {
            general.lock(true);
            nextLang.update(MENU_CAMERA);
            prevLang.update(MENU_CAMERA);
            debug.update(MENU_CAMERA);
            debugRenderer.update(MENU_CAMERA);
        } else if (currentSettingCot == 1) {
            Interface.lock(true);
        } else if (currentSettingCot == 2) {
            gameSettings.lock(true);
            AIPU.update(MENU_CAMERA);
            useConsole.update(MENU_CAMERA);
        } else if (currentSettingCot == 3) {
            sound.lock(true);
            musicVolume.update(MENU_CAMERA);
        }
        //Notification.update(MENU_CAMERA);
        closeButton.update(MENU_CAMERA);
        general.update(MENU_CAMERA);
        Interface.update(MENU_CAMERA);
        gameSettings.update(MENU_CAMERA);
        sound.update(MENU_CAMERA);
        BATCH.begin();
        BATCH.draw(TEXTURES.standardBackground, MENU_CAMERA.X, MENU_CAMERA.Y, width, height);
        BATCH.draw(background0, MENU_CAMERA.X + width / 32, MENU_CAMERA.Y + width / 32, width / 16 * 15, height - width / 16 * 2);
        closeButton.render(BATCH, MENU_CAMERA);
        general.render(BATCH, MENU_CAMERA);
        Interface.render(BATCH, MENU_CAMERA);
        gameSettings.render(BATCH, MENU_CAMERA);
        sound.render(BATCH, MENU_CAMERA);
        if (currentSettingCot == 0) {
            try {
                BATCH.draw(background1, MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 4, width / 16 * 3, width / 16);
                bf.draw(BATCH, LANGUAGES.getString("language"), MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 2 - (width / 16 / 4), width / 16 * 3, Align.center, false);
                bf.draw(BATCH, availableLangs.getString(SETTINGS.lang), MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 3 - (width / 16 / 4), width / 16 * 3, Align.center, false);
                bf.draw(BATCH, LANGUAGES.getString("debug"), MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 5 - (width / 16 / 4), width / 16 * 3, Align.left, false);
                bf.draw(BATCH, LANGUAGES.getString("debugRenderer"), MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 6 - (width / 16 / 4), width / 16 * 3, Align.left, false);
                nextLang.render(BATCH, MENU_CAMERA);
                prevLang.render(BATCH, MENU_CAMERA);
                debug.render(BATCH, MENU_CAMERA);
                debugRenderer.render(BATCH, MENU_CAMERA);
            } catch (Exception e) {

            }
        } else if (currentSettingCot == 1) {

        } else if (currentSettingCot == 2) {
            bf.draw(BATCH, LANGUAGES.getString("autoitemspickup"), MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 2 - (width / 16 / 4), width / 16 * 7, Align.left, false);
            AIPU.render(BATCH, MENU_CAMERA);
            bf.draw(BATCH, LANGUAGES.getString("useConsole"), MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 3 - (width / 16 / 4), width / 16 * 7, Align.left, false);
            useConsole.render(BATCH, MENU_CAMERA);
        } else if (currentSettingCot == 3) {
            bf.draw(BATCH, LANGUAGES.getString("music"), MENU_CAMERA.X + width / 16, MENU_CAMERA.Y + height - width / 16 * 2 - (width / 16 / 4), width / 16 * 2, Align.left, false);
            musicVolume.render(BATCH, MENU_CAMERA);
            bf.draw(BATCH, musicVolume.getValue() + "", MENU_CAMERA.X + musicVolume.x + musicVolume.width + width / 32, MENU_CAMERA.Y + musicVolume.y + musicVolume.height, width / 16 * 2, Align.left, false);
        }
        //Notification.render(BATCH, MENU_CAMERA);
        BATCH.end();
    }

    @Override
    public void resize(int p1, int p2) {
    }

    @Override
    public void show() {
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

    }
}
