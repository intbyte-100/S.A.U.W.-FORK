package com.kgc.sauw.utils;

import com.kgc.sauw.map.Maps;

import static com.kgc.sauw.entity.Entities.PLAYER;
import static com.kgc.sauw.graphic.Graphic.BATCH;
import static com.kgc.sauw.graphic.Graphic.GAME_CAMERA;

public class GameCameraController {
    private static float camX, camY;

    public static void init() {
        setSize();
    }

    public static void setSize() {
        GAME_CAMERA.resize(20);
        setCameraPosition();
        GAME_CAMERA.lookAt(camX, camY, false);
    }

    public static void update() {
        setCameraPosition();
        GAME_CAMERA.lookAt(camX, camY, true);
        GAME_CAMERA.update(BATCH);
    }

    public static void setCameraPosition() {
        camX = (PLAYER.getPosition().x + (PLAYER.getSize().x / 2) - (GAME_CAMERA.W / 2f));
        camY = (PLAYER.getPosition().y + (PLAYER.getSize().y / 2) - (GAME_CAMERA.H / 2f));
        if (camX < 1) camX = 1;
        if (camY < 1) camY = 1;
        if (camX + GAME_CAMERA.W > Maps.xSize - 1)
            camX = Maps.xSize - 1 - GAME_CAMERA.W;
        if (camY + GAME_CAMERA.H > Maps.ySize - 1)
            camY = Maps.ySize - 1 - GAME_CAMERA.H;
    }
}
