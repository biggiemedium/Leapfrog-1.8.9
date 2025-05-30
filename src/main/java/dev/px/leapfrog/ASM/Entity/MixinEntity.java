package dev.px.leapfrog.ASM.Entity;

import dev.px.leapfrog.API.Event.Entity.EntityCollisionBorderSize;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerStrafeEvent;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Shadow public float rotationYaw;

    @Shadow public float rotationPitch;

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void applyEntityCollisionEvent(Entity entity, CallbackInfo ci) {
        if(entity == Minecraft.getMinecraft().thePlayer) {

        }
    }

    @Inject(method = "getCollisionBorderSize", at = @At("HEAD"), cancellable = true)
    public void adjustBorderSize(CallbackInfoReturnable<Float> cir) {
        if((Object) this instanceof EntityPlayer) {
            EntityCollisionBorderSize event = new EntityCollisionBorderSize();
            LeapFrog.EVENT_BUS.post(event);
            if (event.isCancelled()) {
                cir.setReturnValue(event.getBorderSize());
            }
        }
    }

    @Inject(method = "moveFlying", at = @At("HEAD"), cancellable = true)
    public void onMoveFly(float strafe, float forward, float friction, CallbackInfo ci) {
        if((Entity) (Object) this == Minecraft.getMinecraft().thePlayer) {
            PlayerStrafeEvent event = new PlayerStrafeEvent(forward, strafe, friction, rotationYaw);
            LeapFrog.EVENT_BUS.post(event);

            if(event.isCancelled()) {
                ci.cancel();
                forward = event.getForward();
                strafe = event.getStrafe();
                friction = event.getFriction();
                rotationYaw = event.getYaw();
            }
        }
    }

    @Inject(method = "moveEntity", at = @At("HEAD"), cancellable = true)
    public void onMoveEntity(double x, double y, double z, CallbackInfo ci) {
        if((Object) this == Minecraft.getMinecraft().thePlayer) {
            PlayerMoveEvent event = new PlayerMoveEvent(x, y, z);
            LeapFrog.EVENT_BUS.post(event);
        }
    }

}
