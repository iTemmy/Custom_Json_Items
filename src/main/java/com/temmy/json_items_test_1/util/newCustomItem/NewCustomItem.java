package com.temmy.json_items_test_1.util.newCustomItem;

import com.google.common.collect.LinkedHashMultimap;
import com.temmy.json_items_test_1.util.newCustomItem.recipe.RecipeInterface;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCustomItem implements CustomItemInterface {

    String name;
    Material material;
    Component displayName;
    RecipeInterface recipeInterface;
    String customAttributes;
    LinkedHashMultimap<Attribute, AttributeModifier> vanillaAttributes = LinkedHashMultimap.create();
    int customModelData;
    Map<Enchantment, Integer> enchants = new HashMap<>();
    boolean unbreakable;
    List<Component> lore = new ArrayList<>();
    ItemStack item = null;

    @Override
    public NewCustomItem setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public NewCustomItem setMaterial(Material material) {
        this.material = material;
        return this;
    }

    @Override
    public NewCustomItem setDisplayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public NewCustomItem setRecipe(RecipeInterface recipe) {
        recipeInterface = recipe;
        return this;
    }

    @Override
    public NewCustomItem setCustomAttributes(String attributes) {
        customAttributes = attributes;
        return this;
    }

    @Override
    public NewCustomItem setVanillaAttributes(LinkedHashMultimap<Attribute, AttributeModifier> attributes) {
        vanillaAttributes = attributes;
        return this;
    }

    @Override
    public NewCustomItem addVanillaAttributes(Attribute attribute, AttributeModifier attributeModifier) {
        vanillaAttributes.put(attribute, attributeModifier);
        return this;
    }

    @Override
    public NewCustomItem setCustomModelData(int data) {
        customModelData = data;
        return this;
    }

    @Override
    public NewCustomItem setEnchants(Map<Enchantment, Integer> enchants) {
        this.enchants = enchants;
        return this;
    }

    @Override
    public NewCustomItem addEnchant(Enchantment enchant, int level) {
        enchants.put(enchant, level);
        return this;
    }

    @Override
    public NewCustomItem setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    @Override
    public NewCustomItem setLore(List<Component> lore) {
        this.lore = lore;
        return this;
    }

    @Override
    public NewCustomItem addLore(Component lore) {
        this.lore.add(lore);
        return this;
    }

    @Override
    public NewCustomItem build() {
        item = new ItemStack(material);
        item.addUnsafeEnchantments(enchants);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        meta.setCustomModelData(customModelData);
        meta.setUnbreakable(unbreakable);
        meta.lore(lore);
        meta.setAttributeModifiers(vanillaAttributes);
        meta.getPersistentDataContainer().set(com.temmy.json_items_test_1.attribute.Attribute.namespacedKey, PersistentDataType.STRING, customAttributes);
        item.setItemMeta(meta);
        return this;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }

    @Override
    public String getStringDisplayName() {
        return LegacyComponentSerializer.legacy('&').serialize(displayName);
    }

    @Override
    public String getCustomAttributes() {
        return customAttributes;
    }

    @Override
    public LinkedHashMultimap<Attribute, AttributeModifier> getVanillaAttributes() {
        return vanillaAttributes;
    }

    @Override
    public int getCustomModelData() {
        return customModelData;
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    @Override
    public boolean getUnbreakable() {
        return unbreakable;
    }

    @Override
    public List<Component> getLore() {
        return lore;
    }

    @Override
    @Nullable
    public ItemStack getItemStack() {
        return item!=null?item:null;
    }

    @Override
    public RecipeInterface getRecipe(){
        return recipeInterface;
    }
}
