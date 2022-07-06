package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class StonecuttingRecipe implements RecipeInterface{

    ItemStack result;
    RecipeInput input;

    public StonecuttingRecipe(RecipeInput input, ItemStack result){
        this.input = input;
        this.result = result;
    }

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

    @Override
    public Recipe getRecipe(NamespacedKey key) {
        return new org.bukkit.inventory.StonecuttingRecipe(key, result, input.getItem());
    }
}
