package CCPCT.betterstackcount.mixin;

import CCPCT.betterstackcount.config.ModConfig;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import CCPCT.betterstackcount.util.Chat;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Function;
@Mixin(DrawContext.class)
public abstract class DrawStack {
    @Final
    @Shadow
    private MatrixStack matrices;

    @Shadow
    public abstract int drawText(TextRenderer textRenderer, @Nullable String text, int x, int y, int color, boolean shadow);


    @Inject(method = "drawStackCount", at = @At("HEAD"), cancellable = true)
    private void onDrawText(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String stackCountText, CallbackInfo ci){
        if (!ModConfig.get().enableMod) return;
        if (ModConfig.get().fontHeight <= 5 || (stack.getCount() <= 1 && stackCountText == null)) {
            ci.cancel();
            return;
        }

        String string = stackCountText == null ? String.valueOf(stack.getCount()) : stackCountText;

        this.matrices.push();

        if (ModConfig.get().position.contains("Right")){
            x+= 17 - textRenderer.getWidth(string)*ModConfig.get().fontHeight/100;
        }
        if (ModConfig.get().position.contains("Bottom")){
            y+= 18 - textRenderer.fontHeight*ModConfig.get().fontHeight/100;
        }

        this.matrices.translate(x, y, 200.0F);
        this.matrices.scale(ModConfig.get().fontHeight/100f, ModConfig.get().fontHeight/100f, 1.0f);



        this.drawText(textRenderer, string, 0, 0, ModConfig.get().colour, true);

        this.matrices.pop();
        ci.cancel();
    }
}

