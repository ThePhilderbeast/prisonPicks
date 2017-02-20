package com.philderbeast.prisonpicks;

import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;


public class Pickoplenty extends Pick{

    public static boolean isPick(ItemStack item)
    {
        if(Pick.isPick(item) && item.getItemMeta().getLore().contains((Object)ChatColor.LIGHT_PURPLE + "Pick o'Plenty"))
        {
            return true;
        }else {
            return false;
        }

    }

    @Override
    public void breakBlock(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if (!block.hasMetadata("blockBreaker") && PrisonPicks.getWorldGuard().canBuild(player, block)) {
            Location center = event.getBlock().getLocation();
            int radius = 2;
            int bX = center.getBlockX();
            int bY = center.getBlockY();
            int bZ = center.getBlockZ();
            Material mat = block.getType();
            int level = 0;
            int x = bX - radius;
            while (x < bX + radius) {
                int y = bY - radius;
                while (y < bY + radius) {
                    int z = bZ - radius;
                    while (z < bZ + radius) {
                        Location check = new Location(center.getWorld(), (double)x, (double)y, (double)z);
                        double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
                        if (distance < (double)(radius * radius) 
                            && PrisonPicks.canBuild(check)
                            && (check.getBlock().getType() != Material.BEDROCK)
                            && (check.getBlock().getType() != Material.AIR)
                            && (!check.getBlock().hasMetadata("blockBreaker"))
                            && Priority.getPriority((Material)check.getBlock().getType()).level >= level) 
                        {
                            mat = check.getBlock().getType();
                            level = Priority.getPriority(mat).level;
                        }
                        ++z;
                    }
                    ++y;
                }
                ++x;
            }
            if (mat != block.getType() && level > 0) {
                event.setCancelled(true);
                Map<String, Boolean> enchants = getEnchantments(item);

                Priority p = Priority.getPriority(mat);

                doDamage(enchants.get("unbreaking"), player);
                doBreak(event.getBlock(), enchants, player, mat);

                block.setType(Material.AIR);
            }
        }
        if (block.hasMetadata("blockBreaker")) {
            block.removeMetadata("blockBreaker", (Plugin)PrisonPicks.getInstance());
        }
    }
}