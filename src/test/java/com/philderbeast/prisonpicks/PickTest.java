package com.philderbeast.prisonpicks;

import java.util.Map;

import org.junit.Test;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;

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

    @Test
    public void hasUnbreakingTest()
    {
        ItemStack pick = mock(ItemStack.class);
        doReturn(true).when(pick).containsEnchantment(Enchantment.DURABILITY);
        
        Pick p = new Pick();

        Map<String, Boolean> enchants = p.getEnchantments(pick);

        assertTrue(enchants.get(Pick.UNBREAKING));
        assertFalse(enchants.get(Pick.FORTUNE));
        assertFalse(enchants.get(Pick.SILK_TOUCH));
    }

    @Test
    public void hasFortuneTest()
    {
        ItemStack pick = mock(ItemStack.class);
        doReturn(true).when(pick).containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
        
        Pick p = new Pick();

        Map<String, Boolean> enchants = p.getEnchantments(pick);

        assertFalse(enchants.get(Pick.UNBREAKING));
        assertTrue(enchants.get(Pick.FORTUNE));
        assertFalse(enchants.get(Pick.SILK_TOUCH));
    }

    @Test
    public void hasSilkTouchTest()
    {
        ItemStack pick = mock(ItemStack.class);
        doReturn(true).when(pick).containsEnchantment(Enchantment.SILK_TOUCH);
        
        Pick p = new Pick();

        Map<String, Boolean> enchants = p.getEnchantments(pick);

        assertFalse(enchants.get(Pick.UNBREAKING));
        assertFalse(enchants.get(Pick.FORTUNE));
        assertTrue(enchants.get(Pick.SILK_TOUCH));
    }

}