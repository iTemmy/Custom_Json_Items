package com.temmy.json_items_test_1.util.PersistentDataTypes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackPersistentDataType implements PersistentDataType<byte[], ItemStack> {

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ItemStack itemStack, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return toBase64(itemStack);
    }

    @Override
    public @NotNull ItemStack fromPrimitive(byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            return fromBase64(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ItemStack(Material.STONE);
    }

    public byte[] toBase64(@NotNull ItemStack itemStack) throws IllegalStateException{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);

            dataOutput.close();
            return outputStream.toByteArray();
        }catch (Exception e){
            throw new IllegalStateException("Unable to save item.", e);
        }
    }

    public @NotNull ItemStack fromBase64(byte[] bytes) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        }catch (ClassNotFoundException e){
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
