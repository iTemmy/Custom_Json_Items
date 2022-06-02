package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.inventory.ItemStack;

public class SmithingRecipe implements RecipeInterface{

    ItemStack result;
    RecipeInput input;
    RecipeInput addition;

    public void setResult(ItemStack result){
        this.result = result;
    }

    public void setInput(RecipeInput input){
        this.input = input;
    }

    public void setAddition(RecipeInput addition){
        this.addition = addition;
    }

    public RecipeInput getInput(){
        return input;
    }

    public RecipeInput getAddition(){
        return addition;
    }

    @Override
    public ItemStack getResult() {
        return null;
    }
}
