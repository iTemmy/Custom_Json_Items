package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import java.util.logging.Logger;

public class ArmorAttributeEffects {

    private ArmorAttributeEffects(){}

    static Logger log = Bukkit.getLogger();

    public static void trigger(Event e, String[] args) {
        if (!(e instanceof PlayerArmorChangeEvent)) return;
        PlayerArmorChangeEvent event = (PlayerArmorChangeEvent) e;
        for (String arg : args) {
            String[] attributes = arg.split(":");
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i].contains("health")) {
                    attributes[i+1] = attributes[i+1].replace("}", "");
                    event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()+Integer.parseInt(attributes[i+1]));
                }else if (attributes[i].contains("speed")) {
                    attributes[i + 1] = attributes[i + 1].replace("}", "");
                    event.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(event.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() + Integer.parseInt(attributes[i+1]));
                }
            }
        }
    }
}
