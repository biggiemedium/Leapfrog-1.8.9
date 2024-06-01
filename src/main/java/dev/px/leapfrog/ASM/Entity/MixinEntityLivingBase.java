package dev.px.leapfrog.ASM.Entity;

import dev.px.leapfrog.API.Event.Player.PlayerSpoofLadderEvent;
import dev.px.leapfrog.Client.Module.Movement.Spider;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {

    @Inject(method = "isOnLadder", at = @At("HEAD"), cancellable = true)
    public void isOnLadder(CallbackInfoReturnable<Boolean> value) {
        PlayerSpoofLadderEvent event = new PlayerSpoofLadderEvent();
        LeapFrog.EVENT_BUS.post(event);

        if(event.isCancelled()) {
            value.setReturnValue(event.isSpoofing());
        }

    }

}
