package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public final class Attribute {
    public static final NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(), "attributes");
    private static final Map<String, AttributeMethod> attributeMethods = new HashMap<>();

    //TODO: Create Attribute for hoes that when used to break crops they will auto replant and
    // possibly have a chance to drop extra crops from harvest

    static {
        attributeMethods.put("AUTO_SMELT", AutoSmelt::trigger);
        attributeMethods.put("ARMOREFFECTS", ArmorEffects::trigger);
        attributeMethods.put("EFFECT", AttackEffect::trigger);
        attributeMethods.put("SPEEDFURNACE", SpeedFurnace::trigger);
        attributeMethods.put("PLACE", DenyCustomItemPlace::trigger);
        attributeMethods.put("CRAFT", DenyCustomItemCraft::trigger);
        attributeMethods.put("FULLSETARMOREFFECTS", FullSetArmorEffects::trigger);
        attributeMethods.put("ARMORATTRIBUTES", ArmorAttributeEffects::trigger);
        attributeMethods.put("FULLSETARMORATTRIBUTES", FullSetArmorAttributeEffects::trigger);
        attributeMethods.put("ARROWATTRIBUTE", ArrowAttribute::trigger);
        attributeMethods.put("HELDITEM", HeldItemEffects::trigger);
        attributeMethods.put("GREEDSIN", GreedSin::trigger);
        attributeMethods.put("PRIDESIN", PrideSin::trigger);
        attributeMethods.put("SLOTHSIN", SlothSin::trigger);
        attributeMethods.put("LUSTSIN", LustSin::trigger);
        attributeMethods.put("GLUTTONYSIN", GluttonySin::trigger);
        attributeMethods.put("AUTOFEED", AutoFeed::trigger);
    }

    public static void invoke(String attribute, Event event, String[] args){
        AttributeMethod attributeMethod = attributeMethods.get(attribute);
        if (attributeMethod != null) attributeMethod.trigger(event, args);
        else Main.getPlugin().getLogger().severe(String.format("Unrecognized attribute '%s'.", attribute));
    }
}
