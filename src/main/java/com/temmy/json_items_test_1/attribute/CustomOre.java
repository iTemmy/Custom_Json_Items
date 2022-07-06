package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.Parser.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.logging.Logger;

public class CustomOre {


    static Logger log = Bukkit.getLogger();

    /**
     * Used for checking if a custom ore should be dropped and if so which one should be dropped.
     * @param block the block that was mined.
     * @param player the player that mined the block.
     * @return whether the item dropped should be replaced with the custom item or not.
     */
    public static boolean customOre(Block block, Player player, int amount){
        Random rnd = new Random();
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (block == null) return false;
        ItemStack item;
        if (block.getWorld().getName().equals(Main.customOreWorld) && player.getGameMode() == GameMode.SURVIVAL) {
            switch (block.getType()) {
                case COAL_ORE:
                case DEEPSLATE_COAL_ORE:
                    item = new Item().read("coalCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case COPPER_ORE:
                case DEEPSLATE_COPPER_ORE:
                    item = new Item().read("copperCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case DIAMOND_ORE:
                case DEEPSLATE_DIAMOND_ORE:
                    item = new Item().read("diamondCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case EMERALD_ORE:
                case DEEPSLATE_EMERALD_ORE:
                    item = new Item().read("emeraldCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case GOLD_ORE:
                case DEEPSLATE_GOLD_ORE:
                    item = new Item().read("goldCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case IRON_ORE:
                case DEEPSLATE_IRON_ORE:
                    item = new Item().read("ironCustomORe".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case LAPIS_ORE:
                case DEEPSLATE_LAPIS_ORE:
                    item = new Item().read("lapisCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case REDSTONE_ORE:
                case DEEPSLATE_REDSTONE_ORE:
                    item = new Item().read("redstoneCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                default:
                    return false;
            }
        }else if (block.getWorld().getName().equals(Main.customOreWorld+"_nether") && player.getGameMode() == GameMode.SURVIVAL) {
            switch (block.getType()){
                case NETHER_GOLD_ORE:
                    item = new Item().read("goldCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                case NETHER_QUARTZ_ORE:
                    item = new Item().read("quartzCustomOre".toLowerCase()).getItemStack();
                    item.setAmount(amount);
                    return dropChance(block, data, item);
                default:
                    return false;
            }
        }
        return false;
    }

    private static boolean dropChance(Block block, PersistentDataContainer data, ItemStack item){
        Random rnd = new Random();
        if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1){
            block.getWorld().dropItem(block.getLocation(), item);
            data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER,0);
            return true;
        }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1){
            block.getWorld().dropItem(block.getLocation(), item);
            data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER,0);
            return true;
        }else{
            data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
            return false;
        }
    }
}
