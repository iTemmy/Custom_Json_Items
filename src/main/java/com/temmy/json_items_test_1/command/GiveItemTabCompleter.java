package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Parser.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GiveItemTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            List<String> tabComplete = new ArrayList<>(new Item().getAllItems());
            if (args.length == 1)
                for (Player p : Bukkit.getOnlinePlayers())
                    tabComplete.add(p.getName());
            if (args.length == 2 && Bukkit.getPlayer(args[0]) == null || args.length >= 3) {
                tabComplete.clear();
                return tabComplete;
            }
            return tabComplete;
        }else return null;
    }
}
