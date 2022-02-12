package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.MagicFireball;
import com.temmy.json_items_test_1.util.CustomDataTypes;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityExplode implements Listener {

    static final Logger log = Main.getPlugin().getLogger();

    @EventHandler
    public static void onEntityExplode(EntityExplodeEvent e){
        PersistentDataContainer container = e.getEntity().getPersistentDataContainer();
        if (container.getKeys().contains(MagicFireball.fireballKey)) FireballExplode(e);
    }

    private static void FireballExplode(EntityExplodeEvent e){
        if (!e.getEntity().getPersistentDataContainer().has(MagicFireball.fireballRangeKey, PersistentDataType.INTEGER)) return;
        int range = e.getEntity().getPersistentDataContainer().get(MagicFireball.fireballRangeKey, PersistentDataType.INTEGER);
        List<Entity> entityList = e.getEntity().getNearbyEntities(range, range, range);
        for (Entity ent : entityList){
            if (!(ent instanceof LivingEntity)) continue;
            double distance = ent.getLocation().distance(e.getEntity().getLocation());
            double damage = e.getEntity().getPersistentDataContainer().get(MagicFireball.fireballDamageKey, PersistentDataType.DOUBLE);
            UUID pUUID = e.getEntity().getPersistentDataContainer().get(MagicFireball.fireballShooterKey, CustomDataTypes.UUID);
            if (ent.getUniqueId().equals(pUUID)) continue;
            damage = damage/distance;
            ((LivingEntity) ent).damage(damage, e.getEntity());
        }
        e.setCancelled(true);
        e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getEntity().getLocation(), 10);
        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100, 1);
        e.getEntity().remove();
    }
}
