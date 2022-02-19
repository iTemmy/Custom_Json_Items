package com.temmy.json_items_test_1.attribute;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.temmy.json_items_test_1.Main;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AttackEffect {

    private AttackEffect(){
    }

    public static void trigger(Event e, String[] args) {
        if (!(e instanceof EntityDamageByEntityEvent event)) return;
        if (!(event.getDamager() instanceof LivingEntity damager)) return;
        if (Main.worldGuardEnabled)
            worldGuard(event);
        LivingEntity damagee = (LivingEntity) event.getEntity();

        for (String s : args){
            int power = 0;
            int time = 0;
            PotionEffectType type = null;

            String[] effects = s.split(",");
            for (String string : effects) {
                if (string.contains("type")) {
                    String[] effect = string.split("\"");
                    type = PotionEffectType.getByName(effect[3]);
                }
                if (string.contains("power")){
                    String[] sPower = string.split(":");
                    power = Integer.parseInt(sPower[1]);
                }
                if (string.contains("time")) {
                    String[] sTime = string.split(":");
                    time = Integer.parseInt(sTime[1]);
                }
            }
            damagee.addPotionEffect(new PotionEffect(type, time, power));
            return;
        }
    }

    private static void worldGuard(EntityDamageByEntityEvent e){
        WorldGuard worldGuard = Main.getWorldGuard();
        RegionContainer container = worldGuard.getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getEntity().getLocation()));
        if (set == null) return;
        if (!set.testState(null, Main.attributesEnabledFlag)) {
            e.setCancelled(true);
            return;
        }
        set = query.getApplicableRegions(BukkitAdapter.adapt(e.getDamager().getLocation()));
        if (set == null) return;
        if (!set.testState(null, Main.attributesEnabledFlag)) {
            e.setCancelled(true);
        }
    }

}
