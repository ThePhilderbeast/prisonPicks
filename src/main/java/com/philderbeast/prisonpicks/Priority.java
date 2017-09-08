package com.philderbeast.prisonpicks; 

import org.bukkit.Material;

public enum Priority
{
    NONE(0, Material.AIR),
    COAL(Config.COAL_PRIORITY, Material.COAL_ORE),
    IRON(Config.IRON_PRIORITY, Material.IRON_ORE),
    REDSTONE(Config.REDSTONE_PRIORITY, Material.REDSTONE_ORE),
    LAPIS(Config.LAPIS_PRIORITY, Material.LAPIS_ORE),
    GOLD(Config.GOLD_PRIORITY, Material.GOLD_ORE),
    QUARTZ(Config.QUARTZ_PRIORITY, Material.QUARTZ_ORE),
    DIAMOND(Config.DIAMOND_PRIORITY, Material.DIAMOND_ORE),
    DIAMOND_BLOCK(Config.DIAMOND_BLOCK_PRIORITY, Material.DIAMOND_BLOCK),
    EMERALD(Config.EMERALD_PRIORITY, Material.EMERALD_ORE);

    final int level;
    final Material mat;

    Priority(int level, Material mat)
    {
        this.level = level;
        this.mat = mat;
    }


    public static Priority getPriority(Material mat)
    {
        Priority[] arrayPriority = Priority.values();
        int n = arrayPriority.length;
        int n2 = 0; 
        while (n2 < n)
        {
            Priority p = arrayPriority[n2];
            if (p.mat == mat)
            {
                return p; 
            } ++n2; 
        }
        return NONE; 
    }
}

