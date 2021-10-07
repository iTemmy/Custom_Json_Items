package com.temmy.json_items_test_1.attribute;

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

public class HeldItemEffects {
    private HeldItemEffects(){}

    public static void trigger(Event e, String[] args){
        if (e instanceof PlayerSwapHandItemsEvent){
            getItemEffects(((PlayerSwapHandItemsEvent) e).getPlayer(),((PlayerSwapHandItemsEvent) e).getOffHandItem(), "off");
            getItemEffects(((PlayerSwapHandItemsEvent )e).getPlayer(), ((PlayerSwapHandItemsEvent) e).getMainHandItem(), "main");
        }
        if (e instanceof PlayerItemHeldEvent){
            getItemEffects(((PlayerItemHeldEvent) e).getPlayer(), ((PlayerItemHeldEvent) e).getPlayer().getInventory().getItem(((PlayerItemHeldEvent) e).getNewSlot()), "main");
        }
        if (e instanceof InventoryClickEvent){
            getItemEffects((Player) ((InventoryClickEvent) e).getView().getPlayer(), ((InventoryClickEvent) e).getView().getPlayer().getInventory().getItemInMainHand(), "main");
            getItemEffects((Player) ((InventoryClickEvent) e).getView().getPlayer(), ((InventoryClickEvent) e).getView().getPlayer().getInventory().getItemInOffHand(), "off");
        }
        if (e instanceof EntityPickupItemEvent){
            if (!(((EntityPickupItemEvent) e).getEntity() instanceof Player)) return;
            getItemEffects((Player) ((EntityPickupItemEvent) e).getEntity(), ((EntityPickupItemEvent) e).getItem().getItemStack(), "main");
        }
    }

    public static void getItemEffects(Player player, ItemStack item, String hand){
        boolean b = false;
        if (item == null) return;
        if (!(item.hasItemMeta())) return;
        if (!item.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) return;
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        String[] attributes = data.get(Attribute.namespacedKey, PersistentDataType.STRING).split(",");
        for (String s : attributes){
            String[] arg = s.split(":");
            for (String ss : arg) {
                ss = ss.toUpperCase().replaceAll("\"", "");
                if (ss.toUpperCase().contains(hand.toUpperCase())) {
                    b = true;
                }
            }
            if (b) giveEffects(attributes, player);
        }
    }

    public static void giveEffects(String[] args, Player player){
        PotionEffectType type = null;
        int power = -99;
        for (String arg : args){
            String[] s = arg.split(":");
            for (String effect : s){
                effect = effect.replaceAll("[\"{\\[}]", "");
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
