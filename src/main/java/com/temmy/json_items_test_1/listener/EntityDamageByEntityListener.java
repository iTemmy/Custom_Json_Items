package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof LivingEntity)) return;

        LivingEntity damager = (LivingEntity) e.getDamager();
        if (damager.getEquipment().getItemInMainHand() == null) return;
        ItemMeta itemMeta = ((LivingEntity) e.getDamager()).getEquipment().getItemInMainHand().getItemMeta();
        if (itemMeta == null) return;

        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(((LivingEntity) e.getDamager()).getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet()) {
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }
}
