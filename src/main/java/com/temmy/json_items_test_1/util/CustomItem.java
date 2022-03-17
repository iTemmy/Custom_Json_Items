package com.temmy.json_items_test_1.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItem {
    String uniqueName = null;
    Component itemName = null;
    Material material = null;
    Integer model = null;
    Double damage = null;
    EquipmentSlot damageSlot = null;
    Double health = null;
    EquipmentSlot healthSlot = null;
    Double attackSpeed = null;
    EquipmentSlot attackSpeedSlot = null;
    Map<Enchantment, Integer> enchants = new HashMap<>();
    Boolean unbreakable = false;
    List<Component> lore = new ArrayList<>();
    String attributes = "";
    String requires = "";
    Map<Character, Material> materialIngredients = new HashMap<>();
    Map<Character, ItemStack> itemstackIngredients = new HashMap<>();
    ShapedRecipe recipe = null;
    ItemStack customItem = null;
    int itemID;

    /**
     * @Deprecated
     * Creates new Custom items with the variables provided
     * @param itemName
     * @param material
     * @param model
     * @param damage
     * @param damageSlot
     * @param health
     * @param healthSlot
     * @param attackSpeed
     * @param attackSpeedSlot
     * @param enchants
     * @param unbreakable
     * @param lore
     * @param attributes
     * @param requires
     * @param itemstackIngredients
     * @param materialIngredients
     * @param recipe
     */
    @Deprecated
    public CustomItem(@NotNull String uniqueName, @Nullable Component itemName, @NotNull Material material, @Nullable Integer model, @Nullable Double damage, @Nullable EquipmentSlot damageSlot,
                      @Nullable Double health, @Nullable EquipmentSlot healthSlot, @Nullable Double attackSpeed, @Nullable EquipmentSlot attackSpeedSlot,
                      @Nullable Map<Enchantment, Integer> enchants, @Nullable Boolean unbreakable, @Nullable List<Component> lore, @Nullable String attributes,
                      @Nullable String requires, @Nullable Map<Character, ItemStack> itemstackIngredients, @Nullable Map<Character, Material> materialIngredients, @Nullable ShapedRecipe recipe) {
        this.uniqueName = uniqueName;
        if (itemName != null)this.itemName = itemName;
        this.material = material;
        if (model != null)this.model = model;
        if (damage != null)this.damage = damage;
        if (damageSlot != null)this.damageSlot = damageSlot;
        if (health != null)this.health = health;
        if (healthSlot != null)this.healthSlot = healthSlot;
        if (attackSpeed != null)this.attackSpeed = attackSpeed;
        if (attackSpeedSlot != null)this.attackSpeedSlot = attackSpeedSlot;
        if (enchants != null)this.enchants = enchants;
        if (unbreakable != null)this.unbreakable = unbreakable;
        if (lore != null)this.lore = lore;
        if (attributes != null)this.attributes = attributes;
        if (requires != null)this.requires = requires;
        if (itemstackIngredients != null)this.itemstackIngredients = itemstackIngredients;
        if (materialIngredients != null)this.materialIngredients = materialIngredients;
        if (recipe != null)this.recipe = recipe;
    }

    /**
     * @Deprecated
     * @param itemName
     * @param material
     * @param lore
     * @param attributes
     */
    @Deprecated
    public CustomItem(@NotNull Component itemName, @NotNull Material material, @Nullable List<Component> lore, @Nullable String attributes){
        this.itemName = itemName;
        this.material = material;
        this.lore = lore;
        this.attributes = attributes;
    }

    /**
     * @Deprecated
     * Internal testing only
     */
    @Deprecated
    public CustomItem(){

    }

    public CustomItem(String uniqueName){
        this.uniqueName = uniqueName;
    }

    public String getUniqueName(){
        return uniqueName;
    }

    public CustomItem setUniqueName(String uniqueName){
        Validate.notNull(uniqueName);
        this.uniqueName = uniqueName;
        return this;
    }

    public CustomItem setStringItemName(String stringItemName){
        Validate.notNull(stringItemName);
        itemName = LegacyComponentSerializer.legacy('&').deserialize(stringItemName);
        return this;
    }

    public CustomItem setComponentItemName(Component comp){
        Validate.notNull(comp);
        itemName = comp;
        return this;
    }

    public CustomItem setMaterial(Material mat){
        Validate.notNull(mat);
        material = mat;
        return this;
    }

    public CustomItem setModel(int model){
        Validate.notNull(model);
        this.model = model;
        return this;
    }

    public CustomItem setDamage(double damage){
        Validate.notNull(damage);
        this.damage = damage;
        return this;
    }

    public CustomItem setDamageSlot(EquipmentSlot slot){
        Validate.notNull(slot);
        damageSlot = slot;
        return this;
    }

    public CustomItem setHealth(double health){
        Validate.notNull(health);
        this.health = health;
        return this;
    }

    public CustomItem setHealthSlot(EquipmentSlot slot){
        Validate.notNull(slot);
        healthSlot = slot;
        return this;
    }

    public CustomItem setAttackSpeed(double attackSpeed){
        Validate.notNull(attackSpeed);
        this.attackSpeed = attackSpeed;
        return this;
    }

    public CustomItem setAttackSpeedSlot(EquipmentSlot slot){
        Validate.notNull(slot);
        attackSpeedSlot = slot;
        return this;
    }

    public CustomItem setEnchants(Map<Enchantment, Integer> enchants){
        Validate.notNull(enchants);
        this.enchants = enchants;
        return this;
    }

    public CustomItem setUnbreakable(boolean unbreakable){
        Validate.notNull(unbreakable);
        this.unbreakable = unbreakable;
        return this;
    }

    public CustomItem setStringLore(List<String> stringLore){
        Validate.notNull(stringLore);
        List<Component> lore = new ArrayList<>();
        for (String string : stringLore)
            lore.add(LegacyComponentSerializer.legacy('&').deserialize(string));
        this.lore = lore;
        return this;
    }

    public CustomItem setStringLore(String lore){
        Validate.notNull(lore);
        String[] s = lore.split(";");
        List<Component> compLore = new ArrayList<>();
        for (String ss : s){
            compLore.add(LegacyComponentSerializer.legacy('&').deserialize(ss));
        }
        this.lore = compLore;
        return this;
    }

    public CustomItem setComponentLore(List<Component> lore){
        Validate.notNull(lore);
        this.lore = lore;
        return this;
    }

    public CustomItem setAttributes(String attributes){
        Validate.notNull(attributes);
        this.attributes = attributes;
        return this;
    }

    public CustomItem setRequires(String requires){
        Validate.notNull(requires);
        this.requires = requires;
        return this;
    }

    public CustomItem setItemstackIngredients(Map<Character, ItemStack> itemstackIngredients){
        Validate.notNull(itemstackIngredients);
        this.itemstackIngredients = itemstackIngredients;
        return this;
    }

    public CustomItem setMaterialIngredients(Map<Character, Material> materialIngredients){
        Validate.notNull(materialIngredients);
        this.materialIngredients = materialIngredients;
        return this;
    }

    public Component getItemName() {
        return itemName;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getModel() {
        return model;
    }

    public Double getDamage() {
        return damage;
    }

    public EquipmentSlot getDamageSlot() {
        return damageSlot;
    }

    public Double getAttackSpeed() {
        return attackSpeed;
    }

    public Double getHealth() {
        return health;
    }

    public Map<Character, ItemStack> getItemstackIngredients() {
        return itemstackIngredients;
    }

    public Map<Character, Material> getMaterialIngredients(){
        return materialIngredients;
    }

    public String getRequires() {
        return requires;
    }

    public EquipmentSlot getAttackSpeedSlot() {
        return attackSpeedSlot;
    }

    public EquipmentSlot getHealthSlot() {
        return healthSlot;
    }

    public String getAttributes() {
        return attributes;
    }

    public Boolean isUnbreakable() {
        return unbreakable;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public List<Component> getLore() {
        return lore;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    public String getStringEnchants() {
        StringBuilder t = new StringBuilder();
        for (Enchantment enchant : this.enchants.keySet()){
            t.append(String.format("%s;%d", enchant.getKey().getKey(), this.enchants.get(enchant)));
        }
        return t.toString().trim();
    }

    public String getStringLore(){
        StringBuilder t = new StringBuilder();
        if (lore == null) return null;
        for (Component comp : lore){
            t.append(String.format("\"&s\"",LegacyComponentSerializer.legacy('&').serialize(comp)));
        }
        return t.toString().trim();
    }

    public String getStringIngredients(){
        StringBuilder t = new StringBuilder();
        for (char character : materialIngredients.keySet())
            t.append(String.format("%s;", materialIngredients.get(character)));
        for (Character character : itemstackIngredients.keySet())
            t.append(String.format("%s;", PlainTextComponentSerializer.plainText().serialize(itemstackIngredients.get(character).displayName())));
        return t.toString().trim();
    }

    public String getStringRecipe(){
        StringBuilder t = new StringBuilder();
        if (recipe == null) return null;
        for (String s : recipe.getShape())
            t.append(String.format("\"%s\"", s));
        return t.toString().trim();
    }

    public void addMaterialIngredient(char character, Material mat){
        materialIngredients.put(character, mat);
    }

    public void addItemStackIngredient(char charater, ItemStack itemStack){
        itemstackIngredients.put(charater, itemStack);
    }

    public CustomItem setRecipe(ShapedRecipe recipe){
        Validate.notNull(recipe);
        this.recipe = recipe;
        return this;
    }

    public List<VanillaAttributes> getVanillaAttributes(){
        List<VanillaAttributes> attributes = new ArrayList<>();
        attributes.add(new VanillaAttributes(Attribute.GENERIC_ATTACK_DAMAGE, damage, damageSlot));
        attributes.add(new VanillaAttributes(Attribute.GENERIC_ATTACK_SPEED, attackSpeed, attackSpeedSlot));
        attributes.add(new VanillaAttributes(Attribute.GENERIC_MAX_HEALTH, health, healthSlot));
        return attributes;
    }

    public ItemStack getCustomItemItemStack(){
        customItem = new ItemStack(material);
        ItemMeta meta = customItem.getItemMeta();
        meta.displayName(itemName);
        customItem.setItemMeta(meta);
        return customItem;
    }

    public CustomItem setItem_ID(int id){
        Validate.notNull(id);
        itemID = id;
        return this;
    }

    public int getItemID(){
        return itemID;
    }
}
