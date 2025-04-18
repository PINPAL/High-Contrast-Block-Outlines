package io.pinpal.blockoutlines.config;

import io.pinpal.blockoutlines.BlockOutlines;
import io.pinpal.blockoutlines.util.ConfigColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = BlockOutlines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockOutlinesConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLED;

    public static final ForgeConfigSpec.IntValue OUTER_COLOR_RED;

    public static final ForgeConfigSpec.IntValue OUTER_COLOR_GREEN;

    public static final ForgeConfigSpec.IntValue OUTER_COLOR_BLUE;

    public static final ForgeConfigSpec.IntValue OUTER_OPACITY;

    public static final ForgeConfigSpec.IntValue INNER_COLOR_RED;

    public static final ForgeConfigSpec.IntValue INNER_COLOR_GREEN;

    public static final ForgeConfigSpec.IntValue INNER_COLOR_BLUE;

    public static final ForgeConfigSpec.IntValue INNER_OPACITY;

    static {
        BUILDER.push("BlockOutlines");

        ENABLED = BUILDER
                .comment("Enable High Contrast Block Outlines")
                .define("enabled", true);

        BUILDER.pop();
        BUILDER.push("Outline Color");

        OUTER_COLOR_RED = BUILDER
                .comment("Outer Border Color - RED")
                .defineInRange("outerColorRed", 0, 0, 255);

        OUTER_COLOR_GREEN = BUILDER
                .comment("Outer Border Color - GREEN")
                .defineInRange("outerColorGreen", 0, 0, 255);

        OUTER_COLOR_BLUE = BUILDER
                .comment("Outer Border Color - BLUE")
                .defineInRange("outerColorBlue", 0, 0, 255);

        OUTER_OPACITY = BUILDER
                .comment("Outer Border - OPACITY")
                .defineInRange("outerOpacity", 255, 0, 255);

        BUILDER.pop();
        BUILDER.push("Inner Color");

        INNER_COLOR_RED = BUILDER
                .comment("Inner Line Color - RED")
                .defineInRange("innerColorRed", 87, 0, 255);

        INNER_COLOR_GREEN = BUILDER
                .comment("Inner Line Color - GREEN")
                .defineInRange("innerColorGreen", 255, 0, 255);

        INNER_COLOR_BLUE = BUILDER
                .comment("Inner Line Color - BLUE")
                .defineInRange("innerColorBlue", 225, 0, 255);

        INNER_OPACITY = BUILDER
                .comment("Inner Line - OPACITY")
                .defineInRange("innerOpacity", 255, 0, 255);
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static ConfigColor outerColor;
    public static ConfigColor innerColor;

    public static void updateColorConfigs() {
        OUTER_COLOR_RED.set(outerColor.getRedInt());
        OUTER_COLOR_GREEN.set(outerColor.getGreenInt());
        OUTER_COLOR_BLUE.set(outerColor.getBlueInt());
        OUTER_OPACITY.set(outerColor.getAlphaInt());

        INNER_COLOR_RED.set(innerColor.getRedInt());
        INNER_COLOR_GREEN.set(innerColor.getGreenInt());
        INNER_COLOR_BLUE.set(innerColor.getBlueInt());
        INNER_OPACITY.set(innerColor.getAlphaInt());
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        outerColor = new ConfigColor(
                OUTER_COLOR_RED.get(),
                OUTER_COLOR_GREEN.get(),
                OUTER_COLOR_BLUE.get(),
                OUTER_OPACITY.get());

        innerColor = new ConfigColor(
                INNER_COLOR_RED.get(),
                INNER_COLOR_GREEN.get(),
                INNER_COLOR_BLUE.get(),
                INNER_OPACITY.get());
    }
}
