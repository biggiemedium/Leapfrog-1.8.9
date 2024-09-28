package dev.px.leapfrog.ASM.Render;

import dev.px.leapfrog.API.Event.Render.RenderArmorLayerEvent;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {

    @Shadow private float alpha;

    @Inject(method = "doRenderLayer", at = @At("HEAD"), cancellable = true)
    public void onArmorRender(EntityLivingBase p_doRenderLayer_1_, float p_doRenderLayer_2_, float p_doRenderLayer_3_, float p_doRenderLayer_4_, float p_doRenderLayer_5_, float p_doRenderLayer_6_, float p_doRenderLayer_7_, float p_doRenderLayer_8_, CallbackInfo ci) {
        RenderArmorLayerEvent event = new RenderArmorLayerEvent();
        LeapFrog.EVENT_BUS.post(event);

        if(event.isCancelled()) {
            ci.cancel();
        }
    }

}
