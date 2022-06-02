package com.temmy.json_items_test_1.util.customItems;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.Trie;
import com.temmy.json_items_test_1.util.VanillaAttributes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum RecipeType{
    ShapedRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    FurnaceRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    BlastingRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    CampfireRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    CookingRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    MerchantRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    ShapelessRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    SmithingRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    SmokingRecipe{
        @Override
        void setRecipe(Recipe recipe) {
            super.setRecipe(recipe);
        }
    },
    StonecuttingRecipe{
        @Override
        void setRecipe(Recipe recipe){
            super.setRecipe(recipe);
        }
    };


    void setRecipe(Recipe recipe){
        this.recipe = recipe;
    }
    Recipe recipe;
    Recipe getRecipe(){
        return recipe;
    }
}

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
    RecipeType recipeType;
    ItemStack customItem = null;
    int itemID;

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public CustomItem setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
        return this;
    }

    public void test(){
        org.bukkit.inventory.Recipe recipe = this.recipeType.getRecipe();
        RecipeChoice choice = new RecipeChoice.ExactChoice(new ItemStack(Material.STRUCTURE_VOID, 5));
        this.recipeType.setRecipe(new FurnaceRecipe(new NamespacedKey(Main.getPlugin(), "testrecipe"), new ItemStack(Material.BARRIER), new RecipeChoice.ExactChoice(new ItemStack(Material.STRUCTURE_VOID, 5)), 5f, 5));
    }

    /**
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
        if (recipe != null)this.recipeType.setRecipe(recipe);
    }

    /**
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
        assert uniqueName != null;
        this.uniqueName = uniqueName;
        return this;
    }

    public CustomItem setStringItemName(String stringItemName){
        assert stringItemName != null;
        itemName = LegacyComponentSerializer.legacy('&').deserialize(stringItemName);
        return this;
    }

    public CustomItem setComponentItemName(Component comp){
        assert comp != null;
        itemName = comp;
        return this;
    }

    public CustomItem setMaterial(Material mat){
        assert mat != null;
        material = mat;
        return this;
    }

    public CustomItem setModel(int model){
        this.model = model;
        return this;
    }

    public CustomItem setDamage(double damage){
        this.damage = damage;
        return this;
    }

    public CustomItem setDamageSlot(EquipmentSlot slot){
        assert slot != null;
        damageSlot = slot;
        return this;
    }

    public CustomItem setHealth(double health){
        this.health = health;
        return this;
    }

    public CustomItem setHealthSlot(EquipmentSlot slot){
        assert slot != null;
        healthSlot = slot;
        return this;
    }

    public CustomItem setAttackSpeed(double attackSpeed){
        this.attackSpeed = attackSpeed;
        return this;
    }

    public CustomItem setAttackSpeedSlot(EquipmentSlot slot){
        assert slot != null;
        attackSpeedSlot = slot;
        return this;
    }

    public CustomItem setEnchants(Map<Enchantment, Integer> enchants){
        assert enchants != null;
        this.enchants = enchants;
        return this;
    }

    public CustomItem setUnbreakable(boolean unbreakable){
        this.unbreakable = unbreakable;
        return this;
    }

    public CustomItem setStringLore(List<String> stringLore){
        assert  stringLore != null;
        List<Component> lore = new ArrayList<>();
        for (String string : stringLore)
            lore.add(LegacyComponentSerializer.legacy('&').deserialize(string));
        this.lore = lore;
        return this;
    }

    public CustomItem setStringLore(String lore){
        assert lore != null;
        String[] s = lore.split(";");
        List<Component> compLore = new ArrayList<>();
        for (String ss : s){
            compLore.add(LegacyComponentSerializer.legacy('&').deserialize(ss));
        }
        this.lore = compLore;
        return this;
    }

    public CustomItem setComponentLore(List<Component> lore){
        assert lore != null;
        this.lore = lore;
        return this;
    }

    public CustomItem setAttributes(String attributes){
        assert attributes != null;
        this.attributes = attributes;
        return this;
    }

    public CustomItem setRequires(String requires){
        assert requires != null;
        this.requires = requires;
        return this;
    }

    public CustomItem setItemstackIngredients(Map<Character, ItemStack> itemstackIngredients){
        assert itemstackIngredients != null;
        this.itemstackIngredients = itemstackIngredients;
        return this;
    }

    public CustomItem setMaterialIngredients(Map<Character, Material> materialIngredients){
        assert materialIngredients != null;
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

    public Recipe getRecipe() {
        return this.recipeType.getRecipe();
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

    @SuppressWarnings("ConstantConditions")
    public String getShapedStringRecipe(){
        StringBuilder t = new StringBuilder();
        if (recipeType.getRecipe() == null || recipeType.getRecipe() instanceof ShapedRecipe) return null;
        for (String s : ((ShapedRecipe) recipeType.getRecipe()).getShape())
            t.append(String.format("\"%s\"", s));
        return t.toString().trim();
    }

    public @Nullable String getShapelessStringRecipe(){
        StringBuilder t = new StringBuilder();
        if (recipeType.getRecipe() == null || !(recipeType.getRecipe() instanceof ShapelessRecipe)) return null;
        for (ItemStack s : ((ShapelessRecipe) recipeType.getRecipe()).getIngredientList())
            t.append(String.format("\"%s\"", PlainTextComponentSerializer.plainText().serialize(s.getItemMeta().displayName())));
        return t.toString().trim();
    }

    public void addMaterialIngredient(char character, Material mat){
        assert mat != null;
        materialIngredients.put(character, mat);
    }

    public void addItemStackIngredient(char character, ItemStack itemStack){
        assert itemStack != null;
        itemstackIngredients.put(character, itemStack);
    }

    public CustomItem setRecipe(org.bukkit.inventory.Recipe recipe){
        assert recipe != null;
        this.recipeType.setRecipe(recipe);
        //this.recipe = recipe;
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
        itemID = id;
        return this;
    }

    public int getItemID(){
        return itemID;
    }
}
