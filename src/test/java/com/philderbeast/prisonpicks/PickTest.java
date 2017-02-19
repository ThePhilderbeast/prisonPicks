package com.philderbeast.prisonpicks; 

import java.util.Map; 
import java.util.HashMap; 

import org.junit.Before; 
import org.junit.Test; 

import org.mockito.Mock; 
import org.mockito.MockitoAnnotations; 

import org.bukkit.block.Block; 
import org.bukkit.Material; 
import org.bukkit.inventory.ItemStack; 
import org.bukkit.inventory.PlayerInventory; 
import org.bukkit.inventory.meta.ItemMeta; 
import org.bukkit.enchantments.Enchantment; 
import org.bukkit.entity.Player; 


import static org.mockito.Mockito. * ; 

import static org.junit.Assert. * ; 

public class PickTest {

    @Mock ItemStack pick; 

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); 
    }

    @Test
    public void isPickTest() {
        //make a mock Xpick
        ItemMeta i = mock(ItemMeta.class); 
        doReturn(true).when(i).hasLore(); 

        doReturn(Material.DIAMOND_PICKAXE).when(pick).getType(); 
        doReturn(true).when(pick).hasItemMeta(); 
        doReturn(i).when(pick).getItemMeta(); 

        //this should work
        assertTrue(Pick.isPick(pick)); 
        
        //no lore
        doReturn(false).when(i).hasLore(); 
        assertFalse(Pick.isPick(pick)); 

        //no metadata
        doReturn(true).when(i).hasLore(); 
        doReturn(false).when(pick).hasItemMeta(); 
        assertFalse(Pick.isPick(pick)); 
    }

    @Test
    public void hasUnbreakingTest() {
        doReturn(true).when(pick).containsEnchantment(Enchantment.DURABILITY); 
        
        Pick p = new Pick(); 

        Map < String, Boolean > enchants = p.getEnchantments(pick); 

        assertTrue(enchants.get(Pick.UNBREAKING)); 
        assertFalse(enchants.get(Pick.FORTUNE)); 
        assertFalse(enchants.get(Pick.SILK_TOUCH)); 
    }

    @Test
    public void hasFortuneTest() {
        doReturn(true).when(pick).containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS); 
        
        Pick p = new Pick(); 

        Map < String, Boolean > enchants = p.getEnchantments(pick); 

        assertFalse(enchants.get(Pick.UNBREAKING)); 
        assertTrue(enchants.get(Pick.FORTUNE)); 
        assertFalse(enchants.get(Pick.SILK_TOUCH)); 
    }

    @Test
    public void hasSilkTouchTest() {
        doReturn(true).when(pick).containsEnchantment(Enchantment.SILK_TOUCH); 
        
        Pick p = new Pick(); 
        Map < String, Boolean > enchants = p.getEnchantments(pick); 

        assertFalse(enchants.get(Pick.UNBREAKING)); 
        assertFalse(enchants.get(Pick.FORTUNE)); 
        assertTrue(enchants.get(Pick.SILK_TOUCH)); 
    }

    @Test
    public void doBreakStonetest() {
        
        Block block = mock(Block.class); 
        doReturn(Material.STONE).when(block).getType(); 

        ItemStack tool = mock(ItemStack.class); 
        doReturn(0).when(tool).getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS); 

        PlayerInventory inventory = mock(PlayerInventory.class); 
        doReturn(tool).when(inventory).getItemInMainHand(); 

        Player player = mock(Player.class); 
        doReturn(inventory).when(player).getInventory(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 

        Pick p = new Pick(); 
        
        //test normal picks
        //assertTrue(bloc);
        assertTrue(p.doBreak(block, enchants, player, null)); 
    }

    public void doBreakStoneSilkTouchtest() {
        
        Block block = mock(Block.class); 
        doReturn(Material.STONE).when(block).getType(); 

        ItemStack tool = mock(ItemStack.class); 
        doReturn(0).when(tool).getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS); 

        PlayerInventory inventory = mock(PlayerInventory.class); 
        doReturn(tool).when(inventory).getItemInMainHand(); 

        Player player = mock(Player.class); 
        doReturn(inventory).when(player).getInventory(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, true); 

        Pick p = new Pick(); 
        
        //test normal picks
        //assertTrue(bloc);
        assertTrue(p.doBreak(block, enchants, player, null)); 
    }

    @Test
    public void doBreakBedrocktest() {
        
        Block block = mock(Block.class); 
        doReturn(Material.BEDROCK).when(block).getType(); 

        ItemStack tool = mock(ItemStack.class); 
        doReturn(0).when(tool).getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS); 

        PlayerInventory inventory = mock(PlayerInventory.class); 
        doReturn(tool).when(inventory).getItemInMainHand(); 

        Player player = mock(Player.class); 
        doReturn(inventory).when(player).getInventory(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 

        Pick p = new Pick(); 
        
        //test normal picks
        assertFalse(p.doBreak(block, enchants, player, null)); 
    }

    //TODO: expand the block break tests to test what items we get

}