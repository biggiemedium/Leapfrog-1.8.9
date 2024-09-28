package dev.px.leapfrog.ASM.Render;

import dev.px.leapfrog.API.Event.Render.RenderPlayerLighting;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isUser()Z", shift = At.Shift.AFTER))
    public void onRenderPlayer(AbstractClientPlayer p_doRender_1_, double p_doRender_2_, double p_doRender_3_, double p_doRender_4_, float p_doRender_5_, float p_doRender_6_, CallbackInfo ci) {
        RenderPlayerLighting event = new RenderPlayerLighting();
        LeapFrog.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            GlStateManager.disableLighting();
        }
    }

}
