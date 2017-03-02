package com.philderbeast.prisonpicks;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;

import me.MnMaxon.AutoPickup.AutoPickupPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonPicks extends JavaPlugin {

    private static PrisonPicks instance;
    private static final StateFlag prisonPickFlag = new StateFlag("prison-picks", true);

    @Override
    public void onLoad() {
        WorldGuardPlugin worldGuard = getWorldGuard();

        if (worldGuard != null) {
            try {
                worldGuard.getFlagRegistry().register(prisonPickFlag);
                this.getLogger().info("prison-picks custom WorldGuard flag has been registered");
            } catch (Exception e) {
                this.getLogger().severe("Unable to register flag! Are you running at least WorldGuard 6.1.3?");
                e.printStackTrace();
                this.onDisable();
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new Events(this),this);
        this.getCommand("pick").setExecutor(new PickCommands());
    }

    @Override
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
            World world = location.getWorld();
            ApplicableRegionSet regions = wg.getRegionManager(world).getApplicableRegions(BukkitUtil.toVector(location));

            return wg.canBuild(player, location) && regions.testState(null,  prisonPickFlag);
        } else {
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