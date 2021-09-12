package com.philderbeast.prisonpicks;

import java.util.ArrayList;

import org.bukkit.block.Block;
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
public class PickoplentyTest {

    @Mock World world;
    @Mock PlayerInventory inventory;
    @Mock ItemStack tool;
    @Mock Player player;
    @Mock Block block;
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

        //block stubs
        doReturn(new Location(world, 2, 2, 2)).when(block).getLocation();
        doReturn(Material.EMERALD_ORE).when(block).getType();

        //world stubbs
        doReturn(block).when(world).getBlockAt(any());

        //itemmeta stubbs
        doReturn(true).when(itemMeta).hasLore();

        //Block Stubs
        when(block.hasMetadata(anyString())).thenReturn(false);

        //bukkit Stub
        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.getPluginManager()).thenReturn(pluginManager);
    }

    @Test
    public void isPopTest()
    {
        doReturn(true).when(itemMeta).hasLore();
        doReturn(true).when(tool).hasItemMeta();
        doReturn(itemMeta).when(tool).getItemMeta();
        //this is the wrong lore
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GREEN + "Pick o'Plenty");
        doReturn(lore).when(itemMeta).getLore();
        assertFalse(Pickoplenty.isPick(tool));
 
        //this should work with the correct lore
        lore.clear();
        lore.add(ChatColor.LIGHT_PURPLE + "Pick o'Plenty");
        doReturn(lore).when(itemMeta).getLore();
        assertTrue(Pickoplenty.isPick(tool));
    }

    @Test
    public void testBreakBlock()
    {
        // BlockBreakEvent bbe = new BlockBreakEvent(block, player);
        // Pickoplenty pickoplenty = new Pickoplenty();
        
        // pickoplenty.breakBlock(bbe);
        // verify(block, atLeastOnce()).setType(Material.AIR);
    }
}
