package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Logger;

public class PrideSin {

    private PrideSin(){}

    static Logger log = Bukkit.getLogger();
    static final NamespacedKey defaultArmor = new NamespacedKey(Main.getPlugin(), "defaultarmor");
    public static NamespacedKey getDefaultArmor(){return defaultArmor;}

    public static void trigger(Event e, String[] args){
        if (e instanceof PlayerPickupExperienceEvent) playerPickupExperience(e, args);
        if (e instanceof PlayerSwapHandItemsEvent) playerSwapHandItem(e, args);
        if (e instanceof InventoryClickEvent) InventoryClick(e, args);
    }

    public static void removeArmor(Player player){
        if (player.getPersistentDataContainer().has(defaultArmor, PersistentDataType.DOUBLE)){
            player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).setBaseValue(player.getPersistentDataContainer().get(defaultArmor, PersistentDataType.DOUBLE));
            player.getPersistentDataContainer().remove(defaultArmor);
        }
    }

    private static void InventoryClick(Event e, String[] args) {
        InventoryClickEvent event = (InventoryClickEvent) e;
        Player player = (Player) event.getView().getPlayer();
        if (player.getInventory().getItemInOffHand().getType() == Material.AIR){removeArmor(player); return;}
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (!offHand.hasItemMeta()){removeArmor(player); return;}
        PersistentDataContainer data = offHand.getItemMeta().getPersistentDataContainer();
        double level = -99;
        if (data.has(Attribute.namespacedKey, PersistentDataType.STRING)){
            String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
            for (String s : attributes){
                s = s.replaceAll("[\\{\\[\\}\\]]","");
                String[] arg = s.split(":");
                for (String ss : arg)
                    try {
                        level = Integer.parseInt(ss.trim());
                    }catch (NumberFormatException ignored){}
            } if (level == -99){ removeArmor(player); return;}
            double armorLevel = Math.floor(player.getLevel() / level);
            player.getPersistentDataContainer().set(defaultArmor, PersistentDataType.DOUBLE, player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).getBaseValue());
            player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).getBaseValue()+armorLevel);
        }
    }

    private static void playerSwapHandItem(Event e, String[] args) {
        PlayerSwapHandItemsEvent event = (PlayerSwapHandItemsEvent) e;
        Player player = event.getPlayer();
        if (event.getOffHandItem() == null){ removeArmor(event.getPlayer()); return;}
        ItemStack offHand = event.getOffHandItem();
        if (!offHand.hasItemMeta()){removeArmor(event.getPlayer()); return;}
        PersistentDataContainer data = offHand.getItemMeta().getPersistentDataContainer();
        double level = -99;
        if (data.has(Attribute.namespacedKey, PersistentDataType.STRING)){
            String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
            for (String s : attributes){
                s = s.replaceAll("[\\{\\[\\}\\]]", "");
                String[] arg = s.split(":");
                for (String ss : arg)
                    try {
                        level = Integer.parseInt(ss.trim());
                    }catch (NumberFormatException ignored){}
            }
            if (level == -99){removeArmor(player); return;}
            double armorLevel = Math.floor(player.getLevel() / level);
            player.getPersistentDataContainer().set(defaultArmor, PersistentDataType.DOUBLE, player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).getBaseValue());
            player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).getBaseValue()+armorLevel);
        }
    }

    private static void playerPickupExperience(Event e, String[] args){
        PlayerPickupExperienceEvent event = (PlayerPickupExperienceEvent) e;
        Player player = event.getPlayer();
        if (event.getPlayer().getInventory().getItemInOffHand() == null) {removeArmor(event.getPlayer()); return;}
        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (!offhand.hasItemMeta()){removeArmor(player); return;}
        PersistentDataContainer data = offhand.getItemMeta().getPersistentDataContainer();
        double level = -99;
        if (data.has(Attribute.namespacedKey, PersistentDataType.STRING)){
            String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
            for (String s : attributes){
                s = s.replaceAll("[\\{\\[\\}\\]]", "");
                String[] arg = s.split(":");
                for (String ss : arg){
                    try {
                        level = Integer.parseInt(ss.trim());
                    }catch (NumberFormatException ignored){}
                }
            }
            if (level == -99){removeArmor(player); return;}
            double armorLevel = Math.floor(player.getLevel() / level);
            player.getPersistentDataContainer().set(defaultArmor, PersistentDataType.DOUBLE, player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).getBaseValue());
            player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).setBaseValue(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).getBaseValue()+armorLevel);
        }
    }

}
