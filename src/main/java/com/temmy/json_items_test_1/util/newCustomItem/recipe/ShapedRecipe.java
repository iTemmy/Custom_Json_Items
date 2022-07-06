package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import com.temmy.json_items_test_1.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Map;

public class ShapedRecipe implements RecipeInterface{

    String[] shape;
    ItemStack result;
    Map<Character, RecipeInput> ingredients = new HashMap<>();

    public ShapedRecipe(String[] shape, ItemStack result, Map<Character, RecipeInput> ingredients){
        this.shape = shape;
        this.result = result;
        this.ingredients = ingredients;
    }

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

    public Recipe getRecipe(NamespacedKey key){
        org.bukkit.inventory.ShapedRecipe recipe = new org.bukkit.inventory.ShapedRecipe(key, result);
        recipe.shape(shape);
        for (char charKey : ingredients.keySet()){
            recipe.setIngredient(charKey, ingredients.get(charKey).getItem());
        }
        return recipe;
    }
}
