package com.philderbeast.prisonpicks; 

import static org.junit.Assert.* ;

import org.bukkit.Material;

import org.junit.Test; 

public class PriorityTest {

    @Test
    public void testPriorityEmerald()
    {
        assertTrue(Priority.getPriority(Material.EMERALD_ORE).equals(Priority.EMERALD));
    }

        @Test
    public void testPriorityNone()
    {
        assertTrue(Priority.getPriority(Material.CHEST).equals(Priority.NONE));
    }

}