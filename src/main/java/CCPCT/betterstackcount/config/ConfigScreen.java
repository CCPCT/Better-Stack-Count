package CCPCT.betterstackcount.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


import java.util.List;

public class ConfigScreen extends Screen {

    protected ConfigScreen() {
        super(Component.literal("Totem Utils Config"));
    }

    public static Screen getConfigScreen(Screen parent) {
        ModConfig.load();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Totem Utils Config"))
                .setSavingRunnable(ModConfig::save);

        ConfigCategory generalTab = builder.getOrCreateCategory(Component.literal("General"));
        ConfigCategory screenTab = builder.getOrCreateCategory(Component.literal("Stack Count Text"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Auto Totem toggle
        generalTab.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Mod"),ModConfig.get().enableMod)
                .setDefaultValue(false)
                .setTooltip(Component.literal("false to disable mod"))
                .setSaveConsumer(newValue -> ModConfig.get().enableMod = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startBooleanToggle(Component.literal("Debug"),ModConfig.get().debug)
                .setDefaultValue(false)
                .setTooltip(Component.literal("get debug chat for dev. Not recommended for normal use"))
                .setSaveConsumer(newValue -> ModConfig.get().debug = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startIntField(Component.literal("Font Size %"),ModConfig.get().fontHeight)
                .setDefaultValue(100)
                .setTooltip(Component.literal("% of original size. 100=original, 0 to disable showing count"))
                .setSaveConsumer(newValue -> ModConfig.get().fontHeight = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startAlphaColorField(Component.literal("Font Colour"),ModConfig.get().colour)
                .setDefaultValue(0xFFFFFFFF)
                .setTooltip(Component.literal("both ARGB and RGB acceptable"))
                .setSaveConsumer(newValue -> ModConfig.get().colour = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startStrField(Component.literal("Font"),ModConfig.get().font)
                .setDefaultValue("")
                .setTooltip(Component.literal("Use a font (.ttf) from Componenture pack.\nLeave empty to disable"))
                .setSaveConsumer(newValue -> ModConfig.get().font = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startStringDropdownMenu(Component.literal("Position of Stack count"),ModConfig.get().position)
                .setSelections(List.of("Top Left", "Top Right", "Bottom Left", "Bottom Right"))
                .setDefaultValue("Bottom Right")
                .setTooltip(Component.literal("Select one option\n(Bottom Right is default)"))
                .setSaveConsumer(newValue -> ModConfig.get().position = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show Tool Durability"),ModConfig.get().showToolDurability)
                .setDefaultValue(false)
                .setTooltip(Component.literal("when tools/ armour has stack count 1 and durability\ntoggle this to show its durability"))
                .setSaveConsumer(newValue -> ModConfig.get().showToolDurability = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startBooleanToggle(Component.literal("Tool Durability as percentage"),ModConfig.get().toolDurablityPercentage)
                .setDefaultValue(false)
                .setTooltip(Component.literal("if enabled, stackcount will show percentage of item instead of the actual durability"))
                .setSaveConsumer(newValue -> ModConfig.get().toolDurablityPercentage = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startBooleanToggle(Component.literal("Background for stack count"),ModConfig.get().background)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> ModConfig.get().background = newValue)
                .build());

        screenTab.addEntry(entryBuilder.startAlphaColorField(Component.literal("Background Colour"),ModConfig.get().bgColour)
                .setDefaultValue(0x50000000)
                .setTooltip(Component.literal("both ARGB and RGB acceptable"))
                .setSaveConsumer(newValue -> ModConfig.get().bgColour = newValue)
                .build());

        return builder.build();
    }
}
