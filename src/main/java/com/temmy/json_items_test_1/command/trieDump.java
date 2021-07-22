package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.trie.Trie;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class trieDump implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (String string : Main.getTest().keys()){
            Bukkit.getLogger().log(Level.INFO, string);
        }
        return true;
    }
}
