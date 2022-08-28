package com.temmy.json_items_test_1.attribute;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.PersistentDataTypes.CustomDataTypes;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicNecromancer {

    private MagicNecromancer(){}

    public static final NamespacedKey necromancerMobContainerKey = new NamespacedKey(Main.getPlugin(), "necromancermobs");
    public static final NamespacedKey playerKey = new NamespacedKey(Main.getPlugin(), "playerUUID");
    public static final NamespacedKey necromancerCooldownKey = new NamespacedKey(Main.getPlugin(), "cooldown");
    public static final NamespacedKey necromancerRemoveEntityKey = new NamespacedKey(Main.getPlugin(), "necromancerremoveentity");
    public static final NamespacedKey necromancerSetTargetEntityKey = new NamespacedKey(Main.getPlugin(), "necromancertargetentity");

    public static void trigger(Event e, String[] args){
        if (e instanceof PlayerInteractEvent event) playerInteractEvent(event, args);
        if (e instanceof EntityDamageByEntityEvent event) entityDamageByEntityEvent(event);
    }

    @SuppressWarnings("ConstantConditions")
    private static void entityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if (event.getEntity().getPersistentDataContainer().has(playerKey, CustomDataTypes.UUID))
            if (event.getEntity().getPersistentDataContainer().get(playerKey, CustomDataTypes.UUID).equals(event.getDamager().getUniqueId())) {
                event.setCancelled(true);
            }
    }

    private static void playerInteractEvent(PlayerInteractEvent event, String[] args){
        if (!event.getAction().isRightClick()) return;
        if (Main.worldGuardEnabled)
            if (worldGuard(event)) return;
        Player player = event.getPlayer();
        if (player.getPersistentDataContainer().has(necromancerCooldownKey, CustomDataTypes.Boolean)&& !player.hasPermission("jsonitems.cooldownbypass")) return;
        Location loc = player.getLocation();
        EntityType entity = null;
        int distance = 0;
        int cooldown = 1200;
        for (String s : args) {
            s = s.replaceAll("[\\{\\}\"]", "");
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
        player.getPersistentDataContainer().set(necromancerSetTargetEntityKey, PersistentDataType.INTEGER, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), runnable(player), 20, 20));
        player.getPersistentDataContainer().set(necromancerRemoveEntityKey, PersistentDataType.INTEGER,
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
                    player.getPersistentDataContainer().remove(necromancerRemoveEntityKey);
                }, cooldown));
    }

    private static Runnable runnable(Player player){
        return () -> {
            if (!player.getPersistentDataContainer().has(necromancerMobContainerKey)) return;
            PersistentDataContainer cont = player.getPersistentDataContainer().get(necromancerMobContainerKey, PersistentDataType.TAG_CONTAINER);
            assert cont != null;
            if (cont.getKeys() == null) return;
            for (NamespacedKey key : cont.getKeys()){
                if (cont.has(key, CustomDataTypes.UUID)) {
                    if (player.getWorld().getEntity(cont.get(key, CustomDataTypes.UUID)) instanceof Mob) continue;
                    Mob mob = (Mob) player.getWorld().getEntity(cont.get(key, CustomDataTypes.UUID));
                    if (mob == null) continue;
                    if (mob.getTarget() == null || mob.getPersistentDataContainer().get(playerKey, CustomDataTypes.UUID).equals(player.getUniqueId()))
                        mob.setTarget((LivingEntity) targetEntity(mob));
                }
            }
        };
    }

    private static boolean worldGuard(PlayerInteractEvent e) {
        WorldGuard worldGuard = Main.getWorldGuard();
        RegionContainer container = worldGuard.getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getPlayer().getLocation()));
        if (set == null) return false;
        if (!set.testState(null, Main.attributesEnabledFlag)){
            e.setCancelled(true);
            return true;
        }
        return false;
    }

    public static @Nullable Entity targetEntity(LivingEntity summoned){
        List<Entity> nearby = summoned.getNearbyEntities(20, 20, 20);
        for (Entity ent : nearby) {
            if (ent instanceof Player player)
                if (summoned.getPersistentDataContainer().get(playerKey, CustomDataTypes.UUID).equals(player.getUniqueId())) continue;
            if (ent instanceof LivingEntity && ent != summoned)
                continue;
            Vector vector = ent.getLocation().toVector().subtract(summoned.getLocation().toVector());
            RayTraceResult result = summoned.getLocation().getWorld().rayTraceBlocks(summoned.getLocation(), vector, 16, FluidCollisionMode.NEVER, true);
            if (result == null) continue;
            if (result.getHitBlock() != null){
                return ent;
            }
        }
        return null;
    }

}
