package com.temmy.json_items_test_1.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public interface CustomDataTypes {
    PersistentDataType<byte[], UUID> UUID = new UUIDDataType();
    PersistentDataType<Byte, Boolean> Boolean = new BooleanDataType();
    PersistentDataType<byte[], Inventory> Inventory = new InventoryDataType();
}
