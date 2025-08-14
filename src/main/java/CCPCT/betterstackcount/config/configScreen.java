package CCPCT.betterstackcount.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class configScreen extends Screen {

    protected configScreen() {
        super(Text.literal("Totem Utils Config"));
    }

    public static Screen getConfigScreen(Screen parent) {
        ModConfig.load();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Totem Utils Config"))
                .setSavingRunnable(ModConfig::save);

        ConfigCategory generalTab = builder.getOrCreateCategory(Text.literal("General"));
        ConfigCategory screenTab = builder.getOrCreateCategory(Text.literal("Stack Count Text"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Auto Totem toggle
        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Mod"),ModConfig.get().enableMod)
                .setDefaultValue(false)
                .setTooltip(Text.literal("false to disable mod"))
                .setSaveConsumer(newValue -> ModConfig.get().enableMod = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Debug"),ModConfig.get().debug)
                .setDefaultValue(false)
                .setTooltip(Text.literal("get debug chat for dev. Not recommended for normal use"))
                .setSaveConsumer(newValue -> ModConfig.get().debug = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startIntField(Text.literal("Font Size %"),ModConfig.get().fontHeight)
                .setDefaultValue(100)
                .setTooltip(Text.literal("% of original size. 100=original, 0 to disable showing count"))
                .setSaveConsumer(newValue -> ModConfig.get().fontHeight = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startAlphaColorField(Text.literal("Font Colour"),ModConfig.get().colour)
                .setDefaultValue(0xFFFFFFFF)
                .setTooltip(Text.literal("both ARGB and RGB acceptable"))
                .setSaveConsumer(newValue -> ModConfig.get().colour = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startStrField(Text.literal("Font"),ModConfig.get().font)
                .setDefaultValue("")
                .setTooltip(Text.literal("Use a font (.ttf) from texture pack.\nLeave empty to disable"))
                .setSaveConsumer(newValue -> ModConfig.get().font = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startStringDropdownMenu(Text.literal("Position of Stack count"),ModConfig.get().position)
                .setSelections(List.of("Top Left", "Top Right", "Bottom Left", "Bottom Right"))
                .setDefaultValue("Bottom Right")
                .setTooltip(Text.literal("Select one option\n(Bottom Right is default)"))
                .setSaveConsumer(newValue -> ModConfig.get().position = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Background for stack count"),ModConfig.get().background)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> ModConfig.get().background = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startAlphaColorField(Text.literal("Background Colour"),ModConfig.get().bgColour)
                .setDefaultValue(0x50000000)
                .setTooltip(Text.literal("both ARGB and RGB acceptable"))
                .setSaveConsumer(newValue -> ModConfig.get().bgColour = newValue)
                .build());

        return builder.build();
    }
}
