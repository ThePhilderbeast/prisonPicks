package com.philderbeast.prisonpicks;


import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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

    public static boolean isPick(ItemStack item)
    {
        if(Pick.isPick(item) 
            && item.getItemMeta().getLore().contains((Object)ChatColor.GRAY + "Explosive I")
            && item.getItemMeta().getLore().contains((Object)ChatColor.GRAY + "Pick o'Plenty"))
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
                                && (!block.equals(centerloc))) {
                            locations.add(block);
                        }
                        ++z;
                    }
                    ++y;
                }
                ++x;
            }

            boolean unbreaking = false;

            for (Map.Entry entry : item.getEnchantments().entrySet()) {
                if (!((Enchantment)entry.getKey()).getName().equalsIgnoreCase(Enchantment.DURABILITY.getName())) continue;
                unbreaking = true;
            }

            item = doDamage(unbreaking, item);

            boolean bfortune = false;
            boolean bsilk = false;
            //Get Fortune Level
             for (Map.Entry entry : item.getEnchantments().entrySet()) {
                 if (((Enchantment)entry.getKey()).getName().equalsIgnoreCase(Enchantment.LOOT_BONUS_BLOCKS.getName())) {
                     bfortune = true;
                 }
                 if (((Enchantment)entry.getKey()).getName().equalsIgnoreCase(Enchantment.SILK_TOUCH.getName())) {
                     bsilk = true;
                 }
             }
            int fortune = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

            //Protect against Silk Touch and Fortune conflicting
            if (item.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0)
                fortune = 0;

            boolean noInventorySpace = false;

            if (event.getBlock().getType() != Material.BEDROCK) {
                if (bsilk) {
                    ItemStack blockStack = new ItemStack(event.getBlock().getTypeId(), 1);
                    //they have silk touch so give them the block
                    if (Util.isSpaceAvailable(player, blockStack)) {
                        player.getInventory().addItem(blockStack);
                    } else {
                        noInventorySpace = true;
                        //player.getWorld().dropItemNaturally(event.getBlock().getLocation(), newItem);
                    }
                } else {
                    Collection<ItemStack> stacks = event.getBlock().getDrops(item);
                    for (ItemStack newItem : stacks) {
                        if (bfortune) {
                            newItem.setAmount(Pick.getDropAmount(fortune, event.getBlock()));
                        }

                        if (Util.isSpaceAvailable(player, newItem)) {
                            player.getInventory().addItem(newItem);
                        } else {
                            noInventorySpace = true;
                            //player.getWorld().dropItemNaturally(event.getBlock().getLocation(), newItem);
                        }

                        player.giveExp(event.getExpToDrop()); //Give Player Experience
                    }
                    stacks.clear();
                }
                event.getBlock().setType(Material.AIR);
            }

            for (Location l : locations) {
                Block block = player.getWorld().getBlockAt(l);
                if (block.getType() != Material.BEDROCK) {
                    BlockBreakEvent newEvent = new BlockBreakEvent(block, player);
                    Bukkit.getPluginManager().callEvent(newEvent);

                    //make sure the player has spare space in there inventory
                    if (bsilk) {
                        ItemStack blockStack = new ItemStack(block.getTypeId(), 1);
                        //they have silk touch so give them the block
                        if (Util.isSpaceAvailable(player, blockStack)) {
                            player.getInventory().addItem(blockStack);
                        } else {
                            noInventorySpace = true;
                            //player.getWorld().dropItemNaturally(event.getBlock().getLocation(), newItem);
                        }
                    } else {
                        Collection<ItemStack> stacks = block.getDrops(item);

                        for (ItemStack newItem : stacks) {
                            if (bfortune) {
                                newItem.setAmount(Pick.getDropAmount(fortune, block));
                            }

                            if (Util.isSpaceAvailable(player, newItem)) {
                                player.getInventory().addItem(newItem);
                            } else {
                                noInventorySpace = true;
                                //player.getWorld().dropItemNaturally(l, newItem);
                            }

                        }
                        stacks.clear();
                    }

                    item = doDamage(unbreaking, item);

                    player.giveExp(Util.calculateExperienceForBlock(block));
                    block.setType(Material.AIR);
                }
            }
        }
    }
}