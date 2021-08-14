package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.temmy.json_items_test_1.util.Queue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Logger;

public class FullSetArmorEffects {

    private FullSetArmorEffects(){
    }

    static Logger log = Bukkit.getLogger();

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerArmorChangeEvent)) return;
        PlayerArmorChangeEvent event = (PlayerArmorChangeEvent) e;
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
                        if (armorType(item.getType()) == 1)
                            helmet = true;
                        else if (armorType(item.getType()) == 2)
                            chestplate = true;
                        else if (armorType(item.getType()) == 3)
                            leggings = true;
                        else if (armorType(item.getType()) == 4)
                            boots = true;
                        else return;
                }
            }
        }
        if (helmet && chestplate && leggings && boots){
            Player player  = event.getPlayer();
            Queue<String> stringQueue = new Queue<>();
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
                            stringQueue.enqueue(l);
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
    }

    public static int armorType(Material mat){
        if (mat == Material.CHAINMAIL_HELMET || mat == Material.IRON_HELMET || mat == Material.DIAMOND_HELMET
                || mat == Material.GOLDEN_HELMET || mat == Material.LEATHER_HELMET || mat == Material.NETHERITE_HELMET
                || mat == Material.TURTLE_HELMET) return 1;
        else if (mat == Material.CHAINMAIL_CHESTPLATE || mat == Material.IRON_CHESTPLATE || mat == Material.DIAMOND_CHESTPLATE
                || mat == Material.GOLDEN_CHESTPLATE || mat == Material.LEATHER_CHESTPLATE || mat == Material.NETHERITE_CHESTPLATE) return 2;
        else if (mat == Material.CHAINMAIL_LEGGINGS || mat == Material.LEATHER_LEGGINGS || mat == Material.DIAMOND_LEGGINGS
                || mat == Material.GOLDEN_LEGGINGS || mat == Material.IRON_LEGGINGS || mat == Material.NETHERITE_LEGGINGS) return 3;
        else if (mat == Material.CHAINMAIL_BOOTS || mat == Material.IRON_BOOTS || mat == Material.GOLDEN_BOOTS
                || mat == Material.DIAMOND_BOOTS || mat == Material.LEATHER_BOOTS || mat == Material.NETHERITE_BOOTS) return 4;
        else return -99;
    }
}
