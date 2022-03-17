package com.temmy.json_items_test_1.util;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.Attribute;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class data {
    static DataSource dataSource;
    static Connection conn;
    static Logger log = Main.getPlugin().getLogger();
    static Map<org.bukkit.attribute.Attribute, Integer> vanillaAttributes = new HashMap<>();

    private static void testDatabaseConnection(DataSource dataSource) {
        try {
            if (conn != null && conn.isValid(1)) {
                return;
            }else if (conn == null || !conn.isValid(1)) {
                conn = dataSource.getConnection();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }

    public static void test(){
        dataSource = Main.getDataSource();
        //getDatabaseItem();
        customAttributes(checkCustomAttributesDatabase());
        enchants(checkEnchantmentTable());
        vanillaAttributes(checkVanillaAttributes());
    }

    static Map<String, CustomItem> dbCustomitems = Main.getCustomItems();

    static void getDatabaseItem(){

        CustomItem item;
        try {
            testDatabaseConnection(dataSource);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ID, UniqueName, ItemName, Material, Lore, Model FROM item;");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                item = new CustomItem(resultSet.getString("UniqueName")).
                        setStringItemName(resultSet.getString("ItemName")).
                        setMaterial(Material.valueOf(resultSet.getString("Material"))).
                        setStringLore(resultSet.getString("Lore")).
                        setModel(resultSet.getInt("Model")).
                        setItem_ID(resultSet.getInt("ID"));
                dbCustomitems.put(item.getUniqueName(), item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void getDatabaseItemEnchants(CustomItem item){
        try {
            testDatabaseConnection(dataSource);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM item_enchants WHERE item_id = ?");
            stmt.setInt(1, item.getItemID());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                int i = 0;
                log.info(resultSet.getString(1));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean createCustomItemInDatabase(CustomItem item){

        String currentItem = "";
        try {
            testDatabaseConnection(dataSource);
            for (String key : Main.getDb_customItems_OLDv2().keySet()){
                if (dbCustomitems.containsKey(item.getUniqueName())) continue;
                currentItem = key;
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO item(UniqueName, ItemName, Material, Lore, model) VALUES(?,?,?,?,?);SELECT LAST_INSERT_ID");
                stmt.setString(1, item.getUniqueName());
                stmt.setString(2, LegacyComponentSerializer.legacy('&').serialize(item.getItemName()));
                stmt.setString(3, item.getMaterial().name());
                stmt.setString(4, item.getStringLore());
                stmt.setInt(5, item.getModel());
                stmt.execute();
                ResultSet rs = stmt.getGeneratedKeys();
                item.setItem_ID(rs.getInt(1));
            }
        } catch (SQLException e) {
            if (Main.debug)
                log.log(Level.WARNING, String.format("Unable to register %s",currentItem),e);
            else
                log.warning(String.format("Unable to register %s in database", currentItem));
        }
        return false;
    }

    @SuppressWarnings("RedundantCollectionOperation")
    static List<String> checkCustomAttributesDatabase(){
        List<String> attributes = new ArrayList<>();
        try {
            testDatabaseConnection(dataSource);
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM custom_attributes;");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                attributes.add(resultSet.getString("name"));
            }
            for (String s : attributes){
                log.severe(String.format("attributes: s: --> %s", s));
            }
            for (String s : Attribute.getAttributeMethods()){
                log.severe(String.format("AttributeMethods: s: --> %s", s));
                if (attributes.contains(s)){
                    attributes.remove(s);
                }else {
                    attributes.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    static void customAttributes(List<String> attributes){
        String currentCustomAttribute = "";
        try {
            testDatabaseConnection(dataSource);
            for (String key : attributes){
                currentCustomAttribute = key;
                log.warning(key);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO custom_attributes(name) VALUES(?)");
                stmt.setString(1, key);
                stmt.execute();
            }
        } catch (SQLException e) {
            if (Main.debug)
                log.log(Level.WARNING, String.format("Unable to insert %s custom attribute into 'custom_attributes' table", currentCustomAttribute), e);
            else
                log.warning(String.format("Unable to save %s attribute in database", currentCustomAttribute));
        }
    }

    static Map<String, Integer> checkEnchantmentTable(){
        Map<String, Integer> enchantList = new HashMap<>();
        try {
            testDatabaseConnection(dataSource);
            PreparedStatement stmt = conn.prepareStatement("SELECT name  FROM enchantments;");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                enchantList.put(resultSet.getString("name"), 0);
            }
            for (Enchantment enchant : Enchantment.values()){
                if (enchantList.containsKey(enchant.getKey().getKey()))
                    enchantList.remove(enchant.getKey().getKey());
                else
                    enchantList.put(enchant.getKey().getKey(), enchant.getMaxLevel());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enchantList;
    }

    static void enchants(Map<String, Integer> enchantList){
        String currentEnchant ="";
        try {
            testDatabaseConnection(dataSource);
            for (String enchant : enchantList.keySet()){
                currentEnchant = enchant;
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO enchantments(name, max_level) VALUES(?,?)");
                stmt.setString(1, enchant);
                stmt.setInt(2, enchantList.get(enchant));
                stmt.execute();
            }
        } catch (SQLException e) {
            if (Main.debug)
                log.log(Level.WARNING, String.format("Unable to inset %s enchant into 'enchantments' table", currentEnchant), e);
            else
                log.warning(String.format("Unable to save %s enchant in database", currentEnchant));
        }
    }

    @SuppressWarnings("RedundantCollectionOperation")
    static List<String> checkVanillaAttributes(){
        List<String> attributes = new ArrayList<>();
        try {
            testDatabaseConnection(dataSource);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id,name FROM vanilla_attributes;");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                attributes.add(resultSet.getString("name"));
                vanillaAttributes.put(org.bukkit.attribute.Attribute.valueOf(resultSet.getString("name")), resultSet.getInt("id"));
            }
            for (org.bukkit.attribute.Attribute attribute : org.bukkit.attribute.Attribute.values()){
                if (attributes.contains(attribute.name()))
                    attributes.remove(attribute.name());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    static void vanillaAttributes(List<String> attributes){
        String currentAttribute = "";
        try {
            testDatabaseConnection(dataSource);
            for (String attribute : attributes){
                currentAttribute = attribute;
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO vanilla_attributes(name) VALUES(?)");
                stmt.setString(1, attribute);
                stmt.execute();
            }
        } catch (SQLException e) {
            if (Main.debug)
                log.log(Level.WARNING, String.format("Unable to inset %s vanilla attribute into 'vanilla_attributes' table", currentAttribute), e);
            else
                log.warning(String.format("Unable to save %s vanilla attribute in database", currentAttribute));
        }
    }

    static void itemVanillaAttributes(CustomItem item){
        VanillaAttributes vanillaAttributes = null;
        try {
            testDatabaseConnection(dataSource);
            for (VanillaAttributes attribute : item.getVanillaAttributes()){
                vanillaAttributes = attribute;
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO item_vanilla_attributes(item_id,attribute_id, amount, slot) VALUES(?,?,?)");
                stmt.setInt(1, item.getItemID());
                stmt.setInt(2, data.vanillaAttributes.get(vanillaAttributes.attribute));
                stmt.setDouble(3, attribute.value);
                stmt.setString(4, attribute.slot.name());
            }
        }catch (SQLException e){
            if (Main.debug)
                log.log(Level.WARNING, String.format("Unable to register %s's attribute %s", item.uniqueName, vanillaAttributes.attribute.name()), e);
            else
                log.warning(String.format("Unable to register %s attributes", item.uniqueName));
        }
    }

    static void ingredients(){

    }

    void itemCustomAttributes(){

    }

    void itemEnchants(){

    }

    void recipe(){

    }

}
