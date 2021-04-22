package com.kgc.sauw.UI.Interfaces;

import com.kgc.sauw.UI.Elements.Button;
import com.kgc.sauw.UI.Elements.Image;
import com.kgc.sauw.UI.Elements.Layout;
import com.kgc.sauw.UI.Interface;
import com.kgc.sauw.UI.InterfaceElement;

import static com.kgc.sauw.graphic.Graphic.BLOCK_SIZE;
import static com.kgc.sauw.graphic.Graphic.TEXTURES;

public class TestInterface extends Interface {
    public TestInterface() {
        super(InterfaceSizes.FULL, "TEST_INTERFACE");

        setHeaderText("TOP SECRET");

        Layout testLayout = new Layout(Layout.Orientation.HORIZONTAL);
        testLayout.setSize(Layout.Size.WRAP_CONTENT, Layout.Size.WRAP_CONTENT);
        testLayout.setGravity(Layout.Gravity.RIGHT);

        Button button1 = new Button("", 0, 0, BLOCK_SIZE * 2, BLOCK_SIZE);

        button1.setEventListener(new Button.EventListener() {
            @Override
            public void onClick() {
                for (InterfaceElement e : com.kgc.sauw.UI.Elements.Elements.UI_ELEMENTS)
                    System.out.println(e.ID);
            }
        });

        Image img1 = new Image(0, 0, BLOCK_SIZE / 2, BLOCK_SIZE / 2);
        Image img2 = new Image(0, 0, BLOCK_SIZE / 2, BLOCK_SIZE / 2);
        img1.setImg(TEXTURES.stone_shovel);
        img2.setImg(TEXTURES.apple);

        testLayout.addElements(img1, button1, img2);

        MainLayout.addElements(testLayout);

        initialize();
    }
}