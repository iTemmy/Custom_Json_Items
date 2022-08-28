package com.temmy.json_items_test_1.util.PersistentDataTypes;

import com.temmy.json_items_test_1.util.DankItem;
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

public class DankItemPersistentDataType implements PersistentDataType<byte[], DankItem> {

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<DankItem> getComplexType() {
        return DankItem.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull DankItem complex, @NotNull PersistentDataAdapterContext context) {
        return toBase64(complex);
    }

    @Override
    public @NotNull DankItem fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        try {
            return fromBase64(primitive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DankItem(new ItemStack(Material.AIR)).setAmount(0);
    }

    private byte[] toBase64(@NotNull DankItem item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(item.getAmount());
            dataOutput.writeObject(item.getOriginalItem());
            dataOutput.close();
            return outputStream.toByteArray();
        }catch (Exception e){
            throw new IllegalStateException("Unable to save item.", e);
        }
    }

    private DankItem fromBase64(byte[] data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            DankItem dankItem = new DankItem((ItemStack) dataInput.readObject()).setAmount(dataInput.readInt());
            dataInput.close();
            return dankItem;
        }catch (ClassNotFoundException e){
            throw new IOException("Unable to load item.", e);
        }
    }
}
