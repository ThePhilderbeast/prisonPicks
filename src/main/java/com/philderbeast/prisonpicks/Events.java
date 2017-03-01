package com.philderbeast.prisonpicks;

import com.philderbeast.prisonpicks.PrisonPicks;

import me.MnMaxon.AutoPickup.AutoBlock;
import me.MnMaxon.AutoPickup.AutoPickupPlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class Events implements Listener {

    @SuppressWarnings("unused")
    private PrisonPicks plugin;
    private boolean aoepick = false;


    public Events()
    {
    }

    public Events(PrisonPicks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (aoepick)
        {
            //stop our event recurring
            return;
        }

        //Are we using one of our picks
        if (Xpick.isPick(item))
        {
            aoepick = true;
            //this is an inXpick
            Xpick x = new Xpick();
            x.breakBlock(event);
        }else if( Pickoplenty.isPick(item))
        {
            Pickoplenty pop = new Pickoplenty();
            pop.breakBlock(event);
        }else if( XPickoPlenty.isPick(item))
        {
            aoepick = true;
            XPickoPlenty xpop = new XPickoPlenty();
            xpop.breakBlock(event);
        }

        //TODO: this should look to see if the plugin exsists
        if (PrisonPicks.getAutoPickup() != null
            && AutoPickupPlugin.autoBlock.contains(player.getName()))
        {
            AutoBlock.block(player, false);
        }
        
        aoepick = false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        //are they using one of our picks
        if ((event.getAction() == Action.RIGHT_CLICK_AIR 
            || event.getAction() == Action.RIGHT_CLICK_BLOCK) 
            && Xpick.isPick(item)
            || Pickoplenty.isPick(item)
            || XPickoPlenty.isPick(item)
            && item.getDurability() > 0)
        {
            if (Pickoplenty.isPick(item)) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "[Pickaxe Repaired]");
            } else if (XPickoPlenty.isPick(item)) {
                player.sendMessage(ChatColor.AQUA + "[Pickaxe Repaired]");
            } else {
                player.sendMessage(ChatColor.GOLD + "[Pickaxe Repaired]");
            }
            short s = 0;
            item.setDurability(s);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        b.setMetadata("blockBreaker", (MetadataValue)new FixedMetadataValue((Plugin)PrisonPicks.getInstance(), (Object)player.getUniqueId()));
    }
}

