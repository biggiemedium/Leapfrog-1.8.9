package dev.px.leapfrog.ASM.Render;

import dev.px.leapfrog.Client.Module.Render.ThanosSnapEffect;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "onEntityRemoved", at = @At("HEAD"), cancellable = true)
    public void onEntityRemoved(Entity entityIn, CallbackInfo ci) {
        if(LeapFrog.moduleManager.getModuleByClass(ThanosSnapEffect.class) != null) {
            if (LeapFrog.moduleManager.getModuleByClass(ThanosSnapEffect.class).snap != null) {
                LeapFrog.moduleManager.getModuleByClass(ThanosSnapEffect.class).snap.remove(entityIn);
            }
        }
    }

}
