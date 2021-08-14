package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class onBlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getItemInHand().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }
}
