package dev.px.leapfrog.ASM.Game;

import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "createDisplay", at = @At("RETURN"), cancellable = true)
    public void createDisplayPost(CallbackInfo ci) {
        Display.setTitle(LeapFrog.NAME + " | " + "V" + LeapFrog.VERSION);
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    private void setGameIcon(CallbackInfo c) {
        if(Util.getOSType() == Util.EnumOS.OSX) {
            RenderUtil.setDockIcon("/assets/minecraft/Leapfrog/Images/Froggy-transformed.png");
            c.cancel();
        } else if(Util.getOSType() == Util.EnumOS.WINDOWS) {
            // windows impl
            //      c.cancel();
        }
    }
}
