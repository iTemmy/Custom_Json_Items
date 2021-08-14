package com.temmy.json_items_test_1.util;

import com.temmy.json_items_test_1.attribute.Attribute;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataContainer;
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

    private static String getItemAttrbutes(PersistentDataContainer container){
        return  (container != null ? container.get(Attribute.namespacedKey, PersistentDataType.STRING) : null);
    }

    public static Map<String, String[]> getItemAttributeMap(PersistentDataContainer container){
        String attributes = getItemAttrbutes(container);
        return (attributes != null) ? Convert.stringToMap(attributes) : new HashMap<>();
    }

    public static boolean isOre(Material mat){
        if (mat == Material.COAL_ORE || mat == Material.COPPER_ORE || mat == Material.GOLD_ORE ||
                mat == Material.IRON_ORE || mat == Material.NETHER_GOLD_ORE || mat == Material.REDSTONE_ORE ||
                mat == Material.LAPIS_ORE || mat == Material.DIAMOND_ORE || mat == Material.EMERALD_ORE ||
                mat == Material.DEEPSLATE_COAL_ORE || mat == Material.DEEPSLATE_COPPER_ORE ||
                mat == Material.DEEPSLATE_GOLD_ORE || mat == Material.DEEPSLATE_IRON_ORE ||
                mat == Material.DEEPSLATE_REDSTONE_ORE || mat == Material.DEEPSLATE_LAPIS_ORE ||
                mat == Material.DEEPSLATE_DIAMOND_ORE || mat == Material.DEEPSLATE_EMERALD_ORE ||
                mat == Material.NETHER_QUARTZ_ORE) return true;
        return false;
    }
}
