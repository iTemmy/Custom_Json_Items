package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.temmy.json_items_test_1.util.Queue;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FullSetArmorAttributeEffects {
    private FullSetArmorAttributeEffects(){}

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerArmorChangeEvent event)) return;
        String setName = null;
        boolean helmet = false;
        boolean chestplate = false;
        boolean leggings = false;
        boolean boots = false;
        for (String s : args) {
            String[] arg = s.split(",");
            for (String ss : arg)
                if (ss.contains("setName")) {
                    String[] strings = ss.split(":");
                    strings[1] = strings[1].replaceAll("\"", "");
                    setName = strings[1];
                }
        }
        if (setName == null) return;
        for (ItemStack item : event.getPlayer().getInventory().getArmorContents()){
            if (item == null) return;
            if (!(item.hasItemMeta())) return;
            if (!(item.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING))) return;
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
            String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
            for (String s : attributes){
                String[] attribute = s.split(":");
                for (String a : attribute){
                    if (!(a.contains(setName)))
                        if (FullSetArmorEffects.armorType(item.getType()) == 1)
                            helmet = true;
                        else if (FullSetArmorEffects.armorType(item.getType()) == 2)
                            chestplate = true;
                        else if (FullSetArmorEffects.armorType(item.getType()) == 3)
                            leggings = true;
                        else if (FullSetArmorEffects.armorType(item.getType()) == 4)
                            boots = true;
                        else return;
                }
            }
        }
        if (helmet && chestplate && leggings && boots){
            for (String arg : args){
                String[] attributes = arg.split(":");
                for (int i = 0; i < attributes.length; i++) {
                    if (attributes[i].contains("health")) {
                        attributes[i+1] = attributes[i+1].replace("}", "");
                        event.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(event.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getBaseValue()+Integer.parseInt(attributes[i+1]));
                    }else if (attributes[i].contains("speed")) {
                        attributes[i + 1] = attributes[i + 1].replace("}", "");
                        event.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(event.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() + Integer.parseInt(attributes[i+1]));
                    }
                }
            }
        }
    }
}
