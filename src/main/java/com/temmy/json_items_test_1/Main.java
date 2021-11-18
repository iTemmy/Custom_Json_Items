package com.temmy.json_items_test_1;

import com.temmy.json_items_test_1.Parser.ItemParser;
import com.temmy.json_items_test_1.command.GiveItem;
import com.temmy.json_items_test_1.command.GiveItemTabCompleter;
import com.temmy.json_items_test_1.command.Reload;
import com.temmy.json_items_test_1.command.trieDump;
import com.temmy.json_items_test_1.file.PluginFiles;
import com.temmy.json_items_test_1.listener.*;
import com.temmy.json_items_test_1.util.FoodDetails;
import com.temmy.json_items_test_1.util.Glow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        registerFoods();
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
        getServer().getPluginManager().registerEvents(new PlayerPickupExperienceListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
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

    static Map<Material, FoodDetails> foodMap = new HashMap<>();

    public void registerFoods(){
        Map<PotionEffect, Boolean> map = new HashMap<PotionEffect, Boolean>();
        foodMap.put(Material.APPLE, new FoodDetails(4, 2.4f));
        foodMap.put(Material.BAKED_POTATO, new FoodDetails(5, 6));
        foodMap.put(Material.BEETROOT, new FoodDetails(1, 1.2f));
        foodMap.put(Material.BEETROOT_SOUP, new FoodDetails(6, 7.5f));
        foodMap.put(Material.BREAD, new FoodDetails(5, 6));
        foodMap.put(Material.CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.BLACK_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.BLUE_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.BROWN_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.CYAN_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.GRAY_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.GREEN_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.LIGHT_BLUE_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.LIGHT_GRAY_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.LIME_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.MAGENTA_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.ORANGE_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.PINK_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.PURPLE_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.RED_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.WHITE_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.YELLOW_CANDLE_CAKE, new FoodDetails(14, 2.8f));
        foodMap.put(Material.CARROTS, new FoodDetails(3, 3.6f));
        foodMap.put(Material.CHORUS_FRUIT, new FoodDetails(4, 2.4f, true));
        foodMap.put(Material.COOKED_CHICKEN, new FoodDetails(6, 7.2f));
        foodMap.put(Material.COOKED_COD, new FoodDetails(5, 6));
        foodMap.put(Material.COOKED_MUTTON, new FoodDetails(6, 9.6f));
        foodMap.put(Material.COOKED_PORKCHOP, new FoodDetails(8, 12.8f));
        foodMap.put(Material.COOKED_RABBIT, new FoodDetails(5, 6));
        foodMap.put(Material.COOKED_SALMON, new FoodDetails(6, 9.6f));
        foodMap.put(Material.COOKIE, new FoodDetails(2, 0.4f));
        foodMap.put(Material.DRIED_KELP, new FoodDetails(1, 0.6f));
        foodMap.put(Material.ENCHANTED_GOLDEN_APPLE, new FoodDetails(4, 9.6f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, 400, 1),
                new PotionEffect(PotionEffectType.ABSORPTION, 2400, 3),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0)))));
        foodMap.put(Material.GOLDEN_APPLE, new FoodDetails(4, 9.6f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, 100, 1),
                        new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0)))));
        foodMap.put(Material.GLOW_BERRIES, new FoodDetails(2, 0.4f));
        foodMap.put(Material.GOLDEN_CARROT, new FoodDetails(6, 14.4f));
        map.put(new PotionEffect(PotionEffectType.POISON, 20, 1), false);
        foodMap.put(Material.HONEY_BOTTLE, new FoodDetails(6, 1.2f, map));
        map.clear();
        foodMap.put(Material.MELON_SLICE, new FoodDetails(2, 1.2f));
        foodMap.put(Material.MUSHROOM_STEW, new FoodDetails(6, 7.2f));
        foodMap.put(Material.POISONOUS_POTATO, new FoodDetails(2, 1.2f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.POISON, 100, 0)))));
        foodMap.put(Material.POTATO, new FoodDetails(1, 0.6f));
        foodMap.put(Material.PUFFERFISH, new FoodDetails(1, 0.2f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.HUNGER, 300, 2),
                        new PotionEffect(PotionEffectType.CONFUSION, 300, 0),
                        new PotionEffect(PotionEffectType.POISON, 1200, 1)))));
        foodMap.put(Material.PUMPKIN_PIE, new FoodDetails(8, 4.8f));
        foodMap.put(Material.RABBIT_STEW, new FoodDetails(10, 12));
        foodMap.put(Material.BEEF, new FoodDetails(3, 1.8f));
        foodMap.put(Material.CHICKEN, new FoodDetails(2, 1.2f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.HUNGER, 600, 0)))));
        foodMap.put(Material.COD, new FoodDetails(2, 0.4f));
        foodMap.put(Material.MUTTON, new FoodDetails(2, 1.2f));
        foodMap.put(Material.PORKCHOP, new FoodDetails(3, 1.8f));
        foodMap.put(Material.RABBIT, new FoodDetails(3, 1.8f));
        foodMap.put(Material.SALMON, new FoodDetails(2, 0.4f));
        foodMap.put(Material.ROTTEN_FLESH, new FoodDetails(4, 0.8f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.HUNGER, 600, 0)))));
        foodMap.put(Material.SPIDER_EYE, new FoodDetails(2, 3.2f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.POISON, 100, 0)))));
        foodMap.put(Material.COOKED_BEEF, new FoodDetails(8, 12.8f));
        foodMap.put(Material.SUSPICIOUS_STEW, new FoodDetails(6, 7.2f));
        foodMap.put(Material.SWEET_BERRIES, new FoodDetails(2, 0.4f));
        foodMap.put(Material.TROPICAL_FISH, new FoodDetails(1, 0.2f));
        for (PotionEffectType type : PotionEffectType.values()){
            map.put(new PotionEffect(type, 10, 0), false);
        }
        foodMap.put(Material.MILK_BUCKET, new FoodDetails(map));
        map.clear();
    }

    public static Map<Material, FoodDetails> getFoodMap(){
        return foodMap;
    }
}
