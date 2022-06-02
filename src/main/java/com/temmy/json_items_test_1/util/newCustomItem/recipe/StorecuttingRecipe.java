package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.inventory.ItemStack;

public class StorecuttingRecipe implements RecipeInterface{

    ItemStack result;
    RecipeInput input;

    public void setResult(ItemStack result){
        this.result = result;
    }

    public void setInput(RecipeInput input){
        this.input = input;
    }

    public RecipeInput getInput(){
        return input;
    }

    @Override
    public ItemStack getResult() {
        return null;
    }
}
