package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerSlowDownEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Module.ModuleInterface(name = "No Slow", type = Type.Movement, description = "Prevents slowing when using item")
public class NoSlow extends Module {

    public NoSlow() {

    }

    Setting<Mode> mode = create(new Setting<>("Mode", Mode.NCP));

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(!noSlowCheck()) {
                switch (mode.getValue()) {
                    case Vanilla:

                        break;
                    case NCP:
                        if (mc.thePlayer.isUsingItem()) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        break;
                    case Grim:

                        break;
                }
            }
        } else {
            if(!noSlowCheck()) {
                switch (mode.getValue()) {
                    case Vanilla:

                        break;
                    case NCP:
                        if (mc.thePlayer.isUsingItem()) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                        }
                        break;
                }
            }

        }
    });

    @EventHandler
    private Listener<PlayerSlowDownEvent> playerSlowDownEventListener = new Listener<>(event -> {
        if(!noSlowCheck()) {
            switch (mode.getValue()) {
                case NCP:
                    event.cancel();
                    break;
                case Vulcan:
                    event.cancel(); // Vanilla noslow bypasses - dont fuck with packets
                    break;
                case Grim:
                    event.cancel();
                    break;
                case Vanilla:
                    event.cancel();
                    break;
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private boolean noSlowCheck() {
        if(mc.thePlayer.getCurrentEquippedItem() != null) {
            if(mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                if(mc.thePlayer.isUsingItem()) {
                    return true;
                }
            }
        }

        return false;
    }

    private enum Mode {
        NCP,
        Vulcan,
        Grim,
        Vanilla
    }

}
