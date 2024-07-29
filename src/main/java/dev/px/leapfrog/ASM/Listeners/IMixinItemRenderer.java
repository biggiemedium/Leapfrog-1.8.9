package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface IMixinItemRenderer {

    @Invoker("transformFirstPersonItem")
    void invokeTransformFirstPersonItem(float p_transformFirstPersonItem_1_, float p_transformFirstPersonItem_2_);

}
