package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class GiveItem implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) && (args.length != 2)){
            Bukkit.getLogger().log(Level.INFO,"You must provide a player to give the item to.");
            return false;
        }
        if (args.length == 2) giveItem(Bukkit.getPlayer(args[0]), args[1]);
        else if (args.length == 1){
            giveItem((Player) sender, args[0]);
            return true;
        }
        return false;
    }

    private void giveItem(Player player, String item){
        Recipe recipe = Bukkit.getRecipe(new NamespacedKey(Main.getPlugin(), item.toLowerCase()));
        if (recipe != null){
            player.getInventory().addItem(recipe.getResult());
        }else if (Main.getCustomItems().contains(item)){
            player.getInventory().addItem(Main.getCustomItems().get(item));
        }else {
            player.sendMessage(ChatColor.YELLOW + String.format("Item '%s' doesn't exist", item));
        }
    }
}
