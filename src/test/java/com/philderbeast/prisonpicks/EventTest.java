package com.philderbeast.prisonpicks; 

import org.junit.Test;
import org.junit.Before; 

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMockFactory;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack; 
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.PluginDescriptionFile;

import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Mockito.* ;
import static org.junit.Assert.*; 


@PrepareForTest(PluginDescriptionFile.class)
public class EventTest {

    private PlayerMock player;
    private PluginManagerMock pluginManager;
    private ItemStack xpick, pop, xpop;

    @Before
    public void setUp() {

        ServerMock server = MockBukkit.mock();
        MockBukkit.load(PrisonPicks.class);
        pluginManager = server.getPluginManager();

        //create a player
        PlayerMockFactory factory = new PlayerMockFactory(server);
        player = factory.createRandomPlayer();

        xpick = spy(Util.createItemStack(Config.EXPLOSIVE_COLOR + "Explosive I"));
        Damageable toolMeta = (Damageable) xpick.getItemMeta();
        toolMeta.setDamage(20);
        xpick.setItemMeta(toolMeta);

        pop = spy(Util.createItemStack(Config.PICK_O_PLENTY_COLOR + "Pick o'Plenty"));
        toolMeta = (Damageable) pop.getItemMeta();
        toolMeta.setDamage(20);
        pop.setItemMeta(toolMeta);

        xpop = spy( Util.createItemStack(Config.EXPLOSIVE_COLOR + "Explosive" + Config.PICK_O_PLENTY_COLOR + " Pick o'Plenty"));
        toolMeta = (Damageable) xpop.getItemMeta();
        toolMeta.setDamage(20);
        xpop.setItemMeta(toolMeta);

    }

    @Test
    public void repairTest()
    {
        PlayerInteractEvent pie = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, xpick, null, null);
        pluginManager.callEvent(pie);
        Damageable itemMeta = (Damageable) xpick.getItemMeta();
        assertTrue(itemMeta.getDamage() == 0);

        pie = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, pop, null, null);
        pluginManager.callEvent(pie);
        itemMeta = (Damageable) pop.getItemMeta();
        assertTrue(itemMeta.getDamage() == 0);

        pie = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, xpop, null, null);
        pluginManager.callEvent(pie);
        itemMeta = (Damageable) xpop.getItemMeta();
        assertTrue(itemMeta.getDamage() == 0);
    }
    
}
