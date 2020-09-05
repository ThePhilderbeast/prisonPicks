package com.philderbeast.prisonpicks; 

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before; 

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
  
import org.bukkit.inventory.ItemStack; 
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
 
import org.bukkit.entity.Player;

import static org.mockito.Mockito.* ; 
import static org.junit.Assert.* ;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class UtilTest {

    @Mock private Player player;
    @Mock private ItemFactory itemFactory;
    @Mock private ItemMeta itemMeta;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
        when(itemFactory.getItemMeta(Material.DIAMOND_PICKAXE)).thenReturn(itemMeta);
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
    
    @Test
    public void testCreateItemStack()
    {
        //TODO: look at what else we want to check here
        ItemStack pick = Util.createItemStack("Explosive I");
        assertTrue(pick.getType().equals(Material.DIAMOND_PICKAXE));
    }
    
}