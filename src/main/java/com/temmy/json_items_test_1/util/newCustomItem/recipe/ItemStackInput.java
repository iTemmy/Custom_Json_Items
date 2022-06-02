package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.inventory.ItemStack;

public class ItemStackInput implements RecipeInput{

    ItemStack itemStack;

    @Override
    public void setItem(Object item) {
        if (item instanceof ItemStack)
            itemStack = (ItemStack) item;
    }

    @Override
    public Object getItem() {
        return itemStack;
    }
}
