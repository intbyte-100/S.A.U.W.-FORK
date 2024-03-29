package com.kgc.sauw.core.entity.entities.player;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.intbyte.bdb.DataBuffer;
import com.intbyte.bdb.ExtraData;
import com.kgc.sauw.core.Container;
import com.kgc.sauw.core.achievements.AchievementsData;
import com.kgc.sauw.core.callbacks.Callback;
import com.kgc.sauw.core.callbacks.InteractionButtonClicked;
import com.kgc.sauw.core.entity.AbstractEntityRenderer;
import com.kgc.sauw.core.entity.Entity;
import com.kgc.sauw.core.entity.EntityManager;
import com.kgc.sauw.core.entity.LivingEntity;
import com.kgc.sauw.core.entity.entities.drop.Drop;
import com.kgc.sauw.core.input.PlayerController;
import com.kgc.sauw.core.item.Item;
import com.kgc.sauw.core.item.Type;
import com.kgc.sauw.core.math.Maths;

import static com.kgc.sauw.core.entity.EntityManager.PLAYER;
import static com.kgc.sauw.core.environment.Environment.getWorld;
import static com.kgc.sauw.core.world.WorldRenderer.rayHandler;
import static com.kgc.sauw.game.gui.GameInterfaces.DEATH_INTERFACE;

public class Player extends LivingEntity implements ExtraData {
    public Inventory inventory = new Inventory();

    public float maxWeight = 40.0f;
    public float itemsWeight = 0.0f;

    public final float maxHunger = 100;
    public final float maxThirst = 100;
    public float thirst = 100;

    public AchievementsData achievementsData = new AchievementsData();

    private final PointLight pointLight;

    public int carriedSlot = 0;
    public Container[] hotbar = new Container[8];

    @Override
    public byte[] getBytes() {
        DataBuffer buffer = new DataBuffer();
        buffer.put("health", health);
        buffer.put("hunger", hunger.hunger);
        buffer.put("thirst", thirst);
        buffer.put("X", getPosition().x);
        buffer.put("Y", getPosition().y);
        buffer.put("InvLength", inventory.containers.size());
        for (int i = 0; i < inventory.containers.size(); i++) {
            buffer.put("Inv_" + i, inventory.containers.get(i));
        }
        return buffer.toBytes();
    }

    @Override
    public void readBytes(byte[] bytes, int begin, int end) {
        DataBuffer buffer = new DataBuffer();
        buffer.readBytes(bytes);
        setPosition(buffer.getFloat("X"), buffer.getFloat("Y"));
        health = buffer.getFloat("health");
        hunger.hunger = buffer.getFloat("hunger");
        thirst = buffer.getFloat("thirst");
        inventory = new Inventory(buffer.getInt("InvLength"));
        for (int i = 0; i < buffer.getInt("InvLength"); i++) {
            inventory.containers.add(i, new Container());
            inventory.containers.get(i).readBytes(buffer.getByteArray("Inv_" + i), begin, end);
        }
    }

    @Override
    public Vector2 getBodySize() {
        return new Vector2(entityBodyW, entityBodyH / 4f);
    }

    public Item getCarriedItem() {
        return getItemFromHotbar(carriedSlot);
    }

    public Item getItemFromHotbar(int index) {
        return hotbar[index].item;
    }

    @Override
    public void onDead() {
        DEATH_INTERFACE.open();
    }

    public Player() {
        entityBodyW = 10 / 26f;
        entityBodyH = 1f;
        setSize(new Vector2(entityBodyW, entityBodyH));

        for (int i = 0; i < hotbar.length; i++) {
            this.hotbar[i] = null;
        }

        pointLight = new PointLight(rayHandler, 10, new Color(0.6f, 0.6f, 0, 1f), 5, 0, 0);
        pointLight.attachToBody(body);
        setLightActive(false);
        Callback.addCallback(position -> {
            int bX = (int) position.x;
            int bZ = (int) position.y;
            Item carriedItem = PLAYER.getCarriedItem();
            if (Maths.distanceD((int) PLAYER.getPosition().x, (int) PLAYER.getPosition().y, bX, bZ) <= 2f) {
                carriedItem.onClick(getWorld().map.getTile(bX, getWorld().map.getHighestBlock(bX, bZ), bZ));
                if (carriedItem.getItemConfiguration().type == Type.BLOCK_ITEM) {
                    if (getWorld().map.setBlock(bX, bZ, carriedItem.getItemConfiguration().stringBlockId)) {
                        PLAYER.hotbar[PLAYER.carriedSlot].count -= 1;
                    }
                }
            }
        });
        Callback.addCallback((InteractionButtonClicked) () -> {
            Entity entity = EntityManager.findEntity(PLAYER, 0.6f);
            if (entity instanceof Drop) {
                Drop item = (Drop) entity;
                inventory.addItem((int) item.getExtraData("itemId"), (int) item.getExtraData("itemCount"));
                EntityManager.delete(item);
            }
        });
    }

    public void setLightActive(boolean active) {
        pointLight.setActive(active);
    }

    @Override
    public AbstractEntityRenderer getEntityRenderer() {
        return PlayerRenderer.INSTANCE;
    }

    @Override
    public void tick() {
        if (!isDead) {
            itemsWeight = inventory.getItemsWeight();
            entitySpeed = 1.0f - ((itemsWeight * 1.66f) / 100);
            if (entitySpeed < 0) entitySpeed = 0;
            inventory.removeItemsIfNeed();
            pointLight.setPosition(body.getPosition().x, body.getPosition().y);
            PlayerController.update();
        }
    }

}