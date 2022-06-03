package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface RecipeInterface {

    ItemStack getResult();

    Recipe getRecipe(NamespacedKey key);

}
