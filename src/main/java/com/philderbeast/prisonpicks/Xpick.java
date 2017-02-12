package com.philderbeast.prisonpicks;

import java.util.Map;
import java.util.ArrayList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import com.philderbeast.prisonpicks.PrisonPicks;

public class Xpick extends Pick {

    public static boolean isPick(ItemStack item)
    {
        if(Pick.isPick(item) && item.getItemMeta().getLore().contains((Object)ChatColor.GOLD + "Explosive I"))
        {
            return true;
        }else {
            return false;
        }
    }

    public void breakBlock(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ArrayList<Location> locations = new  ArrayList<Location>();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (PrisonPicks.canBuild(event.getBlock().getLocation()))
        {
            Location center = event.getBlock().getLocation();

            center.getWorld().playEffect(center, Effect.EXPLOSION_LARGE, 1);
            center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

            int radius = 2;

            int bX = center.getBlockX();
            int bY = center.getBlockY();
            int bZ = center.getBlockZ();

            Location centerloc = new Location(center.getWorld(), center.getBlockX(), center.getBlockY(), center.getBlockZ());

            int x = bX - radius;
            while (x < bX + radius) {
                int y = bY - radius;
                while (y < bY + radius) {
                    int z = bZ - radius;
                    while (z < bZ + radius) {

                        double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
                        Location block = new Location(center.getWorld(), (double)x, (double)y, (double)z);
                        if (distance < (double)(radius * radius) 
                                && PrisonPicks.canBuild(block) 
                                && (!block.equals(centerloc))
                                && (block.getBlock().getType() != Material.BEDROCK)
                                && (block.getBlock().getType() != Material.AIR)) {
                            locations.add(block);
                        }
                        ++z;
                    }
                    ++y;
                }
                ++x;
            }

            Map<String, Boolean> enchants = getEnchantments(item);

            item = doDamage(enchants.get(Pick.UNBREAKING), item);
            doBreak(event.getBlock(), enchants, player);

            for (Location l : locations) {
                Block block = player.getWorld().getBlockAt(l);
                item = doDamage(enchants.get(Pick.UNBREAKING), item);
                doBreak(block, enchants, player);
            }
            player.getInventory().getItemInMainHand().setDurability(item.getDurability());
        }
    }

}