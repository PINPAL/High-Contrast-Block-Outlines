package io.pinpal.blockoutlines.config;

import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import static net.minecraft.network.chat.Component.translatable;

public class OptionButton {
    private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP = translatable("options.accessibility.high_contrast_block_outline.tooltip");

    private static final Component OUTER_COLOR_TOOLTIP = translatable("options.pinpal.blockoutlines.outer_color.tooltip");
    private static final Component INNER_COLOR_TOOLTIP = translatable("options.pinpal.blockoutlines.inner_color.tooltip");

    private static final Component OUTLINE_OPACITY_TOOLTIP = translatable("options.pinpal.blockoutlines.outer_opacity.tooltip");
    private static final Component INNER_OPACITY_TOOLTIP = translatable("options.pinpal.blockoutlines.inner_opacity.tooltip");

    private static Component sliderValueText(String translationKey, int value, boolean isDefault) {
        MutableComponent output = translatable(translationKey).append(": " + value);
        if (isDefault) {
            output.append(" (").append(translatable("resourcePack.vanilla.name")).append(")");
        }
        return output;
    }

    private static OptionInstance<Integer> colorSlider(
            String optionKey,
            String translationKey,
            IntValue configKey,
            Component tooltip,
            Consumer<Integer> colorSetter) {
        return new OptionInstance<>(optionKey,
                OptionInstance.cachedConstantTooltip(tooltip),
                (args, currentSliderValue) -> sliderValueText(translationKey, currentSliderValue,
                        (currentSliderValue == configKey.getDefault().intValue())),
                new OptionInstance.IntRange(0, 255),
                Codec.intRange(0, 255),
                configKey.get(),
                newSliderValue -> {
                    colorSetter.accept(newSliderValue);

                    // Refresh the preview if on the correct screen
                    Screen currentScreen = Minecraft.getInstance().screen;
                    if (currentScreen instanceof AccessibilityOptionsScreen) {
                        try {
                            // Reflectively call the mixin method to update the preview widgets
                            currentScreen.getClass().getDeclaredMethod("blockOutlines$updatePreviewWidgets").invoke(currentScreen);
                        } catch (Exception e) {
                            //noinspection CallToPrintStackTrace
                            e.printStackTrace();
                        }
                    }
                });
    }

    private final OptionInstance<Boolean> outerPreview = OptionInstance
            .createBoolean("options.pinpal.blockoutlines.outer_preview", true);
    private final OptionInstance<Boolean> innerPreview = OptionInstance
            .createBoolean("options.pinpal.blockoutlines.inner_preview", true);

    private final OptionInstance<Boolean> highContrastBlockOutline = OptionInstance.createBoolean(
            "options.accessibility.high_contrast_block_outline",
            OptionInstance.cachedConstantTooltip(HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP),
            BlockOutlinesConfig.ENABLED.get(),
            BlockOutlinesConfig.ENABLED::set);

    private final OptionInstance<Integer> outlineColorRed = colorSlider(
            "options.pinpal.blockoutlines.outline_color_red", "color.minecraft.red",
            BlockOutlinesConfig.OUTER_COLOR_RED, OUTER_COLOR_TOOLTIP,
            (value) -> BlockOutlinesConfig.outerColor.setRed(value));

    private final OptionInstance<Integer> outlineColorGreen = colorSlider(
            "options.pinpal.blockoutlines.outline_color_green", "color.minecraft.green",
            BlockOutlinesConfig.OUTER_COLOR_GREEN, OUTER_COLOR_TOOLTIP,
            (value) -> BlockOutlinesConfig.outerColor.setGreen(value));

    private final OptionInstance<Integer> outlineColorBlue = colorSlider(
            "options.pinpal.blockoutlines.outline_color_blue", "color.minecraft.blue",
            BlockOutlinesConfig.OUTER_COLOR_BLUE, OUTER_COLOR_TOOLTIP,
            (value) -> BlockOutlinesConfig.outerColor.setBlue(value));

    private final OptionInstance<Integer> outlineColorAlpha = colorSlider(
            "options.pinpal.blockoutlines.outline_color_alpha", "options.pinpal.blockoutlines.alpha",
            BlockOutlinesConfig.OUTER_OPACITY, OUTLINE_OPACITY_TOOLTIP,
            (value) -> BlockOutlinesConfig.outerColor.setAlpha(value));

    private final OptionInstance<Integer> innerColorRed = colorSlider(
            "options.pinpal.blockoutlines.inner_color_red", "color.minecraft.red",
            BlockOutlinesConfig.INNER_COLOR_RED, INNER_COLOR_TOOLTIP,
            (value) -> BlockOutlinesConfig.innerColor.setRed(value));

    private final OptionInstance<Integer> innerColorGreen = colorSlider(
            "options.pinpal.blockoutlines.inner_color_green", "color.minecraft.green",
            BlockOutlinesConfig.INNER_COLOR_GREEN, INNER_COLOR_TOOLTIP,
            (value) -> BlockOutlinesConfig.innerColor.setGreen(value));

    private final OptionInstance<Integer> innerColorBlue = colorSlider(
            "options.pinpal.blockoutlines.inner_color_blue", "color.minecraft.blue",
            BlockOutlinesConfig.INNER_COLOR_BLUE, INNER_COLOR_TOOLTIP,
            (value) -> BlockOutlinesConfig.innerColor.setBlue(value));

    private final OptionInstance<Integer> innerColorAlpha = colorSlider(
            "options.pinpal.blockoutlines.inner_color_alpha", "options.pinpal.blockoutlines.alpha",
            BlockOutlinesConfig.INNER_OPACITY, INNER_OPACITY_TOOLTIP,
            (value) -> BlockOutlinesConfig.innerColor.setAlpha(value));

    public OptionInstance<Boolean> highContrastBlockOutline() {
        return this.highContrastBlockOutline;
    }

    public OptionInstance<Boolean> outerPreview() {
        return this.outerPreview;
    }

    public OptionInstance<Boolean> innerPreview() {
        return this.innerPreview;
    }

    public OptionInstance<?>[] colorOptions() {
        return new OptionInstance[] {
                this.innerPreview, this.outerPreview,
                this.innerColorRed, this.outlineColorRed,
                this.innerColorGreen, this.outlineColorGreen,
                this.innerColorBlue, this.outlineColorBlue,
                this.innerColorAlpha, this.outlineColorAlpha
        };
    }
}
