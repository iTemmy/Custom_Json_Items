package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

public class WrathSin {
    private WrathSin(){}

    static Logger log = Main.getPlugin().getLogger();
    static final NamespacedKey wrathKey = new NamespacedKey(Main.getPlugin(), "wrathkey");

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerArmorChangeEvent event)) return;
        Player player = event.getPlayer();
        log.info("WRATH IS HERE");
        EquipmentSlot slot = null;
        int damage = 0;
        int level = 0;
        for (String arg : args){
            arg = arg.replaceAll("[\"{}]", "");
            String[] a = arg.split(",");
            for (String b : a){
                if (b.toLowerCase().contains("slot")) {
                    String[] c = b.split(":");
                    slot = EquipmentSlot.valueOf(c[1]);
                }else if (b.toLowerCase().contains("damage")){
                    String[] c = b.split(":");
                    try {
                        damage = Integer.parseInt(c[1]);
                    }catch (NumberFormatException ignored) {}
                }else if (b.toLowerCase().contains("levels")){
                    String[] c = b.split(":");
                    try {
                        level = Integer.parseInt(c[1]);
                    }catch (NumberFormatException ignored){}
                }
                log.info("b: --> "+b);
            }
            if (slot != null && damage != 0) {
                for (ItemStack item : player.getInventory().getArmorContents()){
                    if (player.getInventory().getHelmet() == item && slot == EquipmentSlot.HEAD){
                        Collection<AttributeModifier> mods = item.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
                        for (AttributeModifier mod : mods){
                            item.getItemMeta().addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", damage, AttributeModifier.Operation.ADD_NUMBER, slot));
                        }
                    }else if (player.getInventory().getChestplate() == item && slot == EquipmentSlot.CHEST){

                    }else if (player.getInventory().getLeggings() == item && slot == EquipmentSlot.LEGS){

                    }else if (player.getInventory().getBoots() == item && slot == EquipmentSlot.FEET){

                    }
                }
                double dmg = player.getLevel()/level;
                AttributeInstance att = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

                player.getPersistentDataContainer().set(wrathKey, PersistentDataType.DOUBLE, dmg);
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(dmg);
                event.getNewItem().getItemMeta().getPersistentDataContainer();
            }
        }

    }

}
