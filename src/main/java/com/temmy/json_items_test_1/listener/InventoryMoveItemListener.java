package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.MultiPageChests;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class InventoryMoveItemListener implements Listener {

    @EventHandler
    public static void onInventoryMoveItemListener(InventoryMoveItemEvent e){
        if (e.getDestination().getLocation() == null) return;
        if (!(e.getDestination().getLocation().getBlock().getState() instanceof TileState state)) return;
        if (!state.getPersistentDataContainer().has(MultiPageChests.pageContainerKey)) return;
        e.setCancelled(true);
    }
}
