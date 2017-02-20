package com.philderbeast.prisonpicks; 

import org.junit.Test;
import org.junit.Before; 

import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;
import org.bukkit.Material;
  

import org.bukkit.block.Block;  
import org.bukkit.inventory.ItemStack; 
import org.bukkit.inventory.PlayerInventory; 
import org.bukkit.enchantments.Enchantment; 
import org.bukkit.entity.Player; 

import static org.mockito.Mockito.* ; 
import static org.junit.Assert.* ;

public class UtilTest {

    @Mock PlayerInventory inventory;
    @Mock ItemStack tool;
    @Mock Player player;
    @Mock Block block;

    @Spy Pick pick;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); 
        //default to fortue 0
    }

    @Test
    public void testRandInt()
    {
        int rand;
        for(int i = 0; i < 100; i++)
        {
            rand = Util.randInt(10, 50);
            assertTrue(rand >= 10 && rand <= 50);
        }
    }

    public void testisSpaceAvailable()
    {
        Player player = spy(Player.class);
        ItemStack itemStack = new ItemStack(Material.EMERALD);

        assertTrue(Util.isSpaceAvailable(player, itemStack));

    }

}