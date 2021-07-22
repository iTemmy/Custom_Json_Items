package com.temmy.json_items_test_1.attribute.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.logging.Logger;

public class Poison {
    private Poison(){}

    static Logger log = Bukkit.getLogger();

    public static void trigger(Event e, String[] args) {
        if (!(e instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
        if (!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        LivingEntity damagee = (LivingEntity) event.getEntity();

        String entityname = damagee.getType().name().toUpperCase();
        for (String s : args) {
            if (entityname.matches(String.format("(.*)%s(.*)$", s))) {
                damagee.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5, 1));
                return;
            }
        }

        boolean whitelisted = false;
        boolean blackListed = false;
        for (String s : args) {
            boolean blackList = s.charAt(0) == '!';
            boolean match = entityname.matches(String.format("(.*)%s(.*)^", s));
            if (!blackList && match)
                whitelisted = true;
            else if (blackList && match)
                blackListed = true;
        }
        if (!whitelisted || blackListed){
            return;
        }

    }
}
