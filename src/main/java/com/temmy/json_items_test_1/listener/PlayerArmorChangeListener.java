package com.temmy.json_items_test_1.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class PlayerArmorChangeListener implements Listener {
    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent e){
        removeArmorEffects(e.getPlayer(), e.getOldItem());
        removeArmorAttributes(e.getPlayer(), e.getOldItem());
        ArmorEffects.checkPlayerArmor(e.getPlayer().getInventory().getArmorContents(),e.getPlayer());
        if (e.getNewItem() == null || e.getNewItem().getItemMeta() == null) return;
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getNewItem().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }

    private void removeArmorEffects(Player player, ItemStack armorItem){
        String[] effects = ItemUtils.getItemAttributeMap(armorItem).get("ARMOREFFECTS");
        if (effects == null) return;
        for (String s : effects) {
            String[] shit = s.split("\"");
            for (String ss : shit)
                if (PotionEffectType.getByName(ss) != null)
                    player.removePotionEffect(PotionEffectType.getByName(ss));
        }
        String[] fullSetEffects = ItemUtils.getItemAttributeMap(armorItem.getItemMeta().getPersistentDataContainer()).get("FULLSETARMOREFFECTS");
        if (fullSetEffects == null) return;
        for (String s : fullSetEffects) {
            String[] shit = s.split("\"");
            for (String ss : shit)
                if (PotionEffectType.getByName(ss) != null)
                    player.removePotionEffect(PotionEffectType.getByName(ss));
        }
    }

    private void removeArmorAttributes(Player player, ItemStack armorItem){
        if (armorItem.getItemMeta() == null) return;
        String[] attributes = ItemUtils.getItemAttributeMap(armorItem.getItemMeta().getPersistentDataContainer()).get("ARMORATTRIBUTEEFFECTS");
        if (attributes == null) return;
        for (String s : attributes){
            String[] attribute = s.split(":");
            for (int i = 0; i < attribute.length; i++){
                if (attribute[i].contains("health")){
                    attribute[i+1] = attribute[i+1].replace("}", "");
                    player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(
                            player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getBaseValue() - Integer.parseInt(attribute[i+1]));
                }else if (attribute[i].contains("speed")){
                    attribute[i+1] = attribute[i+1].replace("}", "");
                    player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(
                            player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue()- Double.valueOf(attribute[i+1]));
                }
            }

        }
    }
}
