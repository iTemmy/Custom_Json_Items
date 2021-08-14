package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class DenyCustomItemCraft {

    private DenyCustomItemCraft(){}

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
                if (Main.getTest().contains(item.getItemMeta().getLocalizedName().toLowerCase()) && Main.getTest().contains(event.getRecipe().getResult().getItemMeta().getLocalizedName()))
                    return;
            }else {
                event.setCancelled(true);
                event.getInventory().setResult(null);
            }
        }
    }
}
