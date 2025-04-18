package io.pinpal.blockoutlines.config;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;

import io.pinpal.blockoutlines.util.ConfigColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class OptionButton {
    private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP = Component
            .translatable("options.accessibility.high_contrast_block_outline.tooltip");

    private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_OUTER_COLOR_TOOLTIP = Component
            .translatable("options.pinpal.blockoutlines.outer_color.tooltip");
    private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_INNER_COLOR_TOOLTIP = Component
            .translatable("options.pinpal.blockoutlines.inner_color.tooltip");

    private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_OUTER_OPACITY_TOOLTIP = Component
            .translatable("options.pinpal.blockoutlines.outer_opacity.tooltip");
    private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_INNER_OPACITY_TOOLTIP = Component
            .translatable("options.pinpal.blockoutlines.inner_opacity.tooltip");

    private static final Component sliderValueText(String translationKey, int value, boolean isDefault) {
        MutableComponent output = Component.translatable(translationKey).append(": " + String.valueOf(value));
        if (isDefault) {
            output.append(" (").append(Component.translatable("resourcePack.vanilla.name")).append(")");
        }
        return output;
    }

    private static final OptionInstance<Integer> colorSlider(
            String optionKey,
            String translationKey,
            IntValue configKey,
            Component tooltip,
            Object colorConfigObject,
            BiConsumer<Object, Integer> colorSetter) {
        return new OptionInstance<>(optionKey,
                OptionInstance.cachedConstantTooltip(tooltip),
                (args, currentSliderValue) -> {
                    return sliderValueText(translationKey, currentSliderValue,
                            (currentSliderValue == configKey.getDefault().intValue()));
                },
                new OptionInstance.IntRange(0, 255),
                Codec.intRange(0, 255),
                configKey.get(), // Starting Value
                newValue -> {
                    colorSetter.accept(colorConfigObject, newValue);

                    // Refresh the preview if on the correct screen
                    Screen currentScreen = Minecraft.getInstance().screen;
                    if (currentScreen instanceof AccessibilityOptionsScreen) {
                        try {
                            Method method = currentScreen.getClass().getDeclaredMethod("blockOutlines$updatePreviewWidgets");
                            method.invoke(currentScreen);
                        } catch (Exception e) {
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
            BlockOutlinesConfig.ENABLED.get(), // Default value
            BlockOutlinesConfig.ENABLED::set);

    private final OptionInstance<Integer> outlineColorRed = colorSlider(
            "options.pinpal.blockoutlines.outline_color_red", "color.minecraft.red",
            BlockOutlinesConfig.OUTER_COLOR_RED, HIGH_CONTRAST_BLOCK_OUTLINE_OUTER_COLOR_TOOLTIP,
            BlockOutlinesConfig.outerColor, (target, value) -> ((ConfigColor) target).setRed(value));

    private final OptionInstance<Integer> outlineColorGreen = colorSlider(
            "options.pinpal.blockoutlines.outline_color_green", "color.minecraft.green",
            BlockOutlinesConfig.OUTER_COLOR_GREEN, HIGH_CONTRAST_BLOCK_OUTLINE_OUTER_COLOR_TOOLTIP,
            BlockOutlinesConfig.outerColor, (target, value) -> ((ConfigColor) target).setGreen(value));

    private final OptionInstance<Integer> outlineColorBlue = colorSlider(
            "options.pinpal.blockoutlines.outline_color_blue", "color.minecraft.blue",
            BlockOutlinesConfig.OUTER_COLOR_BLUE, HIGH_CONTRAST_BLOCK_OUTLINE_OUTER_COLOR_TOOLTIP,
            BlockOutlinesConfig.outerColor, (target, value) -> {
                ((ConfigColor) target).setBlue(value);
            });

    private final OptionInstance<Integer> outlineColorAlpha = colorSlider(
            "options.pinpal.blockoutlines.outline_color_alpha", "options.pinpal.blockoutlines.alpha",
            BlockOutlinesConfig.OUTER_OPACITY, HIGH_CONTRAST_BLOCK_OUTLINE_OUTER_OPACITY_TOOLTIP,
            BlockOutlinesConfig.outerColor, (target, value) -> {
                ((ConfigColor) target).setAlpha(value);
            });

    private final OptionInstance<Integer> innerColorRed = colorSlider(
            "options.pinpal.blockoutlines.inner_color_red", "color.minecraft.red",
            BlockOutlinesConfig.INNER_COLOR_RED, HIGH_CONTRAST_BLOCK_OUTLINE_INNER_COLOR_TOOLTIP,
            BlockOutlinesConfig.innerColor, (target, value) -> {
                ((ConfigColor) target).setRed(value);
            });

    private final OptionInstance<Integer> innerColorGreen = colorSlider(
            "options.pinpal.blockoutlines.inner_color_green", "color.minecraft.green",
            BlockOutlinesConfig.INNER_COLOR_GREEN, HIGH_CONTRAST_BLOCK_OUTLINE_INNER_COLOR_TOOLTIP,
            BlockOutlinesConfig.innerColor, (target, value) -> {
                ((ConfigColor) target).setGreen(value);
            });

    private final OptionInstance<Integer> innerColorBlue = colorSlider(
            "options.pinpal.blockoutlines.inner_color_blue", "color.minecraft.blue",
            BlockOutlinesConfig.INNER_COLOR_BLUE, HIGH_CONTRAST_BLOCK_OUTLINE_INNER_COLOR_TOOLTIP,
            BlockOutlinesConfig.innerColor, (target, value) -> {
                ((ConfigColor) target).setBlue(value);
            });

    private final OptionInstance<Integer> innerColorAlpha = colorSlider(
            "options.pinpal.blockoutlines.inner_color_alpha", "options.pinpal.blockoutlines.alpha",
            BlockOutlinesConfig.INNER_OPACITY, HIGH_CONTRAST_BLOCK_OUTLINE_INNER_OPACITY_TOOLTIP,
            BlockOutlinesConfig.innerColor, (target, value) -> {
                ((ConfigColor) target).setAlpha(value);
            });

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
