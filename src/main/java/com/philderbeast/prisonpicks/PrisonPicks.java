package com.philderbeast.prisonpicks; 

import com.sk89q.worldguard.bukkit.WorldGuardPlugin; 
import org.bukkit.plugin.java.JavaPlugin; 

public class PrisonPicks extends JavaPlugin 
{

    private static PrisonPicks instance; 

    @Override
    public void onLoad()
    {
        Config.setConfigFolder(this.getDataFolder().getAbsolutePath()); 
        Config.reloadConfigs(); 

        WorldGuardPlugin worldGuard = Util.getWorldGuard(); 

        if (worldGuard != null)
        {
            try 
            {
                worldGuard.getFlagRegistry().register(Config.PRISON_PICK_FLAG);
                this.getLogger().info("prison-picks custom WorldGuard flag has been registered"); 
            }catch (Exception e)
            {
                this.getLogger().severe("Unable to register flag! Are you running at least WorldGuard 6.1.3?"); 
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