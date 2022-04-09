package com.temmy.json_items_test_1;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.temmy.json_items_test_1.Parser.ItemParser;
import com.temmy.json_items_test_1.command.*;
import com.temmy.json_items_test_1.file.PluginFiles;
import com.temmy.json_items_test_1.listener.*;
import com.temmy.json_items_test_1.util.*;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

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
    private static Map<String, ItemStack> customItems_OLD = new HashMap<>();
    private static Map<String, CustomItem> db_customItems_OLDv2 = new HashMap<>();
    private static Map<String, CustomItem> customItems = new HashMap<>();
    public static boolean debug;
    public static String customOreWorld;
    public static Boolean worldGuardEnabled = false;
    public static WorldGuard worldGuard = null;
    public static StateFlag attributesEnabledFlag;
    public static Map<Player, UUID[]> allies = new HashMap<>();
    public static Map<Player, Boolean> activeEditors;
    public static NamespacedKey glowKey;
    public static Glow glow;
    public static Map<Location, ActiveInventory> newActiveInventories = new HashMap<>();
    static DataSource dataSource;
    Database database;
    Connection conn;

    public static WorldGuard getWorldGuard(){
        return worldGuard;
    }

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

    @Override
    public void onEnable() {
        registerGlow();
        /*loadDatabase();
        try {
            dataSource = initDataBase();
            initDb();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }*/
        new Thread(this::registerFoods).start();
        registerEvents(getServer());
        PluginFiles.init();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        loadConfig();

        getLogger().info("Registering Custom Items.");
        for (File item : PluginFiles.getItemFiles())
            registerItemJsonRecipes(item);
        for (String s : files3.keySet()) {
            registerItemJsonRecipes(files3.get(s));
            l.add(s);
        }
        for (String s : l)
            files3.remove(s);
        //data.test();
        //createCustomItemInDatabase_OLD();
        getLogger().info(String.format("Registered %s items.", customItems_OLD.size()));
        getCommand("giveitem").setExecutor(new GiveItem());
        getCommand("giveitem").setTabCompleter(new GiveItemTabCompleter());
        getCommand("reloadores").setExecutor(new Reload());
        getCommand("debugging").setExecutor(new Debugging());
        getCommand("triedump").setExecutor(new trieDump());
        ores();
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
        server.getPluginManager().registerEvents(new AsyncChatListener(), this);
        server.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        server.getPluginManager().registerEvents(new InventoryOpenListener(), this);
        server.getPluginManager().registerEvents(new InventoryMoveItemListener(), this);
    }

    public void onDisable(){
        try {
            if (conn != null && conn.isValid(1))
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        saveLocalConfig();
    }

    public static Map<String, ItemStack> getCustomItems_OLD(){return customItems_OLD;}

    public static Map<String, CustomItem> getDb_customItems_OLDv2(){return db_customItems_OLDv2;}

    public static Map<String, CustomItem> getCustomItems(){
        return customItems;
    }

    Map<String, File> files3 = new HashMap<>();
    List<String> l = new ArrayList<>();

    private void registerMySQLRecipes(){

    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "ConstantConditions"})
    private void registerItemJsonRecipes(File itemFile){
        try {
            if (itemFile.isDirectory()) return;
            Object jsonObject = new JSONParser().parse(new FileReader(itemFile));
            JSONObject itemJson = (JSONObject) jsonObject;

            Map itemSection = (Map) itemJson.get("ITEM");
            if (itemSection == null) return;

            Map<String, String> ingredients = (Map) itemSection.get("ingredients");
            JSONArray recipe = (JSONArray) itemSection.get("recipe");
            String fileName = itemFile.getName().replaceAll(".json$", "").replaceAll("\\s", "_");
            if (customItems_OLD.containsKey(fileName.toLowerCase())) {
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
                        if (!(customItems_OLD.containsKey(req))) {
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
                    else if (customItems_OLD.containsKey(ingredients.get(key).toLowerCase())){
                        shapedRecipe.setIngredient(key.charAt(0), customItems_OLD.get(ingredients.get(key).toLowerCase()));
                    }
                }
                if (Bukkit.getRecipe(new NamespacedKey(Main.getPlugin(), fileName)) == null) {
                    if (debug)
                        getLogger().info("Registering "+fileName);
                    Bukkit.addRecipe(shapedRecipe);
                }
            }
            customItems_OLD.put(fileName.toLowerCase(), item);

        }catch (IOException | ParseException | IllegalArgumentException | NullPointerException e){
            if (debug) getLogger().warning("Error: "+itemFile.getName());
            e.printStackTrace();
        }
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

    void loadDatabase(){
        database = new Database(plugin.getConfig().getString("database.host"),
                plugin.getConfig().getInt("database.port"), plugin.getConfig().getString("database.database"),
                plugin.getConfig().getString("database.user"), plugin.getConfig().getString("database.password"));
        getLogger().info(database.host);
        getLogger().info(String.valueOf(database.port));
        getLogger().info(database.databaseName);
        getLogger().info(database.user);
        getLogger().info(database.password);
    }

    public DataSource initDataBase() throws SQLException {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(database.host);
        dataSource.setPortNumber(database.port);
        dataSource.setDatabaseName(database.databaseName);
        dataSource.setUser(database.user);
        dataSource.setPassword(database.password);
        testDataSource(dataSource);
        return dataSource;
    }

    private void testDataSource(DataSource dataSource) {
        try {
            if (conn != null && conn.isValid(1)) {
                return;
            }else if (conn == null || !conn.isValid(1)) {
                conn = dataSource.getConnection();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn = dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    private boolean createCustomItemInDatabase_OLD(){
        try {
            testDataSource(dataSource);
            for (String key : db_customItems_OLDv2.keySet()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO CustomItems(UniqueName, ItemName, Material, Model, Damage, DamageSlot, Health, HealthSlot, " +
                                "AttackSpeed, AttackSpeedSlot, Enchants, Unbreakable, Lore, Attributes, Requires, Ingredients, " +
                                "Recipe) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                );
                test(stmt, db_customItems_OLDv2.get(key));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private PreparedStatement test(PreparedStatement stmt, CustomItem item){
        try {
            stmt.setObject(1, item.getUniqueName());
            if (item.getItemName() != null)stmt.setObject(2, LegacyComponentSerializer.legacy('&').serialize(item.getItemName())); else stmt.setObject(2, null);
            stmt.setObject(3, item.getMaterial().name());
            if (item.getModel() != null)stmt.setObject(4, item.getModel()); else stmt.setObject(4, null);
            if (item.getDamage() != null)stmt.setObject(5, item.getDamage()); else stmt.setObject(5, null);
            if (item.getDamageSlot() != null)stmt.setObject(6, item.getDamageSlot().name()); else stmt.setObject(6, null);
            if (item.getHealth() != null)stmt.setObject(7, item.getHealth()); else stmt.setObject(7, null);
            if (item.getHealthSlot() != null)stmt.setObject(8, item.getHealthSlot().name()); else stmt.setObject(8, null);
            if (item.getAttackSpeed() != null)stmt.setObject(9, item.getAttackSpeed()); else stmt.setObject(9, null);
            if (item.getAttackSpeedSlot() != null)stmt.setObject(10, item.getAttackSpeedSlot().name()); else stmt.setObject(10, null);
            if (item.getStringEnchants() != null)stmt.setObject(11, item.getStringEnchants()); else stmt.setObject(11, null);
            if (item.isUnbreakable() != null)stmt.setObject(12, item.isUnbreakable()); else stmt.setObject(12, null);
            if (item.getStringLore() != null)stmt.setObject(13, item.getStringLore()); else stmt.setObject(13, null);
            if (item.getAttributes() != null)stmt.setObject(14, item.getAttributes()); else stmt.setObject(14, null);
            if (item.getRequires() != null)stmt.setObject(15, item.getRequires()); else stmt.setObject(15, null);
            if (item.getStringIngredients() != null)stmt.setObject(16, item.getStringIngredients()); else stmt.setObject(16, null);
            if (item.getStringRecipe() != null)stmt.setObject(17, item.getStringRecipe()); else stmt.setObject(17, null);
            stmt.execute();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Unable to prepare SQL statement to save Custom item to database", e);
        }
        return stmt;
    }

    @SuppressWarnings("ConstantConditions")
    private void initDb() throws IOException {
        String setup;
        try (InputStream in = getClassLoader().getResourceAsStream("dbsetup.sql")){
            setup = new String(in.readAllBytes());
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not read db setup file.",e);
            throw e;
        }

        String[] queries = setup.split(";");
        for (String query : queries){
            if (query.isBlank()) continue;
            try {
                testDataSource(dataSource);
                if (conn == null)
                    throw new SQLException("Unable to connect to database");
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.execute();
            } catch (SQLException e) {
                getLogger().severe(query);
                e.printStackTrace();
            }
        }
        getLogger().info("Database setup complete.");
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
}
