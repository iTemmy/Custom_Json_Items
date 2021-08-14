package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e){
        for (String s : Main.getOres().keySet()) {
            if (!(e.getPlayer().getPersistentDataContainer().has(Main.getOres().get(s), PersistentDataType.INTEGER)))
                e.getPlayer().getPersistentDataContainer().set(Main.getOres().get(s), PersistentDataType.INTEGER, 0);
            else continue;
        }
    }
}
