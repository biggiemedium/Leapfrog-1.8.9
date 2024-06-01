package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.World.WorldBlockAABBEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

@Module.ModuleInterface(name = "Jesus", type = Type.Movement, description = "Walk on water")
public class Jesus extends Module {

    public Jesus() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Vanilla));

    @EventHandler
    private Listener<PlayerMotionEvent> updateEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case Vanilla:

                    break;
                case NCP:
                    if(mc.thePlayer.ticksExisted % 2 == 0 && PlayerUtil.onLiquid()) {
                        event.setY(event.getY() - 0.18f);
                    }
                    break;

                case Grim:

                    break;
            }

        } else {
            switch (mode.getValue()) {
                case Vanilla:

                    break;
                case NCP:

                    break;

                case Grim:

                    break;
            }
        }
    });

    @EventHandler
    private Listener<WorldBlockAABBEvent> bb = new Listener<>(event -> {
        switch (mode.getValue()) {
            case Vanilla:
                if (event.getBlock() instanceof BlockLiquid && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    int x = event.getBlockPos().getX();
                    int y = event.getBlockPos().getY();
                    int z = event.getBlockPos().getZ();

                    event.setBoundingBox(AxisAlignedBB.fromBounds(x, y, z, x + 1, y + 1, z + 1));
                }
                break;
            case NCP:

                break;

            case Grim:

                break;
        }
    });

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();

            switch (mode.getValue()) {
                case Vanilla:

                    break;
                case NCP:
                    if(!PlayerUtil.inLiquid() && PlayerUtil.onLiquid()) {
                        mc.thePlayer.posY = packet.getPositionY() - 0.18f;
                    }
                    break;

                case Grim:

                    break;
            }
        }
    });

    private enum Mode {
        Vanilla,
        NCP,
        Grim
    }

}
