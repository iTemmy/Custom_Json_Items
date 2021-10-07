package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Logger;

public class SlothSin {
    private SlothSin(){
    }

    static Logger log = Main.getPlugin().getLogger();
    static NamespacedKey activated = new NamespacedKey(Main.getPlugin(), "active");
    static NamespacedKey task = new NamespacedKey(Main.getPlugin(), "taskInt");
    static NamespacedKey speedBaseValue = new NamespacedKey(Main.getPlugin(), "speedbaseValue");
    static NamespacedKey damageBaseValue = new NamespacedKey(Main.getPlugin(), "damagebaseValue");

    public static void trigger(Event e, String[] args){
        if (!(e instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        ItemStack hand = damager.getInventory().getItemInMainHand();
        ItemMeta handMeta = hand.getItemMeta();
        int cooldown = -99;
        int speedIncrease = -99;
        int damageIncrease = -99;

        for (String s : args){
            s = s.replaceAll("\"", "");
            String[] ss = s.split(",");
            for (String arg : ss){
                if (arg.toLowerCase().contains("speedincrease".toLowerCase())) {
                    String[] bruh = arg.split(":");
                    for (String a : bruh)
                        try {
                            speedIncrease = Integer.parseInt(a);
                        }catch (NumberFormatException ignored){}
                }else if (arg.toLowerCase().contains("cooldown".toLowerCase())){
                    String[] bruh = arg.split(":");
                    for (String a : bruh)
                        try{
                        cooldown = Integer.parseInt(a);
                        }catch (NumberFormatException ignored){}
                }else if (arg.toLowerCase().contains("damageIncrease".toLowerCase())){
                    arg = arg.replaceAll("}", "");
                    String[] bruh = arg.split(":");
                    for (String a : bruh)
                        try {
                            damageIncrease = Integer.parseInt(a);
                        }catch (NumberFormatException ignored){}
                }
            }
        }

        if (!handMeta.getPersistentDataContainer().has(activated, PersistentDataType.BYTE)) {
            damager.getPersistentDataContainer().set(speedBaseValue, PersistentDataType.DOUBLE, damager.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue());
            damager.getPersistentDataContainer().set(damageBaseValue, PersistentDataType.DOUBLE, damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue());
            if (speedIncrease == -99 || damageIncrease == -99) return;
            damager.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(damager.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() + speedIncrease);
            damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() + damageIncrease);
        }
        if (handMeta.getPersistentDataContainer().has(task, PersistentDataType.INTEGER)) {
            Bukkit.getScheduler().cancelTask(handMeta.getPersistentDataContainer().get(task, PersistentDataType.INTEGER));
            if (speedIncrease == -99 || damageIncrease == -99) return;
            damager.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(damager.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() + speedIncrease);
            damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() + damageIncrease);
        }
        handMeta.getPersistentDataContainer().set(task, PersistentDataType.INTEGER, createTask((cooldown * 20), damager, damager.getInventory().getItemInMainHand()));
        handMeta.getPersistentDataContainer().set(activated, PersistentDataType.BYTE, (byte) 1);
        log.info(String.valueOf(handMeta.getPersistentDataContainer().get(task, PersistentDataType.INTEGER)));
        hand.setItemMeta(handMeta);
    }

    public static int createTask(long delay, Player player, ItemStack oldhand){
        return Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->{
            ItemMeta meta = oldhand.getItemMeta();
            meta.getPersistentDataContainer().remove(activated);
            meta.getPersistentDataContainer().remove(task);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(player.getPersistentDataContainer().get(speedBaseValue, PersistentDataType.DOUBLE));
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(player.getPersistentDataContainer().get(damageBaseValue, PersistentDataType.DOUBLE));
            oldhand.setItemMeta(meta);
        }, delay);
    }
}
