package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class FurnaceRecipe implements RecipeInterface{

    ItemStack result;
    RecipeInput input;
    float experience;
    int cookingTime;

    public FurnaceRecipe(RecipeInput input, ItemStack result, float experience, int cookingTime){
        this.input = input;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    public void setResult(ItemStack result){
        this.result = result;
    }

    public void setInput(RecipeInput input){
        this.input = input;
    }

    public void setExperience(float experience){
        this.experience = experience;
    }

    public void setCookingTime(int cookingTime){
        this.cookingTime = cookingTime;
    }

    public RecipeInput getInput(){
        return input;
    }

    public float getExperience(){
        return experience;
    }

    public int getCookingTime(){
        return cookingTime;
    }

    @Override
    public ItemStack getResult() {
        return null;
    }

    @Override
    public Recipe getRecipe(NamespacedKey key) {
        return new org.bukkit.inventory.FurnaceRecipe(key, result, input.getItem(), experience ,cookingTime);
    }
}
