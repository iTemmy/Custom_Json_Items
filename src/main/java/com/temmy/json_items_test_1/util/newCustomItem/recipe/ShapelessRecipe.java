package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipe implements RecipeInterface{

    List<RecipeInput> ingredients = new ArrayList<>();
    ItemStack result;

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
}
