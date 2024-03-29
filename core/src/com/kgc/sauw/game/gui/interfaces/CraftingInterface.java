package com.kgc.sauw.game.gui.interfaces;

import com.kgc.sauw.core.Container;
import com.kgc.sauw.core.gui.Interface;
import com.jvmfrog.curve.registry.RegistryMetadata;

@RegistryMetadata("sauw:crafting")
public class CraftingInterface extends Interface {
    /*private final TextView craftName;
    private int currentCraft = -1;
    private int currentTab = 0;
    private final Image itemIcon;*/

    private final Container[] craftContainers = new Container[6];

    public CraftingInterface() {

        /*mainLayout.setOrientation(Layout.Orientation.HORIZONTAL);
        mainLayout.setGravity(Layout.Gravity.LEFT);

        InterfaceUtils.createFromXml(Gdx.files.internal("xml/CraftingInterface.xml"), this);
        getElement("sauw.layout.crafting.craftsLayout").setSizeInBlocks(7.5f, 7f);
        ((Layout) getElement("sauw.layout.crafting.craftsLayout")).setStandardBackground(false);
        getElement("sauw.layout.crafting.craftInfoLayout").setSizeInBlocks(6f, 7f);
        ((Layout) getElement("sauw.layout.crafting.craftInfoLayout")).setStandardBackground(false);

        getElement("craftIconLayout").setSizeInBlocks(2f, 2f);
        ((Layout) getElement("craftIconLayout")).setStandardBackground(true);

        getElement("craftIconLayout_0").setSizeInBlocks(0, 2);

        ((Button) getElement("prevCraftTabButton")).setIcon(Resource.getTexture("interface/button_left_0.png"));
        ((Button) getElement("nextCraftTabButton")).setIcon(Resource.getTexture("interface/button_right_0.png"));

        itemIcon = (Image) getElement("craftIcon");
        craftName = (TextView) getElement("craftName");
        Button craft = (Button) getElement("craftButton");
        craft.setText(Languages.getString("craft"));
        craft.addEventListener(new OnClickListener() {
            @Override
            public void onClick() {
                if (currentCraft != -1) {
                    for (int i = 0; i < CRAFTING.crafts.get(currentCraft).ingredients.length; i++) {
                        int IC = PLAYER.inventory.getCountOfItems(CRAFTING.crafts.get(currentCraft).ingredients[i][0]);
                        if (IC < CRAFTING.crafts.get(currentCraft).ingredients[i][1]) {
                            return;
                        }
                    }
                    for (int i = 0; i < CRAFTING.crafts.get(currentCraft).ingredients.length; i++) {
                        PLAYER.inventory.deleteItems(CRAFTING.crafts.get(currentCraft).ingredients[i][0], CRAFTING.crafts.get(currentCraft).ingredients[i][1]);
                    }
                    PLAYER.inventory.addItem(CRAFTING.crafts.get(currentCraft).result[0], CRAFTING.crafts.get(currentCraft).result[1]);
                }
            }
        });

        for (int i = 0; i < 6; i++) {
            craftContainers[i] = new Container();
            ((Slot) getElement("craftItemSlot_" + i)).setSF(new Slot.SlotFunctions() {
                @Override
                public boolean isValid(Container container, String FromSlotWithId) {
                    return false;
                }

                @Override
                public void onClick() {

                }

                @Override
                public boolean possibleToDrag() {
                    return false;
                }

                @Override
                public void onItemSwapping(Container fromContainer) {

                }
            });
        }
        Layout CraftsListLayout = (Layout) getElement("CraftsListLayout");
        for (int y = 0; y < 5; y++) {
            Layout l = new Layout(Layout.Orientation.HORIZONTAL);
            l.setSize(Layout.Size.WRAP_CONTENT, Layout.Size.WRAP_CONTENT);
            l.setGravity(Layout.Gravity.LEFT);
            l.setId("CraftsListLayout_" + y);
            for (int x = 0; x < 6; x++) {
                final int num = y * 6 + x;
                String id = "Craft_" + num;
                Button button = new Button(id, 0, 0, 0, 0);
                button.setSizeInBlocks(1, 1);
                button.addEventListener(new OnClickListener() {
                    @Override
                    public void onClick() {
                        currentCraft = currentTab * 30 + num;
                        craftName.setText(Items.getItemById(CRAFTING.crafts.get(currentCraft).result[0]).getDefaultName());
                        itemIcon.setSprite(Items.getItemById(CRAFTING.crafts.get(currentCraft).result[0]).getTexture(null));
                    }
                });
                l.addElements(button);
            }
            CraftsListLayout.addElements(l);
        }
        updateElementsList();*/
    }

    @Override
    public void tick() {
        /*int temp = 0;
        for (int i = currentTab * 30; i < currentTab + 30; i++) {
            if (getElement("Craft_" + temp) != null) {
                getElement("Craft_" + temp).hide(i >= CRAFTING.crafts.size());
            }
            temp += 1;
        }
        if (currentCraft != -1) {
            for (int i = 0; i < CRAFTING.crafts.get(currentCraft).ingredients.length; i++) {
                Crafting.Craft craft = CRAFTING.crafts.get(currentCraft);
                craftContainers[i].setItem(craft.ingredients[i][0], craft.ingredients[i][1], 0);
                ((Slot) getElement("craftItemSlot_" + i)).setContainer(craftContainers[i]);
            }
        }*/
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void preRender() {

    }

    @Override
    public void postRender() {
        /*for (int i = currentTab * 30; i < CRAFTING.crafts.size(); i++) {
            float x = getElement("Craft_" + i).x;
            float y = getElement("Craft_" + i).y;
            float w = getElement("Craft_" + i).width;
            BATCH.draw(Items.getItemById(CRAFTING.crafts.get(i).result[0]).getTexture(null), x + w / 8, y + w / 8, w - w / 4, w - w / 4);
        }*/
    }
}
