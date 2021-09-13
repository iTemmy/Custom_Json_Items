package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.logging.Logger;

public class PlayerSwapHandItemListener implements Listener {
    @EventHandler
    public static void onPlayerSwapHandItem(PlayerSwapHandItemsEvent e){
        removeHeldItemEffects(e.getPlayer(), e.getMainHandItem());
        removeHeldItemEffects(e.getPlayer(), e.getOffHandItem());

        if (e.getMainHandItem() != null) {
            if (e.getMainHandItem().hasItemMeta()) {
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getMainHandItem().getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap.keySet())
                    Attribute.invoke(attribute, e, attributeMap.get(attribute));
            }
        }
        if (e.getOffHandItem() != null) {
            if (e.getOffHandItem().hasItemMeta()) {
                Map<String, String[]> attributeMap2 = ItemUtils.getItemAttributeMap(e.getOffHandItem().getItemMeta().getPersistentDataContainer());
                for (String attribute : attributeMap2.keySet())
                    Attribute.invoke(attribute, e, attributeMap2.get(attribute));
            }
        }
    }

    static Logger log = Bukkit.getLogger();

    public static void removeHeldItemEffects(Player player, ItemStack item) {
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        String[] effects = ItemUtils.getItemAttributeMap(item.getItemMeta().getPersistentDataContainer()).get("HELDITEM");
        if (effects == null) return;
        for (String s : effects) {
            String[] shit = s.split("\"");
            for (String ss : shit) {
                if (PotionEffectType.getByName(ss) != null) {
                    player.removePotionEffect(PotionEffectType.getByName(ss));
                }
            }
        }
    }
}
