package io.pinpal.blockoutlines;

import io.pinpal.blockoutlines.util.ConfigColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = BlockOutlines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockOutlinesConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("Enable High Contrast Block Outlines")
            .define("enabled", true);

    private static final ForgeConfigSpec.IntValue OUTLINE_COLOR_RED = BUILDER
            .comment("Outline Border Color - RED (0-255)")
            .defineInRange("outlineColorRed", 0, 0, 255);

    private static final ForgeConfigSpec.IntValue OUTLINE_COLOR_GREEN = BUILDER
            .comment("Outline Border Color - GREEN (0-255)")
            .defineInRange("outlineColorGreen", 0, 0, 255);

    private static final ForgeConfigSpec.IntValue OUTLINE_COLOR_BLUE = BUILDER
            .comment("Outline Border Color - BLUE (0-255)")
            .defineInRange("outlineColorBlue", 0, 0, 255);

    private static final ForgeConfigSpec.IntValue OUTLINE_OPACITY = BUILDER
            .comment("Outline Border - OPACITY (0-255)")
            .defineInRange("outlineOpacity", 255, 0, 255);

    private static final ForgeConfigSpec.IntValue INNER_COLOR_RED = BUILDER
            .comment("Inner Line Color - RED (0-255)")
            .defineInRange("innerColorRed", 87, 0, 255);

    private static final ForgeConfigSpec.IntValue INNER_COLOR_GREEN = BUILDER
            .comment("Inner Line Color - GREEN (0-255)")
            .defineInRange("innerColorGreen", 255, 0, 255);

    private static final ForgeConfigSpec.IntValue INNER_COLOR_BLUE = BUILDER
            .comment("Inner Line Color - BLUE (0-255)")
            .defineInRange("innerColorBlue", 225, 0, 255);

    private static final ForgeConfigSpec.IntValue INNER_OPACITY = BUILDER
            .comment("Inner Line - OPACITY (0-255)")
            .defineInRange("innerOpacity", 255, 0, 255);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static ConfigColor outlineColor;
    public static ConfigColor innerColor;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        outlineColor = new ConfigColor(
                OUTLINE_COLOR_RED.get(),
                OUTLINE_COLOR_GREEN.get(),
                OUTLINE_COLOR_BLUE.get(),
                OUTLINE_OPACITY.get());

        innerColor = new ConfigColor(
                INNER_COLOR_RED.get(),
                INNER_COLOR_GREEN.get(),
                INNER_COLOR_BLUE.get(),
                INNER_OPACITY.get());
    }
}
