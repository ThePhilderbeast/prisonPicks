package com.philderbeast.prisonpicks;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import me.MnMaxon.AutoPickup.AutoPickupPlugin;
import me.MnMaxon.AutoPickup.AutoSmelt;
import me.MnMaxon.AutoPickup.AutoBlock;

public class Pick{

    public static final String FORTUNE = "fortune";
    public static final String UNBREAKING = "unbreaking";
    public static final String SILK_TOUCH = "silktouch";

    public Pick()
    {

    }

    public void breakBlock()
    {
    }

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

    public Map<String, Boolean> getEnchantments(ItemStack item)
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

    public boolean doBreak(Block block, Map<String, Boolean> enchants, Player player, Material material)
    {
        boolean blockBroken = false;
        boolean noInventorySpace = false;
        if (block.getType() != Material.BEDROCK
            && block.getType() != Material.AIR) {
            if (enchants.get(SILK_TOUCH)) {
                ItemStack blockStack;
                if (material == null)
                {
                    blockStack = new ItemStack(block.getType(), 1, block.getData());
                } else {
                    blockStack = new ItemStack(material, 1, block.getData());
                }
                //they have silk touch so give them the block
                if (Util.isSpaceAvailable(player, blockStack)) {
                    player.getInventory().addItem(blockStack);
                } else {
                    noInventorySpace = true;
                }
            } else {
                if (material != null)
                {
                    block.setType(material);
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                int fortune = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                ItemStack newItem = getDrop(fortune, block, item);

                if (AutoPickupPlugin.autoSmelt.contains(player.getName())) {
                    newItem = AutoSmelt.smelt(newItem).getNewItem();
                }

                if (Util.isSpaceAvailable(player, newItem)) {
                    player.getInventory().addItem(newItem);
                } else {
                    noInventorySpace = true;
                }

                int exp = Util.calculateExperienceForBlock(block);
                player.giveExp(exp); //Give Player Experience
            }
            block.setType(Material.AIR);
            blockBroken = true;
        }

        if (noInventorySpace && !PrisonPicks.getInstance().getDisabledAlerts().contains(player.getName())) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, SoundCategory.BLOCKS, 1.0f, 1.0f);
            player.sendTitle(ChatColor.RED + "Inventory is Full!", ChatColor.GOLD + "/fullnotify to disable", 1, 15, 5);
        }

        return blockBroken;

    }

    public static ItemStack getDrop(int enchantmentLevel, Block block, ItemStack tool) {
        boolean DEBUG = false;
        int min, max, startAmount;
        int multiple = 1;
        int amount;

        Collection<ItemStack> stacks = block.getDrops(tool);
        Iterator itr = stacks.iterator();
        ItemStack drop;

        if (!itr.hasNext()) {
            return new ItemStack(Material.AIR, 0);
        } else {
            drop = (ItemStack) itr.next();
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

                if (DEBUG) {
                    System.out.println("---------------------------------------------------------------");
                    System.out.println("Start Amount: " + startAmount);
                    System.out.println("Roll: " + roll);
                    System.out.println("Multiple: " + multiple);
                    System.out.println("Chance Each: " + chanceEach);
                    System.out.println("Drop Amount: " + amount);
                    System.out.println("---------------------------------------------------------------");
                }
            }

            drop.setAmount(amount);
            stacks.clear();
        }

        return drop;
    }

    public void doDamage(boolean unbreaking, Player player)
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
        if (tool.getDurability() >tool.getType().getMaxDurability()) {
            //break the pick
           player.getInventory().remove(tool);
        }
    }
}