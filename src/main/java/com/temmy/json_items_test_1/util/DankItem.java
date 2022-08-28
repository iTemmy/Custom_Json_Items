package com.temmy.json_items_test_1.util;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.PersistentDataTypes.CustomDataTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DankItem {
    private ItemStack originalItem;
    private int amount;
    private final NamespacedKey nameKey = new NamespacedKey(Main.getPlugin(), "displayname");
    private final NamespacedKey dankItemAmount = new NamespacedKey(Main.getPlugin(), "amount");
    private int max;
    private ItemStack dankItemStack;

    public DankItem(ItemStack item){
        originalItem = item;
        amount = item.getAmount();
    }

    public DankItem setMax(Dank dank){
        max = dank.getMaxStackSize();
        return this;
    }

    public ItemStack getDankItemStack(){
        dankItemStack = new ItemStack(originalItem);
        String display;
        if (dankItemStack.getItemMeta().hasDisplayName())
            display = LegacyComponentSerializer.legacy('&').serialize(dankItemStack.getItemMeta().displayName());
        else
            display = dankItemStack.getType().name();
        Component comp = Component.text(display).append(Component.text(String.format("x%d", amount)).color(TextColor.fromHexString("#FFFF55")));
        ItemMeta dankItemMeta = dankItemStack.getItemMeta();
        dankItemMeta.displayName(comp);
        dankItemMeta.getPersistentDataContainer().set(com.temmy.json_items_test_1.attribute.Dank.getDankItemItemStackKey(), CustomDataTypes.ItemStack, originalItem);
        dankItemMeta.getPersistentDataContainer().set(dankItemAmount, PersistentDataType.INTEGER, amount);
        dankItemStack.setItemMeta(dankItemMeta);
        dankItemStack.setAmount(1);
        return dankItemStack;
    }

    /**
     * @implNote Should only be used in the {@link org.bukkit.event.inventory.InventoryClickEvent} InventoryClickEvent
     * Used to write the data of the DankItem in the Dank Inventory to the DankItemStack in the player's Inventory
     */
    public void writeDankItemData(){

    }

    public ItemStack getOriginalItem(){
        return originalItem;
    }

    public int getAmount(){
        return amount;
    }

    public DankItem setAmount(int amount){
        this.amount = amount;
        return this;
    }

    public DankItem addItems(ItemStack item){
        int m = max;
        int a = amount;
        if (amount+item.getAmount() > max) {
            m = m - a;
            item.setAmount(item.getAmount() - m);
            amount = max;
        } else {
            amount = amount+item.getAmount();
        }
        return this;
    }

    /**
     * Used for getting the DisplayName of the item without the amount being added.
     * @return Component
     */
    @SuppressWarnings("ConstantConditions")
    public Component getItemDisplayName(){
        return LegacyComponentSerializer.legacy('&').deserialize(originalItem.getItemMeta().getPersistentDataContainer().get(nameKey, PersistentDataType.STRING));
    }

    /**
     * Used for getting the DisplayName of the item with the amount being added.
     * @return Component
     */
    @SuppressWarnings("ConstantConditions")
    public Component getDankItemName(){
        Component comp;
        if (originalItem.getItemMeta().hasDisplayName())
            comp = originalItem.getItemMeta().displayName();
        else
            comp = Component.text(originalItem.getType().name());
        comp = comp.append(Component.text(" x"+amount, TextColor.fromHexString("#ffff55"), TextDecoration.BOLD));
        //comp.append(LegacyComponentSerializer.legacy('&').deserialize(" &e&lx"+amount));
        return comp;
    }
}
