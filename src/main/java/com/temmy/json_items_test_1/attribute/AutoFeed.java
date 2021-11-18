package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.FoodDetails;
import com.temmy.json_items_test_1.util.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class AutoFeed {
    private AutoFeed(){
    }

    static Logger log = Main.getPlugin().getLogger();

    public static void trigger(Event e, String[] args){
        if (!(e instanceof FoodLevelChangeEvent event)) return;
        //if (!(event.getEntity() instanceof Player player)) return;
        Player player = (Player) event.getEntity();
        ItemStack itemstack = null;
        BundleMeta bMeta = null;
        for (ItemStack item : event.getEntity().getInventory().getContents()){
            if (item == null || item.getType() != Material.BUNDLE) continue;
            ItemMeta meta = item.getItemMeta();
            if (meta.getPersistentDataContainer().has(Attribute.namespacedKey, PersistentDataType.STRING)){
                Map<String, String[]> attributeMap = ItemUtils.getItemAttributeMap(meta.getPersistentDataContainer());
                if (attributeMap.containsKey("AUTOFEED")) bMeta = (BundleMeta) meta;
                itemstack = item;
                break;
            }
        }
        if (player.getFoodLevel() < event.getFoodLevel() || !bMeta.hasItems()) return;
        List<ItemStack> items = bMeta.getItems();
        for (ItemStack item : items){
            if (item == null) continue;
            if (item.getType().isEdible()) {
                if (Main.getFoodMap().containsKey(item.getType())) {
                    FoodDetails food = Main.getFoodMap().get(item.getType());
                    player.setFoodLevel(player.getFoodLevel() + food.getFood());
                    player.setSaturation(player.getSaturation() + food.getSaturation());
                    if (!food.getAddingEffects().isEmpty())
                        for (PotionEffect effect : food.getAddingEffects()) {
                            player.addPotionEffect(effect);
                        }
                    if (!food.getRemovingEffects().isEmpty())
                        for (PotionEffect effect : food.getRemovingEffects()){
                            player.removePotionEffect(effect.getType());
                    }
                    if (food.getChorusTeleportation()) {
                        boolean b = false;
                        int i = 0;
                        while (!b){
                            if (i > 16) b = true;
                            Random rnd = new Random();
                            int PosX = rnd.nextInt(8);
                            int NegX = rnd.nextInt(8);
                            int PosZ = rnd.nextInt(8);
                            int NegZ = rnd.nextInt(8);
                            PosX = PosX - NegX;
                            PosZ = PosZ - NegZ;
                            Location loc = new Location(player.getWorld(),
                                player.getLocation().getX() - PosX,
                                player.getLocation().getY() + rnd.nextInt(8),
                                player.getLocation().getZ() - PosZ);
                            Location loc1 = loc;
                            loc1.setY(loc.getY() + 1);
                            if (player.getWorld().getBlockAt(loc).getType().isAir() && player.getWorld().getBlockAt(loc1).getType().isAir()){
                                player.teleport(loc, PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT);
                                break;
                            }
                            i++;
                        }
                    }
                    if (food.getFood() > 0) {
                        event.setFoodLevel(event.getEntity().getFoodLevel()+food.getFood());
                    }
                    if (food.getSaturation() > 0 ) {
                        player.setSaturation(player.getSaturation() + food.getSaturation());
                    }
                    if (item.getAmount() == 1) item.setAmount(0);
                    else item.setAmount(item.getAmount()-1);
                }
            }
            itemstack.setItemMeta(bMeta);
        }

    }
}
