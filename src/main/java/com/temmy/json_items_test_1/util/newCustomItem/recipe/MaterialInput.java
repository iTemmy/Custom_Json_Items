package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import com.temmy.json_items_test_1.Main;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

import javax.annotation.Nullable;

public class MaterialInput implements RecipeInput{

    Material input;

    public MaterialInput setItem(Material item) {
        if (item instanceof Material)
            input = (Material) item;
        else {
            Main.getPlugin().getLogger().severe("Invalid Material input!");
            return null;
        }
        return this;
    }

    @Override
    @Nullable
    public RecipeChoice getItem() {
        return new RecipeChoice.MaterialChoice(input);
    }
}
