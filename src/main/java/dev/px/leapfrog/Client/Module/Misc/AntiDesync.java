package dev.px.leapfrog.Client.Module.Misc;

import com.sun.javafx.scene.traversal.Direction;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

// TODO: FINISH
@Module.ModuleInterface(name = "Anti Desync", type = Type.Misc, description = "Synchronizes Client/Server packets")
public class AntiDesync extends Module {

    public AntiDesync() {

    }

    //private Setting<Boolean> ghostBlock = create(new Setting<>("Ghost Block", true));
    //private Setting<Integer> ghostRange = create(new Setting<>("Ghost Block Range", 4, 0, 10, v -> ghostBlock.getValue()));

    private int slot;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(mc.thePlayer.inventory.currentItem != slot) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot));
            }
        }
    });

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packet = (C09PacketHeldItemChange) event.getPacket();
            this.slot = packet.getSlotId();
        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> receiveEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            /*
            if(ghostBlock.getValue()) {
                for (int x = -ghostRange.getValue(); x < ghostRange.getValue(); x++) {
                    for (int y = -ghostRange.getValue(); y < ghostRange.getValue(); y++) {
                        for (int z = -ghostRange.getValue(); z < ghostRange.getValue(); z++) {
                            mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(
                                    C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK,
                                    new BlockPos(mc.thePlayer.getPosition().getX() + x, mc.thePlayer.getPosition().getX() + y, mc.thePlayer.getPosition().getX() + z),
                                    EnumFacing.UP));
                        }
                    }
                }
            }
             */
        }
    });
}
