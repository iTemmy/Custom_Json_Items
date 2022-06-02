package com.temmy.json_items_test_1.util.newCustomItem;


import com.google.common.collect.LinkedHashMultimap;
import com.temmy.json_items_test_1.util.newCustomItem.recipe.RecipeInterface;
import org.bukkit.attribute.Attribute;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface CustomItemInterface {

  NewCustomItem setName(String name);

  NewCustomItem setMaterial(Material material);

  NewCustomItem setDisplayName(Component displayName);

  NewCustomItem setRecipe(RecipeInterface recipe);

  NewCustomItem setCustomAttributes(String attributes);

  NewCustomItem setVanillaAttributes(LinkedHashMultimap<Attribute, AttributeModifier> attributes);

  NewCustomItem addVanillaAttributes(Attribute attribute, AttributeModifier attributeModifier);

  NewCustomItem setCustomModelData(int data);

  NewCustomItem setEnchants(Map<Enchantment, Integer> enchants);

  NewCustomItem addEnchant(Enchantment enchant, int level);

  NewCustomItem setUnbreakable(boolean unbreakable);

  NewCustomItem setLore(List<Component> lore);

  NewCustomItem addLore(Component lore);

  NewCustomItem build();

  Material getMaterial();

  String getName();

  Component getDisplayName();

  String getStringDisplayName();

  String getCustomAttributes();

  LinkedHashMultimap<Attribute, AttributeModifier> getVanillaAttributes();

  int getCustomModelData();

  Map<Enchantment, Integer> getEnchants();

  boolean getUnbreakable();

  List<Component> getLore();

  ItemStack getItemStack();
}
