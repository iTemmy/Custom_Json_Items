package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.Map;

public class EntityShootBowListener implements Listener {
    @EventHandler
    public static void onEntityShootBow(EntityShootBowEvent e){
        if (!e.getBow().hasItemMeta()) return;
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getBow().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }
}
