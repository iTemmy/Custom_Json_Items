package com.temmy.json_items_test_1.util.PersistentDataTypes;

import com.temmy.json_items_test_1.util.Dank;
import com.temmy.json_items_test_1.util.DankItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

public class DankPersistentDataType implements PersistentDataType<byte[], Dank> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Dank> getComplexType() {
        return Dank.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Dank complex, @NotNull PersistentDataAdapterContext context) {
        return toBase64(complex);
    }

    @Override
    public @NotNull Dank fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        try {
            return fromBase64(primitive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Dank(Component.text("ERROR"), 1);
    }

    private byte[] toBase64(@NotNull Dank dank) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeChars(LegacyComponentSerializer.legacy('&').serialize(dank.getInvTitle()));
            dataOutput.writeInt(dank.getMaxStackSize());
            for (DankItem item : dank.getItems()) {
                dataOutput.writeObject(item.getDankItemStack());
            }
            dataOutput.close();
            return outputStream.toByteArray();
        } catch (Exception e){
            throw new IllegalStateException("Unable to save item.", e);
        }
    }

    private Dank fromBase64(byte[] data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Dank dank = new Dank(LegacyComponentSerializer.legacy('&').deserialize((String) dataInput.readObject()), dataInput.readInt());
            dank.addItem(new DankItem(new ItemStack(Material.STONE)).setAmount(600));
            return dank;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
