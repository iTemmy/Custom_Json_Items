package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings("RegExpRedundantEscape")
public class Hoe {

    public Hoe(){}

    public static void trigger(Event e, String[] args){
        if (e instanceof BlockBreakEvent event) blockBreak(event, args);
        if (e instanceof PlayerInteractEvent event) blockInteract(event, args);
    }

    private static void blockBreak(BlockBreakEvent e, String[] args){
        int radius = 0;
        int depth = 0;
        List<Material> materials = new ArrayList<>();
        for (String s : args){
            s = s.replaceAll("[\"\\{\\}\\[]", "");
            String[] ss = s.split(",");
            for (String arg : ss){
                try {
                    if (arg.toLowerCase().contains("radius")) {
                        arg = arg.toLowerCase().replaceAll("radius:", "");
                        radius = Integer.parseInt(arg);
                    } else if (arg.toLowerCase().contains("depth")) {
                        arg = arg.toLowerCase().replaceAll("depth", "");
                        depth = Integer.parseInt(arg);
                    }else if (arg.toLowerCase().contains("blocks")){
                        arg = arg.toLowerCase().replaceAll("blocks:", "");
                        for (String b : arg.split(";"))
                            materials.add(Material.valueOf(b.toUpperCase()));
                    }
                } catch (NumberFormatException ex) {
                    if (Main.debug) ex.printStackTrace();
                }
            }
        }
        blockBreakOrInteract(e.getPlayer(), e.getBlock(), radius, depth, true, materials, Material.AIR, new ArrayList<>());
    }

    @SuppressWarnings("ConstantConditions")
    private static void blockInteract(PlayerInteractEvent e, String[] args){
        if (e.getClickedBlock() == null) return;
        int radius = 0;
        int depth = 0;
        List<Material> breakableBlocks = new ArrayList<>();
        List<Material> replaceableBlocks = new ArrayList<>();
        Material replaceBlock = null;
        for (String s : args){
            Main.getPlugin().getLogger().info("s: --> "+s);
            s = s.replaceAll("[\"\\{\\}\\[]", "");
            String[] ss = s.split(",");
            for (String arg : ss){
                try {
                    if (arg.toLowerCase().contains("radius")) {
                        arg = arg.toLowerCase().replaceAll("radius:", "");
                        radius = Integer.parseInt(arg);
                    } else if (arg.toLowerCase().contains("depth")) {
                        arg = arg.toLowerCase().replaceAll("depth:", "");
                        depth = Integer.parseInt(arg);
                    }else if (arg.toLowerCase().contains("blocks")){
                        arg = arg.toLowerCase().replaceAll("blocks:", "");
                        for (String b : arg.split(";"))
                            breakableBlocks.add(Material.valueOf(b.toUpperCase()));
                    }else if (arg.toLowerCase().contains("replace_old:")){
                        arg = arg.toLowerCase().replaceAll("replace_old:","");
                        for (String b : arg.split(";"))
                            replaceableBlocks.add(Material.valueOf(b.toUpperCase()));
                    }else if (arg.toLowerCase().contains("replace_new:")){
                        arg = arg.toLowerCase().replaceAll("replace_new:", "");
                        replaceBlock = Material.valueOf(arg.toUpperCase());
                    }
                } catch (IllegalArgumentException ex ) {
                    if (Main.debug) {
                        Main.getPlugin().getLogger().log(Level.WARNING,
                                String.format("Unable to complete %s event for %s using %s", e.getEventName(),
                                        e.getPlayer(),
                                        PlainTextComponentSerializer.plainText().serialize(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().displayName())), ex);
                    }
                }
            }
        }
        blockBreakOrInteract(e.getPlayer(), e.getClickedBlock(), radius, depth, false, breakableBlocks, replaceBlock, replaceableBlocks);
    }

    @SuppressWarnings("ConstantConditions")
    public static void blockBreakOrInteract(Player player, Block eBlock, int radius, int depth, boolean mine, List<Material> materials, Material replaceBlock, List<Material> replaceableBlocks){
        RayTraceResult result = player.rayTraceBlocks(6);
        if (result.getHitBlock() == null) return;
        List<Block> blocks = getBlocksInDirection(eBlock, result.getHitBlockFace().getDirection(), radius, depth);
        for (Block block : blocks) {
            if (!block.getType().isAir()){
                if (mine && materials.contains(block.getType()))
                    block.breakNaturally(player.getInventory().getItemInMainHand());
                else if (!mine && replaceableBlocks.contains(block.getType())){
                    block.setType(replaceBlock);
                }
            }
        }
    }


    public static List<Block> getBlocksInDirection(Block block, Vector dirVector, int radius, int depth){
        List<Block> blocks = new ArrayList<>();
        int vecX = (int)dirVector.getX();
        int vecY = (int)dirVector.getY();
        int vecZ = (int)dirVector.getZ();


        for (int i = 0; i < depth; i++){
            for (int x = -radius; x <= radius; x++){
                for (int y = -radius; y <= radius; y++){
                    for (int z = -radius; z <= radius; z++){
                        Block relativeBlock = block.getRelative(x * boolToInt(vecX == 0), y * boolToInt(vecY == 0), z * boolToInt(vecZ == 0));
                        if (relativeBlock.getType().isSolid())
                            blocks.add(relativeBlock);
                    }
                }
            }
            block = block.getRelative(-vecX, -vecY, -vecZ);
        }
        return blocks;
    }

    private static int boolToInt(boolean b){
        return Boolean.compare(b, false);
    }
}
