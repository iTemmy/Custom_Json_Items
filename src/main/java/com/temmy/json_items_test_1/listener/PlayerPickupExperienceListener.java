package com.temmy.json_items_test_1.listener;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.PrideSin;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerPickupExperienceListener implements Listener {

    @EventHandler
    public static void onPlayerPickupExperience(PlayerPickupExperienceEvent e){
        if (e.getPlayer().getInventory().getItemInOffHand() != null) {PrideSin.removeArmor(e.getPlayer()); return;}
            if (e.getPlayer().getInventory().getItemInOffHand().hasItemMeta()){
                ItemStack item = e.getPlayer().getInventory().getItemInOffHand();
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(item.getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
    }
}
