package com.temmy.json_items_test_1;

import com.temmy.json_items_test_1.Parser.ItemParser;
import com.temmy.json_items_test_1.command.GiveItem;
import com.temmy.json_items_test_1.command.trieDump;
import com.temmy.json_items_test_1.file.PluginFiles;
import com.temmy.json_items_test_1.listener.EntityDamageByEntityListener;
import com.temmy.json_items_test_1.listener.onBlockDropItemListener;
import com.temmy.json_items_test_1.listener.PlayerArmorChangeListener;
import com.temmy.json_items_test_1.util.Queue;
import com.temmy.json_items_test_1.util.trie.Trie;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class Main extends JavaPlugin {
    private static JavaPlugin plugin;
    public static JavaPlugin getPlugin(){return plugin;}
    private static Trie<ItemStack> test = new Trie();


    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new onBlockDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerArmorChangeListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        PluginFiles.init();
        for (File item : PluginFiles.getItemFiles())
            registerItemRecipes(item);
        getCommand("giveitem").setExecutor(new GiveItem());
        getCommand("trieDump").setExecutor(new trieDump());
    }

    public static Trie<ItemStack> getTest(){return test;}

    //TODO: Use Queue system to store items that require another custom item like custom ores such as Corinthium

    Queue<File> files = new Queue<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registerItemRecipes(File itemFile){
        try {
            Object jsonObject = new JSONParser().parse(new FileReader(itemFile));
            JSONObject itemJson = (JSONObject) jsonObject;

            Map itemSection = (Map) itemJson.get("ITEM");
            if (itemSection == null) return;

            if (itemSection.get("requires") != null){
                files.enqueue(itemFile);
                getLogger().info((String) itemSection.get("requires"));
                registerItemRecipes(PluginFiles.getItemFile((String) itemSection.get("requires")));
            }

            Map<String, String> ingredients = (Map) itemSection.get("ingredients");
            JSONArray recipe = (JSONArray) itemSection.get("recipe");

            String fileName = ((String) itemSection.get("file_name")).trim().toLowerCase().replaceAll("\\s", "_");

            ItemStack item = ItemParser.parseItem(fileName);
            if (item == null) return;

            String itemName = ((String) itemSection.get("name")).trim().toLowerCase().replaceAll("\\s", "_");
            if (ingredients != null && recipe != null){
                ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), itemName), item);

                List<String> recipeLine = new ArrayList<String>(recipe);
                shapedRecipe.shape(recipeLine.toArray(new String[0]));

                for (String key : ingredients.keySet())
                    if (Material.valueOf(ingredients.get(key)) != null)
                        shapedRecipe.setIngredient(key.charAt(0), Material.valueOf(ingredients.get(key)));
                    else if (Bukkit.getRecipe(new NamespacedKey(Main.getPlugin(), ingredients.get(key))) != null)
                        shapedRecipe.setIngredient(key.charAt(0), Bukkit.getRecipe(new NamespacedKey(getPlugin(), ingredients.get(key))).getResult());
                    else
                        shapedRecipe.setIngredient(key.charAt(0), test.get(key));

                Bukkit.addRecipe(shapedRecipe);
            }else{
                   test.put(fileName, item);
            }
            if (!(files.isEmpty())) {
                registerItemRecipes(files.peek());
                files.dequeue();
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }
}
