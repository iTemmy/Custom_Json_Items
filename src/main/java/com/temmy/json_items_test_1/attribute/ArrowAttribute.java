package com.temmy.json_items_test_1.attribute;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public final class ArrowAttribute {
    private ArrowAttribute(){}

    public static void trigger(Event e, String[] args){
        if (!(e instanceof EntityShootBowEvent event)) return;
        if (event.getProjectile() == null) return;
        if (Main.worldGuardEnabled)
            if (worldGuard(event)) return;
        for (String arg : args) {
            arg = arg.replaceAll("\"", "");
            arg = arg.replaceAll("}", "");
            String[] s = arg.split(":");
            for (String entity : s){
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

    private static boolean worldGuard(EntityShootBowEvent e){
        WorldGuard worldGuard = Main.getWorldGuard();
        RegionContainer container = worldGuard.getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getEntity().getLocation()));
        if (set == null) return false;
        if (!set.testState(null, Main.attributesEnabledFlag)){
            e.setCancelled(true);
            return true;
        }
        return false;
    }
}
