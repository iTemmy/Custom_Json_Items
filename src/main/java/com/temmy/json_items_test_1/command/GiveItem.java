package com.temmy.json_items_test_1.command;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.listener.InventoryClickListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class GiveItem implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender.hasPermission("jsonitems.giveitem"))) return false;
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
        }else if (Main.getCustomItems().containsKey(item)){
            player.getInventory().addItem(Main.getCustomItems().get(item));
        }else {
            player.sendMessage(ChatColor.YELLOW + String.format("Item '%s' doesn't exist", item));
        }
        InventoryClickListener.onInventoryClick(new InventoryClickEvent(getView(player), InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.NOTHING));
    }

    private InventoryView getView(Player player){
        InventoryView view = new InventoryView() {
            @Override
            public @NotNull Inventory getTopInventory() {
                return Bukkit.createInventory(null, 9);
            }

            @Override
            public @NotNull Inventory getBottomInventory() {
                return player.getInventory();
            }

            @Override
            public @NotNull HumanEntity getPlayer() {
                return player;
            }

            @Override
            public @NotNull InventoryType getType() {
                return InventoryType.PLAYER;
            }

            @Override
            public @NotNull String getTitle() {
                return "placeholder";
            }
        };
        return view;
    }
}
