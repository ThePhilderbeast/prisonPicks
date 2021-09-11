package com.philderbeast.prisonpicks;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;
import org.bukkit.World;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class Xpickoplentytest {

    @Mock World world;
    @Mock PlayerInventory inventory;
    @Mock ItemStack tool;
    @Mock Player player;
    @Mock Location location;
    @Mock Block block;
    @Mock BlockState blockState;
    @Mock PluginManager pluginManager;
    @Mock ItemMeta itemMeta;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        //Tool Stubbs
        doReturn(0).when(tool).getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        doReturn(Material.DIAMOND_PICKAXE).when(tool).getType();
        doReturn((short) 0).when(tool).getDurability(); 
        doReturn(true).when(tool).hasItemMeta();
        doReturn(itemMeta).when(tool).getItemMeta();

        //inventory Stubs
        doReturn(tool).when(inventory).getItemInMainHand();

        //playter stubs
        doReturn(inventory).when(player).getInventory();
        doReturn(world).when(player).getWorld();

        //location stubs
        doReturn(block).when(location).getBlock();

        //block stubs
        doReturn(new Location(world, 2, 2, 2)).when(block).getLocation();
        doReturn(new Location(world, 2, 2, 2)).when(blockState).getLocation();
        doReturn(blockState).when(block).getState();
        doReturn(Material.EMERALD_ORE).when(block).getType();

        //itemmeta stubbs
        doReturn(true).when(itemMeta).hasLore();

        //bukkit Stub
        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.getPluginManager()).thenReturn(pluginManager);
    }

    @Test
    public void isXPickTest()
    {
        doReturn(true).when(itemMeta).hasLore();
        doReturn(true).when(tool).hasItemMeta();
        doReturn(itemMeta).when(tool).getItemMeta();
        //this is the wrong lore
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GREEN + "Explosive Pick o'Plenty");
        doReturn(lore).when(itemMeta).getLore();
        assertFalse(XPickoPlenty.isPick(tool));
 
        //this should work with the correct lore
        lore.clear();
        lore.add(ChatColor.GOLD + "Explosive" +  ChatColor.LIGHT_PURPLE + " Pick o'Plenty");
        doReturn(lore).when(itemMeta).getLore();
        assertTrue(XPickoPlenty.isPick(tool));
    }


    // @Test
    // public void testBreakBlock()
    // {
    //     //TODO: update this test to be more comprehensive
    //     BlockBreakEvent bbe = new BlockBreakEvent(block, player);
    //     XPickoPlenty xpick = new XPickoPlenty();
        
    //     xpick.breakBlock(bbe);
    //     verify(block, atLeastOnce()).setType(Material.AIR);
    // }
}
