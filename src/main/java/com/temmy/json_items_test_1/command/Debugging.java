package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Debugging implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("jsonitems.debugging")) return false;
        Main.debug = !Main.debug;
        sender.sendMessage(Main.debug?ChatColor.GREEN+String.format("[Json Items] Debugging now %s", "Enabled")
                :ChatColor.DARK_RED+String.format("[Json Items] Debugging now %s", "Disabled"));
        return true;
    }
}
