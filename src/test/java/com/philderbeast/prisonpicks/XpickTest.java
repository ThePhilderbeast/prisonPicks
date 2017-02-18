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

public class XpickTest {

    @Test
    public void isXPickTest()
    {
        //make a mock Pick
        ItemMeta i = mock(ItemMeta.class);
        ArrayList<String> lore = new ArrayList<String>();
        doReturn(true).when(i).hasLore();
        ItemStack xpick = mock(ItemStack.class);
        doReturn(Material.DIAMOND_PICKAXE).when(xpick).getType();
        doReturn(true).when(xpick).hasItemMeta();
        doReturn(i).when(xpick).getItemMeta();

        //this is the wrong lore
        lore.add(ChatColor.GREEN + "Explosive I");
        doReturn(lore).when(i).getLore();
        assertFalse(Xpick.isPick(xpick));
 
        //this should work with the correct lore
        lore.clear();
        lore.add(ChatColor.GOLD + "Explosive I");
        doReturn(lore).when(i).getLore();
        assertTrue(Xpick.isPick(xpick));
    }

}