package com.temmy.json_items_test_1.listener;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getBlock().getType() != Material.FURNACE) return;
        Furnace furnace = (Furnace) e.getBlock();
        furnace.setCookSpeedMultiplier(200);
    }
}
