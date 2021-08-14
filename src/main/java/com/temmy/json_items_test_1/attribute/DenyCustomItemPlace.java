package com.temmy.json_items_test_1.attribute;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class DenyCustomItemPlace {

    private DenyCustomItemPlace(){}

    public static void trigger(Event e, String[] args){
        if (!(e instanceof BlockPlaceEvent)) return;
        BlockPlaceEvent event = (BlockPlaceEvent) e;
        for (String arg : args){
            if (!(Boolean.getBoolean(arg))) event.setCancelled(true);
            return;
        }
    }
}
