package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.Map;
import java.util.logging.Logger;

public class ProjectileLaunchListener implements Listener {

    static Logger log = Main.getPlugin().getLogger();

    @EventHandler
    public static void onProjectileLaunch(ProjectileLaunchEvent e){
        if (e.getEntityType() == EntityType.TRIDENT){
            Trident trident = (Trident) e.getEntity();
            if (trident.getItemStack() == null) return;
            if (!trident.getItemStack().hasItemMeta()) return;
            Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(trident.getItemStack().getItemMeta().getPersistentDataContainer());
            for (String attribute : attributeMap.keySet())
                Attribute.invoke(attribute, e, attributeMap.get(attribute));
        }
    }
}
