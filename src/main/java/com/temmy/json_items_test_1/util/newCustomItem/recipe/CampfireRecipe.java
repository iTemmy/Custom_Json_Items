package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.inventory.ItemStack;

public class CampfireRecipe implements RecipeInterface{
    ItemStack result;
    RecipeInput input;
    float experience;
    int cookingTime;

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
}
