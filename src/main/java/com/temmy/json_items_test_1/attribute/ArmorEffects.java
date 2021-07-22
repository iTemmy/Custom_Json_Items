package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ArmorEffects {
    private ArmorEffects(){}

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerArmorChangeEvent)) return;
        PlayerArmorChangeEvent event = (PlayerArmorChangeEvent) e;
        Player player  = event.getPlayer();

        String[] potionEffect;
        int effectAmplifier;
        PotionEffectType potionEffectType;
        for (String s : args){
            System.out.println("test");
            potionEffect = s.split("\s*;", 2);
            potionEffectType = PotionEffectType.getByName(potionEffect[0]);
            if (potionEffectType == null) continue;
            effectAmplifier = Integer.parseInt(potionEffect[1]);
            player.addPotionEffect(new PotionEffect(potionEffectType, Integer.MAX_VALUE, effectAmplifier));
        }
    }
}
