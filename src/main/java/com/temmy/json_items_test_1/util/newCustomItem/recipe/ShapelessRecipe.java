package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipe implements RecipeInterface{

    List<RecipeInput> ingredients = new ArrayList<>();
    ItemStack result;

    public ShapelessRecipe(List<RecipeInput> ingredients, ItemStack result){
        this.ingredients = ingredients;
        this.result = result;
    }

    public void addIngredient(RecipeInput input){
        ingredients.add(input);
    }

    public void setIngredients(List<RecipeInput> ingredients){
        this.ingredients = ingredients;
    }

    public void removeIngredient(RecipeInput ingredient){
        ingredients.remove(ingredient);
    }

    public void setResult(ItemStack result){
        this.result = result;
    }

    public List<RecipeInput> getIngredients(){
        return ingredients;
    }

    @Override
    public ItemStack getResult() {
        return null;
    }

    @Override
    public Recipe getRecipe(NamespacedKey key) {
        org.bukkit.inventory.ShapelessRecipe recipe = new org.bukkit.inventory.ShapelessRecipe(key, result);
        for (RecipeInput input : ingredients)
            recipe.addIngredient(input.getItem());
        return recipe;
    }
}
