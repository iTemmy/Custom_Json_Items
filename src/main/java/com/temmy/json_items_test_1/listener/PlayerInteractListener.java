package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayerInteractListener implements Listener {

    private static final List<Material> blocks = new ArrayList<>(Arrays.asList(Material.CRAFTING_TABLE, Material.FURNACE,
            Material.BLAST_FURNACE, Material.SMOKER, Material.CHEST, Material.TRAPPED_CHEST, Material.ACACIA_BUTTON,
            Material.BIRCH_BUTTON, Material.OAK_BUTTON, Material.DARK_OAK_BUTTON, Material.CRIMSON_BUTTON,
            Material.JUNGLE_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.SPRUCE_BUTTON, Material.STONE_BUTTON,
            Material.WARPED_BUTTON, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.OAK_DOOR,
            Material.CRIMSON_DOOR, Material.IRON_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.WARPED_DOOR,
            Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.CRIMSON_FENCE_GATE, Material.OAK_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.WARPED_FENCE_GATE,
            Material.ACACIA_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.IRON_TRAPDOOR, Material.CRIMSON_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR,
            Material.WARPED_TRAPDOOR, Material.SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX,
            Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.ENCHANTING_TABLE,
            Material.DROPPER, Material.DISPENSER, Material.LEVER, Material.HOPPER, Material.LECTERN, Material.ENDER_CHEST,
            Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL, Material.LOOM, Material.BARREL,
            Material.CARTOGRAPHY_TABLE, Material.FLETCHING_TABLE, Material.GRINDSTONE, Material.SMITHING_TABLE,
            Material.STONECUTTER, Material.BEACON, Material.BREWING_STAND));

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e){
        if (e.getClickedBlock() != null && blocks.contains(e.getClickedBlock().getType())) return;
        if (e.getItem() == null || e.getItem().getItemMeta() == null) return;
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getItem().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }
}
