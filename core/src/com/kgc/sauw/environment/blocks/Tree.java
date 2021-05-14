package com.kgc.sauw.environment.blocks;

import com.kgc.sauw.utils.ID;

import static com.kgc.sauw.graphic.Graphic.TEXTURES;

public class Tree extends Block {
    public Tree() {
        super(ID.registeredId("block:tree", 6), TEXTURES.tree);

        blockConfiguration.setSize(1, 2);
        blockConfiguration.setMaxDamage(5);
        blockConfiguration.setTransparent(true);
        blockConfiguration.setDrop(new int[][]{{8, 3}, {20, 1}});
        blockConfiguration.setCollisionsRectangleByPixels(11, 0, 10, 10, 32);
        blockConfiguration.setInstrumentType(2);
    }
}
