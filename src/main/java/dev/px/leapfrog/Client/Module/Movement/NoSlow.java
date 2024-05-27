package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerSlowDownEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
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
            switch (mode.getValue()) {
                case NCP:
                    if(mc.thePlayer.isUsingItem()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    break;
                case Vulcan:
                    if(mc.thePlayer.isUsingItem()) {
                        if (mc.thePlayer.ticksExisted % 5 == 0) {

                        }
                    }
                    break;
            }
        } else {
            switch (mode.getValue()) {
                case NCP:
                    if (mc.thePlayer.isUsingItem()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    }
                    break;
                case Vulcan:

                    break;
            }
        }
    });

    @EventHandler
    private Listener<PlayerSlowDownEvent> playerSlowDownEventListener = new Listener<>(event -> {
        switch (mode.getValue()) {
            case NCP:
                event.cancel();
                break;
            case Vulcan:
                event.cancel();
                break;

            case Vanilla:
                event.cancel();
                break;
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

    private enum Mode {
        NCP,
        Vulcan,
        Grim,
        Vanilla
    }

}
