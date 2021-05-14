package com.kgc.sauw.environment.blocks;

import com.kgc.sauw.map.Tile;
import com.kgc.sauw.utils.ID;

import static com.kgc.sauw.graphic.Graphic.TEXTURES;
import static com.kgc.sauw.map.World.MAPS;
import static com.kgc.sauw.map.World.WORLD;

public class Table extends Block {
    public Table() {
        super(ID.registeredId("block:table"), TEXTURES.table);

        blockConfiguration.setTransparent(true);
        blockConfiguration.setCollisionsRectangleByPixels(1, 2, 30, 7, 32);
    }

    @Override
    public void tick(Tile tile) {
        if (MAPS.getTile(tile.x - 1, tile.y, tile.z).id == ID.get("block:tool_wall") || MAPS.getTile(tile.x + 1, tile.y, tile.z).id == ID.get("block:tool_wall")) {
            WORLD.setBlock(tile.x, tile.y, tile.z, ID.get("block:workbench"));
        }
    }
}
