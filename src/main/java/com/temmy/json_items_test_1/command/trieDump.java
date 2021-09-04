package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class trieDump implements CommandExecutor {

    Logger log = Bukkit.getLogger();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.getPersistentDataContainer().set(Main.getOres().get("Phosphorus"), PersistentDataType.INTEGER, Main.getChances().get("phosphorus")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Janelite"), PersistentDataType.INTEGER, Main.getChances().get("janelite")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Ellendyte"), PersistentDataType.INTEGER, Main.getChances().get("ellendyte")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Sapphire"), PersistentDataType.INTEGER, Main.getChances().get("sapphire")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Tungsten"), PersistentDataType.INTEGER, Main.getChances().get("tungsten")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Jolixanine"), PersistentDataType.INTEGER, Main.getChances().get("jolixanine")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER, Main.getChances().get("corinthium")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Zinc"), PersistentDataType.INTEGER, Main.getChances().get("zinc")-1);
            player.getPersistentDataContainer().set(Main.getOres().get("Kaylax"), PersistentDataType.INTEGER, Main.getChances().get("kaylax")-1);
            log.info(String.valueOf(Main.getChances().get("phosphorus")));
            log.info(String.valueOf(Main.getChances().get("janelite")));
            log.info(String.valueOf(Main.getChances().get("ellendyte")));
            log.info(String.valueOf(Main.getChances().get("sapphire")));
            log.info(String.valueOf(Main.getChances().get("tungsten")));
            log.info(String.valueOf(Main.getChances().get("jolixanine")));
            log.info(String.valueOf(Main.getChances().get("corinthium")));
            log.info(String.valueOf(Main.getChances().get("zinc")));
            log.info(String.valueOf(Main.getChances().get("kaylax")));
        }
        return true;
    }
}
