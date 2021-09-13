package com.temmy.json_items_test_1.attribute;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public final class ArrowAttribute {
    private ArrowAttribute(){}

    public static void trigger(Event e, String[] args){
        if (!(e instanceof EntityShootBowEvent)) return;
        EntityShootBowEvent event = (EntityShootBowEvent) e;
        if (event.getProjectile() == null) return;
        for (String arg : args) {
            arg = arg.replaceAll("\"", "");
            arg = arg.replaceAll("}", "");
            String[] s = arg.split(":");
            for (String entity : s){
                Bukkit.getLogger().info(entity);
                if (entity.toUpperCase().contains("ENTITY")) continue;
                try {
                    if (!(EntityType.valueOf(entity) == null)) {
                        Entity ent = event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.valueOf(entity.toUpperCase()), CreatureSpawnEvent.SpawnReason.CUSTOM);
                        ent.setVelocity(event.getProjectile().getVelocity());
                        event.setProjectile(ent);
                    }
                }catch (IllegalArgumentException ignored){
                }
            }
        }
    }
}
