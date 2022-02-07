package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.MagicNecromancer;
import com.temmy.json_items_test_1.util.CustomDataTypes;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTargetLivingEntity implements Listener {

    @EventHandler
    public static void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent e){
        if (!(e.getEntity() instanceof Zombie)) return;
        try {
            if (!e.getEntity().getPersistentDataContainer().getKeys().contains(MagicNecromancer.playerKey))
                Main.getPlugin().getLogger().info("fuck this shit");
        } catch (NoSuchMethodError ex) {
            ex.printStackTrace();
        }
        if (e.getTarget() != null && e.getTarget().getUniqueId().equals(e.getEntity().getPersistentDataContainer().get(MagicNecromancer.playerKey, CustomDataTypes.UUID)))
            e.setCancelled(true);
    }
}
