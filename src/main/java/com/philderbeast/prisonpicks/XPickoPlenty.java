package com.philderbeast.prisonpicks;


import java.util.Map;
import java.util.ArrayList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
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

    Priority p;
    Material mat = Priority.NONE.mat;

    public static boolean isPick(ItemStack item)
    {
        return (Pick.isPick(item) && item.getItemMeta().getLore().contains(ChatColor.GOLD + "Explosive" + ChatColor.LIGHT_PURPLE + " Pick o'Plenty"));
    }

    public void breakBlock(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ArrayList < Location > locations = new  ArrayList <> ();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (Util.canBuild(player, event.getBlock().getLocation()))
        {
            Location center = event.getBlock().getLocation();

            center.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, center, 1);
            center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);

            int radius = 2;
            int bX = center.getBlockX();
            int bY = center.getBlockY();
            int bZ = center.getBlockZ();
            int level = 0;
            int x = bX - radius;

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
                            && Util.canBuild(player, block)
                            && (block.getBlock().getType() != Material.BEDROCK)
                            && (block.getBlock().getType() != Material.AIR))
                        {
                            locations.add(block);
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

            Map < String, Boolean > enchants = getEnchantments(item);
            p = Priority.getPriority(mat);
            for (Location l:locations)
            {
                doDamage(enchants.get(Pick.UNBREAKING), player);
                if (player.getInventory().getItemInMainHand() != null)
                {
                    Block block = player.getWorld().getBlockAt(l);
                    if(level > 0)
                    {
                        doBreak(block, enchants, player, p.mat);
                    } else
                    {
                        doBreak(block, enchants, player, null);
                    }

                    if ( !l.equals(event.getBlock().getLocation()))
                    {
                        BlockBreakEvent newEvent = new BlockBreakEvent(block, player);
                        Bukkit.getPluginManager().callEvent(newEvent);
                    }
                }
            }
        }
    }
}
