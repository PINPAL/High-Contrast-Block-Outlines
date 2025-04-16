package io.pinpal.blockoutlines.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;

import io.pinpal.blockoutlines.OptionButton;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {

    @Inject(method = "options(Lnet/minecraft/client/Options;)[Lnet/minecraft/client/OptionInstance;", at = @At("RETURN"), cancellable = true)
    private static void optionsInject(Options options, CallbackInfoReturnable<OptionInstance<?>[]> callback) {
        // Get the original return value
        OptionInstance<?>[] originalReturn = callback.getReturnValue();

        // Create a new array with one extra element
        OptionInstance<?>[] newReturn = new OptionInstance[originalReturn.length + 1];
        System.arraycopy(originalReturn, 0, newReturn, 0, originalReturn.length);

        // Add the new element to the end of the new array
        newReturn[originalReturn.length] = new OptionButton().highContrastBlockOutline();

        // Set the new array as the return value
        callback.setReturnValue(newReturn);
    }
}
