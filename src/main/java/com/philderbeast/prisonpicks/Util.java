package com.philderbeast.prisonpicks;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class Util {

    static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    static /* varargs */ ItemStack createItemStack(Material type, int amt, String name, String ... lores) {
        ItemStack stack = new ItemStack(type, amt);
        ItemMeta im = stack.getItemMeta();
        im.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<String>();
        String[] arrstring = lores;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String str = arrstring[n2];
            lore.add(str);
            ++n2;
        }
        im.setLore(lore);
        stack.setItemMeta(im);
        return stack;
    }

    static boolean isSpaceAvailable(Player player, ItemStack item) {
        //Exclude armor slots - ids 100, 101, 102, 103 - Normal Inventory is slots 0-35
        for (int i = 0; i <= 35; i++) {
            ItemStack slotItem = player.getInventory().getItem(i);
            if (slotItem == null || (slotItem.getType() == item.getType() && item.getAmount() + slotItem.getAmount() <= slotItem.getMaxStackSize())) {
                return true;
            }
        }
        return false;
    }

    static int calculateExperienceForBlock(Block block) {
        Material mat = block.getType();
        int min, max;

        switch (mat) {
            case COAL_ORE:
                min = 0;
                max = 2;
                break;
            case DIAMOND_ORE:
            case EMERALD_ORE:
                min = 3;
                max = 7;
                break;
            case GLOWING_REDSTONE_ORE:
            case REDSTONE_ORE:
                min = 1;
                max = 5;
                break;
            case LAPIS_ORE:
                min = 2;
                max = 5;
                break;
            default:
                min = 0;
                max = 0;
                break;
        }


        return min + (int)(Math.random() * ((max - min) + 1));
    }
}

