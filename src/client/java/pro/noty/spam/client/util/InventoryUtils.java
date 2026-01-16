package pro.noty.spam.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class InventoryUtils {
    public static int findItemInHotbar(Item item) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (int i = 0; i < 9; i++) {
            if (client.player.getInventory().getStack(i).isOf(item)) {
                return i;
            }
        }
        return -1;
    }

    public static void selectSlot(int slot) {
        if (slot != -1) {
            MinecraftClient.getInstance().player.getInventory().setSelectedSlot(slot);
        }
    }
}