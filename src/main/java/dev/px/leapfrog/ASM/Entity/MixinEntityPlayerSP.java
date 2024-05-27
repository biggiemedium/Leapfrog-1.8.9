package dev.px.leapfrog.ASM.Entity;

import com.mojang.authlib.GameProfile;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerSlowDownEvent;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow public abstract boolean isRidingHorse();

    @Shadow public MovementInput movementInput;

    @Shadow protected int sprintToggleTimer;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isCurrentViewEntity()Z", shift = At.Shift.AFTER), cancellable = true)
    public void onWalkingUpdatePre(CallbackInfo ci) {
        PlayerMotionEvent event = new PlayerMotionEvent(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, Event.Stage.Pre);
        LeapFrog.EVENT_BUS.post(event);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onWalkingUpdatePost(CallbackInfo ci) {
        PlayerMotionEvent event = new PlayerMotionEvent(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, Event.Stage.Post);
        LeapFrog.EVENT_BUS.post(event);
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z", ordinal = 0))
    public boolean yes(EntityPlayerSP instance) {
        return false;
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isRiding()Z"))
    public boolean no(EntityPlayerSP instance) {
        return false;
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;pushOutOfBlocks(DDD)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void onSlowDown(CallbackInfo ci) {
        PlayerSlowDownEvent event = new PlayerSlowDownEvent();
        LeapFrog.EVENT_BUS.post(event);
        if(this.isUsingItem() && !this.isRiding()) {
            if(!event.isCancelled()) {
                this.movementInput.moveStrafe *= 0.2F;
                this.movementInput.moveForward *= 0.2F;
                this.sprintToggleTimer = 0;
            }
        }

    }

}
