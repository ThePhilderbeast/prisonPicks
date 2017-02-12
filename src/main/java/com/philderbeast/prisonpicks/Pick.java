package com.philderbeast.prisonpicks;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;


//NMS
import net.minecraft.server.v1_11_R1.BlockOre;
import net.minecraft.server.v1_11_R1.BlockRedstoneOre;
import net.minecraft.server.v1_11_R1.Item;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftMagicNumbers;

public class Pick{

    public Pick()
    {

    }

    public void breakBlock()
    {
    }

    public static boolean isPick(ItemStack item)
    {
        if(item != null && item.getType() == Material.DIAMOND_PICKAXE 
            && item.hasItemMeta() 
            && item.getItemMeta().hasLore())
        {
            return true;
        }else
        {
            return false;
        }
    }

    public static int getDropAmount(int enchantmentLevel, Block block) {
        if (block.getType().name().contains("ORE") && !block.getType().name().contains("REDSTONE")) {
            net.minecraft.server.v1_11_R1.BlockOre nmsBlock = (BlockOre) CraftMagicNumbers.getBlock(block);

            int all_final = 0;
            Random rand = new Random();
            if(enchantmentLevel > 0 && Item.getItemOf(nmsBlock) != nmsBlock.getDropType(nmsBlock.getBlockData() ,rand, enchantmentLevel)) {
                all_final = rand.nextInt(enchantmentLevel + 2) - 1;
                if(all_final < 0) {
                    all_final = 0;
                }

                return nmsBlock.a(rand) * (all_final + 1);
            } else {
                return nmsBlock.a(rand);
            }
        } else if (block.getType().name().contains("REDSTONE")) {
            //Ensure we don't crash when mining Redstone
            net.minecraft.server.v1_11_R1.BlockRedstoneOre nmsBlock = (BlockRedstoneOre) CraftMagicNumbers.getBlock(block);

            int all_final = 0;
            Random rand = new Random();
            if(enchantmentLevel > 0 && Item.getItemOf(nmsBlock) != nmsBlock.getDropType(nmsBlock.getBlockData() ,rand, enchantmentLevel)) {
                all_final = rand.nextInt(enchantmentLevel + 2) - 1;
                if(all_final < 0) {
                    all_final = 0;
                }

                return nmsBlock.a(rand) * (all_final + 1);
            } else {
                return nmsBlock.a(rand);
            }
        } else {
            return 1;
        }
    }

}