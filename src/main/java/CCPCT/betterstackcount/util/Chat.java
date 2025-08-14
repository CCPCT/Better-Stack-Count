package CCPCT.betterstackcount.util;

import CCPCT.betterstackcount.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;


public class Chat {
    public static <T> void debug(T message) {
        if (ModConfig.get().debug) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§7[Debug]§r "+message), false);
            }
        }
    }
}
