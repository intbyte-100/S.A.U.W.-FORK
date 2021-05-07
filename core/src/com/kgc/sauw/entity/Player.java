package com.kgc.sauw.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.intbyte.bdb.DataBuffer;
import com.intbyte.bdb.ExtraData;
import com.kgc.sauw.Inventory;
import com.kgc.sauw.InventoryContainer;
import com.kgc.sauw.environment.items.Item;
import com.kgc.sauw.graphic.Animator;
import com.kgc.sauw.input.PlayerController;
import com.kgc.sauw.math.Maths;
import com.kgc.sauw.particle.Particles;
import com.kgc.sauw.physic.Physic;

import static com.kgc.sauw.config.Settings.SETTINGS;
import static com.kgc.sauw.entity.Entities.ENTITIES_LIST;
import static com.kgc.sauw.environment.Environment.ITEMS;
import static com.kgc.sauw.graphic.Graphic.*;
import static com.kgc.sauw.map.World.WORLD;
import static com.kgc.sauw.ui.interfaces.Interfaces.DEAD_INTERFACE;
import static com.kgc.sauw.ui.interfaces.Interfaces.GAME_INTERFACE;


public class Player extends Entity implements ExtraData {
    @Override
    public byte[] getBytes() {
        DataBuffer buffer = new DataBuffer();
        buffer.put("health", health);
        buffer.put("hunger", hunger);
        buffer.put("coords", new int[]{(int) getPosition().x, (int) getPosition().y});
        buffer.put("InvLength", Inventory.containers.size());
        for (int i = 0; i < Inventory.containers.size(); i++) {
            buffer.put("Inv_" + i, Inventory.containers.get(i));
        }
        return buffer.toBytes();
    }

    @Override
    public void readBytes(byte[] bytes, int begin, int end) {
        DataBuffer buffer = new DataBuffer();
        buffer.readBytes(bytes);
        setPosition(buffer.getIntArray("coords")[0], buffer.getIntArray("coords")[1]);
        health = buffer.getInt("health");
        hunger = buffer.getInt("hunger");
        Inventory = new Inventory(buffer.getInt("InvLength"));
        for (int i = 0; i < buffer.getInt("InvLength"); i++) {
            Inventory.containers.add(i, new InventoryContainer());
            Inventory.containers.get(i).readBytes(buffer.getByteArray("Inv_" + i), begin, end);
        }
    }

    public int carriedSlot = 0;
    public int[] hotbar = new int[8];

    public Item getCarriedItem() {
        return getItemFromHotbar(carriedSlot);
    }

    public Item getItemFromHotbar(int index) {
        if (hotbar[index] != -1 && hotbar[index] < Inventory.containers.size() && ITEMS.getItemById(Inventory.containers.get(hotbar[index]).id) != null) {
            return ITEMS.getItemById(Inventory.containers.get(hotbar[index]).id);
        } else {
            hotbar[index] = -1;
            return ITEMS.getItemById(0);
        }
    }

    @Override
    public void onDead() {
        DEAD_INTERFACE.open();
    }

    public Player() {
        entityBodyW = 10 / 26f;
        entityBodyH = 1f;
        setSize(new Vector2(entityBodyW, entityBodyH));
        setBody(Physic.createRectangleBody(0, 0, entityBodyW, entityBodyH / 4f, BodyDef.BodyType.DynamicBody));

        for (int i = 0; i < hotbar.length; i++) {
            this.hotbar[i] = -1;
        }

        animator = new Animator();
        animator.addAnimationRegion("animation_region:player", TEXTURES.player, 4, 3);
        animator.addAnimation("animation:player_walk_left", "animation_region:player", 0.2f, 0, 1);
        animator.addAnimation("animation:player_walk_right", "animation_region:player", 0.2f, 2, 3);
        animator.addAnimation("animation:player_walk_down", "animation_region:player", 0.2f, 4, 5);
        animator.addAnimation("animation:player_walk_up", "animation_region:player", 0.2f, 6, 7);

        currentFrame = animator.getFrames("animation_region:player")[4];

        randomSpawn();
    }

    @Override
    public void render() {
        if (!isDead) {
            float AL = 0.75f - (Maths.module(720 - WORLD.WorldTime.getTime()) / (720 / 0.3f));
            BATCH.setColor(1f, 1f, 1f, AL);
            //BATCH.draw(TEXTURES.shadow, body.getPosition().x - entityBodyW / 2f, body.getPosition().y - entityBodyH / 2f, entityBodyW, entityBodyH);
            BATCH.setColor(1f, 1f, 1f, 1f);
            BATCH.draw(currentFrame, body.getPosition().x - entityBodyW / 2f, body.getPosition().y - entityBodyH / 8f, entityBodyW, entityBodyH);
        }
    }

    @Override
    public void animationTick() {
        if (rotation == 0) {
            currentFrame = animator.getFrame("animation:player_walk_up");
        } else if (rotation == 1) {
            currentFrame = animator.getFrame("animation:player_walk_right");
        } else if (rotation == 2) {
            currentFrame = animator.getFrame("animation:player_walk_down");
        } else if (rotation == 3) {
            currentFrame = animator.getFrame("animation:player_walk_left");
        }

        if (!isEntityMoving()) {
            if (rotation == 1) {
                currentFrame = animator.getFrames("animation_region:player")[3];
            } else if (rotation == 3) {
                currentFrame = animator.getFrames("animation_region:player")[1];
            } else if (rotation == 0) {
                currentFrame = animator.getFrames("animation_region:player")[9];
            } else if (rotation == 2) {
                currentFrame = animator.getFrames("animation_region:player")[8];
            }
        }
    }

    @Override
    public void tick() {
        if (!isDead) {
            PlayerController.update();
            if (SETTINGS.autopickup || (GAME_INTERFACE.interactionButton.isTouched() || Gdx.input.isKeyPressed(Input.Keys.E))) {
                for (int i = 0; i < ENTITIES_LIST.size(); i++) {
                    if (ENTITIES_LIST.get(i) instanceof ItemEntityL && Maths.distanceD((int) getPosition().x, (int) getPosition().x, ENTITIES_LIST.get(i).posX, ENTITIES_LIST.get(i).posY) < SCREEN_WIDTH / 32) {
                        ItemEntityL item = (ItemEntityL) ENTITIES_LIST.get(i);
                        Inventory.addItem((int) item.getExtraData("itemId"), (int) item.getExtraData("itemCount"));
                        ENTITIES_LIST.remove(i);
                    }
                }
            }
        }
    }
}