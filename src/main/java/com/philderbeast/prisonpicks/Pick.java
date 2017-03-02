package com.philderbeast.prisonpicks;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;

import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import me.MnMaxon.AutoPickup.AutoPickupPlugin;
import me.MnMaxon.AutoPickup.AutoSmelt;

public abstract class Pick{

    public static final String FORTUNE = "fortune";
    public static final String UNBREAKING = "unbreaking";
    public static final String SILK_TOUCH = "silktouch";

    public Pick()
    {

    }

    public abstract void breakBlock(BlockBreakEvent event);

    public static boolean isPick(ItemStack item)
    {
        if(item != null && item.getType() == Material.DIAMOND_PICKAXE 
            && item.hasItemMeta() 
            && item.getItemMeta().hasLore())
        {
            return true;
        }else
        {
            return false;
        }
    }

    protected Map<String, Boolean> getEnchantments(ItemStack item)
    {

        Map<String, Boolean> enchants = new HashMap<String, Boolean>();

            if (item.containsEnchantment(Enchantment.DURABILITY))
            {
                enchants.put(UNBREAKING, true);
            }else{
                enchants.put(UNBREAKING, false);
            }

        if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            enchants.put(FORTUNE, true);
        } else {
            enchants.put(FORTUNE, false);
        }

        if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
            enchants.put(SILK_TOUCH, true);
        } else {
            enchants.put(SILK_TOUCH, false);
        }
        return enchants;
    }

    protected boolean doBreak(Block block, Map<String, Boolean> enchants, Player player, Material material)
    {
        boolean blockBroken = false;
        if (block.getType() != Material.BEDROCK
            && block.getType() != Material.AIR) {
            if (enchants.get(SILK_TOUCH)) {
                ItemStack blockStack;
                if (material == null)
                {
                    //TODO: do we need this data call? if so can we find a better way
                    blockStack = new ItemStack(block.getType(), 1, block.getData());
                } else {
                    blockStack = new ItemStack(material, 1, block.getData());
                }

                //they have silk touch so give them the block
                //TODO: make this a soft depend
                if (PrisonPicks.getAutoPickup() != null)
                {
                    AutoPickupPlugin.giveItem(player, blockStack);
                }else
                {
                    player.getInventory().addItem(blockStack);
                }

            } else {
                if (material != null)
                {
                    block.setType(material);
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                int fortune = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                ItemStack newItem = getDrop(fortune, block, item);

                if (PrisonPicks.getAutoPickup() != null
                    && AutoPickupPlugin.autoSmelt.contains(player.getName()))
                {
                    newItem = AutoSmelt.smelt(newItem).getNewItem();
                    AutoPickupPlugin.giveItem(player, newItem);
                }else if (Util.isSpaceAvailable(player, newItem))
                {
                    player.getInventory().addItem(newItem);
                }

                int exp = Util.calculateExperienceForBlock(block);
                player.giveExp(exp); //Give Player Experience
            }
            block.setType(Material.AIR);
            blockBroken = true;
        }
        return blockBroken;
    }

    protected static ItemStack getDrop(int enchantmentLevel, Block block, ItemStack tool) {

        int min, max, startAmount;
        int multiple = 1;
        int amount;

        Collection<ItemStack> stacks = block.getDrops(tool);
        ItemStack drop;
        if (stacks.size() == 0) {
            return new ItemStack(Material.AIR, 0);
        } else {
            drop = (ItemStack) stacks.toArray()[0];
        }

        if (block.getType() == Material.IRON_ORE || block.getType() == Material.GOLD_ORE) {
            drop.setAmount(1);
            return drop;
        }

        if (block.getType() == Material.REDSTONE_ORE || block.getType() == Material.GLOWING_REDSTONE_ORE || block.getType() == Material.GLOWSTONE) {
            switch (block.getType()) {
                case GLOWING_REDSTONE_ORE:
                case REDSTONE_ORE:
                    min = 4;
                    max = 5;
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
            if (block.getType() == Material.GLOWSTONE && bonus > 4) { bonus = 4; }
            max = max + bonus;
            amount = min + (int)(Math.random() * ((max - min) + 1));
            drop.setAmount(amount);
            return drop;
        } else if (block.getType().name().contains("ORE")) {
            amount = stacks.size();
            startAmount = amount;

            if (enchantmentLevel > 0) {
                Random r = new Random();
                int tiers = 2 + enchantmentLevel;
                int chanceEach = (100 / tiers);
                int chanceForOneDrop = 100 - ((tiers - 2) * chanceEach);
                int currChance = chanceForOneDrop;
                int roll = r.nextInt(100);

                if (roll > chanceForOneDrop) {
                    for (int i = 1; i <= tiers - 2; i++) {
                        currChance += chanceEach;
                        if (roll <= currChance) {
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

    protected void doDamage(boolean unbreaking, Player player)
    {
        ItemStack tool = player.getInventory().getItemInMainHand();

        int unbreakingLevel = tool.getEnchantmentLevel(Enchantment.DURABILITY);

        if (!unbreaking) {
            tool.setDurability((short)(tool.getDurability() + 1));
        } else if (unbreakingLevel > 0) {
            Random r = new Random();
            int chanceToReduce = (100/(unbreakingLevel+1));
            int roll = r.nextInt(100);

            if (roll <= chanceToReduce) {
                tool.setDurability((short) (tool.getDurability() + 1));
            }
        }
        if (tool.getDurability() > tool.getType().getMaxDurability()) {
            String pick="";

            if (Xpick.isPick(tool))
            {
                pick = "Explosive Pick";
            }else if(Pickoplenty.isPick(tool))
            {
                pick = "Pick 'o' Penty";
            }else if(XPickoPlenty.isPick(tool))
            {
                pick = "Explosive Pick 'o' plenty";
            }

            Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + " just broke their " + pick + " while mining... Press 'F' to pay respects.");

            System.out.println("-------------------------------------");
            System.out.println("Player: " + player.getName());
            System.out.println("UUID: " + player.getUniqueId());
            System.out.println("Pick type: " + pick);
            System.out.println("Pick Current Durability: " + tool.getDurability());
            System.out.println("Pick Max Durability: " + tool.getType().getMaxDurability());
            System.out.println("-------------------------------------");
            //break the pick
            player.getInventory().remove(tool);

        }
    }
}