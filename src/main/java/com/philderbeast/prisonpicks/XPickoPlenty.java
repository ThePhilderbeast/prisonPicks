package com.philderbeast.prisonpicks;

import java.util.ArrayList;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Particle;

public class XPickoPlenty extends Pick
{

    public static boolean isPick(ItemStack item)
    {
        return (Pick.isPick(item)
                && item.getItemMeta().getLore()
                    .contains(Config.EXPLOSIVE_COLOR + "Explosive" + Config.PICK_O_PLENTY_COLOR + " Pick o'Plenty"));
    }

    public void breakBlock(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        ArrayList<BlockState> blocksToBreak = new ArrayList<>();

        if (Util.canBuild(player, event.getBlock().getLocation()))
        {
            Location center = event.getBlock().getLocation();

            center.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, center, 1);
            //TODO: is this the right sound cat?
            center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);

            int radius = 2;
            int bX = center.getBlockX();
            int bY = center.getBlockY();
            int bZ = center.getBlockZ();
            int x = bX - radius;

            while (x < bX + radius)
            {
                int y = bY - radius;
                while (y < bY + radius)
                {
                    int z = bZ - radius;
                    while (z < bZ + radius)
                    {
                        //TODO: this math is painful
                        double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
                        Location location = new Location(center.getWorld(), (double)x, (double)y, (double)z);
                        if (distance < (double)(radius * radius)
                            && Util.canBuild(player, location)
                            && (location.getBlock().getType() != Material.BEDROCK)
                            && (location.getBlock().getType() != Material.AIR))
                        {

                            Block block = location.getBlock();
                            BlockState state = block.getState();
                            state.setType(getMaterial(location));
                            blocksToBreak.add(state);

                        } ++ z;
                    } ++ y;
                } ++ x;
            }

            Map<String, Boolean> enchants = getEnchantments(item);
            for (BlockState b : blocksToBreak) {


                Block block = player.getWorld().getBlockAt(b.getLocation());
                b.update(true);

                doDamage(enchants.get(Pick.UNBREAKING), player);
                if(player.getInventory().getItemInMainHand() != null)
                {
                    doBreak(block, enchants, player, null);
                    if (!b.getLocation().equals(event.getBlock().getLocation()))
                    {
                        BlockBreakEvent newEvent = new BlockBreakEvent(block, player);
                        Bukkit.getPluginManager().callEvent(newEvent);
                    }
                }
            }
        }
    }

    private Material getMaterial(Location center)
    {
        int radius = 2;
        int bX = center.getBlockX();
        int bY = center.getBlockY();
        int bZ = center.getBlockZ();
        int level = 0;
        int x = bX - radius;

        Material mat = Priority.NONE.mat;

        while (x < bX + radius)
        {
            int y = bY - radius;
            while (y < bY + radius)
            {
                int z = bZ - radius;
                while (z < bZ + radius)
                {

                    double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
                    Location block = new Location(center.getWorld(), (double)x, (double)y, (double)z);
                    if (distance < (double)(radius * radius)
                            && (block.getBlock().getType() != Material.BEDROCK)
                            && (block.getBlock().getType() != Material.AIR))
                    {
                        if ((level < Priority.getPriority(block.getWorld().getBlockAt(block).getType()).level)
                                &&  ! block.getBlock().hasMetadata("blockBreaker"))
                        {

                            mat = block.getWorld().getBlockAt(block).getType();
                            level = Priority.getPriority(mat).level;
                        }
                    } ++ z;
                } ++ y;
            } ++ x;
        }
        return mat;
    }
}
