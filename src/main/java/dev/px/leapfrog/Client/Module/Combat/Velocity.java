package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinEntityPlayerSP;
import dev.px.leapfrog.ASM.Listeners.IMixinS12PacketEntityVelocity;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@Module.ModuleInterface(name = "Velocity", type = Type.Combat, description = "Prevents knockback")
public class Velocity extends Module {

    public Velocity() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.HurtTick));
    private boolean cancel = false;

    private Setting<Integer> horizontal = create(new Setting<>("Packet Horizontal", 100, 0, 100, v -> mode.getValue() == Mode.Packet));
    private Setting<Integer> vertical = create(new Setting<>("Packet Vertical", 100, 0, 100, v -> mode.getValue() == Mode.Packet));
    private Setting<Integer> vulcanHorizontal = create(new Setting<>("Vulcan Horizontal", 100, 0, 100, v -> mode.getValue() == Mode.Vulcan));
    private Setting<Integer> vulcanVertical = create(new Setting<>("Vulcan Vertical", 100, 0, 100, v -> mode.getValue() == Mode.Vulcan));


    @EventHandler
    private Listener<PlayerMotionEvent> updateEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {

        } else {
            switch (mode.getValue()) {
                case HurtTick:
                if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime < 10) {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                break;
            }
        }
    });

    @EventHandler
    private Listener<PacketSendEvent> packetSendEventListener = new Listener<>(event -> {
        switch (mode.getValue()) {
            case C0F:
            if (event.getPacket() instanceof C0FPacketConfirmTransaction && mc.thePlayer.hurtTime > 0) {
                event.cancel();
            }
            break;
            case Vulcan:
                if (event.getPacket() instanceof C0FPacketConfirmTransaction && mc.thePlayer.hurtTime > 0) {
                    event.cancel();
                }
                break;
        }
    });

    @SuppressWarnings("duplicate")
    @EventHandler
    private Listener<PacketReceiveEvent> receiveEventListener = new Listener<>(event -> {
        switch (mode.getValue()) {
            case HurtTick:
                if(event.getPacket() instanceof S08PacketPlayerPosLook) {
                        if (cancel) {
                            //event.cancel();
                        }
                }
                break;
            case NCP:
                if(event.getPacket() instanceof S12PacketEntityVelocity) {
                    if(mc.thePlayer == null) return;
                    if(((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
                        event.cancel();
                    }
                }
                break;
            case C0F:
                if(event.getPacket() instanceof S12PacketEntityVelocity) {
                    if(mc.thePlayer == null) return;
                    if(((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
                        event.cancel();
                    }
                }
                break;
            case Packet:
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer == null) return;
                    if (((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
                        // Horizontal
                        ((IMixinS12PacketEntityVelocity) event.getPacket()).setMotionX(horizontal.getValue() / 100);
                        ((IMixinS12PacketEntityVelocity) event.getPacket()).setMotionZ(horizontal.getValue() / 100);

                        // Vertical
                        ((IMixinS12PacketEntityVelocity) event.getPacket()).setMotionY(vertical.getValue() / 100);
                    }
                }
                break;
            case Vulcan:
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer == null) return;
                    if (((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
                        if (vulcanHorizontal.getValue() == 0 && vulcanHorizontal.getValue() == 0) {
                            event.cancel();
                            return;
                        }

                        ((IMixinS12PacketEntityVelocity) event.getPacket()).setMotionX(vulcanHorizontal.getValue() / 100);
                        ((IMixinS12PacketEntityVelocity) event.getPacket()).setMotionY(vulcanVertical.getValue() / 100);
                        ((IMixinS12PacketEntityVelocity) event.getPacket()).setMotionZ(vulcanHorizontal.getValue() / 100);

                    }
                }
                break;
        }
    });

    @Override
    public void safeToggle(S08PacketPlayerPosLook packet, boolean teleport) {
        super.safeToggle(packet, teleport);
    }

    private enum Mode {
        HurtTick,
        NCP,
        C0F,
        Packet,
        Vulcan
    }

    @Override
    public String arrayDetails() {
        return this.mode.getValue().name();
    }

}
