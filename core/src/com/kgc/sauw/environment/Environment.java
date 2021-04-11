package com.kgc.sauw.environment;

import com.kgc.sauw.Achievements;
import com.kgc.sauw.config.Settings;
import com.kgc.sauw.utils.Languages;

public final class Environment {
    public static final Blocks BLOCKS;
    public static final Items ITEMS;
    public static final Languages LANGUAGES;
    public static final Settings SETTINGS;
    public static final Crafting CRAFTING;
    public static final Achievements ACHIEVEMENTS;
    static {
        SETTINGS = new Settings();
        LANGUAGES = new Languages(SETTINGS);
        ITEMS = new Items();
        BLOCKS = new Blocks();
        CRAFTING = new Crafting();
        ACHIEVEMENTS = new Achievements();
    }
}