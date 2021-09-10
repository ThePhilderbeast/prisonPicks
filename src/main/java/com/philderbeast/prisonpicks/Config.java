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
    static ChatColor CHAT_PICK_BREAK = ChatColor.RED;
    static ChatColor CHAT_EXPLOSIVE_REPAIR = ChatColor.GOLD;
    static ChatColor CHAT_POP_REPAIR = ChatColor.LIGHT_PURPLE;
    static ChatColor CHAT_XPOP_REPAIR = ChatColor.AQUA;

    static int COAL_PRIORITY = 1;
    static int IRON_PRIORITY = 2;
    static int REDSTONE_PRIORITY = 3;
    static int LAPIS_PRIORITY = 4;
    static int GOLD_PRIORITY = 5;
    static int QUARTZ_PRIORITY = 6;
    static int DIAMOND_PRIORITY = 7;
    static int DIAMOND_BLOCK_PRIORITY = 8;
    static int EMERALD_PRIORITY = 9;
    
    static int DEEPCOAL_PRIORITY = 1;
    static int DEEPIRON_PRIORITY = 2;
    static int DEEPREDSTONE_PRIORITY = 3;
    static int DEEPLAPIS_PRIORITY = 4;
    static int DEEPGOLD_PRIORITY = 5;
    static int DEEPDIAMOND_PRIORITY = 7;
    static int DEEPEMERALD_PRIORITY = 9;

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
        CHAT_PICK_BREAK = ChatColor.getByChar(mainConfig.getString("pick_break_color").charAt(1));
        CHAT_EXPLOSIVE_REPAIR = ChatColor.getByChar(mainConfig.getString("explosive_repair_msg").charAt(1));
        CHAT_POP_REPAIR = ChatColor.getByChar(mainConfig.getString("pop_repair_msg").charAt(1));
        CHAT_XPOP_REPAIR = ChatColor.getByChar(mainConfig.getString("xpop_repair_msg").charAt(1));

        COAL_PRIORITY = mainConfig.getInt("coal_priority");
        IRON_PRIORITY = mainConfig.getInt("iron_priority");
        REDSTONE_PRIORITY = mainConfig.getInt("redstone_priority");
        LAPIS_PRIORITY = mainConfig.getInt("lapis_priority");
        GOLD_PRIORITY = mainConfig.getInt("gold_priority");
        QUARTZ_PRIORITY = mainConfig.getInt("quartz_priority");
        DIAMOND_PRIORITY = mainConfig.getInt("diamond_priority");
        DIAMOND_BLOCK_PRIORITY = mainConfig.getInt("diamond_block_priority");
        EMERALD_PRIORITY = mainConfig.getInt("emerald_priority");
        
        DEEPCOAL_PRIORITY = mainConfig.getInt("deepcoal_priority");
        DEEPIRON_PRIORITY = mainConfig.getInt("deepiron_priority");
        DEEPREDSTONE_PRIORITY = mainConfig.getInt("deepredstone_priority");
        DEEPLAPIS_PRIORITY = mainConfig.getInt("deeplapis_priority");
        DEEPGOLD_PRIORITY = mainConfig.getInt("deepgold_priority");
        DEEPDIAMOND_PRIORITY = mainConfig.getInt("deepdiamond_priority");
        DEEPEMERALD_PRIORITY = mainConfig.getInt("deepemerald_priority");
        
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

        for (Map.Entry < String, Object > entry:defaults.entrySet())
        {
            config.set(entry.getKey(), entry.getValue());
        }

        defaults.clear();
        defaults.put("pick_break_color", ChatColor.RED.toString());
        defaults.put("success_color", ChatColor.GREEN.toString());
        defaults.put("fail_color", ChatColor.RED.toString());
        defaults.put("explosive_repair_msg", ChatColor.GOLD.toString());
        defaults.put("pop_repair_msg", ChatColor.LIGHT_PURPLE.toString());
        defaults.put("xpop_repair_msg", ChatColor.AQUA.toString());

        for (Map.Entry < String, Object > entry:defaults.entrySet())
        {
            config.set(entry.getKey(), entry.getValue()); 
        }

        defaults.clear();
        defaults.put("coal_priority", 1);
        defaults.put("iron_priority", 2);
        defaults.put("redstone_priority", 3);
        defaults.put("lapis_priority", 4);
        defaults.put("gold_priority", 5);
        defaults.put("quartz_priority", 6);
        defaults.put("diamond_priority", 7);
        defaults.put("diamond_block_priority", 8);
        defaults.put("emerald_priority", 9);
        
        defaults.put("deepcoal_priority", 1);
        defaults.put("deepiron_priority", 2);
        defaults.put("deepredstone_priority", 3);
        defaults.put("deeplapis_priority", 4);
        defaults.put("deepgold_priority", 5);
        defaults.put("deepdiamond_priority", 7);
        defaults.put("deepemerald_priority", 9);

        for (Map.Entry < String, Object > entry:defaults.entrySet())
        {
            config.set(entry.getKey(), entry.getValue());
        }

        return config; 
    }
}