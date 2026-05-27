package CCPCT.betterstackcount.util;

import CCPCT.betterstackcount.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;


public class Chat {
    public static <T> void debug(T message) {
        if (ModConfig.get().debug) {
            Minecraft client = Minecraft.getInstance();
            if (client.player != null) {
                client.player.sendSystemMessage(Component.literal("§7[Debug]§r "+message));
            }
        }
    }
}
