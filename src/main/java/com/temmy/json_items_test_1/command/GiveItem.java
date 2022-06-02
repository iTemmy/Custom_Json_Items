package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.Parser.Item;
import com.temmy.json_items_test_1.Parser.ItemRepoTest;
import com.temmy.json_items_test_1.attribute.HeldItemEffects;
import com.temmy.json_items_test_1.listener.PlayerSwapHandItemListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveItem implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender.hasPermission("jsonitems.giveitem"))) {
            sender.sendMessage(ChatColor.RED+"Error: You do not have permission to run this command.");
            sender.sendMessage(ChatColor.RED+"If you believe this to be an error then please contact an admin.");
            return false;
        }
        if (!(sender instanceof Player) && (args.length != 3)){
            sender.sendMessage("You must provide a player to give the item to.");
            return false;
        }
        try {
            if (args.length == 3) {
                giveItem(Bukkit.getPlayer(args[0]), args[1], Integer.parseInt(args[2]));
                sender.sendMessage(String.format("Gave %s %d %s", Bukkit.getPlayer(args[0]).getName(), Integer.parseInt(args[2]), args[1]));
            } else if (args.length == 2) {
                    if (giveItem((Player) sender, args[0], Integer.parseInt(args[1]))) {
                        sender.sendMessage(String.format("Gave %s %d %s", sender.getName(), Integer.parseInt(args[1]), args[0]));
                        return true;
                    }
                    return false;
            } else if (args.length == 1) {
                if (giveItem((Player) sender, args[0], 1)) {
                    sender.sendMessage(String.format("Gave %s %d %s", sender.getName(), 1, args[0]));
                    return true;
                }
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean giveItem(Player player, String item, int amount){
        ItemStack itemstack;
        if (Main.getCustomItems().containsKey(item)){
            itemstack = new Item().getByName(item).getItemStack();
            if (itemstack == null) return false;
            itemstack.setAmount(amount);
            player.getInventory().addItem(itemstack);
        }else {
            player.sendMessage(ChatColor.YELLOW + String.format("Item '%s' doesn't exist", item));
            return false;
        }
        if (itemstack == player.getInventory().getItemInMainHand()) {
            PlayerSwapHandItemListener.removeHeldItemEffects(player, player.getInventory().getItemInMainHand());
            HeldItemEffects.getItemEffects(player, player.getInventory().getItemInMainHand(), "main");
        }
        return true;
    }
}
