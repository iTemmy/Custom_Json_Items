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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ItemParser {

    static Logger log = Bukkit.getLogger();

    public static ItemStack parseItem(String item){
        ItemStack itemStack = null;
        String filename = item.toLowerCase();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Main.getPlugin().getDataFolder()+String.format("/item/%s.json", item));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream == null) {log.info("invalid input stream for file "+item);return null;}

        try {
            Object jsonObject = new JSONParser().parse(new InputStreamReader(inputStream));
            JSONObject  itemJson = (JSONObject) jsonObject;
            Map<String, ?> itemSection = (Map<String, ?>) itemJson.get("ITEM");
            if (itemSection == null) return null;

            itemStack = new ItemStack(Material.valueOf((String)itemSection.get("material")));
            ItemMeta meta = null;
            EnchantmentStorageMeta Emeta = null;
            if (itemStack.getType() == Material.ENCHANTED_BOOK)
                Emeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            else
                meta = itemStack.getItemMeta();

            String name;
            if (itemSection.get("name") != null) {
                name = (String) itemSection.get("name");
                if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                    Emeta.displayName(regextesting(name));
                    Emeta.setLocalizedName(((String) itemSection.get("name")).trim());
                }else{
                    meta.displayName(regextesting(name));
                    meta.setLocalizedName(((String) itemSection.get("name")).trim());
                }
            }
            Integer customModelData = Integer.parseInt(itemSection.get("model").toString());
            if (itemStack.getType() == Material.ENCHANTED_BOOK)
                Emeta.setCustomModelData(customModelData);
            else
                meta.setCustomModelData(customModelData);

            int attackDamage = 0;
            if (itemSection.get("attackDamage") != null) {
                attackDamage = Integer.parseInt(String.valueOf((Long) itemSection.get("attackDamage")));
                meta.addAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),
                        "generic.attack.damage", attackDamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
            }

            int health = 0;
            String slot = null;
            if (itemSection.get("health") != null && itemSection.get("healthSlot") != null){
                health = Integer.parseInt(String.valueOf((long) itemSection.get("health")));
                slot = (String) itemSection.get("healthSlot");
                meta.addAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),
                        "generic.max.health", health, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(slot)));
            }

            JSONArray JSONEnchants = (JSONArray) itemSection.get("enchants");
            if (JSONEnchants != null){
                List<String> enchantsList = new ArrayList<>(JSONEnchants);
                for (String s : enchantsList){
                    if (s.equals(null)||s.equals("")) continue;
                    String[] enchantsWithLevel = s.split(",");
                    for (String enchant : enchantsWithLevel) {
                        String[] enchants = enchant.split(";");
                        if (enchants[0].equalsIgnoreCase("glow")) {
                            NamespacedKey key = new NamespacedKey(Main.getPlugin(), Main.getPlugin().getDescription().getName());
                            Glow glow = new Glow(key);
                            if (itemStack.getType() == Material.ENCHANTED_BOOK){
                                Emeta.addStoredEnchant(glow, 1, true);
                            }else {
                                meta.addEnchant(glow, 1, true);
                            }
                        } else{
                            if (itemStack.getType() == Material.ENCHANTED_BOOK){
                                Emeta.addStoredEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchants[0].toLowerCase())), Integer.parseInt(enchants[1]), true);
                            }else {
                                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchants[0].toLowerCase())), Integer.parseInt(enchants[1]), true);
                            }
                        }
                    }
                }
            }

            boolean unbreable = false;
            if (itemSection.get("unbreakable") != null)
                unbreable = (boolean) itemSection.get("unbreakable");
            if (unbreable)
                if (itemStack.getType() == Material.ENCHANTED_BOOK){
                    Emeta.setUnbreakable(true);
                }else {
                    meta.setUnbreakable(true);
                }

            JSONArray lore = (JSONArray) itemSection.get("lore");
            List<Component> loreList1 = new ArrayList<>(lore);
            List<String> loreList = new ArrayList<>(lore);
            if (loreList != null && loreList.size() > 0) {
                for (int i = 0; i <= loreList.size()-1; i++) {
                    loreList1.set(i, regextesting(loreList.get(i)));
                }
            }
            if (loreList.size() <= 0)
                if (itemStack.getType() == Material.ENCHANTED_BOOK){
                    Emeta.lore(null);
                }else {
                    meta.lore(null);
                }
            else
                if (itemStack.getType() == Material.ENCHANTED_BOOK){
                    Emeta.lore(loreList1);
                }else {
                    meta.lore(loreList1);
                }

            Map<String, JSONObject> attributesJson = (Map<String, JSONObject>) itemSection.get("attributes");
            Map<String, String[]> attributes = new HashMap<>();

            if (attributesJson != null) {
                for (String key : attributesJson.keySet()) {
                    List<String> attributeList = new ArrayList<String>(Collections.singleton(String.valueOf(attributesJson.get(key))));
                    attributes.put(key, attributeList.toArray(new String[attributeList.size()]));
                }
            }

            if (itemStack.getType() == Material.ENCHANTED_BOOK){
                PersistentDataContainer dataContainer = Emeta.getPersistentDataContainer();
                dataContainer.set(
                        Attribute.namespacedKey
                        , PersistentDataType.STRING
                        , Convert.mapToString(attributes)
                );
            }else {
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                dataContainer.set(
                        Attribute.namespacedKey
                        , PersistentDataType.STRING
                        , Convert.mapToString(attributes)
                );
            }

            if (itemStack.getType() == Material.ENCHANTED_BOOK){
                itemStack.setItemMeta(Emeta);
            }else {
                itemStack.setItemMeta(meta);
            }
        }catch (JsonSyntaxException e){
            Bukkit.getLogger().log(Level.WARNING, "Syntax Error in " + filename);
            e.printStackTrace();
        }catch (IOException | ParseException e1){
            e1.printStackTrace();
        }

        return itemStack;
    }

    private static Component regextesting(String name){
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

    public static TextColor getComponentColor(String textToTranslate){
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

    public static Set<TextDecoration> getComponentDecorationSet(String textToTranslate){
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

    private static String removeColourCodes(String textToTranslate){
        char[] b = textToTranslate.toCharArray();
        char[] c = new char[b.length];
        int j = 0;
        for (int i = 0; i < b.length; i++){
            if (b[i] == '&' && b[i+1] == '0') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '1') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '2') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '3') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '4') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '5') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '6') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '7') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '8') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == '9') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'A') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'a') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'B'){
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'b') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'C'){
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'c') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'D'){
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'd') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'E'){
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'e') {
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'F'){
                i=i+2;
            }
            if (b[i] == '&' && b[i+1] == 'f') {
                i=i+2;
            }
            if (b[i] == '&' && "Kk".indexOf(b[i+1])>-1){
                i=i+2;
            }
            if (b[i] == '&' && "Ll".indexOf(b[i+1])>-1){
                i=i+2;
            }
            if (b[i] == '&' && "Mm".indexOf(b[i+1])>-1){
                i=i+2;
            }
            if (b[i] == '&' && "Nn".indexOf(b[i+1])>-1){
                i=i+2;
            }
            if (b[i] == '&' && "Oo".indexOf(b[i+1])>-1){
                i=i+2;
            }
            c[j] = b[i];
            j++;
        }
        return new String(c).trim();
    }
}
