package com.philderbeast.prisonpicks;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.MnMaxon.AutoPickup.AutoPickupPlugin;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonPicks extends JavaPlugin {

    private static PrisonPicks instance;

    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new Events(this),this);
        this.getCommand("pick").setExecutor(new PickCommands());
    }

    public void onDisable() {
    }

    public static PrisonPicks getInstance()
    {
        return instance;
    }

    private static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }

    static boolean canBuild(Player player, Location location)
    {
        WorldGuardPlugin wg = PrisonPicks.getWorldGuard();
        if(wg != null)
        {
            return wg.canBuild(player, location);
        }else {
            return true;
        }
    }

    static AutoPickupPlugin getAutoPickup()
    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AutoPickup");
        if (plugin == null || !(plugin instanceof AutoPickupPlugin)) {
            return null;
        }
        return (AutoPickupPlugin)plugin;
    }
}