package io.pinpal.blockoutlines.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.pinpal.blockoutlines.config.OptionButton;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.Component;

@Mixin(AccessibilityOptionsScreen.class)
public abstract class AccessibilityOptionsScreenMixin extends SimpleOptionsSubScreen {

    @Shadow
    private static OptionInstance<?>[] options(Options p_232691_) {
        return null;
    };

    // Fake constructor to allow the mixin to be created
    public AccessibilityOptionsScreenMixin(Screen screen, Options options) {
        super(screen, options, Component.empty(), options(options));
    }

    // Inject the new button into the options array
    // @Inject(method = "options(Lnet/minecraft/client/Options;)[Lnet/minecraft/client/OptionInstance;", at = @At("RETURN"), cancellable = true)
    // private static void optionsInject(Options options, CallbackInfoReturnable<OptionInstance<?>[]> callback) {
    //     // Get the original return value
    //     OptionInstance<?>[] originalReturn = callback.getReturnValue();

    //     // Create a new array with one extra element
    //     OptionInstance<?>[] newReturn = new OptionInstance[originalReturn.length + 1];
    //     System.arraycopy(originalReturn, 0, newReturn, 0, originalReturn.length);

    //     // Add the new element to the end of the new array
    //     newReturn[originalReturn.length] = new OptionButton().highContrastBlockOutline();

    //     // Set the new array as the return value
    //     callback.setReturnValue(newReturn);
    // }

    // // Add a button to the footer of the screen
    // @Inject(method = "createFooter()V", at = @At("TAIL"), cancellable = true)
    // protected void createFooterInject(CallbackInfo callback) {
    //     this.addRenderableWidget(Button
    //             .builder(Component.translatable("options.accessibility.high_contrast_block_outline"),
    //                     (buttonClickHandler) -> {
    //                         // this.minecraft.setScreen();
    //                         System.out.println("new screen button clicked");
    //                     })
    //             .bounds(this.width - 155, this.height - 27, 150, 20).build());
    // }

    // Add a big button to the list of options
    @Inject(method = "init()V", at = @At("TAIL"), cancellable = true)
    protected void initInject(CallbackInfo callback) {
        this.list.addBig(new OptionButton().highContrastBlockOutline());
        this.list.addSmall(new OptionButton().colorOptions());
    }

}
