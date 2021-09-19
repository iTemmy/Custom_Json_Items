package com.temmy.json_items_test_1;

import com.temmy.json_items_test_1.Parser.ItemParser;
import com.temmy.json_items_test_1.command.GiveItem;
import com.temmy.json_items_test_1.command.GiveItemTabCompleter;
import com.temmy.json_items_test_1.command.Reload;
import com.temmy.json_items_test_1.command.trieDump;
import com.temmy.json_items_test_1.file.PluginFiles;
import com.temmy.json_items_test_1.listener.*;
import com.temmy.json_items_test_1.util.Glow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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
import java.lang.reflect.Field;
import java.util.*;

public final class Main extends JavaPlugin {
    private static JavaPlugin plugin;
    public static JavaPlugin getPlugin(){return plugin;}
    private static Map<String, ItemStack> customItems = new HashMap<String, ItemStack>();
    public static boolean debug;


    @Override
    public void onEnable() {
        plugin = this;
        registerGlow();
        getServer().getPluginManager().registerEvents(new onBlockDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerArmorChangeListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new onBlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new EntityShootBowListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSwapHandItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerItemHeldListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new EntityPickupItemListener(), this);
        PluginFiles.init();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        loadConfig();
        for (File item : PluginFiles.getItemFiles())
            registerItemRecipes(item);
        for (String s : files3.keySet()) {
            registerItemRecipes(files3.get(s));
            l.add(s);
        }
        for (String s : l)
            files3.remove(s);
        getCommand("giveitem").setExecutor(new GiveItem());
        //getCommand("trieDump").setExecutor(new trieDump());
        getCommand("giveitem").setTabCompleter(new GiveItemTabCompleter());
        getCommand("reloadores").setExecutor(new Reload());
        ores();
    }

    public void onDisable(){
        saveLocalConfig();
    }

    public static Map<String, ItemStack> getCustomItems(){return customItems;}

    Map<String, File> files3 = new HashMap<String, File>();
    List<String> l = new ArrayList<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registerItemRecipes(File itemFile){
        try {
            Object jsonObject = new JSONParser().parse(new FileReader(itemFile));
            JSONObject itemJson = (JSONObject) jsonObject;

            Map itemSection = (Map) itemJson.get("ITEM");
            if (itemSection == null) return;

            Map<String, String> ingredients = (Map) itemSection.get("ingredients");
            JSONArray recipe = (JSONArray) itemSection.get("recipe");

            String fileName = ((String) itemSection.get("file_name")).trim().replaceAll("\\s", "_");
            if (customItems.containsKey(fileName.toLowerCase())) {
                return;
            }

            String requires = (String) itemSection.get("requires");
            if (itemSection.get("requires") != null){
                if (requires != null) {
                    String[] require = requires.split(",");
                    for (String req : require) {


                        for (File file : PluginFiles.getItemFiles())
                            if (req.equalsIgnoreCase(file.getName().trim().replace(".json", "")))
                                registerItemRecipes(file);
                        if (!(customItems.containsKey(req))) {
                            if (!files3.containsValue(itemFile))
                                files3.put(fileName, itemFile);
                        }
                    }
                }
            }

            ItemStack item = ItemParser.parseItem(fileName);
            if (item == null) {
                if (debug)
                    getLogger().info("Error "+fileName+" is null.");
                return;}

            if (ingredients != null && recipe != null){
                ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), fileName), item);

                List<String> recipeLine = new ArrayList<String>(recipe);
                shapedRecipe.shape(recipeLine.toArray(new String[0]));

                for (String key : ingredients.keySet()) {
                    if (Material.matchMaterial(ingredients.get(key)) != null)
                        shapedRecipe.setIngredient(key.charAt(0), Material.valueOf(ingredients.get(key).toUpperCase()));
                    else if (Bukkit.getRecipe(new NamespacedKey(Main.getPlugin(), ingredients.get(key))) != null)
                        shapedRecipe.setIngredient(key.charAt(0), Bukkit.getRecipe(new NamespacedKey(getPlugin(), ingredients.get(key))).getResult());
                    else if (customItems.containsKey(ingredients.get(key).toLowerCase())){
                        shapedRecipe.setIngredient(key.charAt(0), customItems.get(ingredients.get(key).toLowerCase()));
                    }
                }
                if (Bukkit.getRecipe(new NamespacedKey(Main.getPlugin(), fileName)) == null) {
                    if (debug)
                        getLogger().info("Registering "+fileName);
                    Bukkit.addRecipe(shapedRecipe);
                }
            }
            customItems.put(fileName.toLowerCase(), item);

        }catch (IOException | ParseException | IllegalArgumentException | NullPointerException e){
            if (debug) getLogger().warning("Error: "+itemFile.getName());
            e.printStackTrace();
        }
    }

    public void registerGlow(){
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), getDescription().getName());

            Glow glow = new Glow(key);
            if (Enchantment.getByKey(key) == null)
                Enchantment.registerEnchantment(glow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Map<String, Integer> chances = new HashMap<>();

    public static void loadConfig(){
        chances.put("Phosphorus".toLowerCase(), plugin.getConfig().getInt("Phosphorus"));
        chances.put("Janelite".toLowerCase(), plugin.getConfig().getInt("Janelite"));
        chances.put("Ellendyte".toLowerCase(), plugin.getConfig().getInt("Ellendyte"));
        chances.put("Sapphire".toLowerCase(), plugin.getConfig().getInt("Sapphire"));
        chances.put("Tungsten".toLowerCase(), plugin.getConfig().getInt("Tungsten"));
        chances.put("Jolixanine".toLowerCase(), plugin.getConfig().getInt("Jolixanine"));
        chances.put("Corinthium".toLowerCase(), plugin.getConfig().getInt("Corinthium"));
        chances.put("Zinc".toLowerCase(), plugin.getConfig().getInt("Zinc"));
        chances.put("Kaylax".toLowerCase(), plugin.getConfig().getInt("Kaylax"));
        debug = plugin.getConfig().getBoolean("IncreasedDebugging");
    }

    private static void saveLocalConfig(){
        for (String s : chances.keySet()) {
            plugin.getConfig().set(s, chances.get(s));
        }
    }

    public static Map<String, Integer> getChances(){
        return chances;
    }


    static Map<String, NamespacedKey> ores = new HashMap<String, NamespacedKey>();

    void ores() {
        NamespacedKey phosphorusKey = new NamespacedKey(plugin, "Phosphorus");
        NamespacedKey janeliteKey = new NamespacedKey(plugin, "Janelite");
        NamespacedKey ellendyteKey = new NamespacedKey(plugin, "Ellendyte");
        NamespacedKey sapphireKey = new NamespacedKey(plugin, "Sapphire");
        NamespacedKey tungstenKey = new NamespacedKey(plugin, "Tungsten");
        NamespacedKey jolixanineKey = new NamespacedKey(plugin, "Jolixanine");
        NamespacedKey corinthiumKey = new NamespacedKey(plugin, "Corinthium");
        NamespacedKey zincKey = new NamespacedKey(plugin, "Zinc");
        NamespacedKey kaylaxKey = new NamespacedKey(plugin, "Kaylax");
        ores.put("Phosphorus", phosphorusKey);
        ores.put("Janelite", janeliteKey);
        ores.put("Ellendyte", ellendyteKey);
        ores.put("Sapphire", sapphireKey);
        ores.put("Tungsten", tungstenKey);
        ores.put("Jolixanine", jolixanineKey);
        ores.put("Corinthium", corinthiumKey);
        ores.put("Zinc", zincKey);
        ores.put("Kaylax", kaylaxKey);
    }
    public static Map<String, NamespacedKey> getOres(){return ores;}

}
