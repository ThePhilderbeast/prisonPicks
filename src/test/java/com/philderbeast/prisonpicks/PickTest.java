package com.philderbeast.prisonpicks; 

import java.util.Map; 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Collection;

import org.junit.Before; 
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.bukkit.block.Block; 
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack; 
import org.bukkit.inventory.PlayerInventory; 
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
 
import org.bukkit.enchantments.Enchantment; 
import org.bukkit.entity.Player; 

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.* ; 

import static org.junit.Assert.* ; 

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class PickTest {

    @Mock PluginManager pluginManager;
    @Mock PlayerInventory inventory;
    @Mock ItemStack tool;
    @Mock Player player;
    @Mock Block block;

    Pick pick;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        doReturn(0).when(tool).getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS); 
        doReturn(tool).when(inventory).getItemInMainHand(); 
        doReturn(inventory).when(player).getInventory(); 

        pick = spy(Xpick.class);

        //bukkit Stub
        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.broadcastMessage(any())).thenReturn(1);
        when(Bukkit.getPluginManager()).thenReturn(pluginManager);
    }

    @Test
    public void testIsPick() {

        //make a mock Xpick
        ItemMeta i = mock(ItemMeta.class); 
        doReturn(true).when(i).hasLore(); 

        doReturn(Material.DIAMOND_PICKAXE).when(tool).getType(); 
        doReturn(true).when(tool).hasItemMeta(); 
        doReturn(i).when(tool).getItemMeta(); 

        //this should work
        assertTrue(Pick.isPick(tool)); 
        
        //no lore
        doReturn(false).when(i).hasLore(); 
        assertFalse(Pick.isPick(tool)); 

        //no metadata
        doReturn(false).when(tool).hasItemMeta(); 
        assertFalse(Pick.isPick(tool)); 
    }

    
    @Test
    public void hasUnbreakingTest() {
        doReturn(true).when(tool).containsEnchantment(Enchantment.DURABILITY);  

        Map < String, Boolean > enchants = pick.getEnchantments(tool); 

        assertTrue(enchants.get(Pick.UNBREAKING)); 
        assertFalse(enchants.get(Pick.FORTUNE)); 
        assertFalse(enchants.get(Pick.SILK_TOUCH)); 
    }

    @Test
    public void hasFortuneTest() {

        doReturn(true).when(tool).containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS); 

        Map < String, Boolean > enchants = pick.getEnchantments(tool); 

        assertFalse(enchants.get(Pick.UNBREAKING)); 
        assertTrue(enchants.get(Pick.FORTUNE)); 
        assertFalse(enchants.get(Pick.SILK_TOUCH)); 
    }

    @Test
    public void hasSilkTouchTest() {
        doReturn(true).when(tool).containsEnchantment(Enchantment.SILK_TOUCH); 

        Map < String, Boolean > enchants = pick.getEnchantments(tool);
        assertFalse(enchants.get(Pick.UNBREAKING));
        assertFalse(enchants.get(Pick.FORTUNE)); 
        assertTrue(enchants.get(Pick.SILK_TOUCH)); 
    }

    @Test
    public void doBreakStonetest() {
        
        doReturn(Material.STONE).when(block).getType(); 

        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.COBBLESTONE));
        doReturn(drops).when(block).getDrops(any());

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 

        //test normal picks
        pick.doBreak(block, enchants, player, null, tool);

        ArgumentCaptor<ItemStack> argumentCaptor = ArgumentCaptor.forClass(ItemStack.class);
        verify(inventory, times(1)).addItem(argumentCaptor.capture());
        ItemStack capturedArgument = argumentCaptor.getValue();
        assertTrue(capturedArgument.getType().equals(Material.COBBLESTONE));
    }

    @Test
    public void doBreakStoneSilkTouchtest() {
        doReturn(Material.STONE).when(block).getType(); 

        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.COBBLESTONE));

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, true); 

        //test normal picks
        pick.doBreak(block, enchants, player, null, tool);
        ArgumentCaptor<ItemStack> argumentCaptor = ArgumentCaptor.forClass(ItemStack.class);
        verify(inventory, times(1)).addItem(argumentCaptor.capture());
        ItemStack capturedArgument = argumentCaptor.getValue();
        assertTrue(capturedArgument.getType().equals(Material.STONE));
    }

    @Test
    public void doBreakAsEmmeraldStonetest() {

        doReturn(Material.STONE).when(block).getType(); 

        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.COBBLESTONE));
        doReturn(drops).when(block).getDrops(any());

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 

        //test normal picks
        pick.doBreak(block, enchants, player, Material.EMERALD_ORE, tool);

        //verify we are settting the block to EMERALD_ORE
        verify(block).setType(Material.EMERALD_ORE);
    }

    
    @Test
    public void doBreakAsEmmeraldStoneSilkTouchtest() {

        doReturn(Material.STONE).when(block).getType(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, true); 

        //test normal picks
        pick.doBreak(block, enchants, player, Material.EMERALD_ORE, tool);

        ArgumentCaptor<ItemStack> argumentCaptor = ArgumentCaptor.forClass(ItemStack.class);
        verify(inventory, times(1)).addItem(argumentCaptor.capture());
        ItemStack capturedArgument = argumentCaptor.getValue();
        assertTrue(capturedArgument.getType().equals(Material.EMERALD_ORE));
    }

    @Test
    public void doBreakBedrocktest() {
        
        doReturn(Material.BEDROCK).when(block).getType(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 
        
        //we shouldnt be able to break this
        pick.doBreak(block, enchants, player, null, tool);
        verify(block, times(0)).setType(Material.AIR);
    }

    @Test
    public void doBreakAirtest() {
        doReturn(Material.AIR).when(block).getType(); 

        Map < String, Boolean > enchants = new HashMap < String, Boolean > (); 
        enchants.put(Pick.UNBREAKING, false); 
        enchants.put(Pick.FORTUNE, false); 
        enchants.put(Pick.SILK_TOUCH, false); 
        
        //we shouldnt be able to break this
        pick.doBreak(block, enchants, player, null, tool);
        verify(block, times(0)).setType(Material.AIR);
    }

    @Test
    public void doDamageTest()
    {
        // doReturn(0).when(tool).getEnchantmentLevel(Enchantment.DURABILITY); 
        // doReturn(Material.DIAMOND_PICKAXE).when(tool).getType();
        // doReturn((short) 0).when(tool).getDurability(); 

        // pick.doDamage(false, player);
        // verify(tool, times(1)).setDurability(anyShort());
    }

    
    @Test
    public void doDamageUnbreakingTest()
    {
        // doReturn(3).when(tool).getEnchantmentLevel(Enchantment.DURABILITY); 
        // doReturn(Material.DIAMOND_PICKAXE).when(tool).getType();
        // doReturn((short) 0).when(tool).getDurability(); 

        // //run this 100 times to check
        // for(int i = 0; i < 100; i++)
        // { 
        //     pick.doDamage(true, player);
        // }
        // //not sure how to test this since its random?
        // verify(tool, atLeast(1)).setDurability(anyShort());
    }

    @Test
    public void doDamageBreakPickTest()
    {
        // doReturn(0).when(tool).getEnchantmentLevel(Enchantment.DURABILITY); 
        // doReturn((short) (Material.DIAMOND_PICKAXE.getMaxDurability() + 1)).when(tool).getDurability(); 
        // doReturn(Material.DIAMOND_PICKAXE).when(tool).getType();

        // pick.doDamage(true, player);
        // verify(inventory).remove(tool);
    }


    @Test
    public void dropIronOreTest()
    {
        doReturn(Material.IRON_ORE).when(block).getType();
        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.IRON_ORE));
        doReturn(drops).when(block).getDrops(any());

        ItemStack dropped = Pick.getDrop(0, block, tool);
        assertTrue(dropped.getType().equals(Material.IRON_ORE));
        assertTrue(dropped.getAmount() == 1);
    }

    @Test
    public void dropRedstoneOreTest()
    {
        doReturn(Material.REDSTONE_ORE).when(block).getType();
        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.REDSTONE, 1));
        drops.add(new ItemStack(Material.REDSTONE, 1));
        drops.add(new ItemStack(Material.REDSTONE, 1));
        drops.add(new ItemStack(Material.REDSTONE, 1));
        doReturn(drops).when(block).getDrops(any());

        //run this 100 times to check
        for(int i = 0; i < 100; i++)
        {   
            ItemStack dropped = Pick.getDrop(0, block, tool);
            assertTrue(dropped.getType().equals(Material.REDSTONE));
            assertTrue(dropped.getAmount() >= 4 && dropped.getAmount() <= 5);
        }
    }

    @Test
    public void dropGlowstoneOreTest()
    {
        doReturn(Material.GLOWSTONE).when(block).getType();
        Collection<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Material.GLOWSTONE_DUST, 1));
        drops.add(new ItemStack(Material.GLOWSTONE_DUST, 1));
        drops.add(new ItemStack(Material.GLOWSTONE_DUST, 1));
        drops.add(new ItemStack(Material.GLOWSTONE_DUST, 1));
        doReturn(drops).when(block).getDrops(any());

        //run this 100 times to check
        for(int i = 0; i < 100; i++)
        {   
            ItemStack dropped = Pick.getDrop(0, block, tool);
            assertTrue(dropped.getType().equals(Material.GLOWSTONE_DUST));
            assertTrue(dropped.getAmount() >= 2 && dropped.getAmount() <= 4);
        }
    }

    @Test
    public void dropLappisTest()
    {
        doReturn(Material.LAPIS_ORE).when(block).getType();

        //run this 100 times to check
        for(int i = 0; i < 100; i++)
        {
            //TODO: find out why we have to reset this every time
            Collection<ItemStack> drops = new ArrayList<ItemStack>();
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            doReturn(drops).when(block).getDrops(any());

            ItemStack dropped = Pick.getDrop(0, block, tool);
            assertTrue(dropped.getType().equals(Material.LAPIS_LAZULI));
            assertTrue(dropped.getAmount() >= 4 && dropped.getAmount() <= 8);
        }
    }

    @Test
    public void dropLappisFortuneTest()
    {
        doReturn(Material.LAPIS_ORE).when(block).getType();

        //run this 100 times to check
        for(int i = 0; i < 100; i++)
        {   
            //TODO: find out why we have to reset this every time
            Collection<ItemStack> drops = new ArrayList<ItemStack>();
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            drops.add(new ItemStack(Material.LAPIS_LAZULI, 1));
            doReturn(drops).when(block).getDrops(any());

            ItemStack dropped = Pick.getDrop(3, block, tool);
            assertTrue(dropped.getType().equals(Material.LAPIS_LAZULI));
            assertTrue(dropped.getAmount() >= 4 && dropped.getAmount() <= 32);
        }
    }

}
