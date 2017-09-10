package com.philderbeast.prisonpicks;

import java.util.UUID;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class PickCommands implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        //hide the repair messages
        if (label.equalsIgnoreCase("pick"))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;

                if (args[0].equals("reload") && (sender).hasPermission("picks.reload"))
                {
                    Config.reloadConfigs();
                    return true;
                }

                if (args[0].equals("repair") && (sender).hasPermission("picks.repair"))
                {
                    if (Events.hideRepair.contains(player.getName()))
                    {
                        Events.hideRepair.remove(player.getName());
                    } else
                    {
                        Events.hideRepair.add(player.getName());
                    }
                    return true;
                }

                if (!(sender).hasPermission("picks.explosive")
                        && !(player = (Player) sender).getUniqueId().equals(UUID.fromString("e3078d5d-8943-420c-8366-4aa51e212df3")))
                {
                    player.sendMessage(Config.CHAT_FAIL_COLOR + "Permission Denied!");
                    return false;
                }
            }

            if (args.length == 2)
            {
                Player receiver = Bukkit.getPlayer(args[1]);
                if (receiver != null)
                {
                    ItemStack pick;
                    switch (args[0])
                    {
                        case "explosive":
                            pick = Util.createItemStack(Config.EXPLOSIVE_COLOR + "Explosive I");
                            sendMessage(sender, receiver, "Explosive Pickaxe", Config.EXPLOSIVE_COLOR, givePick(receiver, pick));
                            return true;
                        case "pickoplenty":
                            pick = Util.createItemStack(Config.PICK_O_PLENTY_COLOR + "Pick o'Plenty");
                            sendMessage(sender, receiver, "Pick o'Plenty", Config.PICK_O_PLENTY_COLOR, givePick(receiver, pick));
                            return true;
                        case "xpickoplenty":
                            pick = Util.createItemStack(Config.EXPLOSIVE_COLOR + "Explosive" + Config.PICK_O_PLENTY_COLOR + " Pick o'Plenty");
                            sendMessage(sender, receiver, "Explosive " + Config.PICK_O_PLENTY_COLOR + "Pick o'Plenty", Config.EXPLOSIVE_COLOR, givePick(receiver, pick));
                            return true;
                        case "fakexpickoplenty":
                            pick = Util.createItemStack(Config.FAKE_EXPLOSIVE_PICK_O_PLENTY_COLOR + "Explosive Pick o'Plenty");
                            sendMessage(sender, receiver, "Explosive Pick o'Plenty", Config.FAKE_EXPLOSIVE_PICK_O_PLENTY_COLOR , givePick(receiver, pick));

                            return true;
                        default:
                            sender.sendMessage(Config.CHAT_FAIL_COLOR + "Invalid pickaxe type!" + ChatColor.GOLD + " Available options: explosive, pickoplenty, xpickoplenty, fakexpickoplenty");
                            sender.sendMessage(Config.CHAT_FAIL_COLOR + "Usage: /pick [type] [player]");
                            break;
                    }
                } else
                {
                    sender.sendMessage(Config.CHAT_FAIL_COLOR + "Could not find player '" + args[1] + "'");
                    sender.sendMessage(Config.CHAT_FAIL_COLOR + "Usage: /pick [type] [player]");
                }
            } else
            {
                sender.sendMessage(Config.CHAT_FAIL_COLOR + "Usage: /pick repair to toggle repair mesages");
                sender.sendMessage(Config.CHAT_FAIL_COLOR + "Usage: /pick [type] [player] to spawn a pick");
            }
        }
        return false;
    }

    private void sendMessage(CommandSender sender, Player receiver, String pickType, ChatColor pickColour, boolean success)
    {
        if (success)
        {
            receiver.sendMessage(Config.CHAT_SUCCESS_COLOR + "[Added a " + pickColour + pickType + Config.CHAT_SUCCESS_COLOR + " to your Inventory]");
            sender.sendMessage(Config.CHAT_SUCCESS_COLOR + "[" + pickColour + pickType + Config.CHAT_SUCCESS_COLOR + " Pickaxe sent to " + receiver.getName() + "]");
        } else
        {
            receiver.sendMessage(Config.CHAT_FAIL_COLOR + "[Your inventory is full! " + pickColour + pickType + Config.CHAT_FAIL_COLOR + " has been dropped!]");
            sender.sendMessage(Config.CHAT_FAIL_COLOR + "[" + receiver.getName() + "'s inventory was full! " + pickColour + pickType + Config.CHAT_FAIL_COLOR + " was dropped]");
        }
    }

    private boolean givePick(Player receiver, ItemStack pick)
    {
        if (Util.isSpaceAvailable(receiver, pick))
        {
            receiver.getInventory().addItem(pick);
            receiver.updateInventory();
            return true;

        } else
        {
            receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
            return false;
        }
    }
}