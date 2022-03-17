package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
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
     * @param fortune true if the item is enchanted with fortune.
     * @return whether the item dropped should be replaced with the custom item or not.
     */
    public static boolean customOre(Block block, Player player, boolean fortune, int amount){
        Random rnd = new Random();
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (block == null) return false;
        ItemStack item;
        if (block.getWorld().getName().equals(Main.customOreWorld) && player.getGameMode() == GameMode.SURVIVAL) {
            switch (block.getType()) {
                case COAL_ORE:
                case DEEPSLATE_COAL_ORE:
                    item = Main.getCustomItems_OLD().get("coalCustomOre".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1) {
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1) {
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else {
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) + 1);
                        return false;
                    }
                case COPPER_ORE:
                case DEEPSLATE_COPPER_ORE:
                    item = Main.getCustomItems_OLD().get("copperCustomOre".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) + 1);
                        return false;
                    }
                case DIAMOND_ORE:
                case DEEPSLATE_DIAMOND_ORE:
                    item = Main.getCustomItems_OLD().get("diamondCustomOre".toLowerCase());
                    item.setAmount(amount);
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
                case EMERALD_ORE:
                case DEEPSLATE_EMERALD_ORE:
                    item = Main.getCustomItems_OLD().get("emeraldCustomOre".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1) {
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case GOLD_ORE:
                case DEEPSLATE_GOLD_ORE:
                    item = Main.getCustomItems_OLD().get("goldCustomOre".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1) {
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case IRON_ORE:
                case DEEPSLATE_IRON_ORE:
                    item = Main.getCustomItems_OLD().get("ironCustomORe".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else {
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case LAPIS_ORE:
                case DEEPSLATE_LAPIS_ORE:
                    item = Main.getCustomItems_OLD().get("lapisCustomOre".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()),PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case REDSTONE_ORE:
                case DEEPSLATE_REDSTONE_ORE:
                    item = Main.getCustomItems_OLD().get("redstoneCustomOre".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                default:
                    return false;
            }
        }else if (block.getWorld().getName().equals(Main.customOreWorld+"_nether") && player.getGameMode() == GameMode.SURVIVAL) {
            switch (block.getType()){
                case NETHER_GOLD_ORE:
                    item = Main.getCustomItems_OLD().get("goldCustomOre".toLowerCase());
                    item.setAmount(amount);
                    log.info("Nether_Gold_Ore");
                    if (rnd.nextInt(Main.getChances().get(block.getType().name().toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1) {
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case NETHER_QUARTZ_ORE:
                    item = Main.getCustomItems_OLD().get("quartzCustomOre".toLowerCase());
                    item.setAmount(amount);
                    if (rnd.nextInt(Main.getChances().get(block.getType().name())) == 1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER) >= Main.getChances().get(block.getType().name())-1){
                        block.getWorld().dropItem(block.getLocation(), item);
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER, data.get(Main.getOres().get(block.getType().name()), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                default:
                    return false;
            }
        }
        return false;
    }
}
