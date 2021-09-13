package com.temmy.json_items_test_1.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    @EventHandler
    public static void onPlayerDropItem(PlayerDropItemEvent e){
        PlayerSwapHandItemListener.removeHeldItemEffects(e.getPlayer(), e.getItemDrop().getItemStack());
    }
}
