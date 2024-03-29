package com.kgc.sauw.core.creative_categories;

import com.kgc.sauw.core.registry.Registry;
import com.kgc.sauw.core.resource.Resource;

public class CreativeCategories {
    public static final Registry<Category> registry = new Registry<>("creative_category");

    static {
        //sauw categories
        registry.register(new Category(Resource.getTexture("item/stick.png")), "sauw", "items");
        registry.register(new Category(Resource.getTexture("item/stone_axe.png")), "sauw", "instruments");
        registry.register(new Category(Resource.getTexture("blocks/grass.png")), "sauw", "blocks");
        registry.register(new Category(Resource.getTexture("item/apple.png")), "sauw", "eat");
    }
}
