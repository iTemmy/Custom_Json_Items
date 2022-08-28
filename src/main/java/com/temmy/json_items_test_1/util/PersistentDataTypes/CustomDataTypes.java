package com.temmy.json_items_test_1.util.PersistentDataTypes;

import com.temmy.json_items_test_1.util.UUIDDataType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public interface CustomDataTypes {
    PersistentDataType<byte[], UUID> UUID = new UUIDDataType();
    PersistentDataType<Byte, Boolean> Boolean = new BooleanDataType();
    PersistentDataType<byte[], Inventory> Inventory = new InventoryDataType();
    PersistentDataType<byte[], ItemStack> ItemStack = new ItemStackPersistentDataType();
    PersistentDataType<byte[], com.temmy.json_items_test_1.util.DankItem> DankItem = new DankItemPersistentDataType();
}
