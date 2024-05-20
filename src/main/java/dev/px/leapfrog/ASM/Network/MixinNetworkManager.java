package dev.px.leapfrog.ASM.Network;

import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.LeapFrog;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void receive(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        LeapFrog.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("RETURN"), cancellable = true)
    public void onPacketSend(Packet packetIn, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(packetIn);
        LeapFrog.EVENT_BUS.post(event);

        if(event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void exception(ChannelHandlerContext exc, Throwable exc_, CallbackInfo callback) {
        if (exc_ instanceof Exception) {
            callback.cancel();
        }
    }

}
