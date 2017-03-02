package com.philderbeast.prisonpicks; 

import org.bukkit.Material; 
import org.bukkit.inventory.ItemStack; 

public enum Priority
{
    NONE(0, Material.AIR, Material.AIR, 0, 0, 0, 0), 
    COAL(1, Material.COAL_ORE, Material.COAL, 1, 1, 1, 4), 
    IRON(2, Material.IRON_ORE, Material.IRON_ORE, 1, 1, 1, 1), 
    REDSTONE(3, Material.REDSTONE_ORE, Material.REDSTONE, 1, 4, 4, 9), 
    LAPIS(4, Material.LAPIS_ORE, new ItemStack(Material.INK_SACK, 1, (short)4), 1, 4, 4, 9), 
    GOLD(5, Material.GOLD_ORE, Material.GOLD_ORE, 1, 1, 1, 1), 
    QUARTZ(6, Material.QUARTZ_ORE, Material.QUARTZ, 1, 1, 1, 1), 
    DIAMOND(7, Material.DIAMOND_ORE, Material.DIAMOND, 1, 1, 1, 4), 
    DIAMONDBLOCK(8, Material.DIAMOND_BLOCK, Material.DIAMOND_BLOCK, 1, 1, 1, 1), 
    EMERALD(9, Material.EMERALD_ORE, Material.EMERALD, 1, 1, 1, 4); 

    int level; 
    Material mat; 
    ItemStack drop; 
    int amtMin; 
    int amtMax; 
    int amtFMin; 
    int amtFMax; 

    private Priority(int level, Material mat, Material drop, int amtMin, int amtMax, int amtFMin, int amtFMax)
    {
        this.level = level; 
        this.mat = mat; 
        this.drop = new ItemStack((Material)drop); 
        this.amtMin = amtMin; 
        this.amtMax = amtMax; 
        this.amtFMin = amtFMin; 
        this.amtFMax = amtFMax; 
    }

    private Priority(int level, Material mat, ItemStack drop, int amtMin, int amtMax, int amtFMin, int amtFMax)
    {
        this.level = level; 
        this.mat = mat; 
        this.drop = drop; 
        this.amtMin = amtMin; 
        this.amtMax = amtMax; 
        this.amtFMin = amtFMin; 
        this.amtFMax = amtFMax; 
    }

    public static Priority getPriority(Material mat)
    {
        Priority[] arrpriority = Priority.values(); 
        int n = arrpriority.length; 
        int n2 = 0; 
        while (n2 < n)
        {
            Priority p = arrpriority[n2]; 
            if (p.mat == mat)
            {
                return p; 
            } ++n2; 
        }
        return NONE; 
    }
}

