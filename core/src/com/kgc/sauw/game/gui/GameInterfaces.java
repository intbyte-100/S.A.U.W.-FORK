package com.kgc.sauw.game.gui;

import com.kgc.sauw.core.gui.Interfaces;
import com.kgc.sauw.game.gui.interfaces.*;
import com.kgc.sauw.game.gui.interfaces.blockInterfaces.ChestInterface;
import com.kgc.sauw.game.gui.interfaces.blockInterfaces.FurnaceInterface;
import com.kgc.sauw.game.gui.interfaces.blockInterfaces.ToolWallInterface;
import com.kgc.sauw.game.gui.interfaces.blockInterfaces.WorkbenchInterface;

public final class GameInterfaces {
    /**
     * S.A.U.W. Interfaces
     */
    public static final CraftingInterface CRAFTING_INTERFACE;
    public static final PauseInterface PAUSE_INTERFACE;
    public static final DeathInterface DEATH_INTERFACE;
    public static final InventoryInterface INVENTORY_INTERFACE;
    public static final TestInterface TEST_INTERFACE;
    public static final EgorInterface EGOR_INTERFACE;
    public static final UpdatesInterface UPDATES_INTERFACE;
    public static final CreativeInterface CREATIVE_INTERFACE;
    public static final ErrorInterface ERROR_INTERFACE;
    public static final CreateNewWorldInterface CREATE_NEW_WORLD_INTERFACE;
    public static final SelectWorldInterface SELECT_WORLD_INTERFACE;
    public static final DevelopersInterface DEVELOPERS_INTERFACE;
    /**
     * BLOCK Interfaces
     */
    public static final ChestInterface CHEST_INTERFACE;
    public static final FurnaceInterface FURNACE_INTERFACE;
    public static final ToolWallInterface TOOL_WALL_INTERFACE;
    public static final WorkbenchInterface WORKBENCH_INTERFACE;

    static {
        CRAFTING_INTERFACE = new CraftingInterface();
        PAUSE_INTERFACE = new PauseInterface();
        DEATH_INTERFACE = new DeathInterface();
        CREATIVE_INTERFACE = new CreativeInterface();
        INVENTORY_INTERFACE = new InventoryInterface();
        TEST_INTERFACE = new TestInterface();
        EGOR_INTERFACE = new EgorInterface();
        ERROR_INTERFACE = new ErrorInterface();
        CREATE_NEW_WORLD_INTERFACE = new CreateNewWorldInterface();
        SELECT_WORLD_INTERFACE = new SelectWorldInterface();
        DEVELOPERS_INTERFACE = new DevelopersInterface();

        CHEST_INTERFACE = new ChestInterface();
        FURNACE_INTERFACE = new FurnaceInterface();
        TOOL_WALL_INTERFACE = new ToolWallInterface();
        WORKBENCH_INTERFACE = new WorkbenchInterface();
        UPDATES_INTERFACE = new UpdatesInterface();

        Interfaces.registry.register(CRAFTING_INTERFACE);
        Interfaces.registry.register(PAUSE_INTERFACE);
        Interfaces.registry.register(DEATH_INTERFACE);
        Interfaces.registry.register(CREATIVE_INTERFACE);
        Interfaces.registry.register(INVENTORY_INTERFACE);
        Interfaces.registry.register(TEST_INTERFACE);
        Interfaces.registry.register(EGOR_INTERFACE);
        Interfaces.registry.register(UPDATES_INTERFACE);
        Interfaces.registry.register(ERROR_INTERFACE);
        Interfaces.registry.register(CREATE_NEW_WORLD_INTERFACE);
        Interfaces.registry.register(SELECT_WORLD_INTERFACE);
        Interfaces.registry.register(DEVELOPERS_INTERFACE);

        Interfaces.registry.register(CHEST_INTERFACE);
        Interfaces.registry.register(FURNACE_INTERFACE);
        Interfaces.registry.register(TOOL_WALL_INTERFACE);
        Interfaces.registry.register(WORKBENCH_INTERFACE);
    }
}
