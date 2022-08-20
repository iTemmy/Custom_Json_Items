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

  CustomItemInterface setName(String name);

  CustomItemInterface setMaterial(Material material);

  CustomItemInterface setDisplayName(Component displayName);

  CustomItemInterface setRecipe(RecipeInterface recipe);

  CustomItemInterface setCustomAttributes(String attributes);

  CustomItemInterface setVanillaAttributes(LinkedHashMultimap<Attribute, AttributeModifier> attributes);

  CustomItemInterface addVanillaAttributes(Attribute attribute, AttributeModifier attributeModifier);

  CustomItemInterface setCustomModelData(int data);

  CustomItemInterface setEnchants(Map<Enchantment, Integer> enchants);

  CustomItemInterface addEnchant(Enchantment enchant, int level);

  CustomItemInterface setUnbreakable(boolean unbreakable);

  CustomItemInterface setLore(List<Component> lore);

  CustomItemInterface addLore(Component lore);

  CustomItemInterface build();

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

  RecipeInterface getRecipe();
}
