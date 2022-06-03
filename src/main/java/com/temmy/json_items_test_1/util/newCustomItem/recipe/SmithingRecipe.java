package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import com.temmy.json_items_test_1.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class SmithingRecipe implements RecipeInterface{

    ItemStack result;
    RecipeInput input;
    RecipeInput addition;

    public SmithingRecipe(RecipeInput input, RecipeInput addition, ItemStack result){
        if (result == null) Main.getPlugin().getLogger().severe("result is null");
        this.input = input;
        this.addition = addition;
        this.result = result;
    }

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

    @Override
    public Recipe getRecipe(NamespacedKey key) {
        return new org.bukkit.inventory.SmithingRecipe(key, result, input.getItem(), addition.getItem());
    }
}
