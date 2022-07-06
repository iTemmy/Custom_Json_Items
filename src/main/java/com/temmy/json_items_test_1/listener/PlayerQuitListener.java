package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @SuppressWarnings("RedundantCollectionOperation")
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if (Main.activeEditors.containsKey(e.getPlayer())) Main.activeEditors.remove(e.getPlayer());
    }
}
