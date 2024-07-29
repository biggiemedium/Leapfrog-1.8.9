package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinS08PacketPlayerPosLook;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;


@Module.ModuleInterface(name = "No Rotate", type = Type.Misc, description = "Prevents server side rotations")
public class NoRotate extends Module {

    public NoRotate() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Normal));

    @EventHandler
    private Listener<PacketReceiveEvent> receiveEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            if(mc.thePlayer == null) return;
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();

            switch (mode.getValue()) {
                case Normal:
                    IMixinS08PacketPlayerPosLook mixin = (IMixinS08PacketPlayerPosLook) event.getPacket();
                    mixin.setPitch(mc.thePlayer.rotationPitch);
                    mixin.setYaw(mc.thePlayer.rotationYaw);
                    break;

                case Cancel:
                    if(mc.thePlayer.ticksExisted > 5) {
                        event.cancel();
                    }
                    break;

                case Spoof:
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packet.getYaw(), packet.getPitch(), mc.thePlayer.onGround));
                    break;
            }

        }
    });

    private enum Mode {
        Normal,
        Cancel,
        Spoof
    }
}
