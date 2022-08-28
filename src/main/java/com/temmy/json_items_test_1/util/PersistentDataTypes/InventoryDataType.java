package com.temmy.json_items_test_1.util.PersistentDataTypes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventoryDataType implements PersistentDataType<byte[], Inventory> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Inventory> getComplexType() {
        return Inventory.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Inventory inventory, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return toBase64(inventory);
    }

    @Override
    public @NotNull Inventory fromPrimitive(byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            return fromBase64(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Bukkit.createInventory(null, 9, Component.text("ERROR: REPORT TO ADMIN!").color(TextColor.fromHexString("#FF0000")));
    }

    public byte[] toBase64(@NotNull Inventory inventory) throws IllegalStateException{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(inventory.getLocation());
            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            dataOutput.close();
            return outputStream.toByteArray();
        }catch (Exception e){
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public @NotNull Inventory fromBase64(byte[] bytes) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Chest chest = (Chest) dataInput.readObject();
            Inventory inventory = Bukkit.createInventory(chest, dataInput.readInt());

            for (int i = 0; i < inventory.getSize(); i++){
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        }catch (ClassNotFoundException e){
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
