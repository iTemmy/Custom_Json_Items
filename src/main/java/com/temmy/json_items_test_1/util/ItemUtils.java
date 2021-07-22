package com.temmy.json_items_test_1.util;

import com.temmy.json_items_test_1.attribute.Attribute;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ItemUtils {
    private ItemUtils(){}

    private final static Map<Material, ItemStack> recipeMap = new HashMap<>();

    static {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        Recipe recipe;
        FurnaceRecipe furnaceRecipe;

        while(recipeIterator.hasNext()){
            recipe = recipeIterator.next();
            if (recipe instanceof FurnaceRecipe){
                furnaceRecipe = (FurnaceRecipe) recipe;
                recipeMap.put(furnaceRecipe.getInput().getType(), furnaceRecipe.getResult());
            }
        }
    }

    public static ItemStack getSmeltingResult(Material item){
        return recipeMap.get(item);
    }

    private static String getItemAttrbutes(ItemStack item){
        return  (item != null && item.hasItemMeta() ? item.getItemMeta().getPersistentDataContainer().get(Attribute.namespacedKey, PersistentDataType.STRING) : null);
    }

    public static Map<String, String[]> getItemAttributeMap(ItemStack item){
        String attributes = getItemAttrbutes(item);
        return (attributes != null) ? Convert.stringToMap(attributes) : new HashMap<>();
    }
}
