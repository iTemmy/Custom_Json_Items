package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.PersistentDataTypes.CustomDataTypes;
import com.temmy.json_items_test_1.util.DankItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class Dank {

    private Dank() {}

    static Logger log = Main.getPlugin().getLogger();
    /**
     * Used for checking if an ItemStack is a Dank
     */
    static final NamespacedKey dankKey = new NamespacedKey(Main.getPlugin(), "Dank");
    /**
     * Used for keeping track of the dank level so that it can be upgraded later
     */
    static final NamespacedKey dankLevelKey = new NamespacedKey(Main.getPlugin(), "level");
    /**
     * Used for storing the max stack size of DankItem in the Dank
     */
    static final NamespacedKey dankPerStackKey = new NamespacedKey(Main.getPlugin(), "perStack");
    /**
     * Used for storing an original copy of the ItemStack stored in the dank for comparing
     * when attempting to add new items to the dank
     */
    static final NamespacedKey dankItemItemStackKey = new NamespacedKey(Main.getPlugin(), "ItemStack");
    /**
     * Used for storing DankItem in the Dank's PersistentDataContainer
     */
    static final NamespacedKey dankItemKey = new NamespacedKey(Main.getPlugin(), "dankItem");

    public static NamespacedKey getDankItemItemStackKey(){
        return dankItemItemStackKey;
    }

    public static void trigger(Event e, String[] args){
        if (e instanceof PlayerInteractEvent) playerInteractEvent((PlayerInteractEvent) e, args);
        if (e instanceof InventoryClickEvent) inventoryClickEvent((InventoryClickEvent) e);
    }

    private static void inventoryClickEvent(InventoryClickEvent e){
        if (e.getClickedInventory() == e.getView().getTopInventory()) dankInventoryClick(e);
        if (e.getClickedInventory() == e.getView().getBottomInventory()) playerInventoryClick(e);
    }

    private static void dankInventoryClick(InventoryClickEvent e){
        if (e.getClick().isShiftClick()) dankInventoryShiftClick(e);
    }

    private static void dankInventoryShiftClick(InventoryClickEvent e){

    }

    private static void playerInventoryClick(InventoryClickEvent e){
        if (e.getClick().isShiftClick()) playerInventoryShiftClick(e);
    }

    private static void playerInventoryShiftClick(InventoryClickEvent e){
        Inventory bottomInv = e.getView().getBottomInventory();
        Inventory topInv = e.getView().getTopInventory();
        ItemStack item = bottomInv.getItem(e.getSlot());
        if (item == null) return;
        ItemStack dank;
        for (int i = 0; i <=8; i++) {
            if ()
        }
        for (int i = 0; i < e.getView().getTopInventory().getSize(); i++) {
            ItemStack dankItemStack = topInv.getItem(i);
            if (!dankItemStack.hasItemMeta()) continue;
            if (dankItemStack.getItemMeta().getPersistentDataContainer().has(com.temmy.json_items_test_1.util.Dank.getDankPlaceholderItemKey()) ) {
                if (!dankItemStack.getItemMeta().getPersistentDataContainer().has(dankItemItemStackKey, CustomDataTypes.ItemStack))
                    continue;
                ItemStack originalDankItemStack = dankItemStack.getItemMeta().getPersistentDataContainer().get(dankItemItemStackKey, CustomDataTypes.ItemStack);
                if (originalDankItemStack == null) continue;
                if (item.isSimilar(originalDankItemStack)) {
                    if (!dankItemStack.getItemMeta().getPersistentDataContainer().has(dankItemKey, CustomDataTypes.DankItem))
                        continue;
                    DankItem dankItem = dankItemStack.getPersistentDataContainer().get(dankItemKey, CustomDataTypes.DankItem);
                    dankItem.addItems(item);
                }
            }else {
                DankItem dankItem = new DankItem(item);
                topInv.setItem(i, dankItem.getDankItemStack());

                dankItem.writeDankItemData();
            }
        }
    }

    private void checkDankItems(@NotNull com.temmy.json_items_test_1.util.Dank dank, @NotNull ItemStack newItem){
        for (DankItem item : dank.getItems()){
            if (item.getOriginalItem().isSimilar(newItem)) {
                item.addItems(newItem);
                break;
            }
        }
    }

    private static void playerInteractEvent(PlayerInteractEvent e, String[] args){
        ItemStack handItem = e.getPlayer().getInventory().getItemInMainHand();
        if (handItem == null) return;
        if (!handItem.getItemMeta().getPersistentDataContainer().has(dankKey, CustomDataTypes.Inventory))
            handItem = newDank(args, handItem);
        e.getPlayer().openInventory(handItem.getPersistentDataContainer().get(dankKey, CustomDataTypes.Inventory));
    }

    private static ItemStack newDank(String[] args, ItemStack item){
        int level = 0;
        int invsize = 0;
        int perstack = 0;
        for (String s : args){
            s = s.replaceAll("[{}\"]","");
            log.info(String.format("S: --> %s", s));
            String[] ss = s.split(",");
            for (String sss : ss){
                if (sss.toLowerCase().contains("level")){
                    sss = sss.replaceAll("level:", "");
                    level = Integer.parseInt(sss);
                }else if (sss.toLowerCase().contains("per-stack")){
                    sss = sss.replaceAll("per-stack:", "");
                    perstack = Integer.parseInt(sss);
                }else if (sss.toLowerCase().contains("invsize")){
                    sss = sss.replaceAll("invsize:" ,"");
                    invsize = Integer.parseInt(sss);
                }
            }
        }
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(dankPerStackKey, PersistentDataType.INTEGER, perstack);
        meta.getPersistentDataContainer().set(dankKey, CustomDataTypes.Inventory, createInventory(invsize, level));
        item.setItemMeta(meta);
        return item;
    }

    private static @NotNull Inventory createInventory(int size, int level){
        assert size >0;
        assert level >0;
        Inventory inv = null;
        Component comp = Component.text(String.format("Dank Tier %s", level));
        if (size == 5)
            inv = Bukkit.createInventory(null, InventoryType.HOPPER, comp);
        else
            inv = Bukkit.createInventory(null, size, comp);

        return inv;
    }
}
