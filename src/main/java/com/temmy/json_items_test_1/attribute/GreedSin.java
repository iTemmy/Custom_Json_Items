package com.temmy.json_items_test_1.attribute;

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
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getProjectile() instanceof Arrow)) return;
        Player player = (Player) e.getEntity();
        Arrow arrow = (Arrow) e.getProjectile();
        double level = -99;
        double baseDamage = -99;
        double damage = -99;
        for (String arg : args) {
            arg = arg.replaceAll("[\"\\{\\}]", "").trim();
            String[] ss = arg.split(",");
            for (String s : ss){
                if (s.toLowerCase().contains("level".toLowerCase())){
                    s = s.replaceAll("level:", "").trim();
                    try {
                        level = Integer.parseInt(s);
                    }catch (NumberFormatException ignored){ignored.printStackTrace();}
                }else if (s.toLowerCase().contains("baseDamage".toLowerCase())){
                    s = s.toLowerCase().replaceAll("baseDamage:".toLowerCase(), "").trim();
                    try {
                        baseDamage = Double.parseDouble(s);
                    }catch (NumberFormatException ignored){ignored.printStackTrace();}
                }
            }
        }
        if (level != -99 && baseDamage != -99){
            double fuck = getExpOnLevel(player, player.getLevel())/level;
            damage = baseDamage*fuck;
            if (damage < 0.2) damage = 0.2;
            arrow.setDamage(damage);
            int newExp = getExpOnLevel(player, player.getLevel())/100*20;
            newExp = getExpOnLevel(player, player.getLevel()) - newExp;
            player.setExp(0);
            player.setLevel(0);
            player.giveExp(newExp);
        }
    }

    public static int getExpOnLevel(Player player, int level){
        return getExpAtLevel(level)+Math.round(player.getExpToLevel()*player.getExp());
    }

    public static int getExpAtLevel(int level){
        if (level <= 16)
            return (int) (Math.pow(level, 2) + 6*level);
        else if (level <= 31)
            return (int) (2.5*Math.pow(level, 2) - 40.5*level + 360.0);
        else
            return (int) (4.5*Math.pow(level, 2) - 162.5*level + 2220.0);
    }

}
