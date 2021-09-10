package com.philderbeast.prisonpicks;

import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;


public class Pickoplenty extends Pick
{

    public static boolean isPick(ItemStack item)
    {
        return(Pick.isPick(item) && item.getItemMeta().getLore().contains(Config.PICK_O_PLENTY_COLOR + "Pick o'Plenty"));

    }

    @Override
    public void breakBlock(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();
        

        if ( !block.hasMetadata("blockBreaker")
                && Util.canBuild(player, block.getLocation()))
        {
            Location center = event.getBlock().getLocation();
            int radius = 2;
            int bX = center.getBlockX();
            int bY = center.getBlockY();
            int bZ = center.getBlockZ();
            Material mat = block.getType();
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
                        Location check = new Location(center.getWorld(), (double)x, (double)y, (double)z);
                        double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
                        if (distance < (double)(radius * radius)
                            && Util.canBuild(player, check)
                            && (check.getBlock().getType() != Material.BEDROCK)
                            && (check.getBlock().getType() != Material.AIR)
                            && ( ! check.getBlock().hasMetadata("blockBreaker"))
                            && Priority.getPriority(check.getBlock().getType()).level >= level)
                        {
                            mat = check.getBlock().getType();
                            level = Priority.getPriority(mat).level;
                            
                        } ++z;
                    } ++y;
                } ++x;
            }
            if ( ! block.hasMetadata("blockBreaker"))
            {
                Map < String, Boolean > enchants = getEnchantments(item);

                doDamage(enchants.get("unbreaking"), player);
                doBreak(event.getBlock(), enchants, player, mat);

                block.setType(Material.AIR);
            }else
            {
                Map < String, Boolean > enchants = getEnchantments(item);

                doDamage(enchants.get("unbreaking"), player);
                doBreak(event.getBlock(), enchants, player, null);
                block.removeMetadata("blockBreaker", PrisonPicks.getInstance());
                block.setType(Material.AIR);
            }
        }
    }

    @Override
    protected char[] parseInt(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}