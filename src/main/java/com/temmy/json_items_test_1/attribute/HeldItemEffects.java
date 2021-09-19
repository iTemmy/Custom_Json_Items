package com.temmy.json_items_test_1.attribute;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Logger;

public class HeldItemEffects {
    private HeldItemEffects(){}

    public static void trigger(Event e, String[] args){
        if (e instanceof PlayerSwapHandItemsEvent) playerSwapHandItem(e, args);
        if (e instanceof PlayerItemHeldEvent) playerItemHeld(e, args);
        if (e instanceof InventoryClickEvent) InventoryClick(e, args);
        if (e instanceof EntityPickupItemEvent) entityPickupItem(e, args);
    }

    public static void playerSwapHandItem(Event e, String[] args){
        PlayerSwapHandItemsEvent event = (PlayerSwapHandItemsEvent) e;
        ItemStack main = event.getMainHandItem();
        ItemStack off = event.getOffHandItem();
        boolean mainb = false;
        boolean offB = false;
        if (main.hasItemMeta()) {
            if (main.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                PersistentDataContainer data = main.getItemMeta().getPersistentDataContainer();
                String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                for (String s : attributes) {
                    String[] arg = s.split(":");
                    for (String ss : arg) {
                        ss = ss.toUpperCase().replaceAll("\"", "");
                        if (ss.toUpperCase().contains("MAIN")) {
                            mainb = true;
                        }
                    }
                    if (mainb) giveEffects(attributes, event.getPlayer());
                }
            }
        }
        if (off != null) {
            if (off.getItemMeta() != null) {
                if (off.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                    PersistentDataContainer data = off.getItemMeta().getPersistentDataContainer();
                    String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                    for (String s : attributes) {
                        String[] arg = s.split(":");
                        for (String ss : arg) {
                            ss = ss.toUpperCase().replaceAll("\"", "");
                            if (ss.toUpperCase().contains("OFF")) {
                                offB = true;
                            }
                        }
                        if (offB) giveEffects(attributes, event.getPlayer());
                    }
                }
            }
        }
    }

    static Logger log = Bukkit.getLogger();

    public static void playerItemHeld(Event e, String[] args){
        PlayerItemHeldEvent event = (PlayerItemHeldEvent) e;
        ItemStack newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
        boolean mainb = false;
        if (newItem != null) {
            if (newItem.hasItemMeta()) {
                if (newItem.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                    PersistentDataContainer data = newItem.getItemMeta().getPersistentDataContainer();
                    String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                    for (String s : attributes) {
                        String[] arg = s.split(":");
                        for (String ss : arg) {
                            ss = ss.toUpperCase().replaceAll("\"", "");
                            if (ss.toUpperCase().contains("MAIN")) {
                                mainb = true;
                            }
                        }
                        if (mainb) giveEffects(attributes, event.getPlayer());
                    }
                }
            }
        }
        ItemStack offhand = event.getPlayer().getInventory().getItemInOffHand();
        boolean offb = false;
        if (offhand != null) {
            if (offhand.hasItemMeta()) {
                if (offhand.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                    PersistentDataContainer data = offhand.getItemMeta().getPersistentDataContainer();
                    String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                    for (String s : attributes) {
                        String[] arg = s.split(":");
                        for (String ss : arg) {
                            ss = ss.toUpperCase().replaceAll("\"", "");
                            if (ss.toUpperCase().contains("OFF")) {
                                offb = true;
                            }
                        }
                        if (offb) giveEffects(attributes, event.getPlayer());
                    }
                }
            }
        }
    }

    public static void InventoryClick(Event e, String[] args){
        InventoryClickEvent event = (InventoryClickEvent) e;
        ItemStack item = event.getView().getPlayer().getInventory().getItemInMainHand();
        boolean mainb = false;
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                    PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                    String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                    for (String s : attributes) {
                        String[] arg = s.split(":");
                        for (String ss : arg) {
                            ss = ss.toUpperCase().replaceAll("\"", "");
                            if (ss.toUpperCase().contains("MAIN")) {
                                mainb = true;
                            }
                        }
                        if (mainb) giveEffects(attributes, (Player) event.getView().getPlayer());
                    }
                }
            }
        }
        ItemStack offhand = event.getView().getPlayer().getInventory().getItemInOffHand();
        boolean offb = false;
        if (offhand != null) {
            if (offhand.hasItemMeta()) {
                if (offhand.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                    PersistentDataContainer data = offhand.getItemMeta().getPersistentDataContainer();
                    String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                    for (String s : attributes) {
                        String[] arg = s.split(":");
                        for (String ss : arg) {
                            ss = ss.toUpperCase().replaceAll("\"", "");
                            if (ss.toUpperCase().contains("OFF")) {
                                offb = true;
                            }
                        }
                        if (offb) giveEffects(attributes, (Player) event.getView().getPlayer());
                    }
                }
            }
        }
    }

    public static void entityPickupItem(Event e, String[] args){
        EntityPickupItemEvent event = (EntityPickupItemEvent) e;
        if (!(event.getEntity().getType() == EntityType.PLAYER)) return;
        ItemStack item = event.getItem().getItemStack();
        boolean mainb = false;
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                    PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                    String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                    for (String s : attributes) {
                        String[] arg = s.split(":");
                        for (String ss : arg) {
                            ss = ss.toUpperCase().replaceAll("\"", "");
                            if (ss.toUpperCase().contains("MAIN")) {
                                mainb = true;
                            }
                        }
                        if (mainb) giveEffects(attributes, (Player) event.getEntity());
                    }
                }
            }
        }
        ItemStack offhand = ((Player) event.getEntity()).getInventory().getItemInOffHand();
        boolean offb = false;
        if (offhand != null) {
            if (offhand.hasItemMeta()) {
                if (offhand.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
                    PersistentDataContainer data = offhand.getItemMeta().getPersistentDataContainer();
                    String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
                    for (String s : attributes) {
                        String[] arg = s.split(":");
                        for (String ss : arg) {
                            ss = ss.toUpperCase().replaceAll("\"", "");
                            if (ss.toUpperCase().contains("OFF")) {
                                offb = true;
                            }
                        }
                        if (offb) giveEffects(attributes, (Player) event.getEntity());
                    }
                }
            }
        }
    }

    public static void giveEffects(String[] args, Player player){
        PotionEffectType type = null;
        int power = -99;
        for (String arg : args){
            String[] s = arg.split(":");
            for (String effect : s){
                effect = effect.replaceAll("\"", "");
                effect = effect.replaceAll("\\{", "");
                effect = effect.replaceAll("\\[", "");
                effect = effect.replaceAll("}", "");
                if (PotionEffectType.getByName(effect) != null)
                    type = PotionEffectType.getByName(effect);
                try {
                    power = Integer.parseInt(effect);
                } catch (NumberFormatException ignored) {
                }
                if (type != null && power != -99){
                    player.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, power-1));
                    power = -99;
                    type = null;
                }
            }
        }
    }
}
