package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.MagicNecromancer;
import com.temmy.json_items_test_1.util.PersistentDataTypes.CustomDataTypes;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTargetLivingEntity implements Listener {

    @EventHandler
    public static void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent e){
        if (!(e.getEntity() instanceof LivingEntity)) return;
        try {
            if (e.getEntity().getPersistentDataContainer().getKeys().contains(MagicNecromancer.playerKey))
                if (e.getTarget() != null && e.getTarget().getUniqueId().equals(e.getEntity().getPersistentDataContainer().get(MagicNecromancer.playerKey, CustomDataTypes.UUID))) {
                    if (!(MagicNecromancer.targetEntity((LivingEntity) e.getEntity()) instanceof LivingEntity ent)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (ent != null)
                        e.setTarget(ent);
                }
        } catch (NoSuchMethodError ex) {
            ex.printStackTrace();
        }
    }
}
