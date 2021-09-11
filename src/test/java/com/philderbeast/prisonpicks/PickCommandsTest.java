package com.philderbeast.prisonpicks; 

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

import org.junit.Before; 
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.File;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.Mockito.* ;
import static org.junit.Assert.* ; 

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, JavaPluginLoader.class, PluginDescriptionFile.class})
public class PickCommandsTest {

    @Mock private Player player;
    @Mock private CommandSender commandSender;
    @Mock private Server mockServer;
    @Mock private PluginManager pluginManager;
    @Mock private ItemFactory itemFactory;
    @Mock private ItemMeta itemMeta;
    @Mock private PlayerInventory playerInventory;
    
    private PickCommands pickCommands;

    public static final File pluginDirectory = new File("build/libs");

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        pickCommands = new PickCommands();

        // MOCKS BELOW HERE
        when(player.getUniqueId()).thenReturn(UUID.fromString("e3078d5d-8943-420c-8366-4aa51e212df3"));
        when(player.getInventory()).thenReturn(playerInventory);

        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.getPlayer(anyString())).thenReturn(player);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);

        when(itemFactory.getItemMeta(Material.DIAMOND_PICKAXE)).thenReturn(itemMeta);

    }

    @Test
    public void commandHelpTest()
    {
        assertFalse(pickCommands.onCommand(commandSender, null, "pick", new String[0]));
    }

    @Test
    public void commandExplosivePicks()
    {
        String[] args = new String[2];
        args[0] = "explosive";
        args[1] = "Philderbeast";

        assertTrue(pickCommands.onCommand(player, null, "pick", args));
    }

    @Test
    public void commandPickoPlentyPicks()
    {
        String[] args = new String[2];

        args[0] = "pickoplenty";
        args[1] = "Philderbeast";

        assertTrue(pickCommands.onCommand(player, null, "pick", args));
    }

    @Test
    public void commandXPickoPlentyPicks()
    {
        String[] args = new String[2];
        args[0] = "xpickoplenty";
        args[1] = "Philderbeast";

        assertTrue(pickCommands.onCommand(player, null, "pick", args));
    }

    @Test
    public void commandFakeXPickoPlentyPicks()
    {
        String[] args = new String[2];
        args[0] = "fakexpickoplenty";
        args[1] = "Philderbeast";

        assertTrue(pickCommands.onCommand(player, null, "pick", args));
    }

}
