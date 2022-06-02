package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.Material;

import javax.annotation.Nullable;

public class MaterialInput implements RecipeInput{

    Material input;

    @Override
    public void setItem(Object item) {
        if (item instanceof Material)
            input = (Material) item;
    }

    @Override
    @Nullable
    public Object getItem() {
        return input;
    }
}
