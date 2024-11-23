package io.github.mikip98.boesearth.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModConfigScreen {
    public static Screen createScreen(Screen parentScreen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setSavingRunnable(ConfigSaver::saveConfigToFile)
                .setParentScreen(parentScreen)
                .setTitle(Text.literal("Boe's Earth Configuration Screen"));

        // Create a root category
        ConfigCategory rootCategory = builder.getOrCreateCategory(Text.literal("General Settings"));

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Snow on Leaves Blockstate"), ModConfig.snowOnLeavesBlockstate)
                .setTooltip(Text.literal("Enables snow on leaves blockstate."))
                .setDefaultValue(DefaultConfig.defaultSnowOnLeavesBlockstate)
                .setSaveConsumer(value -> ModConfig.snowOnLeavesBlockstate = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Vines on Leaves Blockstate"), ModConfig.vinesOnLeavesBlockstate)
                .setTooltip(Text.literal("Enables vines on leaves blockstate."))
                .setDefaultValue(DefaultConfig.defaultVinesOnLeavesBlockstate)
                .setSaveConsumer(value -> ModConfig.vinesOnLeavesBlockstate = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Carpet on Leaves Blockstate"), ModConfig.carpetOnLeavesBlockstate)
                .setTooltip(Text.literal("Enables carpet on leaves blockstate."))
                .setDefaultValue(DefaultConfig.defaultCarpetOnLeavesBlockstate)
                .setSaveConsumer(value -> ModConfig.carpetOnLeavesBlockstate = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Leaves with Snow on Top Blockstate"), ModConfig.leavesWithSnowOnTopBlockstate)
                .setTooltip(Text.literal("Enables leaves with snow on top blockstate."))
                .setDefaultValue(DefaultConfig.defaultLeavesWithSnowOnTopBlockstate)
                .setSaveConsumer(value -> ModConfig.leavesWithSnowOnTopBlockstate = value)
                .build()
        );


        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Correct Snow & Carpets Blockstates with Time"), ModConfig.correctSnowAndCarpetsWithTime)
                .setTooltip(Text.literal("Corrects snow and carpets blockstates with time, increase `randomTickSpeed` to speed up"))
                .setDefaultValue(DefaultConfig.defaultCorrectSnowWithTime)
                .setSaveConsumer(value -> ModConfig.correctSnowAndCarpetsWithTime = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Correct Vines Blockstate with Time"), ModConfig.correctVinesWithTime)
                .setTooltip(Text.literal("Corrects vines blockstate with time, increase `randomTickSpeed` to speed up"))
                .setDefaultValue(DefaultConfig.defaultCorrectVinesWithTime)
                .setSaveConsumer(value -> ModConfig.correctVinesWithTime = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Correct Leaves Blockstate with Time"), ModConfig.correctLeavesWithTime)
                .setTooltip(Text.literal("Corrects leaves blockstate with time, increase `randomTickSpeed` to speed up"))
                .setDefaultValue(DefaultConfig.defaultCorrectLeavesWithTime)
                .setSaveConsumer(value -> ModConfig.correctLeavesWithTime = value)
                .build()
        );


        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startEnumSelector(Text.literal("Vine Priority"), VinePriority.class, ModConfig.vinePriority)
                .setTooltip(Text.literal("Sets the priority of vines.\nBy default if vine is touching both normal block and leaves, it would act as if placed on normal block."))
                .setDefaultValue(DefaultConfig.defaultVinePriority)
                .setSaveConsumer(value -> {
                    ModConfig.vinePriority = value;
                    ModConfig.VinePrioritiseLeaves = value == VinePriority.LEAVES;
                })
                .build()
        );

        return builder.build();
    }

}
