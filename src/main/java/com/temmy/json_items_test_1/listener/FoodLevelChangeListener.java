package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FoodLevelChangeListener implements Listener {
    @EventHandler
    public static void onFoodLevelChange(FoodLevelChangeEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (item.getType() == Material.BUNDLE) {
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(item.getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
        }
    }
}
