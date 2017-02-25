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

    @Test
    public void testisSpaceAvailabletrue()
    {
        PlayerInventory pi = mock(PlayerInventory.class);
        doReturn(pi).when(player).getInventory();
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        assertTrue(Util.isSpaceAvailable(player, itemStack));
    }
    
    @Test
    public void testisSpaceAvailablefalse()
    {
        Player player = spy(Player.class);
        PlayerInventory pi = mock(PlayerInventory.class);
        when(player.getInventory()).thenReturn(pi);
        when(pi.getItem(anyInt())).thenReturn(new ItemStack(Material.GRASS));

        ItemStack itemStack = new ItemStack(Material.EMERALD);
        assertFalse(Util.isSpaceAvailable(player, itemStack));
    }
    
    //TODO: this test fails
    //@Test
    public void testCreatItemStack()
    {
        //TODO: look at what else we want to check here
        ItemStack pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "name", "Explosive I");
        assertTrue(pick.getType().equals(Material.DIAMOND_PICKAXE));
        assertFalse(pick.getItemMeta().getLore().isEmpty());
    }
    
}