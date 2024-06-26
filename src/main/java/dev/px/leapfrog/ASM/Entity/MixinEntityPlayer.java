package dev.px.leapfrog.ASM.Entity;

import dev.px.leapfrog.API.Event.Player.PlayerJumpEvent;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void onJump(CallbackInfo ci) {
        if((Object) this == Minecraft.getMinecraft().thePlayer) {
            PlayerJumpEvent event = new PlayerJumpEvent();
            LeapFrog.EVENT_BUS.post(event);
            if(event.isCancelled()) {
                ci.cancel();
            }
        }

    }
}
