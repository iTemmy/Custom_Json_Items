package com.temmy.json_items_test_1.Parser;

import com.google.gson.JsonSyntaxException;
import com.temmy.json_items_test_1.attribute.Attribute;
import com.temmy.json_items_test_1.util.Convert;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

public final class ItemParser {
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
            Component componentName = Component.text(((String) itemSection.get("name")).trim());
            meta.displayName(componentName);

            Integer customModelData = Integer.parseInt(itemSection.get("model").toString());
            meta.setCustomModelData(customModelData);

            JSONArray lore = (JSONArray) itemSection.get("lore");
            List<String> loreList = new ArrayList<>(lore);
            meta.lore(Collections.singletonList(Component.text(String.valueOf(loreList))));

            Map<String, JSONArray> attributesJson = (Map<String, JSONArray>) itemSection.get("attributes");
            Map<String, String[]> attributes = new HashMap<>();

            for (String key : attributesJson.keySet()){
                List<String> attributeList = new ArrayList<>(attributesJson.get(key));
                attributes.put(key, attributeList.toArray(new String[attributeList.size()]));
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
