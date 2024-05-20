package dev.px.leapfrog.ASM.Render;

import dev.px.leapfrog.API.Event.Render.CameraShakeEvent;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void onCameraShake(float partialTicks, CallbackInfo ci) {
        CameraShakeEvent event = new CameraShakeEvent();
        LeapFrog.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            ci.cancel();
        }
    }

}
