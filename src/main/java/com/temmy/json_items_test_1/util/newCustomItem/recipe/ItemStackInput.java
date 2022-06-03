package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import com.temmy.json_items_test_1.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public class ItemStackInput implements RecipeInput{

    ItemStack itemStack;

    public ItemStackInput setItem(ItemStack item) {
        if (item instanceof ItemStack)
            itemStack = (ItemStack) item;
        else {
            Main.getPlugin().getLogger().severe("Invalid ItemStack input!");
            return null;
        }
        return this;
    }

    @Override
    public RecipeChoice getItem() {
        if (itemStack == null) throw new NullPointerException("ItemStack is null");
        return new RecipeChoice.ExactChoice(itemStack);
    }
}
