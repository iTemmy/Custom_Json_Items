package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.AutoFeed;
import com.temmy.json_items_test_1.util.PersistentDataTypes.CustomDataTypes;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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
    public static void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null && blocks.contains(e.getClickedBlock().getType())) return;

        if (autoFeed(e.getPlayer(), e.getAction())){
            e.setCancelled(true);
            return;
        }
        if (e.getItem() == null || e.getItem().getItemMeta() == null) return;
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(e.getItem().getItemMeta().getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, e, attributeMap.get(attribute));
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean autoFeed(Player player, Action action){
        ItemStack itemstack = null;
        BundleMeta meta = null;
        for (ItemStack item : player.getInventory().getContents()){
            if (item == null || item.getType() != Material.BUNDLE || !item.hasItemMeta()) continue;
            ItemMeta iMeta = item.getItemMeta();
            if (iMeta.getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)){
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(iMeta.getPersistentDataContainer());
                if (attributeMap.containsKey("AUTOFEED")) {
                    itemstack = item;
                    meta = (BundleMeta) itemstack.getItemMeta();
                    break;
                }
            }
        }
        if (meta == null)
            return false;
        if (action.isRightClick() && player.isSneaking()) {
            if (meta.getPersistentDataContainer().has(AutoFeed.activeBundle, CustomDataTypes.Boolean))
                meta.getPersistentDataContainer().set(AutoFeed.activeBundle, CustomDataTypes.Boolean,
                        !meta.getPersistentDataContainer().get(AutoFeed.activeBundle, CustomDataTypes.Boolean));
            else
                meta.getPersistentDataContainer().set(AutoFeed.activeBundle, CustomDataTypes.Boolean, true);
            itemstack.setItemMeta(meta);
            if (meta.getPersistentDataContainer().get(AutoFeed.activeBundle, CustomDataTypes.Boolean))
                bundleTask(player, itemstack, 1200L);//meta.getPersistentDataContainer().get(AutoFeed.taskDelayKey, PersistentDataType.LONG));
            return true;
        }
        return false;
    }

    static final NamespacedKey autofeedTaskKey = new NamespacedKey(Main.getPlugin(), "taskid");

    public static void bundleTask(Player player, ItemStack bundle, long delay){
        BundleMeta meta = (BundleMeta) bundle.getItemMeta();
        ItemStack food = null;
        for (ItemStack item : meta.getItems())
            if (item.getType().isEdible())
                food = item;
        final ItemStack finalFood = food;
        meta.getPersistentDataContainer().set(autofeedTaskKey, PersistentDataType.INTEGER, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), ()->{
            if (!Bukkit.getOnlinePlayers().contains(player)) {
                Bukkit.getScheduler().cancelTask(meta.getPersistentDataContainer().get(autofeedTaskKey, PersistentDataType.INTEGER));
                meta.getPersistentDataContainer().remove(autofeedTaskKey);
                meta.getPersistentDataContainer().remove(AutoFeed.activeBundle);
                bundle.setItemMeta(meta);
                return;
            }
            if (player.getFoodLevel() < 20)
                if (finalFood != null) {
                    new PlayerItemConsumeEvent(player, finalFood);
                }
        }, delay, delay));
        bundle.setItemMeta(meta);
    }
}
