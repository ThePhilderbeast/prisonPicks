package com.philderbeast.prisonpicks; 

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin; 

public class PrisonPicks extends JavaPlugin 
{

    private static PrisonPicks instance; 
    static File customYml;
    static FileConfiguration customConfig;
    
    @Override
    public void onLoad()
    {
        
        Config.setConfigFolder(this.getDataFolder().getAbsolutePath()); 
        Config.reloadConfigs(); 
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
