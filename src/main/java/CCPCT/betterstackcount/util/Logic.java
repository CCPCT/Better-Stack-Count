package CCPCT.betterstackcount.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Logic {
    public static int getItemSpot(Item item){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return -1;
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            if (player.getInventory().main.get(i).getItem() == item){
                return i;
            }
        }
        return -1;
    }

    public static ItemStack getItemStack(int slot){
        // input protocol number
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return null;
        if (slot == 45) return player.getInventory().offHand.getFirst();
        else if (slot <= 8) return player.getInventory().armor.get(8-slot);
        else if (slot >= 36) return player.getInventory().main.get(slot-36);
        else return player.getInventory().main.get(slot);
    }

    public static int invToProtocolSlot(int slot,int invType){
        // invType -> 0:main, 1:armour, 2:offHand
        if (invType==2) return 45;
        if (invType==1) return 8-slot;
        if (invType==0){
            if (slot<=8) {
                return slot + 36;
            } else {
                return slot;
            }
        }
        return -1;
    }

    public static int getItemCount(Item item){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return -1;
        PlayerInventory inventory = player.getInventory();
        int count = 0;
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            if (inventory.main.get(i).getItem() == item){
                count += inventory.main.get(i).getCount();
            }
        }
        if (inventory.offHand.getFirst().getItem() == item){
            count += inventory.offHand.getFirst().getCount();
        }
        return count;
    }
}