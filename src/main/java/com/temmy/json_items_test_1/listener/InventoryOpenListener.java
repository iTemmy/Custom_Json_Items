package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.MultiPageChests;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Logger;

public class InventoryOpenListener implements Listener {

    static Logger log = Main.getPlugin().getLogger();

    @EventHandler
    public static void onInventoryOpenEvent(InventoryOpenEvent e){
        if (checkForBottomRow(e.getInventory())) return;
        if (e.getInventory().getLocation() == null) return;
        Block b = e.getInventory().getLocation().getBlock();
        if (!b.isEmpty() && b.getType() == Material.CHEST){
            Chest chest = (Chest) b.getState();
            Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(chest.getPersistentDataContainer());
            for (String attribute : attributeMap.keySet())
                Attribute.invoke(attribute, e, attributeMap.get(attribute));
        }
    }

    private static boolean checkForBottomRow(@NotNull Inventory inv){
        ItemStack item = inv.getItem(inv.getSize()-5);
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(MultiPageChests.locationKey);
    }
}
