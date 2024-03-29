package com.kgc.sauw.core.world;

import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.physics.box2d.Filter;
import com.kgc.sauw.core.GameContext;
import com.kgc.sauw.core.entity.Entity;
import com.kgc.sauw.core.math.Maths;
import com.kgc.sauw.core.physic.Physic;

import static com.kgc.sauw.core.entity.EntityManager.ENTITIES_LIST;
import static com.kgc.sauw.core.entity.EntityManager.PLAYER;
import static com.kgc.sauw.core.graphic.Graphic.BATCH;
import static com.kgc.sauw.core.graphic.Graphic.GAME_CAMERA;

public class WorldRenderer {
    public static RayHandler rayHandler;

    static {
        rayHandler = new RayHandler(Physic.getWorld());
        rayHandler.setAmbientLight(1, 1, 1, 1);
        rayHandler.setBlur(true);
        RayHandler.useDiffuseLight(true);
        Filter filter = new Filter();
        filter.categoryBits = 0x0001;
        Light.setGlobalContactFilter(filter);
    }

    private static void renderLights(World world) {
        BATCH.end();
        float TL = 720 / 0.6f;
        float AmbientLight = 1.0f - (Maths.module(720 - world.worldTime.getTime()) / TL);
        rayHandler.setAmbientLight(AmbientLight, AmbientLight, AmbientLight, 1);
        rayHandler.setCombinedMatrix(GAME_CAMERA.CAMERA);
        rayHandler.updateAndRender();
        BATCH.begin();
    }

    public static void render(World world) {
        renderMap(world, false);
        renderMap(world, true);
        renderEntities(world);
        renderLights(world);
    }

    private static void renderEntities(World world) {
        renderEntity(PLAYER, world);
        for (Entity entity : ENTITIES_LIST) renderEntity(entity, world);
    }

    private static void renderEntity(Entity entity, World world) {
        if (Maths.rectCrossing(
                entity.getPosition().x, entity.getPosition().y, entity.getSize().x, entity.getSize().y,
                GAME_CAMERA.X, GAME_CAMERA.Y, GAME_CAMERA.W, GAME_CAMERA.H)) {
            entity.render();
            renderBlock(entity.getCurrentTileX(), entity.getCurrentTileZ(), true, world);
            if (entity.getPosition().x < entity.getCurrentTileX())
                renderBlock(entity.getCurrentTileX() - 1, entity.getCurrentTileZ(), true, world);
            if (entity.getPosition().x + entity.getSize().x > entity.getCurrentTileX())
                renderBlock(entity.getCurrentTileX() + 1, entity.getCurrentTileZ(), true, world);
        }
    }

    private static void renderMap(World world, boolean highLayer) {
        int camX = (int) Math.floor(GAME_CAMERA.X);
        int camXW = (int) Math.ceil(GAME_CAMERA.X + GAME_CAMERA.W);
        int camY = (int) Math.floor(GAME_CAMERA.Y);
        int camYH = (int) Math.ceil(GAME_CAMERA.Y + GAME_CAMERA.H);
        for (int x = camX; x < camXW; x++) {
            for (int z = camYH - 1; z >= camY; z--) {
                renderBlock(x, z, highLayer, world);
            }
        }
    }

    private static void renderBlock(int x, int z, boolean highLayer, World world) {
        int y = world.map.getHighestBlock(x, z);
        if (y != -1)
            if (!highLayer || y == 0) {
                if (!highLayer && y == 0 && GameContext.getBlock(world.map.getTile(x, y, z).id).getBlockConfiguration().isTransparent()) {
                    y = y + 1;
                }
                world.map.getTile(x, y, z).render();
            }
    }
}
