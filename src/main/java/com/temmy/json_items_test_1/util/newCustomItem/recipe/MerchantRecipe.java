package com.temmy.json_items_test_1.util.newCustomItem.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MerchantRecipe implements RecipeInterface{

    ItemStack result;
    int uses;
    int maxUses;
    boolean experienceReward;
    int villagerExperience;
    float priceMultiplier;
    int demand;
    int specialPrice;
    boolean ignoreDiscounts;
    List<RecipeInput> ingredients = new ArrayList<>();

    public MerchantRecipe(List<RecipeInput> ingredients, ItemStack result, int uses, int maxUses, boolean experienceReward, int villagerExperience, float priceMultiplier, int demand, int specialPrice, boolean ignoreDiscounts){
        this.ingredients = ingredients;
        this.result = result;
        this.uses = uses;
        this.maxUses = maxUses;
        this.experienceReward = experienceReward;
        this.villagerExperience = villagerExperience;
        this.priceMultiplier = priceMultiplier;
        this.demand = demand;
        this.specialPrice = specialPrice;
        this.ignoreDiscounts = ignoreDiscounts;
    }


    public void setResult(ItemStack result){
        this.result = result;
    }

    public void addIngredient(RecipeInput ingredient){
        ingredients.add(ingredient);
    }

    public void setIngredients(List<RecipeInput> ingredients){
        this.ingredients = ingredients;
    }

    public void setUses(int uses){
        this.uses = uses;
    }

    public void setMaxUses(int maxUses){
        this.maxUses = uses;
    }

    public void setExperienceReward(boolean experienceReward){
        this.experienceReward = experienceReward;
    }

    public void setVillagerExperience(int villagerExperience){
        this.villagerExperience = villagerExperience;
    }

    public void setPriceMultiplier(float priceMultiplier){
        this.priceMultiplier = priceMultiplier;
    }

    public void setDemand(int demand){
        this.demand = demand;
    }

    public void setSpecialPrice(int specialPrice){
        this.specialPrice = specialPrice;
    }

    public void setIgnoreDiscounts(boolean ignoreDiscounts){
        this.ignoreDiscounts = ignoreDiscounts;
    }

    public List<RecipeInput> getIngredients(){
        return ingredients;
    }

    public int getUses(){
        return uses;
    }

    public int getMaxUses(){
        return maxUses;
    }

    public boolean isExperienceReward() {
        return experienceReward;
    }

    public int getVillagerExperience() {
        return villagerExperience;
    }

    public float getPriceMultiplier() {
        return priceMultiplier;
    }

    public int getDemand(){
        return demand;
    }

    public int getSpecialPrice() {
        return specialPrice;
    }

    public boolean getIgnoresDiscounts(){
        return ignoreDiscounts;
    }

    @Override
    public ItemStack getResult() {
        return null;
    }

    @Override
    public Recipe getRecipe(NamespacedKey key) {
        return new org.bukkit.inventory.MerchantRecipe(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier, demand, specialPrice, ignoreDiscounts);
    }
}
