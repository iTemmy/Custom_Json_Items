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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ItemParser {

    static Logger log = Bukkit.getLogger();

    public static ItemStack parseItem(String item){
        ItemStack itemStack = null;
        String filename = item.toLowerCase();
        InputStream inputStream = ItemParser.class.getResourceAsStream(String.format("/item/%s.json", filename));

        if (inputStream == null) return null;

        try {
            Object jsonObject = new JSONParser().parse(new InputStreamReader(inputStream));
            JSONObject  itemJson = (JSONObject) jsonObject;
            Map<String, ?> itemSection = (Map<String, ?>) itemJson.get("ITEM");
            if (itemSection == null) return null;

            itemStack = new ItemStack(Material.valueOf((String)itemSection.get("material")));
            ItemMeta meta = itemStack.getItemMeta();
            String name;
            name = ChatColor.translateAlternateColorCodes('&', (String) itemSection.get("name"));
            Component componentName = Component.text(name);

            meta.displayName(componentName);
            meta.setLocalizedName(((String) itemSection.get("name")).trim());

            Integer customModelData = Integer.parseInt(itemSection.get("model").toString());
            meta.setCustomModelData(customModelData);

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
                            meta.addEnchant(glow, 1, true);
                        } else{
                            meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchants[0].toLowerCase())), Integer.parseInt(enchants[1]), true);
                        }
                    }
                }
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
                meta.lore(null);
            else
                meta.lore(loreList1);

            Map<String, JSONObject> attributesJson = (Map<String, JSONObject>) itemSection.get("attributes");
            Map<String, String[]> attributes = new HashMap<>();

            if (attributesJson != null) {
                for (String key : attributesJson.keySet()) {
                    List<String> attributeList = new ArrayList<String>(Collections.singleton(String.valueOf(attributesJson.get(key))));
                    attributes.put(key, attributeList.toArray(new String[attributeList.size()]));
                }
            }

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(
                    Attribute.namespacedKey
                    , PersistentDataType.STRING
                    , Convert.mapToString(attributes)
            );

            itemStack.setItemMeta(meta);
        }catch (JsonSyntaxException e){
            Bukkit.getLogger().log(Level.WARNING, "Syntax Error in " + filename);
            e.printStackTrace();
        }catch (IOException | ParseException e1){
            e1.printStackTrace();
        }

        return itemStack;
    }
}
