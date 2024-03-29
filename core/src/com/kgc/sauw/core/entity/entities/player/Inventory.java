package com.kgc.sauw.core.entity.entities.player;

import com.kgc.sauw.core.Container;
import com.kgc.sauw.core.GameContext;

import java.util.ArrayList;

import static com.kgc.sauw.core.GameContext.SAUW;
import static com.kgc.sauw.core.environment.Environment.gameMode;

public class Inventory {
    public ArrayList<Container> containers;
    private final ArrayList<Container> slotsToRemove = new ArrayList<>();

    public Inventory(int count) {
        containers = new ArrayList<>(count);
    }

    public Inventory() {
        this(0);
    }

    public void deleteItems() {
        ArrayList<Container> toBeRemoved = new ArrayList<>(containers);
        containers.removeAll(toBeRemoved);
    }

    public void deleteItems(int id) {
        ArrayList<Container> toBeRemoved = new ArrayList<>();
        for (Container slot : containers) {
            if (slot.id == id)
                toBeRemoved.add(slot);
        }
        containers.removeAll(toBeRemoved);
    }

    public void deleteItems(int id, int count) {
        int r = count;
        for (int i = 0; i < containers.size(); i++) {
            if (containers.get(i).id == id) {
                if (containers.get(i).count > r) {
                    containers.get(i).count = containers.get(i).count - r;
                    r -= r;
                } else {
                    r -= containers.get(i).count;
                    clearSlot(i);
                }
            }
        }
    }

    public void addItem(Container container) {
        addItem(container.getId(), container.getCount());
    }

    public boolean addItem(String package_, String stringId, int count) {
        return addItem(GameContext.get(package_).getId(stringId), count);
    }

    public boolean addItem(int id, int count) {
        for (Container container : containers) {
            if (container.id == id && container.count < GameContext.getItem(container.id).getItemConfiguration().maxCount) {
                int canAdd = GameContext.getItem(container.id).getItemConfiguration().maxCount - container.count;
                if (canAdd > count) {
                    container.count = container.count + count;
                    count -= count;
                } else {
                    container.count = container.count + canAdd;
                    count -= canAdd;
                }
            }
            if (count == 0) {
                return true;
            }
        }
        if (count > 0) {
            int slotsCount = (count % GameContext.getItem(id).getItemConfiguration().maxCount) + 1;
            for (int i = 0; i < slotsCount; i++)
                containers.add(new Container());
        }
        for (Container container : containers) {
            if (container.id == 0) {
                int canAdd = GameContext.getItem(id).getItemConfiguration().maxCount;
                container.id = id;
                container.damage = 0;
                if (canAdd > count) {
                    container.count = container.count + count;
                    count -= count;
                } else {
                    container.count = container.count + canAdd;
                    count -= canAdd;
                }
            }
            if (count == 0) {
                return true;
            }
        }
        return false;
    }

    public void clearSlot(int slot) {
        containers.remove(slot);
    }

    public int getCountOfItems(String id) {
        return getCountOfItems(SAUW.getId(id));
    }

    public int getCountOfItems(int id) {
        int count = 0;
        for (Container container : containers) {
            if (container.id == id) {
                count += container.count;
            }
        }
        return count;
    }

    public float getItemsWeight() {
        if (gameMode == GameMode.CREATIVE) return 0f;

        float itemsWeight = 0f;
        for (Container slot : containers) {
            itemsWeight += slot.count * GameContext.getItem(slot.id).getItemConfiguration().weight;
        }
        return itemsWeight;
    }

    public void removeItemsIfNeed() {
        for (Container container : containers) {
            if (container.damage >= GameContext.getItem(container.id).getItemConfiguration().maxDamage && GameContext.getItem(container.id).getItemConfiguration().maxDamage != 0) {
                slotsToRemove.add(container);
            }
            if (container.count <= 0) {
                slotsToRemove.add(container);
            }
        }
        if (slotsToRemove.size() > 0) {
            containers.removeAll(slotsToRemove);
            slotsToRemove.clear();
        }
    }

    public boolean isInventoryContainer(Container container) {
        for (Container container1 : containers) {
            if (container == container1) return true;
        }
        return false;
    }
}