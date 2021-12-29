package com.temmy.json_items_test_1.Parser;

import com.google.gson.JsonSyntaxException;
import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.Convert;
import com.temmy.json_items_test_1.util.Glow;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public final class ItemParser {

    static Logger log = Bukkit.getLogger();

    public static @Nullable ItemStack parseItem(@NotNull String item){
        ItemStack itemStack = null;
        String filename = item.toLowerCase();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Main.getPlugin().getDataFolder()+String.format("/item/%s.json", item));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream == null) {log.warning("Invalid input stream for file "+item);return null;}

        try {
            Object jsonObject = new JSONParser().parse(new InputStreamReader(inputStream));
            JSONObject itemJson = (JSONObject) jsonObject;
            Map<String, ?> itemSection = (Map<String, ?>) itemJson.get("ITEM");
            itemStack = new ItemStack(Material.valueOf((String)itemSection.get("material")));
            Map<String, JSONObject> attributesJson = (Map<String, JSONObject>) itemSection.get("attributes");
            Map<String, String[]> attributes = new HashMap<>();

            if (attributesJson != null){
                for (String key : attributesJson.keySet()){
                    List<String> attributeList = new ArrayList<String>(Collections.singleton(String.valueOf(attributesJson.get(key))));
                    attributes.put(key, attributeList.toArray(new String[attributeList.size()]));
                }
            }

            ItemMeta meta = itemStack.getItemMeta();
            itemStack.addUnsafeEnchantments(getEnchants(itemSection));
            meta.lore(getLore(itemSection));
            meta.setUnbreakable(getUnbreakable(itemSection));
            meta.displayName(getName(itemSection));
            meta.setCustomModelData(getCustomModelData(itemSection));
            Map<org.bukkit.attribute.Attribute, AttributeModifier> attributeMap = getAttributes(itemSection);
            for (org.bukkit.attribute.Attribute attribute : attributeMap.keySet()){
                meta.addAttributeModifier(attribute, attributeMap.get(attribute));
            }
            meta.getPersistentDataContainer().set(Attribute.namespacedKey,
                    PersistentDataType.STRING,
                    Convert.mapToString(attributes));


            if (meta.hasEnchants() && itemStack.getType() == Material.ENCHANTED_BOOK){
                EnchantmentStorageMeta eMeta = (EnchantmentStorageMeta) meta;
                for (Enchantment enchant : meta.getEnchants().keySet()) {
                    eMeta.addStoredEnchant(enchant, meta.getEnchants().get(enchant), true);
                    meta.removeEnchant(enchant);
                }
                meta = eMeta;
            }

            itemStack.setItemMeta(meta);
        } catch (JsonSyntaxException e){
            log.warning("Syntax Error in "+filename);
            if (Main.debug)
                e.printStackTrace();
        }catch (IOException | ParseException e1){
            if (Main.debug)
                e1.printStackTrace();
        }
        return itemStack;
    }

    private static boolean getUnbreakable(@NotNull Map<String, ?> itemSection){
        if (itemSection.get("unbreakable") == null) return false;
        return ((boolean) itemSection.get("unbreakable"));
    }

    private static @NotNull Map<Enchantment, Integer> getEnchants(@NotNull Map<String, ?> itemSection){
        Map<Enchantment, Integer> finalEnchants = new HashMap<>();
        JSONArray JSONEnchants = (JSONArray) itemSection.get("enchants");
        if (JSONEnchants == null) return new HashMap<>();
        List<String> enchantsList = new ArrayList<>(JSONEnchants);
        for (String s : enchantsList){
            if (s.equals(null)||s.equals(""))continue;
            String[] enchantsWithLevel = s.split(",");
            for (String enchant : enchantsWithLevel){
                String[] enchants = enchant.split(";");
                if (enchants[0].equalsIgnoreCase("glow")){
                    NamespacedKey key = new NamespacedKey(Main.getPlugin(), Main.getPlugin().getDescription().getName());
                    Glow glow = new Glow(key);
                    finalEnchants.put(glow, 1);
                }else {
                    finalEnchants.put(Enchantment.getByKey(NamespacedKey.minecraft(enchants[0].toLowerCase())), Integer.parseInt(enchants[1]));
                }
            }
        }
        return finalEnchants;
    }

    private static @Nullable List<Component> getLore(@NotNull Map<String, ?> itemSection){
        JSONArray lore = (JSONArray) itemSection.get("lore");
        List<Component> loreList = new ArrayList<>(lore);
        List<String> stringLore = new ArrayList<>(lore);
        if (stringLore != null && stringLore.size() > 0){
            for (int i = 0; i <= stringLore.size()-1; i++)
                loreList.set(i, ItemParser.regextesting(stringLore.get(i)));
        }
        if (stringLore.size() <= 0){
            return null;
        }else
            return loreList;
    }

    private static @NotNull Map<org.bukkit.attribute.Attribute, AttributeModifier> getAttributes(@NotNull Map<String, ?> itemSection){
        Map<org.bukkit.attribute.Attribute, AttributeModifier> attributes = new HashMap<>();
        int health = 0;
        String healthSlot = null;
        if (itemSection.get("health") != null && itemSection.get(itemSection.get("healthSlot")) != null){
            health = Integer.parseInt(String.valueOf((long) itemSection.get("health")));
            healthSlot = (String) itemSection.get("healthSlot");
            attributes.put(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH,
                    new AttributeModifier(UUID.randomUUID(), "generic.max.health", health,
                            AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(healthSlot)));
        }

        int damage = 0;
        String damageSlot = null;
        if (itemSection.get("damage") != null && itemSection.get("damageSlot") != null){
            damage = Integer.parseInt(String.valueOf((long) itemSection.get("damage")));
            damageSlot = (String) itemSection.get("damageSlot");
            attributes.put(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE,
                    new AttributeModifier(UUID.randomUUID(), "generic.attack.damage", damage,
                            AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(damageSlot)));
        }

        int attackSpeed = 0;
        String attackSpeedSlot = null;
        if (itemSection.get("attackSpeed") != null && itemSection.get("attackSpeedSlot") !=null){
            attackSpeed = Integer.parseInt(String.valueOf((long) itemSection.get("attackSpeed")));
            attackSpeedSlot = (String) itemSection.get("attackSpeedSlot");
            attributes.put(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED,
                    new AttributeModifier(UUID.randomUUID(), "generic.attack.speed", attackSpeed,
                            AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(attackSpeedSlot)));
        }

        return attributes;
    }

    private static @Nullable Component getName(@NotNull Map<String, ?> itemSection){
        if (itemSection.get("name") == null) return null;
        String name = (String) itemSection.get("name");
        return ItemParser.regextesting(name);
    }

    private static int getCustomModelData(@NotNull Map<String, ?> itemSection){
        if (itemSection.get("model") == null) return 0;
        return Integer.parseInt(itemSection.get("model").toString());
    }

    public static Component regextesting(@NotNull String name){
        String[] s = name.split("&[0-9a-fl-oA-FL-O]");
        Component comp = Component.text("");
        char[] chararray = name.toCharArray();
        char[] newchararray = new char[chararray.length];
        int j = 2;
        int k = 2;
        TextColor[] color = new TextColor[10];
        Set<TextDecoration>[] decor1 = new Set[10];
        for (int i = 0; i <= chararray.length-1; i++){
            if (chararray[i] == '&' && "0123456789AaBbCcDdEeFf".indexOf(chararray[i + 1]) > -1){
                newchararray[0] = chararray[i];
                newchararray[1] = chararray[i+1];
                color[j] = getComponentColor(new String(newchararray));
                newchararray = new char[10];
                i=i+2;
                j++;
            }
            if (chararray[i] == '&' && "KkLlMmNnOo".indexOf(chararray[i + 1]) > -1) {
                newchararray[0] = chararray[i];
                newchararray[1] = chararray[i+1];
                decor1[k] = getComponentDecorationSet(new String(newchararray));
                i=i+2;
                k++;
            }
        }
        for (int i = 0; i <= s.length-1; i++){
            if (k >= 3) {
                if (decor1[i] != null)
                    comp = comp.append(Component.text().content(s[i]).color(color[i]).decorations(decor1[i], true));
                else
                    comp = comp.append(Component.text().content(s[i]).color(color[i]));
            }else{
                if (decor1[i+1] != null)
                    comp = comp.append(Component.text().content(s[i]).color(color[i+1]).decorations(decor1[i+1], true));
                else
                    comp = comp.append(Component.text().content(s[i]).color(color[i+1]));
            }
        }
        return comp;
    }

    public static @Nullable TextColor getComponentColor(@NotNull String textToTranslate){
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && b[i+1] == '0') {
                return TextColor.fromHexString("#000000");
            }
            if (b[i] == '&' && b[i+1] == '1') {
                return TextColor.fromHexString("#0000AA");
            }
            if (b[i] == '&' && b[i+1] == '2') {
                return TextColor.fromHexString("#00AA00");
            }
            if (b[i] == '&' && b[i+1] == '3') {
                return TextColor.fromHexString("#00AAAA");
            }
            if (b[i] == '&' && b[i+1] == '4') {
                return TextColor.fromHexString("#AA0000");
            }
            if (b[i] == '&' && b[i+1] == '5') {
                return TextColor.fromHexString("#AA00AA");
            }
            if (b[i] == '&' && b[i+1] == '6') {
                return TextColor.fromHexString("#FFAA00");
            }
            if (b[i] == '&' && b[i+1] == '7') {
                return TextColor.fromHexString("#AAAAAA");
            }
            if (b[i] == '&' && b[i+1] == '8') {
                return TextColor.fromHexString("#555555");
            }
            if (b[i] == '&' && b[i+1] == '9') {
                return TextColor.fromHexString("#5555FF");
            }
            if (b[i] == '&' && b[i+1] == 'A') {
                return TextColor.fromHexString("#55FF55");
            }
            if (b[i] == '&' && b[i+1] == 'a') {
                return TextColor.fromHexString("#55FF55");
            }
            if (b[i] == '&' && b[i+1] == 'B'){
                return TextColor.fromHexString("#55FFFF");
            }
            if (b[i] == '&' && b[i+1] == 'b') {
                return TextColor.fromHexString("#55FFFF");
            }
            if (b[i] == '&' && b[i+1] == 'C'){
                return TextColor.fromHexString("#FF5555");
            }
            if (b[i] == '&' && b[i+1] == 'c') {
                return TextColor.fromHexString("#FF5555");
            }
            if (b[i] == '&' && b[i+1] == 'D'){
                return TextColor.fromHexString("#FF55FF");
            }
            if (b[i] == '&' && b[i+1] == 'd') {
                return TextColor.fromHexString("#FF55FF");
            }
            if (b[i] == '&' && b[i+1] == 'E'){
                return TextColor.fromHexString("#FFFF55");
            }
            if (b[i] == '&' && b[i+1] == 'e') {
                return TextColor.fromHexString("#FFFF55");
            }
            if (b[i] == '&' && b[i+1] == 'F'){
                return TextColor.fromHexString("#FFFFFF");
            }
            if (b[i] == '&' && b[i+1] == 'f') {
                return TextColor.fromHexString("#FFFFFF");
            }
        }
        return null;
    }

    public static @NotNull Set<TextDecoration> getComponentDecorationSet(@NotNull String textToTranslate){
        char[] b =textToTranslate.toCharArray();
        Set<TextDecoration> decor = new HashSet<>();
        for (int i = 0; i< b.length-1;i++){
            if (b[i] == '&' && "Kk".indexOf(b[i+1])>-1){
                decor.add(TextDecoration.OBFUSCATED);
            }
            if (b[i] == '&' && "Ll".indexOf(b[i+1])>-1){
                decor.add(TextDecoration.BOLD);
            }
            if (b[i] == '&' && "Mm".indexOf(b[i+1])>-1){
                decor.add(TextDecoration.STRIKETHROUGH);
            }
            if (b[i] == '&' && "Nn".indexOf(b[i+1])>-1){
                decor.add(TextDecoration.UNDERLINED);
            }
            if (b[i] == '&' && "Oo".indexOf(b[i+1])>-1){
                decor.add(TextDecoration.ITALIC);
            }
        }
        return decor;
    }
}
