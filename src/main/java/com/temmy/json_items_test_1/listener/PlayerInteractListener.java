package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e){
        if (e.getItem() == null || e.getItem().getItemMeta() == null) return;
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getItem().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }
}
