package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.CustomDataTypes;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class AsyncChatListener implements Listener {

    Logger log = Main.getPlugin().getLogger();
    public static final NamespacedKey inEditorKey = new NamespacedKey(Main.getPlugin(), "inEditor");

    @EventHandler
    public void onAsyncChat(AsyncChatEvent e){
        if (e.getPlayer().getPersistentDataContainer().has(inEditorKey, CustomDataTypes.Boolean)) e.setCancelled(true);
        log.info(PlainTextComponentSerializer.plainText().serialize(e.message()));
    }
}
