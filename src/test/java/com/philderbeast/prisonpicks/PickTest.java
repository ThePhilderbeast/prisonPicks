package com.philderbeast.prisonpicks; 

import java.util.Map; 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Collection;

import org.junit.Before; 
import org.junit.Test; 

import org.mockito.Mock;
import org.mockito.Mockito;
 
import org.mockito.MockitoAnnotations; 
import org.mockito.ArgumentCaptor;

import org.bukkit.block.Block; 
import org.bukkit.Material; 
import org.bukkit.inventory.ItemStack; 
import org.bukkit.inventory.PlayerInventory; 
import org.bukkit.inventory.meta.ItemMeta; 
import org.bukkit.enchantments.Enchantment; 
import org.bukkit.entity.Player; 

import static org.mockito.Mockito.* ; 

import static org.junit.Assert.* ; 

public class PickTest {

    @Mock ItemStack pick; 
    @Mock PlayerInventory inventory;
    @Mock ItemStack tool;
    @Mock Player player;
    @Mock Block block;

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
        
        doReturn(Material.STONE).when(block).getType(); 

        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.COBBLESTONE));
        doReturn(drops).when(block).getDrops(Mockito.anyObject());

        playerSetup();

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 
        
        Pick p = new Pick();

        //test normal picks
        assertTrue(p.doBreak(block, enchants, player, null)); 

        ArgumentCaptor<ItemStack> argumentCaptor = ArgumentCaptor.forClass(ItemStack.class);
        verify(inventory, times(1)).addItem(argumentCaptor.capture());
        ItemStack capturedArgument = argumentCaptor.getValue();
        System.err.println(capturedArgument.getType());
        assertTrue(capturedArgument.getType().equals(Material.COBBLESTONE));

    }

    @Test
    public void doBreakStoneSilkTouchtest() {


        doReturn(Material.STONE).when(block).getType(); 

        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.COBBLESTONE));
        doReturn(drops).when(block).getDrops(Mockito.anyObject());

        playerSetup();

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, true); 

        Pick p = new Pick();
        //test normal picks
        assertTrue(p.doBreak(block, enchants, player, null)); 
        ArgumentCaptor<ItemStack> argumentCaptor = ArgumentCaptor.forClass(ItemStack.class);
        verify(inventory, times(1)).addItem(argumentCaptor.capture());
        ItemStack capturedArgument = argumentCaptor.getValue();
        System.err.println(capturedArgument.getType());
        assertTrue(capturedArgument.getType().equals(Material.STONE));
    }

    @Test
    public void doBreakDiamondSilkTouchtest() {
        
        doReturn(Material.DIAMOND_ORE).when(block).getType(); 

        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.DIAMOND));
        doReturn(drops).when(block).getDrops(Mockito.anyObject());

        playerSetup(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, true); 

        Pick p = new Pick();
        //test normal picks
        assertTrue(p.doBreak(block, enchants, player, null)); 

        //test normal picks
        ArgumentCaptor<ItemStack> argumentCaptor = ArgumentCaptor.forClass(ItemStack.class);
        verify(inventory, times(1)).addItem(argumentCaptor.capture());
        ItemStack capturedArgument = argumentCaptor.getValue();
        System.err.println(capturedArgument.getType());
        assertTrue(capturedArgument.getType().equals(Material.DIAMOND_ORE));
    }

    @Test
    public void doBreakBedrocktest() {
        
        doReturn(Material.BEDROCK).when(block).getType(); 

        playerSetup(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 

        Pick p = new Pick(); 
        
        //test normal picks
        assertFalse(p.doBreak(block, enchants, player, null)); 
    }

    @Test
    public void doBreakAirtest() {
        doReturn(Material.AIR).when(block).getType(); 

        playerSetup();

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 

        Pick p = new Pick(); 
        
        //test normal picks
        assertFalse(p.doBreak(block, enchants, player, null)); 
    }

    private void playerSetup()
    {
        doReturn(0).when(tool).getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS); 
        doReturn(tool).when(inventory).getItemInMainHand(); 
        doReturn(inventory).when(player).getInventory(); 
    }

}