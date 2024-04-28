package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderManager.class)
public interface IMixinRenderManager {

    @Accessor(value = "renderPosX")
    double getRenderPosX();

    @Accessor(value = "renderPosY")
    double getRenderPosY();

    @Accessor(value = "renderPosZ")
    double getRenderPosZ();

}
