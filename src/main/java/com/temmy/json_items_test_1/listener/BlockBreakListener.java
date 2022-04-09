package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.CustomOre;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        tileState(e);

        Collection<ItemStack> items = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());
        int amount = 1;
        for (ItemStack item : items){
            amount = item.getAmount();
        }

        if (ItemUtils.isOre(e.getBlock().getType())) {
            if (e.getPlayer().getInventory().getItemInMainHand().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                if (CustomOre.customOre(e.getBlock(), e.getPlayer(), true, amount))
                    e.setDropItems(false);
            } else if (CustomOre.customOre(e.getBlock(), e.getPlayer(), false, amount))
                    e.setDropItems(false);
        }
    }

    private void tileState(BlockBreakEvent event){
        if (!(event.getBlock().getState() instanceof TileState state)) return;
        Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(state.getPersistentDataContainer());
        for (String attribute : attributeMap.keySet())
            Attribute.invoke(attribute, event, attributeMap.get(attribute));
    }
}
