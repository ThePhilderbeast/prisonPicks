package com.philderbeast.prisonpicks; 

import org.bukkit.NamespacedKey;

import java.io.File;

import com.sk89q.worldguard.WorldGuard; 
import com.sk89q.worldguard.bukkit.WorldGuardPlugin; 
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader; 

public class PrisonPicks extends JavaPlugin 
{

    private static PrisonPicks instance; 

    public PrisonPicks()
    {
        super();
    }

    protected PrisonPicks(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onLoad()
    {
        Config.BLOCKS_BROKEN = new NamespacedKey(this, "blocks-broken");
        Config.EMERALDS_EXPLODED = new NamespacedKey(this, "emeralds-exploded");
        Config.setConfigFolder(this.getDataFolder().getAbsolutePath()); 
        Config.reloadConfigs(); 

        WorldGuardPlugin worldGuard = Util.getWorldGuard(); 

        if (worldGuard != null)
        {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            try 
            {
                registry.register(Config.PRISON_PICK_FLAG);
                this.getLogger().info("prison-picks custom WorldGuard flag has been registered"); 
            }catch (Exception e)
            {
                this.getLogger().severe("Unable to register flag! Are you running at least WorldGuard 7.0 ?"); 
                e.printStackTrace(); 
                this.onDisable(); 
            }
        }
    }

    @Override
    public void onEnable()
    {
        instance = this; 
        this.getServer().getPluginManager().registerEvents(new Events(), this);
        this.getCommand("pick").setExecutor(new PickCommands()); 
    }

    @Override
    public void onDisable()
    {
    }

    public static PrisonPicks getInstance()
    {
        return instance; 
    }

}
