package com.philderbeast.prisonpicks;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.BlockPlaceEvent;
import com.philderbeast.prisonpicks.PrisonPicks;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class Events implements Listener {

    private PrisonPicks plugin;
    boolean aoepick = false;

    Events(PrisonPicks plugin) {
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

        aoepick = false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        //are they using one of our picks
        if ((event.getAction() == Action.RIGHT_CLICK_AIR 
            || event.getAction() == Action.RIGHT_CLICK_BLOCK) 
            && item != null && item.getType() == Material.DIAMOND_PICKAXE 
            && item.hasItemMeta() 
            && item.getItemMeta().hasLore()
            && (item.getItemMeta().getLore().contains(ChatColor.GOLD + "Explosive I")
            || item.getItemMeta().getLore().contains(ChatColor.LIGHT_PURPLE + "Pick o'Plenty")
            || item.getItemMeta().getLore().contains(ChatColor.GOLD + "Explosive" + ChatColor.LIGHT_PURPLE + " Pick o'Plenty"))
            && item.getDurability() > 0)
            {
            player.sendMessage((Object)ChatColor.GOLD + "[Pickaxe Repaired]");
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

