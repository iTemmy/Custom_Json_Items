package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.util.Convert;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Logger;

public class SpeedFurnace {

    private SpeedFurnace(){
    }

    static Logger log = Bukkit.getLogger();

    public static void trigger(Event e, String[] args){
        if (e instanceof BlockPlaceEvent) blockPlace(e, args);
        if (e instanceof BlockBreakEvent) blockBreak(e, args);
    }

    private static void blockBreak(Event e, String[] args){
        BlockBreakEvent event = (BlockBreakEvent) e;
        for (String s : args) {
            for (ItemStack item : event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand())){
                if (event.isDropItems())
                    event.setDropItems(false);
                if (event.getBlock().getType() == Material.FURNACE) {
                    ItemMeta meta = item.getItemMeta();
                    Furnace furnace = (Furnace) event.getBlock().getState();
                    meta.getPersistentDataContainer().set(Attribute.namespacedKey, PersistentDataType.STRING, furnace.getPersistentDataContainer().get(Attribute.namespacedKey, PersistentDataType.STRING));
                    item.setItemMeta(meta);
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
                }
            }

        }
    }

    private static void blockPlace(Event e, String[] args){
        BlockPlaceEvent event = (BlockPlaceEvent) e;
        if (event.getBlock().getType() != Material.FURNACE) return;
        for (String s : args) {
            if (s.contains("Multiplier")){
                String[] multiplier = s.split(":");
                multiplier[1] = multiplier[1].replace("}", "");
                Furnace furnace = (Furnace) event.getBlock().getState();
                furnace.setCookSpeedMultiplier(Integer.parseInt(multiplier[1]));
                furnace.update();
                furnace.getPersistentDataContainer().set(Attribute.namespacedKey, PersistentDataType.STRING, Convert.mapToString(ItemUtils.getItemAttributeMap(event.getItemInHand().getItemMeta().getPersistentDataContainer())));
                furnace.update(true);
            }
        }
    }
}
