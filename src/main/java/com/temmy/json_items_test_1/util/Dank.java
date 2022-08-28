package com.temmy.json_items_test_1.util;

import com.temmy.json_items_test_1.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Dank {
    Inventory inv;
    List<DankItem> items = new ArrayList<>();
    ItemStack item = new ItemStack(Material.STICK);
    int maxStackSize;
    Component invTitle;
    final NamespacedKey DankContainerKey = new NamespacedKey(Main.getPlugin(), "DankContainer");
    final NamespacedKey DankStackSizeKey = new NamespacedKey(Main.getPlugin(), "DankStackSize");
    static final NamespacedKey dankPlaceholderItemKey = new NamespacedKey(Main.getPlugin(), "placeholder");

    public Dank(Component invTitle, int maxStackSize){
        this.invTitle = invTitle;
        inv = createDankInv(invTitle);
        this.maxStackSize = maxStackSize;
    }

    public static NamespacedKey getDankPlaceholderItemKey(){
        return dankPlaceholderItemKey;
    }

    private Inventory createDankInv(Component invTitle){
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, invTitle);
        ItemStack blank = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = blank.getItemMeta();
        meta.getPersistentDataContainer().set(dankPlaceholderItemKey, PersistentDataType.BYTE, (byte) 1);
        meta.displayName(Component.text(" "));
        blank.setItemMeta(meta);
        for (int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, blank);
        }
        return inv;
    }

    public Inventory getInv(){
        return inv;
    }

    public List<DankItem> getItems(){
        return items;
    }

    public ItemStack getItemStack(){
        return item;
    }

    public void addItem(DankItem item){
        items.add(item);
    }

    public void setItem(List<DankItem> items){
        this.items = items;
    }

    public void removeItem(DankItem item){
        items.remove(item);
    }

    public void removeItem(int index){
        items.remove(index);
    }

    public int getMaxStackSize(){
        return maxStackSize;
    }

    public Component getInvTitle(){
        return invTitle;
    }
}
