package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.temmy.json_items_test_1.util.ItemUtils;
import com.temmy.json_items_test_1.util.Queue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.logging.Logger;

public final class ArmorEffects {
    private ArmorEffects(){}

    static Logger log = Bukkit.getLogger();

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerArmorChangeEvent)) return;
        PlayerArmorChangeEvent event = (PlayerArmorChangeEvent) e;
        Player player  = event.getPlayer();

        for (String arg : args){
            String[] effects = arg.split(",");
            int power = -99;
            PotionEffectType type = null;
            for (String s : effects) {
                String[] effect = s.split("\"");
                for (String ss : effect) {
                    String[] strings = ss.split(":");
                    for (String l : strings) {
                        if (l.contains("}"))
                            l = l.replaceFirst("}", "");
                        if (l.equals("{"))
                            continue;
                        if (PotionEffectType.getByName(l) != null)
                            type = PotionEffectType.getByName(l);
                        try {
                            power = Integer.parseInt(l);
                        }catch (NumberFormatException ignored){

                        }
                        if (type != null && power != -99) {
                            player.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, power-1));
                            power = -99;
                            type = null;
                        }
                    }
                }
            }
        }

    }

    public static void checkPlayerArmor(ItemStack[] armor, Player player){
        for (ItemStack item : armor){
            if (item == null) continue;
            Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(item.getItemMeta().getPersistentDataContainer());
            String[] armorEffects = attributeMap.get("ARMOREFFECTS");
            Queue<String> stringQueue = new Queue<>();
            for (String arg : armorEffects) {
                String[] effects = arg.split(",");
                int power = -99;
                PotionEffectType type = null;
                for (String s : effects) {
                    String[] effect = s.split("\"");
                    for (String ss : effect) {
                        String[] strings = ss.split(":");
                        for (String l : strings) {
                            if (l.contains("}"))
                                l = l.replaceFirst("}", "");
                            if (l.equals("{"))
                                continue;
                            if (PotionEffectType.getByName(l) != null)
                                type = PotionEffectType.getByName(l);
                            try {
                                power = Integer.parseInt(l);
                            } catch (NumberFormatException ignored) {

                            }
                            stringQueue.enqueue(l);
                            if (type != null && power != -99) {
                                player.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, power - 1));
                                power = -99;
                                type = null;
                            }
                        }
                    }
                }
            }
        }
    }
}
