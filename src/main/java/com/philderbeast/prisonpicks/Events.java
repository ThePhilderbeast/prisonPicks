package com.philderbeast.prisonpicks;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener
{

    public static boolean aoepick = false;

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
    


        //are they using one of our picks
        ItemMeta itemMeta = item.getItemMeta();
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            && (Xpick.isPick(item) || Pickoplenty.isPick(item) || XPickoPlenty.isPick(item))
            && ((Damageable) itemMeta).getDamage() > 0)
        {
            if (!hideRepair.contains(player.getName()))
            {
                if (Pickoplenty.isPick(item))
                {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Config.CHAT_POP_REPAIR + "[Pickaxe Repaired]"));
                } else if (XPickoPlenty.isPick(item))
                {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Config.CHAT_XPOP_REPAIR + "[Pickaxe Repaired]"));
                }else
                {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Config.CHAT_EXPLOSIVE_REPAIR + "[Pickaxe Repaired]"));
                }
            }
            //short s = 0;
            
            ((Damageable) itemMeta).setDamage(0);
            item.setItemMeta(itemMeta);
        }
    }
