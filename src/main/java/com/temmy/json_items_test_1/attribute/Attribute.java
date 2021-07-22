package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.attribute.effects.Poison;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class Attribute {
    public static final NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(), "attributes");
    private static final Map<String, AttributeMethod> attributeMethods = new HashMap<>();

    static {
        attributeMethods.put("AUTO_SMELT", AutoSmelt::trigger);
        attributeMethods.put("POISON", Poison::trigger);
        attributeMethods.put("ARMOREFFECTS", ArmorEffects::trigger);
    }

    public static void invoke(String attribute, Event event, String[] args){
        AttributeMethod attributeMethod = attributeMethods.get(attribute);
        if (attributeMethod != null) attributeMethod.trigger(event, args);
        else Bukkit.getLogger().log(Level.SEVERE, String.format("Unrecognized attribute '%s'.", attribute));
    }
}
