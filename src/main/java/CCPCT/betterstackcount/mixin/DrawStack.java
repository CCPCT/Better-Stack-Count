package CCPCT.betterstackcount.mixin;

import CCPCT.betterstackcount.config.ModConfig;
import CCPCT.betterstackcount.util.Chat;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawStack {
    @Final
    @Shadow
    private MatrixStack matrices;

    @Shadow
    public abstract int drawText(TextRenderer textRenderer, @Nullable String text, int x, int y, int color, boolean shadow);

    @Shadow
    public abstract void fill(int x1, int y1, int x2, int y2, int color);


    @Inject(method = "drawStackCount", at = @At("HEAD"), cancellable = true)
    private void onDrawText(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String stackCountText, CallbackInfo ci){
        if (!ModConfig.get().enableMod) return;
        if (ModConfig.get().fontHeight <= 5 || // font too small
                !(stack.getCount() == 1 && stack.getMaxDamage()>0 && ModConfig.get().showToolDurability && stack.getDamage()!=0) && // tool/armour
                (stack.getCount()<=1 && stackCountText == null)) { // no getCount available
            ci.cancel();
            return;
        }

        String string;
        if (stack.getCount()==1){
            int durability = stack.getMaxDamage()-stack.getDamage();
            if (ModConfig.get().toolDurablityPercentage) string = 100 * durability / stack.getMaxDamage() + "%";

            else {
                string = switch ((int) Math.floor(Math.log10(durability))) {
                    case 3,4,5 -> (int)(durability*0.001)+"k";
                    case 6,7,8 -> (int)(durability*0.000001)+"M";
                    default -> String.valueOf(durability);
                };
            }
        } else string = stackCountText == null ? String.valueOf(stack.getCount()) : stackCountText;

        this.matrices.push();

        if (ModConfig.get().position.contains("Right")){
            x+= 17 - textRenderer.getWidth(string)*ModConfig.get().fontHeight/100;
        }
        if (ModConfig.get().position.contains("Bottom")){
            y+= 18 - textRenderer.fontHeight*ModConfig.get().fontHeight/100;
        }

        this.matrices.translate(x, y, 200.0F);
        this.matrices.scale(ModConfig.get().fontHeight/100f, ModConfig.get().fontHeight/100f, 1.0f);

        if (ModConfig.get().background){
            this.fill(-1,-1,textRenderer.getWidth(string),textRenderer.fontHeight-1,ModConfig.get().bgColour);
            this.matrices.translate(0, 0, 1.0F);
        }

        this.drawText(textRenderer, string, 0, 0, ModConfig.get().colour, !ModConfig.get().background);

        this.matrices.pop();
        ci.cancel();
    }
}

