package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class onBlockDropItemListener implements Listener {
    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent e){
        ItemMeta itemMeta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        if (itemMeta == null) return;
        String attributes = itemMeta.getPersistentDataContainer().get(Attribute.namespacedKey, PersistentDataType.STRING);
        if (attributes == null) return;

        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getPlayer().getInventory().getItemInMainHand());
        for (String attribute : attributeMap.keySet()){
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
        }
    }
}
