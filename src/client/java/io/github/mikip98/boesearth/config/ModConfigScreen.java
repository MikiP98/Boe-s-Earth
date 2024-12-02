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
                .setSaveConsumer(value -> {
                    ModConfig.snowOnLeavesBlockstate = value;
                    ModConfig.isOnLeavesBlockstate = ModConfig.snowOnLeavesBlockstate && ModConfig.carpetOnLeavesBlockstate;
                })
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
                .setSaveConsumer(value -> {
                    ModConfig.carpetOnLeavesBlockstate = value;
                    ModConfig.isOnLeavesBlockstate = ModConfig.snowOnLeavesBlockstate && ModConfig.carpetOnLeavesBlockstate;
                })
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
                .startBooleanToggle(Text.literal("Update Snow Blockstate on random ticks"), ModConfig.doRandomTickSnowUpdates)
                .setTooltip(Text.literal("Corrects snow blockstates with time, increase `randomTickSpeed` to speed up\nIt can also update leaves if beneath snow"))
                .setDefaultValue(DefaultConfig.defaultDoRandomTickSnowUpdates)
                .setSaveConsumer(value -> ModConfig.doRandomTickSnowUpdates = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Update Vine Blockstate on random ticks"), ModConfig.doRandomTickVineUpdates)
                .setTooltip(Text.literal("Corrects vine blockstates with time, increase `randomTickSpeed` to speed up"))
                .setDefaultValue(DefaultConfig.defaultDoRandomTickVineUpdates)
                .setSaveConsumer(value -> ModConfig.doRandomTickVineUpdates = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Update Leaves Blockstate on random ticks"), ModConfig.doRandomTickLeavesUpdates)
                .setTooltip(Text.literal("Corrects leaves blockstate with time, increase `randomTickSpeed` to speed up\nIt can also update snow and carpets if above leaves"))
                .setDefaultValue(DefaultConfig.defaultDoRandomTickLeavesUpdates)
                .setSaveConsumer(value -> ModConfig.doRandomTickLeavesUpdates = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Update Snow Blockstate on neighbor change"), ModConfig.updateSnowOnNeighborChange)
                .setTooltip(Text.literal("Updates snow blockstates on neighbor change"))
                .setDefaultValue(DefaultConfig.defaultUpdateSnowOnNeighborChange)
                .setSaveConsumer(value -> ModConfig.updateSnowOnNeighborChange = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Update Vine Blockstate on neighbor change"), ModConfig.updateVineOnNeighborChange)
                .setTooltip(Text.literal("Updates vine blockstates on neighbor change"))
                .setDefaultValue(DefaultConfig.defaultUpdateVineOnNeighborChange)
                .setSaveConsumer(value -> ModConfig.updateVineOnNeighborChange = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Update Leaves Blockstate on neighbor change"), ModConfig.updateLeavesOnNeighborChange)
                .setTooltip(Text.literal("Updates leaves blockstates on neighbor change"))
                .setDefaultValue(DefaultConfig.defaultUpdateLeavesOnNeighborChange)
                .setSaveConsumer(value -> ModConfig.updateLeavesOnNeighborChange = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Update Leaves Blockstate on neighbor change only above"), ModConfig.updateLeavesOnNeighborChangeOnlyAbove)
                .setTooltip(Text.literal("Updates leaves blockstates on neighbor change only if above"))
                .setDefaultValue(DefaultConfig.defaultUpdateLeavesOnNeighborChangeOnlyAbove)
                .setSaveConsumer(value -> ModConfig.updateLeavesOnNeighborChangeOnlyAbove = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Max Snow Update Chain"), ModConfig.maxSnowUpdateChain, 2, 512)
                .setTooltip(Text.literal("Maximum update chain for snow blockstate."))
                .setDefaultValue(DefaultConfig.defaultMaxSnowUpdateChain)
                .setSaveConsumer(value -> ModConfig.maxSnowUpdateChain = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Max Vine Update Chain"), ModConfig.maxVineUpdateChain, 2, 512)
                .setTooltip(Text.literal("Maximum update chain for vine blockstate."))
                .setDefaultValue(DefaultConfig.defaultMaxVineUpdateChain)
                .setSaveConsumer(value -> ModConfig.maxVineUpdateChain = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Max Leaves Update Chain"), ModConfig.maxLeavesUpdateChain, 2, 512)
                .setTooltip(Text.literal("Maximum update chain for leaves blockstate."))
                .setDefaultValue(DefaultConfig.defaultMaxLeavesUpdateChain)
                .setSaveConsumer(value -> ModConfig.maxLeavesUpdateChain = value)
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
