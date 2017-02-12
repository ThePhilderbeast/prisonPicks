package com.philderbeast.prisonpicks;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

//NMS
import net.minecraft.server.v1_11_R1.BlockOre;
import net.minecraft.server.v1_11_R1.BlockRedstoneOre;
import net.minecraft.server.v1_11_R1.Item;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftMagicNumbers;

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
            }else{
                enchants.put(FORTUNE, false);
            }

            if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
                enchants.put(SILK_TOUCH, true);
            }else{
                enchants.put(SILK_TOUCH, false);
            }
        return enchants;
    }

    public boolean doBreak(Block block, Map<String, Boolean> enchants, Player player)
    {
        boolean noInventorySpace = false;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (block.getType() != Material.BEDROCK) {
                if (enchants.get(SILK_TOUCH)) {
                    ItemStack blockStack = new ItemStack(block.getTypeId(), 1);
                    //they have silk touch so give them the block
                    if (Util.isSpaceAvailable(player, blockStack)) {
                        player.getInventory().addItem(blockStack);
                    } else {
                        noInventorySpace = true;
                    }
                } else {
                    Collection<ItemStack> stacks = block.getDrops(player.getInventory().getItemInMainHand());
                    for (ItemStack newItem : stacks) {
                        if (enchants.get(FORTUNE)) {
                            int fortune = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                            newItem.setAmount(Pick.getDropAmount(fortune, block));
                        }

                        if (Util.isSpaceAvailable(player, newItem)) {
                            player.getInventory().addItem(newItem);
                        } else {
                            noInventorySpace = true;
                        }
                        int exp = Util.calculateExperienceForBlock(block);
                        //System.out.println("giving " + player.getName() + " " + exp + " experance")
                        player.giveExp(exp); //Give Player Experience
                    }
                    stacks.clear();
                }
                block.setType(Material.AIR);
        }
        return noInventorySpace;
    }


    public static int getDropAmount(int enchantmentLevel, Block block) {
        if (block.getType().name().contains("ORE") && !block.getType().name().contains("REDSTONE")) {
            BlockOre nmsBlock = (BlockOre) CraftMagicNumbers.getBlock(block);

            int all_final = 0;
            Random rand = new Random();
            if(Item.getItemOf(nmsBlock) != nmsBlock.getDropType(nmsBlock.getBlockData(), rand, enchantmentLevel)) {
                all_final = rand.nextInt(enchantmentLevel + 2) - 1;
                if(all_final < 0) {
                    all_final = 0;
                }

                return nmsBlock.a(rand) * (all_final + 1);
            } else {
                return nmsBlock.a(rand);
            }
        } else if (block.getType().name().contains("REDSTONE")) {
            //Ensure we don't crash when mining Redstone
            BlockRedstoneOre nmsBlock = (BlockRedstoneOre) CraftMagicNumbers.getBlock(block);

            int all_final = 0;
            Random rand = new Random();
            if(Item.getItemOf(nmsBlock) != nmsBlock.getDropType(nmsBlock.getBlockData(), rand, enchantmentLevel)) {
                all_final = rand.nextInt(enchantmentLevel + 2) - 1;
                if(all_final < 0) {
                    all_final = 0;
                }
                return nmsBlock.a(rand) * (all_final + 1);
            } else {
                return nmsBlock.a(rand);
            }
        }
        return 1;
    }

    public ItemStack doDamage(boolean unbreaking, ItemStack item)
    {   
        if (!unbreaking) {
            item.setDurability((short)(item.getDurability() + 1));
        } else if (Util.randInt(1, 3) == 1) {
            item.setDurability((short)(item.getDurability() + 1));
        }
        if (item.getDurability() > item.getType().getMaxDurability()) {
            //break the pick
            item = null;
        }
        System.out.println(item.getDurability());
        return item;
    }

}