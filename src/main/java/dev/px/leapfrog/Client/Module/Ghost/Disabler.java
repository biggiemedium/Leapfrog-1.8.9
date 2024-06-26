package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerJumpEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

@Module.ModuleInterface(name = "Disabler", type = Type.Ghost, description = "Disables anti-cheats")
public class Disabler extends Module {

    public Disabler() {

    }

    // Vulcan
    private Setting<Boolean> vulcan = create(new Setting<>("Vulcan", false));
    private Setting<Boolean> vulcanOmniSprint = create(new Setting<>("Vulcan Sprint", false, v -> vulcan.getValue()));
    private Setting<Boolean> vulcanScaffold = create(new Setting<>("Vulcan Scaffold", false, v -> vulcan.getValue()));

    // Verus
    private Setting<Boolean> verus = create(new Setting<>("Verus", false));

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(mc.thePlayer == null) {
                return;
            }
            if(vulcan.getValue()) {
                if (vulcanOmniSprint.getValue() && ((!mc.gameSettings.keyBindForward.isKeyDown() && MoveUtil.isMoving())) && event.isOnGround()) {
                    event.setOnGround(LeapFrog.positionManager.getOnGroundTicks() % 2 == 0);
                }
            }

            if (vulcan.getValue() && vulcanScaffold.getValue()) {
                if (!mc.thePlayer.isSwingInProgress) {
                    if (mc.thePlayer.ticksExisted % 20 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    } else if (mc.thePlayer.ticksExisted % 10 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                    }
                }
            }

        } else {

        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> packetReceiveEventListener = new Listener<>(event -> {

    });

    @EventHandler
    private Listener<PacketSendEvent> packetSendEventListener = new Listener<>(event -> {
        if(verus.getValue()) {
            if(event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive) {
                event.cancel();
            }
        }
    });

    @EventHandler
    private Listener<PlayerJumpEvent> jumpEventListener = new Listener<>(event -> {
        if(vulcan.getValue()) {
            if(vulcanOmniSprint.getValue() && ((!mc.gameSettings.keyBindForward.isKeyDown() && MoveUtil.isMoving()))) {
                event.cancel();
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }


}
