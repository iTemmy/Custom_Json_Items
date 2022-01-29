package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class Reach {
    private Reach(){}

    static Logger log = Main.getPlugin().getLogger();

    public static void trigger(Event e, String[] args){
        if (!(e instanceof PlayerInteractEvent event) || !event.getAction().isLeftClick()) return;
        Player player = event.getPlayer();
        String[] arg = args[0].split(":");
        arg[1] = arg[1].replace("}", "");
        Entity entity = getEntityInDirection(player, 3+Integer.parseInt(arg[1]));
        if (entity instanceof LivingEntity)
            ((LivingEntity) entity).damage(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue(), player);
    }

    /**
     * Found this method on spigot https://www.spigotmc.org/threads/gun-target-entity-problem.518030/
     * @param livingEntity The start point of the search.
     * @param range how far to look for entities.
     * @return return any entity found.
     */
    public static Entity getEntityInDirection(LivingEntity livingEntity, int range){
        ArrayList<Entity> entities = (ArrayList<Entity>)livingEntity.getNearbyEntities(range,range,range);
        ArrayList<Block> sightBlock = (ArrayList<Block>)livingEntity.getLineOfSight(null, range);
        ArrayList<Location> sight = new ArrayList<>();
        for (Block block : sightBlock) {
            sight.add(block.getLocation());
        }
        for (Location location : sight) {
            for (Entity entity : entities) {
                if ((Math.abs(entity.getLocation().getX() - location.getX()) < 1.3D) &&
                        (Math.abs(entity.getLocation().getY() - location.getY()) < 1.5D) &&
                        (Math.abs(entity.getLocation().getZ() - location.getZ()) < 1.3D)) {
                    return entity;
                }
            }
        }
        return null;
    }
}
