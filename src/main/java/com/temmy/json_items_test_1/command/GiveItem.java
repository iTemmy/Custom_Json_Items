package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.HeldItemEffects;
import com.temmy.json_items_test_1.listener.PlayerSwapHandItemListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class GiveItem implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender.hasPermission("jsonitems.giveitem"))) {
            sender.sendMessage(ChatColor.RED+"Error: You do not have permission to run this command.");
            sender.sendMessage(ChatColor.RED+"If you believe this to be an error then please contact an admin.");
            return false;
        }
        if (!(sender instanceof Player) && (args.length != 3)){
            Bukkit.getLogger().log(Level.INFO,"You must provide a player to give the item to.");
            return false;
        }
        if (args.length == 3) giveItem(Bukkit.getPlayer(args[0]), args[1], Integer.parseInt(args[2]));
        else if (args.length == 2){
            try {
                if (giveItem((Player) sender, args[0], Integer.parseInt(args[1]))) {
                    sender.sendMessage("Given " + sender.getName() + args[0]);
                    return true;
                } return false;
            }catch (NumberFormatException e){
                sender.sendMessage("Invalid number.");
            }
        }
        else if (args.length == 1){
            if (giveItem((Player) sender, args[0], 1)) {
                sender.sendMessage("Given " + sender.getName() + " " + args[0]);
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean giveItem(Player player, String item, int amount){
        Recipe recipe = Bukkit.getRecipe(new NamespacedKey(Main.getPlugin(), item.toLowerCase()));
        if (recipe != null){
            ItemStack itemstack = recipe.getResult();
            itemstack.setAmount(amount);
            player.getInventory().addItem(itemstack);
        }else if (Main.getCustomItems().containsKey(item)){
            ItemStack itemstack = Main.getCustomItems().get(item);
            itemstack.setAmount(amount);
            player.getInventory().addItem(itemstack);
        }else {
            player.sendMessage(ChatColor.YELLOW + String.format("Item '%s' doesn't exist", item));
            return false;
        }
        PlayerSwapHandItemListener.removeHeldItemEffects(player, player.getInventory().getItemInMainHand());
        HeldItemEffects.getItemEffects(player, player.getInventory().getItemInMainHand(), "main");
        return true;
    }
}
