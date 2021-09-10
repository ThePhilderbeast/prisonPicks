package com.philderbeast.prisonpicks;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.philderbeast.autopickup.API.AutoPickupMethods;

abstract class Pick
{

    static final String FORTUNE = "fortune";
    static final String UNBREAKING = "unbreaking";
    static final String SILK_TOUCH = "silktouch";
    
    int chance = (int) (Math.random() * (4 - 1)) + 1;

    public abstract void breakBlock(BlockBreakEvent event);

    static boolean isPick(ItemStack item)
    {
        return item != null && item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.NETHERITE_PICKAXE && item.hasItemMeta() && item.getItemMeta().hasLore();
    }

    Map < String, Boolean > getEnchantments(ItemStack item)
    {

        Map < String, Boolean > enchants = new HashMap<>();

            if (item.containsEnchantment(Enchantment.DURABILITY))
            {
                enchants.put(UNBREAKING, true);
            }else
        {
                enchants.put(UNBREAKING, false);
            }

        if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))
        {
            enchants.put(FORTUNE, true);
        }else
        {
            enchants.put(FORTUNE, false);
        }

        if (item.containsEnchantment(Enchantment.SILK_TOUCH))
        {
            enchants.put(SILK_TOUCH, true);
        }else
        {
            enchants.put(SILK_TOUCH, false);
        }
        return enchants;
    }

    void doBreak(Block block, Map<String, Boolean> enchants, Player player, Material material)
    {
        
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (block.getType() != Material.BEDROCK && block.getType() != Material.AIR)
        {
            if (enchants.get(SILK_TOUCH))
            {
                int silk_touch = item.getEnchantmentLevel(Enchantment.SILK_TOUCH);
                ItemStack newItem = getDrop(silk_touch, block, item);

                if (newItem != null)
                {
                    if (Util.getAutoPickup() != null)
                    {
                        AutoPickupMethods.autoGive(player, newItem);
                    }else if (Util.isSpaceAvailable(player, newItem))
                    {
                        player.getInventory().addItem(newItem);
                    }
                }

            }else
            {
                if (material != null)
                {
                    block.setType(material);
                }

                int fortune = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                ItemStack newItem = getDrop(fortune, block, item);

                if (newItem != null)
                {
                    if (Util.getAutoPickup() != null)
                    {
                        AutoPickupMethods.autoGive(player, newItem);
                    }else if (Util.isSpaceAvailable(player, newItem))
                    {
                        player.getInventory().addItem(newItem);
                    }
                }

                int exp = Util.calculateExperienceForBlock(block);
                player.giveExp(exp); //Give Player Experience
            }
            block.setType(Material.AIR);
        }
    }

    protected abstract char[] parseInt(String string);

    static ItemStack getDrop(int enchantmentLevel, Block block, ItemStack tool)
    {
        
        int min, max, startAmount;
        int multiple;
        int amount;

        Collection < ItemStack > stacks = block.getDrops(tool);
        ItemStack drop;
        if (stacks.size() == 0)
        {
            return null;
        }else
        {
            drop = (ItemStack)stacks.toArray()[0];
        }

        if (block.getType() == Material.IRON_ORE || block.getType() == Material.DEEPSLATE_IRON_ORE || block.getType() == Material.GOLD_ORE || block.getType() == Material.DEEPSLATE_GOLD_ORE)
        {
            drop.setAmount(1);
            return drop;
        }

        if (block.getType() == Material.REDSTONE_ORE  || block.getType() == Material.DEEPSLATE_REDSTONE_ORE || block.getType() == Material.GLOWSTONE || block.getType() == Material.COAL_ORE || block.getType() == Material.DEEPSLATE_COAL_ORE)
        {
            switch (block.getType())
            {
                case REDSTONE_ORE:
                case DEEPSLATE_REDSTONE_ORE:
                    min = 4;
                    max = 5;
                    break;
                case COAL_ORE:
                case DEEPSLATE_COAL_ORE:
                    min = 1;
                    max = 6;
                    break;
                case GLOWSTONE:
                    min = 2;
                    max = 4;
                    break;
                default:
                    min = 1;
                    max = 1;
                    break;
            }

            int bonus = enchantmentLevel;
            if (block.getType() == Material.GLOWSTONE && bonus > 4)
            {bonus = 4; }
            max = max + bonus;
            amount = min + (int)(Math.random() * ((max - min) + 1));
            drop.setAmount(amount);
            return drop;
        }else if (block.getType().name().contains("ORE"))
        {
            amount = stacks.size();
            startAmount = amount;

            if (enchantmentLevel > 0)
            {
                Random r = new Random();
                int tiers = 2 + enchantmentLevel;
                int chanceEach = (100 / tiers);
                int chanceForOneDrop = 100 - ((tiers - 2) * chanceEach);
                int currChance = chanceForOneDrop;
                int roll = r.nextInt(100);

                if (roll > chanceForOneDrop)
                {
                    for (int i = 1; i <= tiers - 2; i++)
                    {
                        currChance += chanceEach;
                        if (roll <= currChance)
                        {
                            multiple = (1 + i);
                            amount = startAmount * multiple;
                            break;
                        }
                    }
                }
            }

            drop.setAmount(amount);
            stacks.clear();
        }

        return drop;
    }
    
    void doDamage(boolean unbreaking, Player player)
    {
        ItemStack tool = player.getInventory().getItemInMainHand();

        int unbreakingLevel = tool.getEnchantmentLevel(Enchantment.DURABILITY);

        if ( ! unbreaking)
        {
            ItemMeta itemMeta = tool.getItemMeta();
            if (itemMeta instanceof Damageable){
                ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage() + 1);
            }
            tool.setItemMeta(itemMeta);
            
        }else if (unbreakingLevel > 0)
        {
            Random r = new Random();
            int chanceToReduce = (100/(unbreakingLevel + 1));
            int roll = r.nextInt(100);

            if (roll <= chanceToReduce)
            {
                
                ItemMeta itemMeta = tool.getItemMeta();
                if (itemMeta instanceof Damageable){
                    ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage() + 1);
                }
                tool.setItemMeta(itemMeta);
            }
        }
        if (((Damageable) tool.getItemMeta()) == null) {
            return;
        }else
        if (((Damageable) tool.getItemMeta()).getDamage() > tool.getType().getMaxDurability())
        {
            String pick = "";
            String type = "";

            if (Xpick.isPick(tool))
            {
                pick = Config.EXPLOSIVE_COLOR + "Explosive Pick";
            }else if (Pickoplenty.isPick(tool))
            {
                pick = Config.PICK_O_PLENTY_COLOR + "Pick 'o' Plenty";
            }else if (XPickoPlenty.isPick(tool))
            {
                pick = Config.EXPLOSIVE_COLOR + "Explosive " + Config.PICK_O_PLENTY_COLOR + " Pick 'o' plenty";
            }

            if (tool.getType() == Material.DIAMOND_PICKAXE) {
                type = ChatColor.BLUE + "Diamond";
            }
            
            if (tool.getType() == Material.NETHERITE_PICKAXE) {
                type = ChatColor.DARK_PURPLE + "Netherite";
            }
            if (chance == 1 && player.getName() != null) {
                Bukkit.broadcastMessage(Config.CHAT_PICK_BREAK + "Uh oh, " + player.getName() + " wasn't paying attention. Don't forget to right click because they broke a " + type + " " + pick + Config.CHAT_PICK_BREAK + " while mining... Press 'F' to pay respects. ");
                
            }
            else
            if (chance != 1 && player.getName() != null) {
                Bukkit.broadcastMessage(Config.CHAT_PICK_BREAK + player.getName() + " just broke their " + type + " " + pick + Config.CHAT_PICK_BREAK + " while mining... Press 'F' to pay respects. ");
                    }
                
            if(Config.DEBUG)
            {
                if (Xpick.isPick(tool))
                {
                    pick = Config.EXPLOSIVE_COLOR + "Explosive Pick";
                }else if (Pickoplenty.isPick(tool))
                {
                    pick = Config.PICK_O_PLENTY_COLOR + "Pick 'o' Plenty";
                }else if (XPickoPlenty.isPick(tool))
                {
                    pick = Config.EXPLOSIVE_COLOR + "Explosive " + Config.PICK_O_PLENTY_COLOR + " Pick 'o' plenty";
                }
                System.out.println("-------------------------------------");
                System.out.println("Player: " + player.getName());
                System.out.println("UUID: " + player.getUniqueId());
                System.out.println("Pick type: " + type + " " + pick);
                System.out.println("Pick Current Durability: " + ((Damageable) tool.getItemMeta()).getDamage());
                System.out.println("Pick Max Durability: " + tool.getType().getMaxDurability());
                System.out.println("-------------------------------------");
            }
            //break the pick
            player.getInventory().remove(tool);
        }
    }
}