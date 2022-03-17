package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class DenyCustomItemCraft {

    private DenyCustomItemCraft(){}

    //TODO Change the ItemMeta.hasLocalizedName to the new version or checking if displayName is instanceof translatable

    public static void trigger(Event e, String[] args){
        if (!(e instanceof CraftItemEvent)) return;
        CraftItemEvent event = (CraftItemEvent) e;
        boolean recipeLocalized = false;
        boolean itemLocalized = false;
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item == null) continue;
            if (item.getItemMeta().hasLocalizedName())
                itemLocalized = true;
            if (event.getRecipe().getResult().getItemMeta().hasLocalizedName())
                recipeLocalized = true;
            if (recipeLocalized && itemLocalized) {
                if (Main.getCustomItems_OLD().containsValue(item.getItemMeta().getLocalizedName().toLowerCase()) && Main.getCustomItems_OLD().containsValue(event.getRecipe().getResult().getItemMeta().getLocalizedName()))
                    return;
            }else {
                event.setCancelled(true);
                event.getInventory().setResult(null);
            }
        }
    }
}
