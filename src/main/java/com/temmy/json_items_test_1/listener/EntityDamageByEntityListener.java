package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.GluttonySin;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.logging.Logger;

public class EntityDamageByEntityListener implements Listener {

    static Logger log = Bukkit.getLogger();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Arrow) arrowDamage((Arrow) e.getDamager(), e);
        if (e.getDamager() instanceof Trident) GluttonySin.entityHitByTrident(e);
        if (!(e.getDamager() instanceof LivingEntity)) return;

        if (e.isCancelled()) return;

        LivingEntity damager = (LivingEntity) e.getDamager();
        if (damager.getEquipment().getItemInMainHand() == null) return;
        ItemMeta itemMeta = ((LivingEntity) e.getDamager()).getEquipment().getItemInMainHand().getItemMeta();
        if (itemMeta == null) return;

        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(((LivingEntity) e.getDamager()).getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet()) {
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
        }
    }

    private static void arrowDamage(Arrow arrow, Event e){
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(arrow.getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }
}
