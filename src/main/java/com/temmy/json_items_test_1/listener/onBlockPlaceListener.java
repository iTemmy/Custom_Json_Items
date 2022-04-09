package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Logger;

public class onBlockPlaceListener implements Listener {

    static Logger log = Main.getPlugin().getLogger();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (doubleChest(e)) {
            e.setCancelled(true);
            return;
        }
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getItemInHand().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }

    boolean b = false;
    @SuppressWarnings("ConstantConditions")
    private boolean doubleChest(@NotNull BlockPlaceEvent event){
        b = false;
        if (event.getBlockPlaced().getType() != Material.CHEST) return b;
        Chest chest = (Chest) event.getBlockPlaced().getState();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->{
            if (chest.getInventory().getSize() == 54){
                DoubleChestInventory doubleChest = (DoubleChestInventory) chest.getInventory();
                if (doubleChest.getLeftSide().getLocation().getBlock().getType() == Material.CHEST) {
                    Chest leftSide = (Chest) doubleChest.getLeftSide().getLocation().getBlock().getState();
                    if (leftSide.getPersistentDataContainer().has(Attribute.namespacedKey)) {
                        event.getBlockPlaced().setType(Material.AIR);
                        event.getItemInHand().setAmount(event.getItemInHand().getAmount()+1);
                        b = true;
                    }
                }
                if (doubleChest.getRightSide().getLocation().getBlock().getType() == Material.CHEST){
                    Chest rightSide = (Chest) doubleChest.getRightSide().getLocation().getBlock().getState();
                    if (rightSide.getPersistentDataContainer().has(Attribute.namespacedKey)) {
                        event.getBlockPlaced().setType(Material.AIR);
                        event.getItemInHand().setAmount(event.getItemInHand().getAmount()+1);
                        b = true;
                    }
                }
            }
        }, 2L);
        return b;
    }
}
