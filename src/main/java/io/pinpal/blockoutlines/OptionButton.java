package io.pinpal.blockoutlines;

import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

public class OptionButton {
    private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP = Component
            .translatable("options.accessibility.high_contrast_block_outline.tooltip");

    private final OptionInstance<Boolean> highContrastBlockOutline = OptionInstance.createBoolean(
            "options.accessibility.high_contrast_block_outline",
            OptionInstance.cachedConstantTooltip(HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP),
            BlockOutlinesConfig.ENABLED.get(), // Default value
            (newButtonState) -> {
                BlockOutlinesConfig.ENABLED.set(newButtonState);
            });

    public OptionInstance<Boolean> highContrastBlockOutline() {
        return this.highContrastBlockOutline;
    }
}
