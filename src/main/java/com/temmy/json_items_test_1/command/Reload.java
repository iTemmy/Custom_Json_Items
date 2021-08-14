package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("Reloading ore drop chances...");
        Main.getPlugin().reloadConfig();
        Main.loadConfig();
        sender.sendMessage("Reload complete!");
        return false;
    }
}
