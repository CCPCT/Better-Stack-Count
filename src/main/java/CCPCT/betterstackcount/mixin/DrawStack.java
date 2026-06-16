package CCPCT.betterstackcount.mixin;

import CCPCT.betterstackcount.config.ModConfig;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.joml.Matrix3x2fStack;

@Mixin(GuiGraphicsExtractor.class)
public abstract class DrawStack {
    @Final
    @Shadow
    private Matrix3x2fStack pose;

    @Shadow
    public abstract void text(Font font, @Nullable String str, int x, int y, int color, boolean dropShadow);

    @Shadow
    public abstract void fill(int x1, int y1, int x2, int y2, int color);


    @Inject(method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    private void onDrawText(final Font font, final ItemStack itemStack, int x, int y, final @Nullable String countText, CallbackInfo ci){
        if (!ModConfig.get().enableMod) return;
        if (ModConfig.get().fontHeight <= 5 || // font too small
                !(itemStack.getCount() == 1 && itemStack.getMaxDamage()>0 && ModConfig.get().showToolDurability && itemStack.getDamageValue()!=0) && // tool/armour
                (itemStack.getCount()<=1 && countText == null)) { // no getCount available
            ci.cancel();
            return;
        }

        String string;
        if (itemStack.getCount()==1){
            int maxDamage = itemStack.getMaxDamage();
            int durability = maxDamage-itemStack.getDamageValue();
            if (ModConfig.get().toolDurablityPercentage) {
                if (maxDamage==0){
                    // prevent devide by 0
                    string = "100%";
                } else {
                    string = 100 * durability / maxDamage + "%";
                }
            } else {
                String dura = switch ((int) Math.floor(Math.log10(durability))) {
                    case 3,4,5 -> (int)(durability*0.001)+"k";
                    case 6,7,8 -> (int)(durability*0.000001)+"M";
                    default -> String.valueOf(durability);
                };
                String max = switch ((int) Math.floor(Math.log10(maxDamage))) {
                    case 3,4,5 -> (int)(maxDamage*0.001)+"k";
                    case 6,7,8 -> (int)(maxDamage*0.000001)+"M";
                    default -> String.valueOf(maxDamage);
                };
                string = dura+"/"+max;
            }
        } else string = (countText == null ? String.valueOf(itemStack.getCount()) : countText);

        this.pose.pushMatrix();

        if (ModConfig.get().position.contains("Right")){
            x+= 17 - font.width(string)*ModConfig.get().fontHeight/100;
        }
        if (ModConfig.get().position.contains("Bottom")){
            y+= 18 - font.lineHeight*ModConfig.get().fontHeight/100;
        }

        this.pose.translate(x, y);
        this.pose.scale(ModConfig.get().fontHeight/100f, ModConfig.get().fontHeight/100f);

        if (ModConfig.get().background){
            this.fill(-1,-1,font.width(string),font.lineHeight-1,ModConfig.get().bgColour);
            this.pose.translate(0, 0);
        }

        this.text(font, string, 0, 0, ModConfig.get().colour, !ModConfig.get().background);

        this.pose.popMatrix();
        ci.cancel();
    }
}

