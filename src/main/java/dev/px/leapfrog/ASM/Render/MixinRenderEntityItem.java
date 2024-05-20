package dev.px.leapfrog.ASM.Render;

import dev.px.leapfrog.Client.Module.Render.ItemPhysics;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if(LeapFrog.moduleManager.getModuleByClass(ItemPhysics.class) != null) {
            if(LeapFrog.moduleManager.isModuleToggled(ItemPhysics.class)) {
                LeapFrog.moduleManager.getModuleByClass(ItemPhysics.class).itemPhysic(entity, x, y, z);
                ci.cancel();
            }
        }
    }

}
