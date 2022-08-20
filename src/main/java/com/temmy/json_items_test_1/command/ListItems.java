package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.newCustomItem.NewCustomItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ListItems implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("jsonitems.listitems")) return false;
        int i = 1;
        for (String s : Main.getCustomItems().keySet()) {
            NewCustomItem item = Main.getCustomItems().get(s);
            sender.sendMessage(String.format("%d: %s", i, item.getName()));
            i++;
        }
        return false;
    }
}
