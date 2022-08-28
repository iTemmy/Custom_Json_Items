package com.temmy.json_items_test_1.Parser;

import com.google.common.collect.LinkedHashMultimap;
import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.Convert;
import com.temmy.json_items_test_1.util.Glow;
import com.temmy.json_items_test_1.util.newCustomItem.NewCustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public final class ItemParser {

    static Glow glow = Main.glow;
    static Logger log = Bukkit.getLogger();
    static final NamespacedKey vanillaAttributeContainerKey = new NamespacedKey(Main.getPlugin(), "vanillaAttributesContainer");
    static int length = 0;

    /*@SuppressWarnings({"ConstantConditions", "unchecked", "ToArrayCallWithZeroLengthArrayArgument"})
    public static @Nullable ItemStack parseItem(@NotNull String item){
        ItemStack itemStack = null;
        String filename = item.toLowerCase();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Main.getPlugin().getDataFolder()+String.format("/item/%s.json", item));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream == null) {log.warning("Invalid input stream for file "+item);return null;}

        try {
            Gson gson = new Gson();
            Object jsonObject = new JSONParser().parse(new InputStreamReader(inputStream));
            JSONObject itemJson = (JSONObject) jsonObject;
            CustomItem test = gson.fromJson(itemJson.toJSONString(), CustomItem.class);
            Map<String, ?> itemSection = (Map<String, ?>) itemJson.get("ITEM");
            itemStack = new ItemStack(Material.valueOf((String)itemSection.get("material")));
            Map<String, JSONObject> attributesJson = (Map<String, JSONObject>) itemSection.get("attributes");
            Map<String, String[]> attributes = new HashMap<>();

            if (attributesJson != null){
                for (String key : attributesJson.keySet()){
                    List<String> attributeList = new ArrayList<>(Collections.singleton(String.valueOf(attributesJson.get(key))));
                    attributes.put(key, attributeList.toArray(new String[attributeList.size()]));
                }
            }
            Map<Enchantment, Integer> enchants = getEnchants(itemSection);
            itemStack.addUnsafeEnchantments(enchants);
            ItemMeta meta = itemStack.getItemMeta();
            meta.lore(getLore(itemSection));
            meta.setUnbreakable(getUnbreakable(itemSection));
            meta.displayName(getName(itemSection));
            meta.setCustomModelData(getCustomModelData(itemSection));
            PersistentDataAdapterContext context = meta.getPersistentDataContainer().getAdapterContext();
            PersistentDataContainer vanillaAttributesContainer = context.newPersistentDataContainer();
            Map<org.bukkit.attribute.Attribute, AttributeModifier> attributeMap = getAttributes(itemSection);
            for (org.bukkit.attribute.Attribute attribute : attributeMap.keySet()){
                vanillaAttributesContainer.set(attribute.getKey(), CustomDataTypes.UUID, attributeMap.get(attribute).getUniqueId());
                meta.addAttributeModifier(attribute, attributeMap.get(attribute));
            }
            meta.getPersistentDataContainer().set(vanillaAttributeContainerKey, PersistentDataType.TAG_CONTAINER, vanillaAttributesContainer);
            meta.getPersistentDataContainer().set(Attribute.namespacedKey,
                    PersistentDataType.STRING,
                    Convert.mapToString(attributes));
            if (meta.hasEnchants() && itemStack.getType() == Material.ENCHANTED_BOOK){
                EnchantmentStorageMeta eMeta = (EnchantmentStorageMeta) meta;
                for (Enchantment enchant : meta.getEnchants().keySet()) {
                    eMeta.addStoredEnchant(enchant, meta.getEnchants().get(enchant), true);
                    meta.removeEnchant(enchant);
                }
                meta = eMeta;
            }

            itemStack.setItemMeta(meta);

            //convertFromJsonToCustomItem(itemStack, itemSection, filename);
        } catch (JsonSyntaxException e){
            log.warning("Syntax Error in "+filename);
            if (Main.debug)
                e.printStackTrace();
        }catch (IOException | ParseException e1){
            if (Main.debug)
                e1.printStackTrace();
        }

        return itemStack;
    }*/

    private static boolean getUnbreakable(@NotNull Map<String, ?> itemSection){
        if (itemSection.get("unbreakable") == null) return false;
        return ((boolean) itemSection.get("unbreakable"));
    }


    private static @NotNull Map<Enchantment, Integer> getEnchants(@NotNull Map<String, ?> itemSection){
        Map<Enchantment, Integer> finalEnchants = new HashMap<>();
        try {
            if (itemSection.get("enchants") == null) return new HashMap<>();
            JSONArray JSONEnchants = (JSONArray) itemSection.get("enchants");
            if (JSONEnchants == null) return new HashMap<>();
            List<String> enchantsList = new ArrayList<>(JSONEnchants);
            for (int i = 0; i< enchantsList.size(); i++) {
                String s = String.valueOf(enchantsList.get(i));
                if (s.equals(null) || s.equals("")) continue;
                s = s.replaceAll("[\\{\"\\}]", "");
                String[] enchantsWithLevel = s.split(",");
                for (String enchant : enchantsWithLevel) {
                    String[] enchants = enchant.split(";");
                    if (enchants[0].equalsIgnoreCase("glow")) {
                        finalEnchants.put(glow, 1);
                    } else {
                        finalEnchants.put(Enchantment.getByKey(NamespacedKey.minecraft(enchants[0].toLowerCase())), Integer.parseInt(enchants[1]));
                    }
                }
            }
        } catch (NumberFormatException | ClassCastException e) {
            e.printStackTrace();
        }
        return finalEnchants;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static @Nullable List<Component> getLore(@NotNull Map<String, ?> itemSection){
        JSONArray lore = (JSONArray) itemSection.get("lore");
        List<Component> loreList = new ArrayList<>(lore);
        List<String> stringLore = new ArrayList<>(lore);
        if (stringLore != null && stringLore.size() > 0){
            for (int i = 0; i <= stringLore.size()-1; i++)
                loreList.set(i, LegacyComponentSerializer.legacy('&').deserialize(stringLore.get(i)));
        }
        if (stringLore.size() <= 0){
            return null;
        }else
            return loreList;
    }

    private static @NotNull LinkedHashMultimap<org.bukkit.attribute.Attribute, AttributeModifier> getAttributes(@NotNull Map<String, ?> itemSection){
        LinkedHashMultimap<org.bukkit.attribute.Attribute, AttributeModifier> attributes = LinkedHashMultimap.create();
        int health = 0;
        String healthSlot = null;
        try {
            if (itemSection.get("health") != null && itemSection.get("healthSlot") != null) {
                health = Integer.parseInt(String.valueOf((long) itemSection.get("health")));
                healthSlot = (String) itemSection.get("healthSlot");
                attributes.put(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH,
                        new AttributeModifier(UUID.randomUUID(), "generic.max.health", health,
                                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(healthSlot)));
            }

            int damage = 0;
            String damageSlot = null;
            if (itemSection.get("damage") != null && itemSection.get("damageSlot") != null) {
                damage = Integer.parseInt(String.valueOf((long) itemSection.get("damage")));
                damageSlot = (String) itemSection.get("damageSlot");
                attributes.put(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(UUID.randomUUID(), "generic.attack.damage", damage,
                                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(damageSlot)));
            }

            int attackSpeed = 0;
            String attackSpeedSlot = null;
            if (itemSection.get("attackSpeed") != null && itemSection.get("attackSpeedSlot") != null) {
                attackSpeed = Integer.parseInt(String.valueOf((long) itemSection.get("attackSpeed")));
                attackSpeedSlot = (String) itemSection.get("attackSpeedSlot");
                attributes.put(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED,
                        new AttributeModifier(UUID.randomUUID(), "generic.attack.speed", attackSpeed,
                                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(attackSpeedSlot)));
            }
        } catch (IllegalArgumentException e) {
            if (Main.debug) e.printStackTrace();
        }
        return attributes;
    }

    private static @Nullable Component getName(@NotNull Map<String, ?> itemSection){
        if (itemSection.get("name") == null) return null;
        String name = (String) itemSection.get("name");
        return LegacyComponentSerializer.legacy('&').deserialize(name);
    }

    private static int getCustomModelData(@NotNull Map<String, ?> itemSection){
        if (itemSection.get("model") == null) return 0;
        return Integer.parseInt(itemSection.get("model").toString());
    }

    @SuppressWarnings("ConstantConditions")
    public static @Nullable NewCustomItem parseItem(@NotNull String item){
        log.info(String.format("Now registering %s", item));
        NewCustomItem customItem = null;
        String fileName = item.toLowerCase();
        InputStream inputStream = null;
        Item itemClass = new Item();
        try {
            inputStream = new FileInputStream(Main.getPlugin().getDataFolder()+String.format("/item/%s.json", item));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        if (inputStream == null) {
            log.warning("Invalid input stream for file "+item);
            return null;
        }
        try {
            Object jsonObject = new JSONParser().parse(new InputStreamReader(inputStream));
            JSONObject itemJson = (JSONObject) jsonObject;
            Map<String, ?> itemSection = (Map<String, ?>) itemJson.get("ITEM");
            itemClass.create(item);
            customItem = itemClass.getByName(item.toLowerCase());
            customItem.setMaterial(Material.valueOf((String) itemSection.get("material")));
            customItem.setDisplayName(getName(itemSection));
            Map<String, JSONObject> attributesJson = (Map<String, JSONObject>) itemSection.get("attributes");
            Map<String, String[]> attributes = new HashMap<>();
            if (attributesJson != null){
                for (String key : attributesJson.keySet()){
                    List<String> attributeList = new ArrayList<>(Collections.singleton(String.valueOf(attributesJson.get(key))));
                    attributes.put(key, attributeList.toArray(new String[attributeList.size()]));
                }
            }
            Map<Enchantment, Integer> enchants = getEnchants(itemSection);
            customItem.setEnchants(enchants);
            customItem.setLore(getLore(itemSection));
            customItem.setUnbreakable(getUnbreakable(itemSection));
            customItem.setCustomModelData(getCustomModelData(itemSection));
            LinkedHashMultimap<org.bukkit.attribute.Attribute, AttributeModifier> attributeMap = getAttributes(itemSection);
            customItem.setVanillaAttributes(attributeMap);
            customItem.setCustomAttributes(Convert.mapToString(attributes));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return customItem;
    }

    /*
    @SuppressWarnings({"ConstantConditions", "unchecked", "rawtypes"})
    private static void convertFromJsonToCustomItem(ItemStack item, Map<String, ?> itemSection, String filename) {
        Integer model = null;
        if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData())
            model = item.getItemMeta().getCustomModelData();
        LinkedHashMultimap<org.bukkit.attribute.Attribute, AttributeModifier> vanillaAttributes = getAttributes(itemSection);
        Double damage = null;
        EquipmentSlot dmgSlot = null;
        if (vanillaAttributes.containsKey(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE)){
            damage = vanillaAttributes.get(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).getAmount();
            dmgSlot = vanillaAttributes.get(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).getSlot();
        }
        Double health = null;
        EquipmentSlot hpSlot = null;
        if (vanillaAttributes.containsKey(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH)){
            health = vanillaAttributes.get(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getAmount();
            hpSlot = vanillaAttributes.get(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getSlot();
        }
        Double atkspeed = null;
        EquipmentSlot atkSpdSlot = null;
        if (vanillaAttributes.containsKey(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED)){
            atkspeed = vanillaAttributes.get(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED).getAmount();
            atkSpdSlot = vanillaAttributes.get(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED).getSlot();
        }
        Map<Enchantment, Integer> enchants = getEnchants(itemSection);
        boolean unbreakable = item.getItemMeta().isUnbreakable();
        List<Component> lore = item.getItemMeta().lore();
        String customAttributes = null;
        if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING))
            customAttributes = item.getPersistentDataContainer().get(Attribute.namespacedKey, PersistentDataType.STRING);
        String requires = null;
        if (itemSection.containsKey("requires"))
            requires = (String) itemSection.get("requires");
        Map<String, String> ingredients = (Map) itemSection.get("ingredients");
        Map<Character, Material> ingredientsMaterials = new HashMap<>();
        Map<Character, ItemStack> ingredientsItems = new HashMap<>();
        if (ingredients != null)
            for (String key : ingredients.keySet()){
                if (Material.matchMaterial(ingredients.get(key)) != null)
                    ingredientsMaterials.put(key.charAt(0), Material.valueOf(ingredients.get(key).toUpperCase()));
                else if (Main.getCustomItems_OLD().containsKey(ingredients.get(key).toLowerCase()))
                    ingredientsItems.put(key.charAt(0), Main.getCustomItems_OLD().get(ingredients.get(key).toUpperCase()));
            }
        String ss = PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName()).replaceAll("&[A-FK-Ok-oa-f0-9]", "").replaceAll(
                "[!@#$%^&*()\\{\\}\\[\\];:'\",./-=+]","").replaceAll("\s", "_").toLowerCase();

        CustomItem customItem = new CustomItem(filename).setComponentItemName(item.getItemMeta().displayName()).
                setMaterial(item.getType()).setModel(model).setDamage(damage).setDamageSlot(dmgSlot).setHealth(health).
                setHealthSlot(hpSlot).setAttackSpeed(atkspeed).setAttackSpeedSlot(atkSpdSlot).setEnchants(enchants);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), ss), customItem.getCustomItemItemStack());
        JSONArray JSONrecipe = (JSONArray) itemSection.get("recipe");
        if (JSONrecipe != null) {
            List<String> recipeLine = new ArrayList<String>(JSONrecipe);
            recipe.shape(recipeLine.toArray(new String[0]));
            for (char character : customItem.getMaterialIngredients().keySet())
                recipe.setIngredient(character, customItem.getMaterialIngredients().get(character));
            for (char character : customItem.getItemstackIngredients().keySet())
                recipe.setIngredient(character, customItem.getItemstackIngredients().get(character));
            customItem.setRecipe(recipe);
        }

        Main.getDb_customItems_OLDv2().put(customItem.getUniqueName(), customItem);
    }
     */
}
