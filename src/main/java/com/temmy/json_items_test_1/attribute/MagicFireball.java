package com.temmy.json_items_test_1.attribute;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.CustomDataTypes;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class MagicFireball {

    private MagicFireball(){}

    public static final NamespacedKey fireballKey = new NamespacedKey(Main.getPlugin(), "fireball");
    public static final NamespacedKey fireballDamageKey = new NamespacedKey(Main.getPlugin(), "fireballdamage");
    public static final NamespacedKey fireballRangeKey = new NamespacedKey(Main.getPlugin(), "fireballrange");
    public static final NamespacedKey fireballShooterKey = new NamespacedKey(Main.getPlugin(), "fireballshooter");
    public static final NamespacedKey fireballCooldownKey = new NamespacedKey(Main.getPlugin(), "fireballcooldown");

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerInteractEvent event)) return;
        if (!event.getAction().isRightClick()) return;
        if (Main.worldGuardEnabled)
            if (worldGuard(event)) return;
        Player player = event.getPlayer();
        if (player.getPersistentDataContainer().has(fireballCooldownKey, CustomDataTypes.Boolean) && !player.hasPermission("jsonitems.cooldownbypass")) return;
        double damage = 0;
        int range = 0;
        int distance = 0;
        int cooldown = 60;
        for (String s : args) {
            s = s.replaceAll("[\\{\"\\}]","");
            String[] ss = s.split(",");
            for (String sss : ss){
                if (sss.contains("damage")){
                    String[] dmg = sss.split(":");
                    damage = Double.parseDouble(dmg[1]);
                }else if (sss.contains("range")){
                    String[] rng = sss.split(":");
                    range = Integer.parseInt(rng[1]);
                }else if (sss.contains("distance")){
                    String[] dist = sss.split(":");
                    distance = Integer.parseInt(dist[1]);
                }else if (sss.contains("cooldown")){
                    String[] cool = sss.split(":");
                    cooldown = Integer.parseInt(cool[1])*20;
                }
            }
        }
        RayTraceResult res = player.rayTraceBlocks(distance, FluidCollisionMode.NEVER);
        if (res == null || res.getHitBlock() == null) return;
        Location location = new Location(res.getHitBlock().getWorld(), res.getHitBlock().getX(), res.getHitBlock().getY()+10, res.getHitBlock().getZ());
        Location loc = new Location(location.getWorld(), location.getX(), location.getY()-12, location.getZ());
        org.bukkit.entity.Fireball ent = (org.bukkit.entity.Fireball) player.getWorld().spawnEntity(location, EntityType.FIREBALL, CreatureSpawnEvent.SpawnReason.CUSTOM);
        Vector vec = loc.toVector().subtract(location.toVector());
        vec = vec.multiply(0.1);
        ent.setVelocity(vec);
        ent.setIsIncendiary(false);
        ent.setShooter(player);
        ent.setYield(0);
        ent.getPersistentDataContainer().set(fireballKey, CustomDataTypes.Boolean, true);
        ent.getPersistentDataContainer().set(fireballDamageKey, PersistentDataType.DOUBLE, damage);
        ent.getPersistentDataContainer().set(fireballRangeKey, PersistentDataType.INTEGER, range);
        ent.getPersistentDataContainer().set(fireballShooterKey, CustomDataTypes.UUID, player.getUniqueId());
        player.getPersistentDataContainer().set(fireballCooldownKey, CustomDataTypes.Boolean, true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()-> player.getPersistentDataContainer().remove(fireballCooldownKey), cooldown);
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

}
