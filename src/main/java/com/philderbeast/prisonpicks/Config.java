package com.philderbeast.prisonpicks;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.protection.flags.StateFlag;

public class Config
{

	private static String configFolder;
	private static YamlConfiguration mainConfig = null;
	private static boolean WORLDGUARD_DEFAULT = true;

	public static boolean DEBUG = false;
    public static StateFlag prisonPickFlag = new StateFlag("prison-picks", WORLDGUARD_DEFAULT);

    private static final String MAIN_CONFIG = "config.yaml";

    public static void setConfigFolder(String configFolder)
    {
        Config.configFolder = configFolder;
    }

	private static YamlConfiguration load(String FileLocation)
	{
        File f = new File(FileLocation);
		YamlConfiguration cfg = new YamlConfiguration();
		cfg = setDefaults();
		if (!f.exists())
		{
			try 
			{
                f.getParentFile().mkdir();
				f.createNewFile();
				Bukkit.getServer().getLogger().log(Level.INFO, "[PrisonPicks] New Config Created at: " + FileLocation);
				cfg.save(new File(FileLocation));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else
		{
			try 
			{
				cfg.load(f);
			} catch (IOException | InvalidConfigurationException e) 
			{
				e.printStackTrace();
			}
		}
		return cfg;
	}


	public static void reloadConfigs()
	{
        mainConfig = load(configFolder + "/" + MAIN_CONFIG);

		WORLDGUARD_DEFAULT = mainConfig.getBoolean("worldguard_flag_enable");
		prisonPickFlag = new StateFlag("prison-picks", WORLDGUARD_DEFAULT);
    }

	private static YamlConfiguration setDefaults()
	{
		HashMap<String, Object> defaults = new HashMap<String, Object>();
		defaults.put("worldguard_flag_enable", true);
        defaults.put("debug", false);

		YamlConfiguration config = new YamlConfiguration();

		for (Map.Entry<String, Object> entry : defaults.entrySet())
		{
			config.set(entry.getKey(), entry.getValue());
		}
		return config;
	}
}