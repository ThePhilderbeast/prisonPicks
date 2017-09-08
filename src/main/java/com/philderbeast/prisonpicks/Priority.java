package com.philderbeast.prisonpicks; 

import org.bukkit.Material;

public enum Priority
{
    NONE(0, Material.AIR),
    COAL(1, Material.COAL_ORE),
    IRON(2, Material.IRON_ORE),
    REDSTONE(3, Material.REDSTONE_ORE),
    LAPIS(4, Material.LAPIS_ORE),
    GOLD(5, Material.GOLD_ORE),
    QUARTZ(6, Material.QUARTZ_ORE),
    DIAMOND(7, Material.DIAMOND_ORE),
    DIAMONDBLOCK(8, Material.DIAMOND_BLOCK),
    EMERALD(9, Material.EMERALD_ORE);

    final int level;
    final Material mat;

    Priority(int level, Material mat)
    {
        this.level = level;
        this.mat = mat;
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

