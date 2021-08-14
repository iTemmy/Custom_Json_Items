package com.temmy.json_items_test_1.listener;

import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.attribute.CustomOre;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Furnace;
import org.bukkit.block.Smoker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        switch (e.getBlock().getType()) {
            case FURNACE:
                Furnace furnace = null;
                furnace = (Furnace) e.getBlock().getState();
                furnace.getPersistentDataContainer();
                Map<String, String[]> furnaceAttributeMap = ItemUtils.getItemAttributeMap(furnace.getPersistentDataContainer());
                for (String attribute : furnaceAttributeMap.keySet())
                    Attribute.invoke(attribute, e, furnaceAttributeMap.get(attribute));
                break;
            case SMOKER:
                Smoker smokerFurnace = null;
                smokerFurnace = (Smoker) e.getBlock().getState();
                smokerFurnace.getPersistentDataContainer();
                Map<String, String[]> smokerAttributeMap = ItemUtils.getItemAttributeMap(smokerFurnace.getPersistentDataContainer());
                for (String attribute : smokerAttributeMap.keySet())
                    Attribute.invoke(attribute, e, smokerAttributeMap.get(attribute));
                break;
            case BLAST_FURNACE:
                BlastFurnace blastFurnace = null;
                blastFurnace = (BlastFurnace) e.getBlock().getState();
                blastFurnace.getPersistentDataContainer();
                Map<String, String[]> blastFurnaceAttributeMap = ItemUtils.getItemAttributeMap(blastFurnace.getPersistentDataContainer());
                for (String attribute : blastFurnaceAttributeMap.keySet())
                    Attribute.invoke(attribute, e, blastFurnaceAttributeMap.get(attribute));
                break;
            default: break;
        }
        if (ItemUtils.isOre(e.getBlock().getType()))
            if (CustomOre.customOre(e.getBlock(), e.getPlayer()))
                e.setDropItems(false);
            else return;
    }
}
