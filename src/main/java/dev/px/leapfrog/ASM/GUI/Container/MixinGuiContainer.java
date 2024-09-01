package dev.px.leapfrog.ASM.GUI.Container;

import dev.px.leapfrog.Client.Module.Misc.ChestStealer;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void chestStealer(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_, CallbackInfo ci) {
        if(LeapFrog.moduleManager.isModuleToggled(ChestStealer.class)) {
            ChestStealer stealer = (ChestStealer) LeapFrog.moduleManager.getModule(ChestStealer.class);
            if(stealer.silent.getValue()) {
                stealer.silentStealing = true;
                Minecraft.getMinecraft().inGameHasFocus = true;
                Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
                Minecraft.getMinecraft().currentScreen = null;
                return;
            }
        }
    }

}
