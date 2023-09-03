package dev.isxander.controlify.mixins.feature.settingsbutton;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.gui.screen.ControllerCarouselScreen;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public class ControlsScreenMixin extends OptionsSubScreen {
    public ControlsScreenMixin(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addControllerSettings(CallbackInfo ci, @Local(ordinal = 0) int leftX, @Local(ordinal = 1) int rightX, @Local(ordinal = 2) int currentY) {
        addRenderableWidget(Button.builder(Component.translatable("controlify.gui.button"), btn -> this.openControllerSettings())
                .pos(leftX, currentY)
                .width(150)
                .build());
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 2), index = 1)
    private int modifyDoneButtonY(int y) {
        return y + 24;
    }

    @Unique
    private void openControllerSettings() {
        Controlify.instance().askNatives()
                .whenComplete((result, error) ->
                        minecraft.setScreen(ControllerCarouselScreen.createConfigScreen(lastScreen))
                );
    }
}
