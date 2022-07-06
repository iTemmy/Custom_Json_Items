package com.temmy.json_items_test_1.attribute;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.temmy.json_items_test_1.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.logging.Logger;

public class GreedSin {
    private GreedSin(){}

    static Logger log = Bukkit.getLogger();

    public static void trigger(Event e, String[] args){
        if (e instanceof EntityShootBowEvent) entityShootBow((EntityShootBowEvent) e, args);
    }

    private static void entityShootBow(EntityShootBowEvent e, String[] args){
        if (!(e.getEntity() instanceof Player player)) return;
        if (!(e.getProjectile() instanceof Arrow arrow)) return;
        if (Main.worldGuardEnabled)
            if (worldGuard(e)) return;
        double level = -99;
        double baseDamage = -99;
        double damage;
        for (String arg : args) {
            arg = arg.replaceAll("[\"\\{\\}]", "").trim();
            String[] ss = arg.split(",");
            for (String s : ss){
                if (s.toLowerCase().contains("level".toLowerCase())){
                    s = s.replaceAll("level:", "").trim();
                    try {
                        level = Integer.parseInt(s);
                    }catch (NumberFormatException exception){if (Main.debug) exception.printStackTrace();}
                }else if (s.toLowerCase().contains("baseDamage".toLowerCase())){
                    s = s.toLowerCase().replaceAll("baseDamage:".toLowerCase(), "").trim();
                    try {
                        baseDamage = Double.parseDouble(s);
                    }catch (NumberFormatException exception){if (Main.debug) exception.printStackTrace();}
                }
            }
        }
        if (level != -99 && baseDamage != -99){
            double fuck = getExpOnLevel(player, player.getLevel())/level;
            damage = baseDamage*fuck;
            if (damage < 0.2) damage = 0.2;
            arrow.setDamage(damage);
            long newExp = getExpOnLevel(player, player.getLevel())/100*20;
            newExp = getExpOnLevel(player, player.getLevel()) - newExp;
            player.setExp(0);
            player.setLevel(0);
            if (newExp > Integer.MAX_VALUE) newExp = Integer.MAX_VALUE;
            player.giveExp((int) newExp);
        }
    }

    public static long getExpOnLevel(Player player, long level){
        return getExpAtLevel(level)+Math.round((long) player.getExpToLevel()* (double) player.getExp());
    }

    public static long getExpAtLevel(long level){
        if (level <= 16)
            return (long) (Math.pow(level, 2) + 6L *level);
        else if (level <= 31)
            return (long) (2.5*Math.pow(level, 2) - 40.5*level + 360.0);
        else
            return (long) (4.5*Math.pow(level, 2) - 162.5*level + 2220.0);
    }

    private static boolean worldGuard(EntityShootBowEvent e){
        WorldGuard worldGuard = Main.getWorldGuard();
        RegionContainer container = worldGuard.getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getEntity().getLocation()));
        if (set == null) return false;
        if (!set.testState(null, Main.attributesEnabledFlag)) {
            e.setCancelled(true);
            return true;
        }
        return false;
    }

}
