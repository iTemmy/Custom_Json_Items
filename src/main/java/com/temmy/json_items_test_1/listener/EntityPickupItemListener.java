package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EntityPickupItemListener implements Listener {
    @EventHandler
    public static void onEntityPickupItem(EntityPickupItemEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        ItemStack item = e.getItem().getItemStack();
        if (item != null) {
            if (item.hasItemMeta()) {
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(item.getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
        }
    }
}
