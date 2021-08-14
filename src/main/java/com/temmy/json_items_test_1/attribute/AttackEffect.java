package com.temmy.json_items_test_1.attribute;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.logging.Logger;

public class AttackEffect {

    private AttackEffect(){
    }

    public static void trigger(Event e, String[] args) {
        if (!(e instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
        if (!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
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
}
