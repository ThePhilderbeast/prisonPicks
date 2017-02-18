package com.philderbeast.prisonpicks;

import java.util.ArrayList;

import org.junit.Test;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class PickTest {

    @Test
    public void isPickTest()
    {
        //make a mock Xpick
        ItemMeta i = mock(ItemMeta.class);
        doReturn(true).when(i).hasLore();

        ItemStack pick = mock(ItemStack.class);
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

}