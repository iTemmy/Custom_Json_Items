package com.temmy.json_items_test_1.attribute;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Logger;

public class LustSin {
    private LustSin(){
    }

    static Logger log = Bukkit.getLogger();
    static NamespacedKey healthKey = new NamespacedKey(Main.getPlugin(), "health");

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerArmorChangeEvent event)) return;
        Player player = event.getPlayer();
        int levels = -99;
        int health = -99;
        for (String s : args){
            s = s.replaceAll("[\"\\{\\}\\[]", "");
            String[] ss = s.split(",");
            for (String arg : ss){
                if (arg.toLowerCase().contains("health")){
                    arg = arg.toLowerCase().replaceAll("health:", "");
                    health = Integer.parseInt(arg);
                }else if (arg.toLowerCase().contains("levels")){
                    arg = arg.toLowerCase().replaceAll("levels:", "");
                    levels = Integer.parseInt(arg);
                }
            }
        }
        if (levels == -99 || health == -99) return;
        PersistentDataContainer data = player.getPersistentDataContainer();
        int increase = (int) Math.floor(player.getLevel()/levels)*health;
        data.set(healthKey, PersistentDataType.INTEGER, increase);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()+increase);
    }

    public static void removeLustHealth(Player player, ItemStack oldItem){
        if (!oldItem.hasItemMeta()) return;
        if (!oldItem.getItemMeta().getPersistentDataContainer().has(com.temmy.json_items_test_1.attribute.Attribute.namespacedKey, PersistentDataType.STRING)) return;
        if (ItemUtils.getItemAttributeMap(oldItem.getItemMeta().getPersistentDataContainer()).get("LUSTSIN") == null){return;}
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (!data.has(healthKey, PersistentDataType.INTEGER)) return;
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()-data.get(healthKey, PersistentDataType.INTEGER));
        data.remove(healthKey);
    }
}
