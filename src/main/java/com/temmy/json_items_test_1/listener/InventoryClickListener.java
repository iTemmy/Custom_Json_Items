package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.PrideSin;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.logging.Logger;

public class InventoryClickListener implements Listener {
    static Logger log = Bukkit.getLogger();
    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e){
        PlayerSwapHandItemListener.removeHeldItemEffects((Player) e.getView().getPlayer(), e.getView().getPlayer().getInventory().getItemInMainHand());
        PlayerSwapHandItemListener.removeHeldItemEffects((Player) e.getView().getPlayer(), e.getView().getPlayer().getInventory().getItemInOffHand());
        PrideSin.removeArmor((Player) e.getView().getPlayer());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->{
            if (e.getView().getPlayer() != null)
            if (e.getView().getPlayer().getInventory().getItemInMainHand() == null) return;
            Player player = (Player) e.getView().getPlayer();
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand.hasItemMeta()){
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(hand.getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
        }, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->{
            if (e.getView().getPlayer() != null)
            if (e.getView().getPlayer().getInventory().getItemInOffHand() == null) return;
            Player player = (Player) e.getView().getPlayer();
            ItemStack offhand = player.getInventory().getItemInOffHand();
            if (offhand.hasItemMeta()){
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(offhand.getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
        }, 1L);
    }
}
