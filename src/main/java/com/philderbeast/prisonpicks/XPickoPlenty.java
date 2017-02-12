package com.philderbeast.prisonpicks;


import java.util.Map;
import java.util.ArrayList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import com.philderbeast.prisonpicks.PrisonPicks;

public class XPickoPlenty extends Pick{

    Priority p;
    Material mat = Priority.NONE.mat;

    public static boolean isPick(ItemStack item)
    {
        if(Pick.isPick(item) 
            && item.getItemMeta().getLore().contains((Object)ChatColor.GREEN + "Explosive Pick o'Plenty"))
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

            int level = 0;

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
                                && (block.getBlock().getType() != Material.BEDROCK)
                                && (block.getBlock().getType() != Material.AIR)
                                && !block.getBlock().hasMetadata("blockBreaker")) {
                                locations.add(block);
                                if (level < Priority.getPriority(block.getWorld().getBlockAt(block).getType()).level)
                                {
                                    mat = block.getWorld().getBlockAt(block).getType();
                                    level = Priority.getPriority(mat).level;
                                }
                                
                            }
                            ++z;
                        }
                        ++y;
                    }
                    ++x;
                }

                Map<String, Boolean> enchants = getEnchantments(item);

                int fortune = 0;
                if (enchants.get(Pick.FORTUNE))
                {
                    fortune = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                }
                p = Priority.getPriority(mat);
                //doDamage(enchants.get(Pick.UNBREAKING), player);
                //doBreak(event.getBlock(), enchants, player, p.mat);
                for (Location l : locations) {

                    if(player.getInventory().getItemInMainHand() != null)
                    {   
                        Block block = player.getWorld().getBlockAt(l);
                        doDamage(enchants.get(Pick.UNBREAKING), player);
                        doBreak(block, enchants, player, p.mat);
                    }
                }
            }
        }
    }
