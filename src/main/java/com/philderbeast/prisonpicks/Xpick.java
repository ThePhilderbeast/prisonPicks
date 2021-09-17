package com.philderbeast.prisonpicks;

import java.util.Map;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.inventory.meta.ItemMeta;

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

            short breakEmerald = (short) (event.getBlock().getType() == Material.EMERALD_ORE ? 1 : 0);

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
                            if (block.getBlock().getType() == Material.EMERALD_ORE )
                            {
                                breakEmerald += 1;
                            }
                            locations.add(block);
                        } ++ z;
                    } ++ y;
                } ++ x;
            }

            if (breakEmerald > 0)
            {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "RIP That Emerald"));
                item.setItemMeta(increaseNBTCount(item.getItemMeta(), Config.EMERALDS_EXPLODED, (long)breakEmerald));
            }

            Map < String, Boolean > enchants = getEnchantments(item);

            doDamage(enchants.get(Pick.UNBREAKING), player);
            doBreak(event.getBlock(), enchants, player, null, item);

            for (Location l:locations)
            {
                //check to see if the pick broke
                if (player.getInventory().getItemInMainHand() != null)
                {
                    Block block = player.getWorld().getBlockAt(l);
                    doBreak(block, enchants, player, null, item);
                    BlockBreakEvent newEvent = new BlockBreakEvent(block, player);
                    Bukkit.getPluginManager().callEvent(newEvent);
                }
            }
            doDamage(enchants.get(Pick.UNBREAKING), player, locations.size());
            ItemMeta meta = item.getItemMeta();
            meta = increaseNBTCount(meta, Config.BLOCKS_BROKEN, (long)locations.size()+1);
            item.setItemMeta(updateLore(meta));
        }
    }
}
