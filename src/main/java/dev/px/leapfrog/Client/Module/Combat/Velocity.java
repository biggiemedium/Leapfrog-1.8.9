package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@Module.ModuleInterface(name = "Velocity", type = Type.Combat, description = "Prevents knockback")
public class Velocity extends Module {

    public Velocity() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.NCP));
    private boolean cancel = false;


    @EventHandler
    private Listener<PlayerMotionEvent> updateEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {

        } else {
            switch (mode.getValue()) {
                case NCP:
                if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime < 10) {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                break;
            }
        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> receiveEventListener = new Listener<>(event -> {
        switch (mode.getValue()) {
            case NCP:
                if(event.getPacket() instanceof S08PacketPlayerPosLook) {
                        if (cancel) {
                            event.cancel();
                        }

                }
                break;

            case Grim:
                if(event.getPacket() instanceof S12PacketEntityVelocity) { // unintentionally just made a grim AC bypass lets gooo
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                    if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        event.cancel();
                    }
                }

                break;
        }
    });

    private enum Mode {
        NCP,
        Grim
    }

}
