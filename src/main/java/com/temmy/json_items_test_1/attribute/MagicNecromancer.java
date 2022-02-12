package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.CustomDataTypes;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicNecromancer {

    private MagicNecromancer(){}

    public static final NamespacedKey necromancerMobContainerKey = new NamespacedKey(Main.getPlugin(), "necromancermobs");
    public static final NamespacedKey playerKey = new NamespacedKey(Main.getPlugin(), "playerUUID");
    public static final NamespacedKey necromancerCooldownKey = new NamespacedKey(Main.getPlugin(), "cooldown");

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerInteractEvent event)) return;
        if (!event.getAction().isRightClick()) return;
        Player player = event.getPlayer();
        if (player.getPersistentDataContainer().has(necromancerCooldownKey, CustomDataTypes.Boolean)&& !player.hasPermission("jsonitems.cooldownbypass")) return;
        Location loc = player.getLocation();
        EntityType entity = null;
        int distance = 0;
        int cooldown = 1200;
        for (String s : args) {
            s = s.replaceAll("[\\{\\}\"]", "");
            Main.getPlugin().getLogger().info(s);
            String[] ss = s.split(",");
            for (String sss : ss){
                try {
                    String[] ssc = sss.split(":");
                    if (sss.contains("entity"))
                        entity = EntityType.valueOf(ssc[1]);
                    else if (sss.contains("distance"))
                        distance = Integer.parseInt(ssc[1]);
                    else if (sss.contains("cooldown"))
                        cooldown = Integer.parseInt(ssc[1]) * 20;
                } catch (IllegalArgumentException ex) {
                    if (Main.debug) ex.printStackTrace();
                }
            }
        }
        double PosX = loc.getX()+distance;
        double NegX = loc.getX()-distance;
        double PosZ = loc.getZ()+distance;
        double NegZ = loc.getZ()-distance;
        List<LivingEntity> entList = new ArrayList<>();
        PersistentDataAdapterContext context = player.getPersistentDataContainer().getAdapterContext();
        PersistentDataContainer entityContainer = context.newPersistentDataContainer();
        List<Location> locs = new ArrayList<>(Arrays.asList(new Location(loc.getWorld(), PosX, loc.getY(), loc.getZ()),
                new Location(loc.getWorld(), NegX, loc.getY(), loc.getZ()),
                new Location(loc.getWorld(), loc.getX(), loc.getY(), PosZ),
                new Location(loc.getWorld(), loc.getX(), loc.getY(), NegZ)));
        int i = 0;
        for (Location locc : locs) {
            if (entity == null) {
                if (Main.debug) {
                    Main.getPlugin().getLogger().warning(String.format("Entity on %s's %s is null", player.getName(),
                            PlainTextComponentSerializer.plainText().serialize(event.getItem().getItemMeta().displayName())));
                }
                return;
            }
            entList.add((LivingEntity) loc.getWorld().spawnEntity(locc, entity));
            Ageable age = (Ageable) entList.get(i);
            if (!age.isAdult()) age.setAdult();
            List<Entity> nearby = entList.get(i).getNearbyEntities(5, 5, 5);
            for (Entity ent : nearby)
                if (ent instanceof Player && ent != player)
                    ((Ageable) entList.get(i)).setTarget((LivingEntity) ent);
                else if (ent instanceof LivingEntity && ent != player && !entList.contains(ent))
                    ((Ageable) entList.get(i)).setTarget((LivingEntity) ent);
            entList.get(i).getPersistentDataContainer().set(playerKey, CustomDataTypes.UUID, player.getUniqueId());
            entityContainer.set(new NamespacedKey(Main.getPlugin(), String.format("necromancermob_%d", i)), CustomDataTypes.UUID, entList.get(i).getUniqueId());
            i++;
        }
        player.getPersistentDataContainer().set(necromancerMobContainerKey, PersistentDataType.TAG_CONTAINER, entityContainer);
        player.getPersistentDataContainer().set(necromancerCooldownKey, CustomDataTypes.Boolean, true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->{
            for (LivingEntity ent : entList){
                if (ent.isDead()) continue;
                ent.remove();
                if (!player.getPersistentDataContainer().has(necromancerMobContainerKey, PersistentDataType.TAG_CONTAINER)) return;
                PersistentDataContainer entContainer = player.getPersistentDataContainer().get(necromancerMobContainerKey, PersistentDataType.TAG_CONTAINER);
                assert entContainer != null;
                if (entContainer.getKeys() == null) return;
                for (NamespacedKey key : entContainer.getKeys()) {
                    if (entContainer.has(key, CustomDataTypes.UUID))
                        loc.getWorld().getEntity(entContainer.get(key, CustomDataTypes.UUID));
                }
            }
            player.getPersistentDataContainer().remove(necromancerMobContainerKey);
            player.getPersistentDataContainer().remove(necromancerCooldownKey);
        }, cooldown);
    }
}
