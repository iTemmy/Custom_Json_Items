package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Keyed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CraftItemListener implements Listener {
    @EventHandler
    public void onCraftItem(CraftItemEvent e){
        if (e.getRecipe() instanceof Keyed) {
            Main.getPlugin().getLogger().info(Main.getPlugin().getName());
            Main.getPlugin().getLogger().info(((Keyed) e.getRecipe()).getKey().getNamespace());
            if (((Keyed) e.getRecipe()).getKey().getNamespace().equalsIgnoreCase(Main.getPlugin().getName())){
                Main.getPlugin().getLogger().info("correct");
            }
        }
        for (ItemStack item : e.getInventory().getMatrix()) {
            if (item == null || !item.hasItemMeta()) continue;
            Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(item.getItemMeta().getPersistentDataContainer());
            for (String attribute : attributeMap.keySet())
                Attribute.invoke(attribute, e, attributeMap.get(attribute));
        }
    }
}
