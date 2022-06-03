package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.MultiPageChests;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.logging.Logger;

public class InventoryCloseListener implements Listener {

    Logger log = Main.getPlugin().getLogger();

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (e.getInventory().getType() != InventoryType.CHEST) return;
        ItemStack pageItem = e.getInventory().getItem(e.getInventory().getSize()-5);
        if (pageItem == null) return;
        if (!pageItem.hasItemMeta()) return;
        if (pageItem.getItemMeta().getPersistentDataContainer().has(MultiPageChests.locationKey)){
            PersistentDataContainer locationContainer = pageItem.getPersistentDataContainer().get(MultiPageChests.locationKey, PersistentDataType.TAG_CONTAINER);
            Location loc = new Location(
                    Bukkit.getWorld(locationContainer.get(MultiPageChests.worldKey, PersistentDataType.STRING)),
                    locationContainer.get(MultiPageChests.xKey, PersistentDataType.DOUBLE),
                    locationContainer.get(MultiPageChests.yKey, PersistentDataType.DOUBLE),
                    locationContainer.get(MultiPageChests.zKey, PersistentDataType.DOUBLE));
            if (!(loc.getBlock().getState() instanceof TileState state)) return;
            Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(state.getPersistentDataContainer());
            for (String attribute: attributeMap.keySet())
                Attribute.invoke(attribute, e, attributeMap.get(attribute));
        }
    }
}
