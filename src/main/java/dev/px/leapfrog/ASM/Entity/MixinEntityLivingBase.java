package dev.px.leapfrog.ASM.Entity;

import dev.px.leapfrog.API.Event.Player.PlayerSpoofLadderEvent;
import dev.px.leapfrog.Client.Module.Movement.Spider;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase { // extends MixinEntity

    /*
    private float prevRotationPitchHead;
    private float rotationHeadPitch;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initRotations(World p_i1594_1_, CallbackInfo ci) {
        rotationHeadPitch = this.rotationPitch;
    }

    @Inject(method = "onEntityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;updatePotionEffects()V", shift = At.Shift.AFTER))
    public void updatePrevRotations(CallbackInfo ci) {
        prevRotationPitchHead = rotationHeadPitch;
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", ordinal = 1, shift = At.Shift.BEFORE))
    public void updateRotations(CallbackInfo ci) {
        while(this.rotationHeadPitch - this.prevRotationPitchHead < -180.0F) {
            this.prevRotationPitchHead -= 360.0F;
        }

        while(this.rotationHeadPitch - this.prevRotationPitchHead >= 180.0F) {
            this.prevRotationPitchHead += 360.0F;
        }

        LeapFrog.rotationManager.visualRotations[1] = rotationHeadPitch;
    }

     */

    @Inject(method = "isOnLadder", at = @At("HEAD"), cancellable = true)
    public void isOnLadder(CallbackInfoReturnable<Boolean> value) {
        PlayerSpoofLadderEvent event = new PlayerSpoofLadderEvent();
        LeapFrog.EVENT_BUS.post(event);

        if(event.isCancelled()) {
            value.setReturnValue(event.isSpoofing());
        }

    }

}
