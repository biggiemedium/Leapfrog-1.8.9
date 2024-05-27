package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@Module.ModuleInterface(name = "Packet Sneak", type = Type.Movement, description = "sends serverside packets that player is sneaking")
public class PacketSneak extends Module {

    public PacketSneak() {

    }

    Setting<Mode> mode = create(new Setting<>("Mode", Mode.NCP));

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case NCP:
                    mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                    break;
            }
        } else {
            switch (mode.getValue()) {
                case NCP:
                    mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                    break;
            }
        }

    });

    @Override
    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }

    private enum Mode {
        NCP
    }
}
