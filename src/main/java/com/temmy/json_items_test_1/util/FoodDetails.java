package com.temmy.json_items_test_1.util;

import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodDetails {

    int food = 0;
    float saturation = 0;
    List<PotionEffect> addingEffects = new ArrayList<PotionEffect>();
    List<PotionEffect> removingEffects = new ArrayList<PotionEffect>();
    boolean chorusTeleportation = false;

    /**
     @param food is the amount of food that is replenished when the player eats the item
     @param saturation is the amount of saturation that is replenished when the player eats the item
     @param effects is the list of effects that should be applied when the player eats the item
     **/
    public FoodDetails(int food, float saturation, List<PotionEffect> effects) {
        setFood(food);
        setSaturation(saturation);
        setAddingEffects(effects);
    }

    /**
     @param food is the amount of food that is replenished when the player eats the item
     @param saturation is the amount of saturation that is replenished when the player eats the item
     @param effects is the list of effects that should be applied when the player eats the item
     @param teleport if set to true will make the player teleport like they had just eaten a chorus fruit
     **/
    public FoodDetails(int food, float saturation, List<PotionEffect> effects, boolean teleport) {
        setFood(food);
        setSaturation(saturation);
        setAddingEffects(effects);
        setChorusTeleportation(teleport);
    }

    /**
     @param food is the amount of food that is replenished when the player eats the item
     @param saturation is the amount of saturation that is replenished when the player eats the item
     @param teleport if set to true will make the player teleport like they had just eaten a chorus fruit
     **/
    public FoodDetails(int food, float saturation, boolean teleport) {
        setFood(food);
        setSaturation(saturation);
        setChorusTeleportation(teleport);
    }

    /**
     @param food is the amount of food that is replenished when the player eats the item
     @param saturation is the amount of saturation that is replenished when the player eats the item
     **/
    public FoodDetails(int food, float saturation) {
        setFood(food);
        setSaturation(saturation);
    }

    /**
     @param food is the amount of food that is replenished when the player eats the item
     @param saturation is the amount of saturation that is replenished when the player eats the item
     @param effects <PotionEffect, Boolean> if the boolean is set to true then it will give the player the status effect and if
     it is set to false it will remove the potionEffect from the player
     **/
    public FoodDetails(int food, float saturation, Map<PotionEffect, Boolean> effects){
        setFood(food);
        setSaturation(saturation);
        for (PotionEffect effect : effects.keySet()){
            if (effects.get(effect) == true){

            }
        }
    }

    /**
     @param effects </PotionEffect, Boolean> if the Boolean is set to true the PotionEffect will be applied to the
     player and if it is set to false it will be removed from the player
     **/
    public FoodDetails(Map<PotionEffect, Boolean> effects){
        for (PotionEffect effect : effects.keySet()){
            if (effects.get(effect) == true) addingEffects.add(effect);
            else removingEffects.add(effect);
        }
    }

    public int getFood() {
        return food;
    }


    public float getSaturation() {
        return saturation;
    }

    public List<PotionEffect> getAddingEffects() {
        return addingEffects;
    }

    public List<PotionEffect> getRemovingEffects(){
        return removingEffects;
    }

    public boolean getChorusTeleportation() {
        return chorusTeleportation;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public void setAddingEffects(List<PotionEffect> addingEffects) {
        this.addingEffects = addingEffects;
    }

    public void setRemovingEffects(List<PotionEffect> removingEffects){
        this.removingEffects = removingEffects;
    }

    public void addEffect(PotionEffect newEffect) {
        for (PotionEffect effect : addingEffects)
            if (effect.getType().equals(newEffect.getType()) && effect.getAmplifier() > newEffect.getAmplifier())
                return;
        addingEffects.add(newEffect);
    }

    public void setChorusTeleportation(boolean newTeleportation) {
        chorusTeleportation =newTeleportation;
    }
}
