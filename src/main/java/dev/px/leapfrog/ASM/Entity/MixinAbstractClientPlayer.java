package dev.px.leapfrog.ASM.Entity;

import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {

    @Shadow private NetworkPlayerInfo playerInfo;

    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void leapfrogCapes(CallbackInfoReturnable<ResourceLocation> cir) {
        UUID uuid = Objects.requireNonNull(getPlayerInfo()).getGameProfile().getId();

        if(getPlayerInfo().getGameProfile().getName().equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getName()) || LeapFrog.capeManager.hasDevCape(uuid)) {
     //       cir.setReturnValue(new ResourceLocation("minecraft", "/Leapfrog/Images/user-dev.png"));
        }

    }

}
