package com.temmy.json_items_test_1;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.temmy.json_items_test_1.Parser.Item;
import com.temmy.json_items_test_1.Parser.ItemParser;
import com.temmy.json_items_test_1.attribute.MultiPageChests;
import com.temmy.json_items_test_1.command.*;
import com.temmy.json_items_test_1.file.PluginFiles;
import com.temmy.json_items_test_1.listener.*;
import com.temmy.json_items_test_1.util.*;
import com.temmy.json_items_test_1.util.newCustomItem.NewCustomItem;
import com.temmy.json_items_test_1.util.newCustomItem.recipe.*;
import org.bukkit.*;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

//TODO: REMEMBER CHANGED THE SEPARATOR BETWEEN ATTRIBUTES TO ~ FROM ; TO ALLOW LISTING OF BLOCKS WITH THE ; AS THE SEPARATOR
//TODO: REMEMBER CHANGED THE SEPARATOR BETWEEN ATTRIBUTES TO ~ FROM ; TO ALLOW LISTING OF BLOCKS WITH THE ; AS THE SEPARATOR
//TODO: REMEMBER CHANGED THE SEPARATOR BETWEEN ATTRIBUTES TO ~ FROM ; TO ALLOW LISTING OF BLOCKS WITH THE ; AS THE SEPARATOR
//TODO: REMEMBER CHANGED THE SEPARATOR BETWEEN ATTRIBUTES TO ~ FROM ; TO ALLOW LISTING OF BLOCKS WITH THE ; AS THE SEPARATOR
//TODO: REMEMBER CHANGED THE SEPARATOR BETWEEN ATTRIBUTES TO ~ FROM ; TO ALLOW LISTING OF BLOCKS WITH THE ; AS THE SEPARATOR
//TODO: REMEMBER CHANGED THE SEPARATOR BETWEEN ATTRIBUTES TO ~ FROM ; TO ALLOW LISTING OF BLOCKS WITH THE ; AS THE SEPARATOR


//TODO: Working on changing the saving format to be sql so items will have to be made via in-game gui editor or
// possibly have command to import .json files from the item folder for importing old items or creating items via json


@SuppressWarnings("FieldMayBeFinal")
public final class Main extends JavaPlugin {
    private static JavaPlugin plugin;
    public static JavaPlugin getPlugin(){return plugin;}
    private static Map<String, NewCustomItem> customItems = new HashMap<>();
    public static boolean debug;
    public static String customOreWorld;
    public static Boolean worldGuardEnabled = false;
    public static WorldGuard worldGuard = null;
    public static StateFlag attributesEnabledFlag;
    public static NamespacedKey glowKey;
    public static Glow glow;
    public static Map<Location, ActiveInventory> newActiveInventories = new HashMap<>();

    public static WorldGuard getWorldGuard(){
        return worldGuard;
    }
    private Map<String, File> files3 = new HashMap<>();
    private List<String> l = new ArrayList<>();

    public static void registerCustomFlag(){
        FlagRegistry registry = worldGuard.getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("Attribute-Use", true);
            registry.register(flag);
            attributesEnabledFlag = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("Attribute-Use");
            if (existing instanceof StateFlag) {
                attributesEnabledFlag = (StateFlag) existing;
            }else {
                plugin.getLogger().severe("SEVERE ERROR with WorldGuard Flag");
            }
            return;
        }
        plugin.getLogger().info("Registered Custom WorldGuard Flag");
    }

    @Override
    public void onLoad(){
        plugin = this;
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardEnabled = true;
            getLogger().info("Registering Custom WorldGuard Flag");
            worldGuard = WorldGuard.getInstance();
            registerCustomFlag();
        }else
            getLogger().warning("WorldGuard not detected, players will be able to use attributes anywhere!");
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        registerGlow();
        new Thread(this::registerFoods).start();
        registerEvents(getServer());
        PluginFiles.init();
        new Thread( () -> {
            getConfig().options().copyDefaults();
            saveDefaultConfig();
            loadConfig();
        }).start();
        getLogger().info("Registering Custom Items.");
        for (File item : PluginFiles.getItemFiles())
            registerItemJsonRecipes(item);
        for (String s : files3.keySet()) {
            registerItemJsonRecipes(files3.get(s));
            l.add(s);
        }
        for (String s : l)
            files3.remove(s);
        getLogger().info(String.format("Registered %s items.", new Item().getAllItems().size()));
        getCommand("giveitem").setExecutor(new GiveItem());
        getCommand("giveitem").setTabCompleter(new GiveItemTabCompleter());
        getCommand("reloadores").setExecutor(new Reload());
        getCommand("debugging").setExecutor(new Debugging());
        getCommand("triedump").setExecutor(new trieDump());
        ores();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, checkForActiveInventories(), 72000L, 72000L);
    }

    private void registerEvents(Server server){
        server.getPluginManager().registerEvents(new onBlockDropItemListener(), this);
        server.getPluginManager().registerEvents(new PlayerArmorChangeListener(), this);
        server.getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        server.getPluginManager().registerEvents(new onBlockPlaceListener(), this);
        server.getPluginManager().registerEvents(new BlockBreakListener(), this);
        server.getPluginManager().registerEvents(new CraftItemListener(), this);
        server.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        server.getPluginManager().registerEvents(new EntityShootBowListener(), this);
        server.getPluginManager().registerEvents(new PlayerSwapHandItemListener(), this);
        server.getPluginManager().registerEvents(new PlayerItemHeldListener(), this);
        server.getPluginManager().registerEvents(new InventoryClickListener(), this);
        server.getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        server.getPluginManager().registerEvents(new EntityPickupItemListener(), this);
        server.getPluginManager().registerEvents(new PlayerPickupExperienceListener(), this);
        server.getPluginManager().registerEvents(new ProjectileLaunchListener(), this);
        server.getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        server.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        server.getPluginManager().registerEvents(new EntityTargetLivingEntity(), this);
        server.getPluginManager().registerEvents(new EntityExplode(), this);
        server.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        server.getPluginManager().registerEvents(new InventoryOpenListener(), this);
        server.getPluginManager().registerEvents(new InventoryMoveItemListener(), this);
    }

    public void onDisable(){
        saveLocalConfig();
        saveActiveInventories();
    }

    private void saveActiveInventories(){
        for (Location loc : newActiveInventories.keySet()){
            ActiveInventory activeInv = newActiveInventories.get(loc);
            for (Player player : activeInv.getViewers()){
                player.closeInventory();
            }
            Map<Integer, Inventory> map = activeInv.getPages();
            for (int i : map.keySet()){
                map.get(i);
                if (loc.getBlock().getState() instanceof TileState chest){
                    PersistentDataContainer container = chest.getPersistentDataContainer().get(MultiPageChests.pageContainerKey, PersistentDataType.TAG_CONTAINER);
                    assert container != null;
                    MultiPageChests.savePage(container, map.get(i), i);
                }else getLogger().warning(String.format("Unable to save active Inventory for inventory located at %sx, %sy, %sz in world %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));

            }
        }
    }

    public static Map<String, NewCustomItem> getCustomItems(){
        return customItems;
    }

    @SuppressWarnings({"rawtypes"})
    private void registerItemJsonRecipes(File itemFile){
        try {
            if (itemFile.isDirectory()) return;
            Object jsonObject = new JSONParser().parse(new FileReader(itemFile));
            JSONObject itemJson = (JSONObject) jsonObject;

            Map itemSection = (Map) itemJson.get("ITEM");
            if (itemSection == null) return;

            String fileName = itemFile.getName().replaceAll(".json$", "").replaceAll("\\s", "_");
            if (new Item().getByName(fileName.toLowerCase()) != null){
                return;
            }

            String requires = (String) itemSection.get("requires");
            if (itemSection.get("requires") != null){
                if (requires != null) {
                    String[] require = requires.split(",");
                    for (String req : require) {
                        for (File file : PluginFiles.getItemFiles())
                            if (req.equalsIgnoreCase(file.getName().trim().replace(".json", "")))
                                registerItemJsonRecipes(file);
                        if (new Item().getByName(req) == null){
                            if (!files3.containsValue(itemFile)) {
                                files3.put(fileName, itemFile);
                            }
                        }
                    }
                }
            }

            NewCustomItem customItem = ItemParser.parseItem(fileName);

            recipe(itemSection, customItem);

        }catch (IOException | ParseException | IllegalArgumentException | NullPointerException e){
            if (debug) getLogger().warning("Error: "+itemFile.getName());
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void recipe(Map itemSection, NewCustomItem customItem){
        if (itemSection.containsKey("smoking"))
            smoking(itemSection, customItem, "smoking");
        if (itemSection.containsKey("campfire"))
            smoking(itemSection, customItem, "campfire");
        if (itemSection.containsKey("furnace"))
            smoking(itemSection, customItem, "furnace");
        if (itemSection.containsKey("blasting"))
            smoking(itemSection, customItem, "blasting");
        if (itemSection.containsKey("merchant"))
            merchant(itemSection, customItem);
        if (itemSection.containsKey("shaped"))
            shaped(itemSection, customItem);
        if (itemSection.containsKey("shapeless"))
            shapeless(itemSection, customItem);
        if (itemSection.containsKey("smithing"))
            smithing(itemSection, customItem);
        if (itemSection.containsKey("stonecutting"))
            stonecutting(itemSection, customItem);
    }

    private void stonecutting(Map<String, Object> itemSection, NewCustomItem customItem){
        Map<String, Object> map = (Map<String, Object>) itemSection.get("stonecutting");

        RecipeInput input = null;
        if (Material.matchMaterial((String) map.get("input")) != null)
            input = new MaterialInput().setItem(Material.matchMaterial((String) map.get("input")));
        else if (new Item().getByName(((String) map.get("input")).toLowerCase()) != null)
            input = new ItemStackInput().setItem(new Item().getByName(((String) map.get("input")).toLowerCase()).build().getItemStack());
        else {
            getLogger().warning(String.format("Invalid input %s for %s for stonecutting recipe", map.get("input"), customItem.getName()));
            return;
        }

        customItem.setRecipe(new StonecuttingRecipe(input, customItem.build().getItemStack()));
        Bukkit.getServer().addRecipe(customItem.getRecipe().getRecipe(new NamespacedKey(plugin, customItem.getName()+"stonecutting")));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void smithing(Map<String, ?> itemSection, NewCustomItem customItem){
        Map<String, Object> map = (Map<String, Object>) itemSection.get("smithing");

        RecipeInput input = null;
        if (Material.matchMaterial((String) map.get("input")) != null)
            input = new MaterialInput().setItem(Material.matchMaterial((String) map.get("input")));
        else if (new Item().getByName(((String) map.get("input")).toLowerCase()) != null)
            input = new ItemStackInput().setItem(new Item().getByName(((String) map.get("input")).toLowerCase()).build().getItemStack());
        else {
            getLogger().warning(String.format("Invalid input %s for %s for smithing recipe", map.get("input"), customItem.getName()));
            return;
        }

        RecipeInput addition = null;
        if (Material.matchMaterial((String) map.get("addition")) != null)
            addition = new MaterialInput().setItem(Material.matchMaterial((String) map.get("addition")));
        else if (new Item().getByName(((String) map.get("addition")).toLowerCase()) != null)
            addition = new ItemStackInput().setItem(new Item().getByName(((String) map.get("addition")).toLowerCase()).build().getItemStack());
        else {
            getLogger().warning(String.format("Invalid addition %s for %s for smithing recipe", map.get("addition"), customItem.getName()));
            return;
        }

        customItem.setRecipe(new SmithingRecipe(input, addition, customItem.build().getItemStack()));
        Bukkit.getServer().addRecipe(customItem.getRecipe().getRecipe(new NamespacedKey(plugin, customItem.getName()+"smithing")));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void shapeless(Map<String, ?> itemSection, NewCustomItem customItem){
        Map<String, Object> map = (Map<String, Object>) itemSection.get("shapeless");

        JSONArray ingredientsJson = (JSONArray) map.get("ingredients");
        List<RecipeInput> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientsJson.size(); i++)
            if (Material.matchMaterial(String.valueOf(ingredientsJson.get(i))) != null)
                ingredients.add(new MaterialInput().setItem(Material.matchMaterial(String.valueOf(ingredientsJson.get(i)))));
            else if (new Item().getByName(String.valueOf(ingredientsJson.get(i)).toLowerCase()) != null)
                ingredients.add(new ItemStackInput().setItem(new Item().getByName(String.valueOf(ingredientsJson.get(i)).toLowerCase()).build().getItemStack()));
            else {
                getLogger().warning(String.format("Invalid ingredient %s for %s for shapeless recipe", ingredients.get(i), customItem.getName()));
                return;
            }

        customItem.setRecipe(new ShapelessRecipe(ingredients, customItem.build().getItemStack()));
        Bukkit.getServer().addRecipe(customItem.getRecipe().getRecipe(new NamespacedKey(plugin, customItem.getName()+"shaped")));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void shaped(@NotNull Map<String, ?> itemSection, NewCustomItem customItem){
        Map<String, Object> map = (Map<String, Object>) itemSection.get("shaped");

        JSONArray shapeJson = (JSONArray) map.get("shape");
        String[] shape = new String[3];
        for (int i = 0; i < shapeJson.size(); i++)
            shape[i] = shapeJson.get(i).toString();

        Map<String, String> ingredientsJsonMap = (Map<String, String>) map.get("ingredients");
        Map<Character, RecipeInput> ingredientsMap = new HashMap<>();
        for (String key : ingredientsJsonMap.keySet()) {
            if (Material.matchMaterial(ingredientsJsonMap.get(key)) != null)
                ingredientsMap.put(key.charAt(0), new MaterialInput().setItem(Material.matchMaterial(ingredientsJsonMap.get(key))));
            else if (new Item().getByName(ingredientsJsonMap.get(key).toLowerCase()) != null)
                ingredientsMap.put(key.charAt(0), new ItemStackInput().setItem(new Item().getByName(ingredientsJsonMap.get(key).toLowerCase()).build().getItemStack()));
            else {
                getLogger().warning(String.format("Invalid ingredient %s for %s for shaped recipe", ingredientsMap.get(key), customItem.getName()));
                return;
            }
        }
        customItem.setRecipe(new ShapedRecipe(shape, customItem.build().getItemStack(), ingredientsMap));
        Bukkit.getServer().addRecipe(customItem.getRecipe().getRecipe(new NamespacedKey(plugin, customItem.getName()+"shaped")));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void merchant(Map<String, ?> itemSection, NewCustomItem customItem){
        Map<String, Object> map = (Map<String, Object>) itemSection.get("merchant");

        List<RecipeInput> input = new ArrayList<>();

        JSONArray ingredients = (JSONArray) map.get("ingredients");
        for (Object ingredient : ingredients) {
            if (Material.matchMaterial(ingredient.toString()) != null)
                input.add(new MaterialInput().setItem(Material.matchMaterial((String) ingredient)));
            else if (new Item().getByName(((String) ingredient).toLowerCase()) != null)
                input.add(new ItemStackInput().setItem(new Item().getByName(((String) ingredient).toLowerCase()).build().getItemStack()));
            else {
                getLogger().warning(String.format("Invalid ingredient %s for %s for merchant recipe", ingredient, customItem.getName()));
                return;
            }
        }

        int uses = Integer.parseInt(String.valueOf(map.get("uses")));
        int maxUses = Integer.parseInt(String.valueOf(map.get("maxUses")));
        boolean experienceReward = (boolean) map.get("experienceReward");
        int villagerExperience = Integer.parseInt(String.valueOf(map.get("villagerExperience")));
        float priceMultiplier = Float.parseFloat(String.valueOf(map.get("priceMultiplier")));
        int demand = Integer.parseInt(String.valueOf(map.get("demand")));
        int specialPrice = Integer.parseInt(String.valueOf(map.get("specialPrice")));
        boolean ignoreDiscounts = (boolean) map.get("ignoreDiscounts");

        customItem.setRecipe(new MerchantRecipe(input, customItem.build().getItemStack(), uses, maxUses,experienceReward, villagerExperience, priceMultiplier, demand, specialPrice, ignoreDiscounts));
        Bukkit.getServer().addRecipe(customItem.getRecipe().getRecipe(new NamespacedKey(plugin, customItem.getName()+"merchant")));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void smoking(Map<String, ?> itemSection, NewCustomItem customItem, String recipeType){
        Map<String, Object> map = (Map<String, Object>) itemSection.get(recipeType);

        RecipeInput inputChoice = null;
        if (Material.matchMaterial((String) map.get("input")) != null)
            inputChoice = new MaterialInput().setItem(Material.matchMaterial((String) map.get("input")));
        else if (new Item().getByName(((String) map.get("input")).toLowerCase()) != null)
            inputChoice = new ItemStackInput().setItem(new Item().getByName(((String) map.get("input")).toLowerCase()).build().getItemStack());
        else  {
            getLogger().warning(String.format("Invalid input %s for %s for %s", map.get("input"), customItem.getName(), recipeType));
            return;
        }



        float experience = Float.parseFloat(String.valueOf(map.get("experience")));
        int cookingTime = Integer.parseInt(String.valueOf(map.get("cookingTime")));

        switch (recipeType) {
            case "smoking" -> customItem.setRecipe(new SmokingRecipe(inputChoice, customItem.getItemStack(), experience, cookingTime));
            case "campfire" -> customItem.setRecipe(new CampfireRecipe(inputChoice, customItem.getItemStack(), experience, cookingTime));
            case "furnace" -> customItem.setRecipe(new FurnaceRecipe(inputChoice, customItem.getItemStack(), experience, cookingTime));
            case "blasting" -> customItem.setRecipe(new BlastingRecipe(inputChoice, customItem.getItemStack(), experience, cookingTime));
        }
        Bukkit.getServer().addRecipe(customItem.getRecipe().getRecipe(new NamespacedKey(plugin, customItem.getName()+recipeType)));
    }

    public void registerGlow(){
        glowKey = new NamespacedKey(plugin, "glow");
        glow = new Glow(glowKey);
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            if (debug)
                e.printStackTrace();
        }
        try {
            if (Enchantment.getByKey(glowKey) == null)
                Enchantment.registerEnchantment(glow);
        } catch (Exception e) {
            if (debug)
                e.printStackTrace();
        }
    }

    static Map<String, Integer> chances = new HashMap<>();

    public static void loadConfig() {
        customOreWorld = plugin.getConfig().getString("CustomOresWorld");
        chances.put(Material.COAL_ORE.name(), plugin.getConfig().getInt(Material.COAL_ORE.name()));
        chances.put(Material.DIAMOND_ORE.name(), plugin.getConfig().getInt(Material.DIAMOND_ORE.name()));
        chances.put(Material.EMERALD_ORE.name(), plugin.getConfig().getInt(Material.EMERALD_ORE.name()));
        chances.put(Material.LAPIS_ORE.name(), plugin.getConfig().getInt(Material.LAPIS_ORE.name()));
        chances.put(Material.REDSTONE_ORE.name(), plugin.getConfig().getInt(Material.REDSTONE_ORE.name()));
        chances.put(Material.IRON_ORE.name(), plugin.getConfig().getInt(Material.IRON_ORE.name()));
        chances.put(Material.GOLD_ORE.name(), plugin.getConfig().getInt(Material.GOLD_ORE.name()));
        chances.put(Material.NETHER_QUARTZ_ORE.name(), plugin.getConfig().getInt(Material.NETHER_QUARTZ_ORE.name()));
        chances.put(Material.COPPER_ORE.name(), plugin.getConfig().getInt(Material.COPPER_ORE.name()));
        debug = plugin.getConfig().getBoolean("IncreasedDebugging");
    }

    private static void saveLocalConfig(){
        for (String s : chances.keySet()) {
            plugin.getConfig().set(s, chances.get(s));
        }
        plugin.getConfig().set("IncreasedDebugging", debug);
    }

    public static Map<String, Integer> getChances(){
        return chances;
    }

    static Map<String, NamespacedKey> ores = new HashMap<>();

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
        ores.put(Material.COAL_ORE.name(), phosphorusKey);
        ores.put(Material.DIAMOND_ORE.name(), janeliteKey);
        ores.put(Material.EMERALD_ORE.name(), ellendyteKey);
        ores.put(Material.LAPIS_ORE.name(), sapphireKey);
        ores.put(Material.REDSTONE_ORE.name(), tungstenKey);
        ores.put(Material.IRON_ORE.name(), jolixanineKey);
        ores.put(Material.GOLD_ORE.name(), corinthiumKey);
        ores.put(Material.NETHER_QUARTZ_ORE.name(), zincKey);
        ores.put(Material.COPPER_ORE.name(), kaylaxKey);
    }

    public static Map<String, NamespacedKey> getOres(){return ores;}

    static Map<Material, FoodDetails> foodMap = new HashMap<>();

    void registerFoods(){
        Map<PotionEffect, Boolean> map = new HashMap<>();
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
                new ArrayList<>(List.of(new PotionEffect(PotionEffectType.POISON, 100, 0)))));
        foodMap.put(Material.POTATO, new FoodDetails(1, 0.6f));
        foodMap.put(Material.PUFFERFISH, new FoodDetails(1, 0.2f,
                new ArrayList<>(Arrays.asList(new PotionEffect(PotionEffectType.HUNGER, 300, 2),
                        new PotionEffect(PotionEffectType.CONFUSION, 300, 0),
                        new PotionEffect(PotionEffectType.POISON, 1200, 1)))));
        foodMap.put(Material.PUMPKIN_PIE, new FoodDetails(8, 4.8f));
        foodMap.put(Material.RABBIT_STEW, new FoodDetails(10, 12));
        foodMap.put(Material.BEEF, new FoodDetails(3, 1.8f));
        foodMap.put(Material.CHICKEN, new FoodDetails(2, 1.2f,
                new ArrayList<>(List.of(new PotionEffect(PotionEffectType.HUNGER, 600, 0)))));
        foodMap.put(Material.COD, new FoodDetails(2, 0.4f));
        foodMap.put(Material.MUTTON, new FoodDetails(2, 1.2f));
        foodMap.put(Material.PORKCHOP, new FoodDetails(3, 1.8f));
        foodMap.put(Material.RABBIT, new FoodDetails(3, 1.8f));
        foodMap.put(Material.SALMON, new FoodDetails(2, 0.4f));
        foodMap.put(Material.ROTTEN_FLESH, new FoodDetails(4, 0.8f,
                new ArrayList<>(List.of(new PotionEffect(PotionEffectType.HUNGER, 600, 0)))));
        foodMap.put(Material.SPIDER_EYE, new FoodDetails(2, 3.2f,
                new ArrayList<>(List.of(new PotionEffect(PotionEffectType.POISON, 100, 0)))));
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

    public static Runnable checkForActiveInventories(){
        return () -> {
            for (Location loc : newActiveInventories.keySet()){
                if (!newActiveInventories.get(loc).getViewers().isEmpty())
                    continue;
                newActiveInventories.remove(loc);
            }
        };
    }
}
