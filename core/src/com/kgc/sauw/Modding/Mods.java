package com.kgc.sauw.modding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kgc.sauw.gui.Interfaces;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.kgc.sauw.gui.Interfaces.HUD;
import static com.kgc.sauw.config.Settings.SETTINGS;
import static com.kgc.sauw.entity.EntityManager.PLAYER;
import static com.kgc.sauw.environment.Environment.*;
import static com.kgc.sauw.game.SAUW.MOD_API;
import static com.kgc.sauw.graphic.Graphic.TEXTURES;
import static com.kgc.sauw.map.World.WORLD;

public class Mods {
    Context cx;
    mod mods[] = null;

    public void load() {
        cx = Context.enter();
        cx.setOptimizationLevel(-1);
        try {
            ArrayList<String> names = new ArrayList<>();
            FileHandle t = Gdx.files.external("S.A.U.W./Mods");
            FileHandle modsJson = Gdx.files.external("S.A.U.W./Mods/Mods.json");
            JSONArray modsArray = new JSONArray(modsJson.readString());
            for (int i = 0; i < modsArray.length(); i++) {
                if (modsArray.getJSONObject(i).getBoolean("isOn"))
                    names.add(modsArray.getJSONObject(i).getString("Mod"));
            }
            mods = new mod[names.size()];
            for (int i = 0; i < names.size(); i++) {
                if (Gdx.files.external("S.A.U.W./Mods/" + names.get(i) + "/manifest.json").exists()) {
                    mods[i] = new mod("S.A.U.W./Mods/" + names.get(i));
                    mods[i].sc = cx.initStandardObjects();

                    FileHandle MainJSFile = Gdx.files.external("S.A.U.W./Mods/" + names.get(i) + "/main.js");
                    String result = MainJSFile.readString();
                    //ITEMS.createItems(mods[i].ItemsFolder, mods[i].resFolder);
                    //CRAFTING.addCraftsFromDirectory(mods[i].CraftsFolder);
                    ScriptableObject.putProperty(mods[i].sc, "Player", PLAYER);
                    ScriptableObject.putProperty(mods[i].sc, "Blocks", BLOCKS);
                    ScriptableObject.putProperty(mods[i].sc, "Items", ITEMS);
                    ScriptableObject.putProperty(mods[i].sc, "ModAPI", MOD_API);
                    ScriptableObject.putProperty(mods[i].sc, "Settings", SETTINGS);
                    ScriptableObject.putProperty(mods[i].sc, "GI", HUD);
                    ScriptableObject.putProperty(mods[i].sc, "World", WORLD);
                    ScriptableObject.putProperty(mods[i].sc, "Interfaces", Interfaces.class);
                    ScriptableObject.putProperty(mods[i].sc, "Textures", TEXTURES);

                    cx.evaluateString(mods[i].sc, result, names.get(i), 1, null);
                }
            }
        } catch (Exception e) {
            Gdx.app.log("error", e.toString());
        } finally {
            Context.exit();
        }
        new ModTick().start();
    }

    public void HookFunction(String functionName, Object args[]) {
        cx = Context.enter();
        cx.setOptimizationLevel(-1);
        try {
            for (mod mod : mods) {
                Object fObj = mod.sc.get(functionName, mod.sc);
                if ((fObj instanceof Function)) {
                    Function f = (Function) fObj;
                    Object result = f.call(cx, mod.sc, mod.sc, args);
                }
            }
        } catch (Exception e) {
            Gdx.app.log("MOD_ERROR", e.toString());
        } finally {
            Context.exit();
        }
    }

    class ModTick extends Thread {
        Timer timer = new Timer();
        Object[] obj = new Object[]{};

        @Override
        public void run() {
            super.run();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    HookFunction("tick", obj);
                }
            }, 0, 50);

        }
    }

    public class mod {
        public Scriptable sc;
        public String modPath;
        public JSONObject manifest;
        public FileHandle CraftsFolder;
        public FileHandle ItemsFolder;
        public FileHandle resFolder;

        public mod(String ModPath) {
            try {
                String manifest = Gdx.files.external(ModPath + "/manifest.json").readString();
                this.manifest = new JSONObject(manifest);
                this.modPath = ModPath;
                CraftsFolder = Gdx.files.external(modPath + "/" + this.manifest.getString("crafts_path"));
                ItemsFolder = Gdx.files.external(modPath + "/" + this.manifest.getString("items_path"));
                resFolder = Gdx.files.external(modPath + "/" + this.manifest.getString("resources_path"));
            } catch (Exception e) {
                Gdx.app.log("ModAPI_CreateModError", e.toString());
            }

        }
    }
}
