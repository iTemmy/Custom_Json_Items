package com.temmy.json_items_test_1.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

public class VanillaAttributes {
    Double value;
    EquipmentSlot slot;
    Attribute attribute;

    public VanillaAttributes(Attribute attribute, Double value, EquipmentSlot slot){
        this.attribute = attribute;
        this.value = value;
        this.slot = slot;
    }


}
