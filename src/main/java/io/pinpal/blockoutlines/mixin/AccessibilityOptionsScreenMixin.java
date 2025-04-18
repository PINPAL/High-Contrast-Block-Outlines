package io.pinpal.blockoutlines.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.Component;

import io.pinpal.blockoutlines.config.OptionButton;
import io.pinpal.blockoutlines.config.BlockOutlinesConfig;
import io.pinpal.blockoutlines.interfaces.AccessibilityOptionsScreenAccessor;

@SuppressWarnings("DataFlowIssue")
@Mixin(AccessibilityOptionsScreen.class)
public abstract class AccessibilityOptionsScreenMixin extends SimpleOptionsSubScreen implements AccessibilityOptionsScreenAccessor {

    @Unique
    private final OptionButton blockOutlines$options = new OptionButton();

    @Unique
    private AbstractWidget blockOutlines$outerPreviewWidget;
    @Unique
    private AbstractWidget blockOutlines$innerPreviewWidget;

    @Shadow
    private static OptionInstance<?>[] options(Options options) {
        return null;
    }

    // Fake constructor to allow the mixin to be created
    public AccessibilityOptionsScreenMixin(Screen screen, Options options) {
        super(screen, options, Component.empty(), options(options));
    }

    // Set the color of the preview widgets based on the current dynamic config values
    // Called when the screen is initialized & by the color sliders when changed
    @Unique
    public void blockOutlines$updatePreviewWidgets() {
        this.blockOutlines$outerPreviewWidget.setFGColor(BlockOutlinesConfig.outerColor.getARGB32());
        this.blockOutlines$innerPreviewWidget.setFGColor(BlockOutlinesConfig.innerColor.getARGB32());
    }

    // Enable/disable options based if the Block Outlines are enabled
    @Unique
    public void blockOutlines$rebuildWidgets() {
        // Fetch the current scroll position to restore it after rebuilding the widgets
        double scrollPosition = this.list.getScrollAmount();
        this.rebuildWidgets();
        this.list.setScrollAmount(scrollPosition);
    }

    // Replace the "DONE" button to ensure it saves the config when clicked
    @ModifyArg(method = "createFooter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 1), index = 1)
    private Button.OnPress adjustCallback(Button.OnPress onPress) {
        return (buttonClickEvent) -> {
            BlockOutlinesConfig.updateConfigs();
            this.minecraft.setScreen(this.lastScreen);
        };
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    protected void initInject(CallbackInfo callback) {
        this.list.addBig(blockOutlines$options.highContrastBlockOutline());

        if (BlockOutlinesConfig.isEnabled) {
            this.list.addSmall(blockOutlines$options.colorOptions());

            this.blockOutlines$outerPreviewWidget = this.list.findOption(this.blockOutlines$options.outerPreview());
            this.blockOutlines$outerPreviewWidget.setMessage(Component.translatable("options.pinpal.blockoutlines.outer_color.preview"));
            this.blockOutlines$outerPreviewWidget.active = false;

            this.blockOutlines$innerPreviewWidget = this.list.findOption(this.blockOutlines$options.innerPreview());
            this.blockOutlines$innerPreviewWidget.setMessage(Component.translatable("options.pinpal.blockoutlines.inner_color.preview"));
            this.blockOutlines$innerPreviewWidget.active = false;

            this.blockOutlines$updatePreviewWidgets();
        }
    }

}
