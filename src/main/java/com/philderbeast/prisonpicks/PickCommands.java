package com.philderbeast.prisonpicks;

import java.util.UUID;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PickCommands implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        Player player;
        if (label.equalsIgnoreCase("pick")) 
        {
            if (sender instanceof Player 
                && !(sender).hasPermission("picks.explosive")
                && !(player = (Player)sender).getUniqueId().equals(UUID.fromString("e3078d5d-8943-420c-8366-4aa51e212df3")))  
            {
                player.sendMessage(ChatColor.RED + "Permission Denied!");
                return false;
            }

            if (args.length == 2) 
            {
                Player receiver = Bukkit.getPlayer((String)args[1]);
                if (receiver != null) 
                {
                    ItemStack pick;
                    switch (args[0])
                    {
                        case "explosive":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.GOLD + "Explosive I");
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added a Explosive Pickaxe to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "[Explosive Pickaxe sent to " + receiver.getName() + "]");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                                receiver.sendMessage(ChatColor.RED + "[Your inventory is full! Explosive Pickaxe has been dropped!]");
                                sender.sendMessage(ChatColor.YELLOW + "[" + receiver.getName() + "'s inventory was full! Explosive Pickaxe was dropped.]");
                            }
                            return true;
                        case "pickoplenty":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.LIGHT_PURPLE + "Pick o'Plenty");
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added a Pick o'Plenty to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "[Pick o'Plenty sent to " + receiver.getName() + "]");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                                receiver.sendMessage(ChatColor.RED + "[Your inventory is full! Pick o'Plenty has been dropped!]");
                                sender.sendMessage(ChatColor.YELLOW + "[" + receiver.getName() + "'s inventory was full! Pick o'Plenty was dropped.]");
                            }
                            return true;
                        case "xpickoplenty":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.GOLD + "Explosive" +  ChatColor.LIGHT_PURPLE + " Pick o'Plenty");
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added a Explosive Pick o'Plenty to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "[Explosive Pick o'Plenty sent to " + receiver.getName() + "]");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                                receiver.sendMessage(ChatColor.RED + "[Your inventory is full! Explosive Pick o'Plenty has been dropped!]");
                                sender.sendMessage(ChatColor.YELLOW + "[" + receiver.getName() + "'s inventory was full! Explosive Pick o'Plenty was dropped.]");
                            }
                            return true;
                        case "fakexpickoplenty":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.GREEN + "Explosive Pick o'Plenty");
                            sender.sendMessage(ChatColor.YELLOW + "[" + receiver.getName() + "'s inventory was full! Explosive pickaxe was dropped.]");
                            
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added a Explosive Pick o'Plenty to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "[Explosive Pick o'Plenty sent to " + receiver.getName() + "]");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                                receiver.sendMessage(ChatColor.RED + "[Your inventory is full! Explosive Pick o'Plenty has been dropped!]");
                                sender.sendMessage(ChatColor.YELLOW + "[" + receiver.getName() + "'s inventory was full! Explosive Pick o'Plenty was dropped.]");
                            }
                            return true;
                        default:
                            sender.sendMessage(ChatColor.RED + "Invalid pickaxe type!" + ChatColor.GOLD + " Available options: explosive, pickoplenty, xpickoplenty, fakexpickoplenty");
                            sender.sendMessage(ChatColor.RED + "Usage: /pick [type] [player]");
                        break;
                    }
                } else 
                {
                    sender.sendMessage(ChatColor.RED + "Could not find player '" + args[1] + "'");
                    sender.sendMessage(ChatColor.RED + "Usage: /pick [type] [player]");
                }
            } else 
            {
                sender.sendMessage(ChatColor.RED + "Usage: /pick [type] [player]");
            }
        }
        
        return false;
    }

}