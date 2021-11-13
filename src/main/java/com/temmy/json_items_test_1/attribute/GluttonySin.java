package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Logger;

public class GluttonySin {
    private GluttonySin(){}

    static Logger log = Main.getPlugin().getLogger();
    static final NamespacedKey effectKey = new NamespacedKey(Main.getPlugin(), "effect");
    static final NamespacedKey timeLevelKey = new NamespacedKey(Main.getPlugin(), "timelevel");
    static final NamespacedKey effectLevelKey = new NamespacedKey(Main.getPlugin(), "effectlevel");

    public static void trigger(Event e, String[] args){
        if (e instanceof ProjectileLaunchEvent) ProjectileLaunchEvent((ProjectileLaunchEvent) e, args);
    }

    public static void entityHitByTrident(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Trident)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) e.getEntity();
        Trident trident = (Trident) e.getDamager();
        PersistentDataContainer data = trident.getPersistentDataContainer();
        if (!data.has(effectKey, PersistentDataType.STRING) || !data.has(timeLevelKey, PersistentDataType.INTEGER)
                || !data.has(effectLevelKey, PersistentDataType.INTEGER)) return;
        PotionEffectType type = PotionEffectType.getByName(data.get(effectKey, PersistentDataType.STRING));
        int timeLevel = data.get(timeLevelKey, PersistentDataType.INTEGER);
        int effectLevel = data.get(effectLevelKey, PersistentDataType.INTEGER);
        if (effectLevel > 255) effectLevel = 255;
        livingEntity.addPotionEffect(new PotionEffect(type,timeLevel, effectLevel-1));
    }

    public static void ProjectileLaunchEvent(ProjectileLaunchEvent event, String[] args){
        if (!(event.getEntity() instanceof Trident)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();
        Trident trident = (Trident) event.getEntity();
        ItemStack tridentI = trident.getItemStack();
        if (!tridentI.hasItemMeta()) return;
        PotionEffectType type = null;
        int timelevel = 0;
        int effectlevel = 0;
        if (tridentI.getItemMeta().getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)) {
            for (String s : args) {
                s = s.replaceAll("[\\{\\[\\]\\}]", "");
                String[] ss = s.split(",");
                for (String string : ss) {
                    if (string.toLowerCase().contains("effectlevel")){
                        String[] effectlevels = string.split(":");
                        try{
                            effectlevel = Integer.parseInt(effectlevels[1]);
                        } catch (NumberFormatException ignored) {
                            ignored.printStackTrace();
                        }
                    }else if (string.toLowerCase().contains("effect")){
                        string = string.toLowerCase().replaceAll("\"", "");
                        String[] effects = string.split(":");
                        for (String effect : effects){
                            try {
                                type = PotionEffectType.getByName(effects[1]);
                            }catch (NullPointerException ignored){ignored.printStackTrace();}
                        }
                    }else if (string.toLowerCase().contains("timelevel")){
                        String[] levels = string.split(":");
                        try {
                            timelevel = Integer.parseInt(levels[1]);
                        } catch (NumberFormatException ignored) {ignored.printStackTrace();}
                    }
                }
            }
            if (type != null && timelevel != 0 && effectlevel != 0){
                timelevel = player.getLevel()/timelevel;
                effectlevel = player.getLevel()/effectlevel;
                trident.getPersistentDataContainer().set(effectKey, PersistentDataType.STRING, type.getName());
                trident.getPersistentDataContainer().set(timeLevelKey, PersistentDataType.INTEGER, timelevel);
                trident.getPersistentDataContainer().set(effectLevelKey, PersistentDataType.INTEGER, effectlevel);
            }
        }
    }
}
