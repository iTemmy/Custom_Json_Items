package com.temmy.json_items_test_1.Parser;

import com.google.gson.JsonSyntaxException;
import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.Convert;
import com.temmy.json_items_test_1.util.Glow;
import net.kyori.adventure.text.Component;
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
            name = ChatColor.translateAlternateColorCodes('&', (String) itemSection.get("name"));
            Component componentName = Component.text(name);

            if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                Emeta.displayName(componentName);
                Emeta.setLocalizedName(((String) itemSection.get("name")).trim());
            }else{
                meta.displayName(componentName);
                meta.setLocalizedName(((String) itemSection.get("name")).trim());
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
                    loreList.set(i, ChatColor.translateAlternateColorCodes('&', loreList.get(i)));
                    loreList1.set(i, Component.text(loreList.get(i)));
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
}
