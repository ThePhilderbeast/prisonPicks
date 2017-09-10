package com.philderbeast.prisonpicks;

import org.bukkit.entity.Player;
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

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener
{

    private boolean aoepick = false;

    static final List< String > hideRepair = new ArrayList<>();

    Events(){}

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {

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
            //this is an Xpick
            Xpick x = new Xpick();
            x.breakBlock(event);
        }else if (Pickoplenty.isPick(item))
        {
            Pickoplenty pop = new Pickoplenty();
            pop.breakBlock(event);
        }else if (XPickoPlenty.isPick(item))
        {
            aoepick = true;
            XPickoPlenty xpop = new XPickoPlenty();
            xpop.breakBlock(event);
        }

        aoepick = false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        //are they using one of our picks
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            && (Xpick.isPick(item) || Pickoplenty.isPick(item) || XPickoPlenty.isPick(item))
            && item.getDurability() > 0)
        {
            if (!hideRepair.contains(player.getName()))
            {
                if (Pickoplenty.isPick(item))
                {
                    player.sendMessage(Config.CHAT_POP_REPAIR + "[Pickaxe Repaired]");
                } else if (XPickoPlenty.isPick(item))
                {
                    player.sendMessage(Config.CHAT_XPOP_REPAIR + "[Pickaxe Repaired]");
                } else
                {
                    player.sendMessage(Config.CHAT_EXPLOSIVE_REPAIR + "[Pickaxe Repaired]");
                }
            }
            short s = 0;
            item.setDurability(s);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        b.setMetadata("blockBreaker", new FixedMetadataValue(PrisonPicks.getInstance(), player.getUniqueId()));
    }
}

