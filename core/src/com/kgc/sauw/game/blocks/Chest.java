package com.kgc.sauw.game.blocks;

import com.kgc.sauw.core.block.Block;
import com.kgc.sauw.core.resource.Resource;
import com.kgc.sauw.core.utils.ID;
import com.kgc.sauw.core.item.InstrumentItem;

import static com.kgc.sauw.game.gui.Interfaces.CHEST_INTERFACE;

public class Chest extends Block {
    public Chest() {
        super(ID.registeredId("block:chest", 5), Resource.getTexture("Blocks/chest.png"));

        blockConfiguration.setInstrumentType(InstrumentItem.Type.AXE);
        blockConfiguration.setCollisionsRectangleByPixels(2, 2, 30, 9, 32);
        blockConfiguration.setTransparent(true);

        GUI = CHEST_INTERFACE;
    }
}
