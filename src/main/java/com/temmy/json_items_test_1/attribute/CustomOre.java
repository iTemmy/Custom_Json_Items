package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.logging.Logger;

public class CustomOre {


    static Logger log = Bukkit.getLogger();

    public static boolean customOre(Block block, Player player){
        Random rnd = new Random();
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (block == null) return false;
        if (block.getWorld().getName().equals("resource_world") && player.getGameMode() == GameMode.SURVIVAL) {
            switch (block.getType()) {
                case COAL_ORE:
                case DEEPSLATE_COAL_ORE:
                    if (rnd.nextInt(Main.getChances().get("Phosphorus".toLowerCase())) == 1) {
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("phosphorus"));
                        data.set(Main.getOres().get("Phosphorus"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Phosphorus"), PersistentDataType.INTEGER) >= Main.getChances().get("Phosphorus".toLowerCase())-1) {
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("phosphorus"));
                        data.set(Main.getOres().get("Phosphorus"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else {
                        data.set(Main.getOres().get("Phosphorus"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Phosphorus"), PersistentDataType.INTEGER) + 1);
                        return false;
                    }
                case COPPER_ORE:
                case DEEPSLATE_COPPER_ORE:
                    if (rnd.nextInt(Main.getChances().get("Kaylax".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("kaylax"));
                        data.set(Main.getOres().get("Kaylax"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Kaylax"), PersistentDataType.INTEGER) >= Main.getChances().get("Kaylax".toLowerCase())-1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("kaylax"));
                        data.set(Main.getOres().get("Kaylax"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Kaylax"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Kaylax"), PersistentDataType.INTEGER) + 1);
                        return false;
                    }
                case DIAMOND_ORE:
                case DEEPSLATE_DIAMOND_ORE:
                    if (rnd.nextInt(Main.getChances().get("Janelite".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("janelite"));
                        data.set(Main.getOres().get("Janelite"), PersistentDataType.INTEGER,0);
                        return true;
                    }else if (data.get(Main.getOres().get("Janelite"), PersistentDataType.INTEGER) >= Main.getChances().get("Janelite".toLowerCase())-1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("janelite"));
                        data.set(Main.getOres().get("Janelite"), PersistentDataType.INTEGER,0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Janelite"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Janelite"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case EMERALD_ORE:
                case DEEPSLATE_EMERALD_ORE:
                    if (rnd.nextInt(Main.getChances().get("Ellendyte".toLowerCase())) == 1) {
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("ellendyte"));
                        data.set(Main.getOres().get("Ellendyte"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Ellendyte"), PersistentDataType.INTEGER) >= Main.getChances().get("Ellendyte".toLowerCase())-1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("ellendyte"));
                        data.set(Main.getOres().get("Ellendyte"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Ellendyte"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Ellendyte"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case GOLD_ORE:
                case DEEPSLATE_GOLD_ORE:
                    if (rnd.nextInt(Main.getChances().get("Corinthium".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("corthium"));
                        data.set(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER) >= Main.getChances().get("Corinthium".toLowerCase())-1) {
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("corinthium"));
                        data.set(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case IRON_ORE:
                case DEEPSLATE_IRON_ORE:
                    if (rnd.nextInt(Main.getChances().get("Jolixanine".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("jolixanine"));
                        data.set(Main.getOres().get("Jolixanine"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Jolixanine"), PersistentDataType.INTEGER) >= Main.getChances().get("Jolixanine".toLowerCase())-1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("jolixanine"));
                        data.set(Main.getOres().get("Jolixanine"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else {
                        data.set(Main.getOres().get("Jolixanine"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Jolixanine"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case LAPIS_ORE:
                case DEEPSLATE_LAPIS_ORE:
                    if (rnd.nextInt(Main.getChances().get("Sapphire".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("Sapphire"));
                        data.set(Main.getOres().get("Sapphire"),PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Sapphire"), PersistentDataType.INTEGER) >= Main.getChances().get("Sapphire".toLowerCase())-1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("sapphire"));
                        data.set(Main.getOres().get("Sapphire"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Sapphire"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Sapphire"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case REDSTONE_ORE:
                case DEEPSLATE_REDSTONE_ORE:
                    if (rnd.nextInt(Main.getChances().get("Tungsten".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("tungsten"));
                        data.set(Main.getOres().get("Tungsten"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Tungsten"), PersistentDataType.INTEGER) >= Main.getChances().get("Tungsten".toLowerCase())-1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("tungsten"));
                        data.set(Main.getOres().get("Tungsten"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Tungsten"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Tungsten"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                default:
                    return false;
            }
        }else if (block.getWorld().getName().equals("resource_world_nether") && player.getGameMode() == GameMode.SURVIVAL) {
            switch (block.getType()){
                case NETHER_GOLD_ORE:
                    log.info("Nether_Gold_Ore");
                    if (rnd.nextInt(Main.getChances().get("Corinthium".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("corthium"));
                        data.set(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER) >= Main.getChances().get("Corinthium".toLowerCase())-1) {
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("corinthium"));
                        data.set(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Corinthium"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                case NETHER_QUARTZ_ORE:
                    if (rnd.nextInt(Main.getChances().get("Zinc".toLowerCase())) == 1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("zinc"));
                        data.set(Main.getOres().get("Zinc"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else if (data.get(Main.getOres().get("Zinc"), PersistentDataType.INTEGER) >= Main.getChances().get("Zinc".toLowerCase())-1){
                        block.getWorld().dropItem(block.getLocation(), Main.getCustomItems().get("zinc"));
                        data.set(Main.getOres().get("Zinc"), PersistentDataType.INTEGER, 0);
                        return true;
                    }else{
                        data.set(Main.getOres().get("Zinc"), PersistentDataType.INTEGER, data.get(Main.getOres().get("Zinc"), PersistentDataType.INTEGER)+1);
                        return false;
                    }
                default:
                    return false;
            }
        }
        return false;
    }

    //Iron, Gold, nether gold, Redstone, Lapis, Coal, Diamond, Quartz, Emerald, copper
    
}
