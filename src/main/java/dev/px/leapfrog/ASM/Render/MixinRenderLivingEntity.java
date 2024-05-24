package dev.px.leapfrog.ASM.Render;

import dev.px.leapfrog.Client.Module.Render.NameTags;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRenderLivingEntity <T extends EntityLivingBase> extends Render<T> {

    protected MixinRenderLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "canRenderName(Lnet/minecraft/entity/EntityLivingBase;)Z", at = @At("HEAD"), cancellable = true)
    private void handleBetterF1AndShowOwnNametag(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (!Minecraft.isGuiEnabled()) {
            cir.setReturnValue(false);
        }

        if(LeapFrog.moduleManager.isModuleToggled(NameTags.class)) {
            cir.setReturnValue(false);
        } else if (entity == renderManager.livingPlayer && !entity.isInvisible()) {
            cir.setReturnValue(true);
        }

    }

}
