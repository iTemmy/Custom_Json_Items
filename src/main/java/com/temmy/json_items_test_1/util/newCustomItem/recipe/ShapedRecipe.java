package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShapedRecipe implements RecipeInterface{

    String[] shape;
    ItemStack result;
    Map<Character, RecipeInput> ingredients = new HashMap<>();

    public void setIngredient(char key, RecipeInput input){
        ingredients.put(key, input);
    }

    public void setShape(String[] ingredientKey){
        this.shape = ingredientKey;
    }

    public void setResult(ItemStack result){
        this.result = result;
    }

    public String[] getShape(){
        return shape;
    }

    public Map<Character, RecipeInput> getIngredients(){
        return ingredients;
    }

    @Override
    public ItemStack getResult() {
        return null;
    }
}
