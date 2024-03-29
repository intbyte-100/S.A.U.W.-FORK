package com.kgc.sauw.game.gui.interfaces;

import com.badlogic.gdx.Gdx;
import com.kgc.sauw.core.gui.Interface;
import com.kgc.sauw.core.gui.InterfaceUtils;
import com.kgc.sauw.core.gui.elements.Image;
import com.jvmfrog.curve.registry.RegistryMetadata;
import com.kgc.sauw.core.resource.Resource;
@RegistryMetadata("sauw:egor")
public class EgorInterface extends Interface {
    public EgorInterface() {
        InterfaceUtils.createFromXml(Gdx.files.internal("xml/EgorInterface.xml"), this);

        ((Image) getElement("image.mem")).setImg(Resource.getTexture("egor_memes/mem1.jpg"));
    }
}
