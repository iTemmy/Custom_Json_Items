package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerItemHeldListener implements Listener {
    @EventHandler
    public static void onPlayerItemHeld(PlayerItemHeldEvent e){
        PlayerSwapHandItemListener.removeHeldItemEffects(e.getPlayer(), e.getPlayer().getInventory().getItem(e.getPreviousSlot()));
        ItemStack newItem = e.getPlayer().getInventory().getItem(e.getNewSlot());
        ItemStack offhand = e.getPlayer().getInventory().getItemInOffHand();

        if (newItem != null) {
            if (newItem.hasItemMeta()) {
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(newItem.getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
        }
        if (offhand != null) {
            if (offhand.hasItemMeta()) {
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(offhand.getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
        }
    }
}
