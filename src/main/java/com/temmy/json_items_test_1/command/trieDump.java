package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.MultiPageChests;
import com.temmy.json_items_test_1.util.PersistentDataTypes.CustomDataTypes;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class trieDump implements CommandExecutor {

    Logger log = Main.getPlugin().getLogger();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        RayTraceResult result = player.rayTraceBlocks(5);
        if (result == null) return false;
        if (!(result.getHitBlock().getState() instanceof TileState state)) return false;
        player.openInventory(state.getPersistentDataContainer().get(MultiPageChests.pageContainerKey, PersistentDataType.TAG_CONTAINER).get(new NamespacedKey(Main.getPlugin(), "page_0"), CustomDataTypes.Inventory));
        return true;
    }
}
