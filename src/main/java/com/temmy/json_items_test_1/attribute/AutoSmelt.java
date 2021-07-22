package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

public final class AutoSmelt {
    private AutoSmelt(){}

    public static void trigger(Event e, String[] args) {
        if (!(e instanceof BlockDropItemEvent)) return;
        BlockDropItemEvent event = (BlockDropItemEvent) e;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        event.setCancelled(true);
        BlockState blockstate = event.getBlockState();
        World world  = blockstate.getWorld();

        Material block = blockstate.getType();
        ItemStack blockDrop = null;
        boolean dropped = false;
        for (Item item : event.getItems()){
            ItemStack droppedItem = item.getItemStack();
            if (droppedItem.getType() == block){
                blockDrop = droppedItem;
                dropped = true;
                break;
            }
            world.dropItem(item.getLocation(), droppedItem);
        }
        if (!dropped) return;

        boolean whitelisted = false;
        boolean blacklisted = false;
        String blockName = block.name().toUpperCase();
        for (String s : args) {
            boolean blackList = s.charAt(0) == '!';
            boolean match = blockName.matches(String.format("(.*)%s(.*)$"));
            if (!blackList && match)
                whitelisted = true;
            else if (blackList && match)
                blacklisted = true;
        }
        if (!whitelisted || blacklisted) {
            world.dropItem(blockstate.getLocation(), blockDrop);
            return;
        }

        ItemStack item = ItemUtils.getSmeltingResult(block);
        if (item == null) {
            world.dropItem(blockstate.getLocation(), blockDrop);
            return;
        }
        player.getInventory().addItem(item);
    }
}

