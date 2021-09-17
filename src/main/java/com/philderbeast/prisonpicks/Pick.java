package com.philderbeast.prisonpicks;

import java.util.*;

import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.philderbeast.autopickup.API.AutoPickupMethods;

abstract class Pick
{

    static final String FORTUNE = "fortune";
    static final String UNBREAKING = "unbreaking";
    static final String SILK_TOUCH = "silktouch";

    public abstract void breakBlock(BlockBreakEvent event);

    static boolean isPick(ItemStack item)
    {
        return item != null 
            && ( item.getType() == Material.DIAMOND_PICKAXE 
              || item.getType() == Material.NETHERITE_PICKAXE ) 
            && item.hasItemMeta()
            && item.getItemMeta().hasLore();
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

    void doBreak(Block block, Map<String, Boolean> enchants, Player player, Material material, ItemStack tool)
    {
        if (block.getType() != Material.BEDROCK && block.getType() != Material.AIR)
        {
            if (enchants.get(SILK_TOUCH))
            {
                ItemStack blockStack;
                if (material == null)
                {
                    blockStack = new ItemStack(block.getType(), 1);
                }else
                {
                    blockStack = new ItemStack(material, 1);
                }

                //they have silk touch so give them the block
                if (Util.getAutoPickup() != null)
                {
                    AutoPickupMethods.autoGive(player, blockStack);
                }else
                {
                    player.getInventory().addItem(blockStack);
                }

            }else
            {
                if (material != null)
                {
                    block.setType(material);
                }

                ItemStack item = player.getInventory().getItemInMainHand();
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

        if (block.getType() == Material.IRON_ORE || block.getType() == Material.GOLD_ORE)
        {
            drop.setAmount(1);
            return drop;
        }

        if (block.getType() == Material.REDSTONE_ORE || block.getType() == Material.GLOWSTONE)
        {
            switch (block.getType())
            {
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

    // for compatability
    void doDamage(boolean unbreaking, Player player)
    {
        doDamage(unbreaking, player, 1);
    }

    void doDamage(boolean unbreaking, Player player, int blocksHit)
    {
        ItemStack tool = player.getInventory().getItemInMainHand();

        int unbreakingLevel = tool.getEnchantmentLevel(Enchantment.DURABILITY);

        int damage = 0;

        for (int i = 0; i < blocksHit; i++)
        {
        
            if ( ! unbreaking)
            {
                Damageable toolMeta = (Damageable) tool.getItemMeta();
                toolMeta.setDamage(toolMeta.getDamage() + 1);
                tool.setItemMeta(toolMeta);
            }else if (unbreakingLevel > 0)
            {
                Random r = new Random();
                int chanceToReduce = (100/(unbreakingLevel + 1));
                int roll = r.nextInt(100);

                if (roll <= chanceToReduce)
                {
                    damage++;
                }
            }
        }

        if (damage > 0) {
            Damageable toolMeta = (Damageable) tool.getItemMeta();
            toolMeta.setDamage(toolMeta.getDamage() + 1);
            tool.setItemMeta(toolMeta);
        }

        if (((Damageable) tool.getItemMeta()).getDamage() > tool.getType().getMaxDurability())
        {
            String pick = "";

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

            Bukkit.broadcastMessage(Config.CHAT_PICK_BREAK + player.getName() + " just broke their " + pick + Config.CHAT_PICK_BREAK + " while mining... Press 'F' to pay respects.");

            if(Config.DEBUG)
            {
                System.out.println("-------------------------------------");
                System.out.println("Player: " + player.getName());
                System.out.println("UUID: " + player.getUniqueId());
                System.out.println("Pick type: " + pick);
                System.out.println("Pick Current Durability: " + ((Damageable) tool.getItemMeta()).getDamage());
                System.out.println("Pick Max Durability: " + tool.getType().getMaxDurability());
                System.out.println("-------------------------------------");
            }
            //break the pick
            player.getInventory().remove(tool);
        }
    }

    ItemMeta increaseNBTCount(ItemMeta meta, NamespacedKey key, Long increase)
    {
        PersistentDataContainer container = meta.getPersistentDataContainer();

        Long tagCount = (long)0;
        if (!container.has(key, PersistentDataType.LONG))
        {
            container.set(key, PersistentDataType.LONG, (long)0);
        } else
        {
            tagCount = container.get(key, PersistentDataType.LONG);
        }
        container.set(key, PersistentDataType.LONG, tagCount + increase);
        return meta;
    }

    Long getNBTCount(ItemMeta meta, NamespacedKey key)
    {
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(key, PersistentDataType.LONG))
        {
            return -1L;
        } else
        {
            return container.get(key, PersistentDataType.LONG);
        }
    }

    ItemMeta updateLore(ItemMeta meta)
    {
        List<String> lore = meta.getLore();
        boolean blocksBrokenExists = false;
        boolean emeraldsExplodedExists = false;
        Long blocksBroken = getNBTCount(meta, Config.BLOCKS_BROKEN);
        Long emeraldsExploded = getNBTCount(meta, Config.EMERALDS_EXPLODED);
        for (int i = 0; i < lore.size(); i++)
        {
            String lore_line = lore.get(i);
            if (blocksBroken > -1 && lore_line.contains(Config.BLOCKS_BROKEN_LORE))
            {
                lore.set(i, Config.BLOCKS_BROKEN_LORE + blocksBroken);
                blocksBrokenExists = true;
            }
            else if (emeraldsExploded > -1 && lore_line.contains(Config.EMERALDS_EXPLODED_LORE))
            {
                lore.set(i, Config.EMERALDS_EXPLODED_LORE + emeraldsExploded);
                emeraldsExplodedExists = true;
            }
        }
        if (!blocksBrokenExists && blocksBroken > -1) lore.add(Config.BLOCKS_BROKEN_LORE + blocksBroken);
        if (!emeraldsExplodedExists && emeraldsExploded > -1) lore.add(Config.EMERALDS_EXPLODED_LORE + emeraldsExploded);
        meta.setLore(lore);
        return meta;
    }
}
