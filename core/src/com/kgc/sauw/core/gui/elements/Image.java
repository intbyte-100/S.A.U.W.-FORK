package com.kgc.sauw.core.gui.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kgc.sauw.core.gui.InterfaceElement;
import com.kgc.utils.Camera2D;

public class Image extends InterfaceElement {
    private final Sprite sprite = new Sprite();

    public void setSprite(Sprite sprite) {
        this.sprite.set(sprite);
    }

    public void setImg(Texture t) {
        this.sprite.setRegion(t);
    }

    public void setImg(TextureRegion textureRegion) {
        this.sprite.setRegion(textureRegion);
    }

    @Override
    protected void tick(Camera2D cam) {
    }

    @Override
    public void renderTick(SpriteBatch batch, Camera2D cam) {
        sprite.setPosition(cam.X + x, cam.Y + y);
        sprite.setSize(width, height);
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void onClick(boolean onElement) {
    }
}
