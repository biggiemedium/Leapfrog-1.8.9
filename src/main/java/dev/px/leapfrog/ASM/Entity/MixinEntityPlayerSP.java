package dev.px.leapfrog.ASM.Entity;

import com.mojang.authlib.GameProfile;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

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

}
