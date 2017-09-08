package com.philderbeast.prisonpicks;

import java.util.Map;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Xpick extends Pick
{

    public static boolean isPick(ItemStack item)
    {
        return(Pick.isPick(item) && item.getItemMeta().getLore().contains(Config.EXPLOSIVE_COLOR + "Explosive I"));
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

            Location centerloc = new Location(center.getWorld(), center.getBlockX(), center.getBlockY(), center.getBlockZ());

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
                            && ( ! block.equals(centerloc))
                            && (block.getBlock().getType() != Material.BEDROCK)
                            && (block.getBlock().getType() != Material.AIR))
                        {
                            locations.add(block);
                        } ++ z;
                    } ++ y;
                } ++ x;
            }

            Map < String, Boolean > enchants = getEnchantments(item);

            doDamage(enchants.get(Pick.UNBREAKING), player);
            doBreak(event.getBlock(), enchants, player, null);

            for (Location l:locations)
            {
                //check to see if the pick broke
                if (player.getInventory().getItemInMainHand() != null)
                {
                    Block block = player.getWorld().getBlockAt(l);
                    doDamage(enchants.get(Pick.UNBREAKING), player);
                    doBreak(block, enchants, player, null);
                    BlockBreakEvent newEvent = new BlockBreakEvent(block, player);
                    Bukkit.getPluginManager().callEvent(newEvent);
                }
            }
        }
    }
}