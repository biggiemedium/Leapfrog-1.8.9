package dev.px.leapfrog.ASM.Network;

import dev.px.leapfrog.Client.Module.Misc.ClientSpoofer;
import dev.px.leapfrog.LeapFrog;
import io.netty.buffer.Unpooled;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @ModifyArgs(method = "handleJoinGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V"))
    public void setArgs(Args args) {
        if(LeapFrog.moduleManager.getModule(ClientSpoofer.class).isToggled()) {
            LeapFrog.LOGGER.info("Handling login...");
            switch (LeapFrog.moduleManager.getModuleByClass(ClientSpoofer.class).type.getValue()) {
                case Vanilla:
                args.set(0, new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString("vanilla")));
                break;
                case Forge:
                    args.set(0, new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
                break;
                //case Lunar: // fix this - I don't actually know the name of lunars custom payloads
                //    args.set(0, new C17PacketCustomPayload("MC|Lunar", (new PacketBuffer(Unpooled.buffer())).writeString("Lunar")));
                //break;
            }
        }
    }

}
