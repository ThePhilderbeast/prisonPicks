package com.philderbeast.prisonpicks; 

import java.io.File; 
import java.io.IOException; 
import java.util.HashMap; 
import java.util.Map; 
import java.util.logging.Level; 

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration; 

import com.sk89q.worldguard.protection.flags.StateFlag; 

/**
 * Creates the YMAL configuration file for the Plugin that contains all settings
 **/
@SuppressWarnings("CanBeFinal")
class Config
{

    private static final String MAIN_CONFIG = "config.yaml";

    private static String configFolder;
    private static boolean WORLDGUARD_DEFAULT = true;

	static boolean DEBUG = false;
    static StateFlag PRISON_PICK_FLAG = new StateFlag("prison-picks", WORLDGUARD_DEFAULT);
    static ChatColor EXPLOSIVE_COLOR = ChatColor.GOLD;
    static ChatColor PICK_O_PLENTY_COLOR = ChatColor.LIGHT_PURPLE;
    static ChatColor FAKE_EXPLOSIVE_PICK_O_PLENTY_COLOR = ChatColor.GREEN;
    static ChatColor CHAT_SUCCESS_COLOR = ChatColor.GREEN;
    static ChatColor CHAT_FAIL_COLOR = ChatColor.RED;

    static void setConfigFolder(String configFolder)
    {
        Config.configFolder = configFolder; 
    }

	static void reloadConfigs()
    {
        YamlConfiguration mainConfig = load(configFolder + "/" + MAIN_CONFIG);

		WORLDGUARD_DEFAULT = mainConfig.getBoolean("worldguard_flag_enable"); 
		PRISON_PICK_FLAG = new StateFlag("prison-picks", WORLDGUARD_DEFAULT);
        EXPLOSIVE_COLOR = ChatColor.getByChar(mainConfig.getString("explosive_color").charAt(1));
        PICK_O_PLENTY_COLOR = ChatColor.getByChar(mainConfig.getString("pick_o_plenty_color").charAt(1));
        FAKE_EXPLOSIVE_PICK_O_PLENTY_COLOR = ChatColor.getByChar(mainConfig.getString("fake_xpop_color").charAt(1));
        CHAT_SUCCESS_COLOR = ChatColor.getByChar(mainConfig.getString("success_color").charAt(1));
        CHAT_FAIL_COLOR = ChatColor.getByChar(mainConfig.getString("fail_color").charAt(1));
    }

	private static YamlConfiguration load(String FileLocation)
    {
        File f = new File(FileLocation);
		YamlConfiguration cfg;
		cfg = setDefaults(); 
		if ( ! f.exists())
        {
			try 
            {
                if(f.getParentFile().mkdir() && f.createNewFile())
                {
                    Bukkit.getServer().getLogger().log(Level.INFO, "[PrisonPicks] New Config Created at: " + FileLocation);
                    cfg.save(new File(FileLocation));
                } else
                {
                    Bukkit.getServer().getLogger().log(Level.SEVERE, "[PrisonPicks] Failed to create Config file");
                }
			}catch (IOException e1)
            {
				e1.printStackTrace(); 
			}
		}else 
        {
			try 
            {
				cfg.load(f); 
			}catch (IOException | InvalidConfigurationException e)
            {
				e.printStackTrace(); 
			}
		}
		return cfg; 
	}

	private static YamlConfiguration setDefaults()
    {
        YamlConfiguration config = new YamlConfiguration();
		HashMap < String, Object > defaults = new HashMap<>();
		defaults.put("worldguard_flag_enable", true); 
        defaults.put("debug", false);

        for (Map.Entry < String, Object > entry:defaults.entrySet())
        {
            config.set(entry.getKey(), entry.getValue());
        }

        defaults.clear();

        defaults.put("explosive_color", ChatColor.GOLD.toString());
        defaults.put("pick_o_plenty_color", ChatColor.LIGHT_PURPLE.toString());
        defaults.put("fake_xpop_color", ChatColor.GREEN.toString());
        defaults.put("success_color", ChatColor.GREEN.toString());
        defaults.put("fail_color", ChatColor.RED.toString());

		for (Map.Entry < String, Object > entry:defaults.entrySet())
        {
			config.set(entry.getKey(), entry.getValue()); 
		}
		return config; 
	}
}