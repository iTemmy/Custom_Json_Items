package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.Parser.Item;
import org.bukkit.Keyed;
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
            if (item.hasLocalizedName())
                itemLocalized = true;
            if (event.getRecipe().getResult().hasLocalizedName())
                recipeLocalized = true;
            if (event.getRecipe() instanceof Keyed) {
                Main.getPlugin().getLogger().info(Main.getPlugin().getName());
                if (((Keyed) event.getRecipe()).getKey().getNamespace().toLowerCase().equals(Main.getPlugin().getName())){
                    Main.getPlugin().getLogger().info("correct");
                }
            }
            if (recipeLocalized && itemLocalized) {
                if (new Item().read(item.getLocalizedName().toLowerCase()) != null && new Item().read(event.getRecipe().getResult().getLocalizedName()) != null)
                    return;
            }else {
                event.setCancelled(true);
                event.getInventory().setResult(null);
            }
        }
    }
}
