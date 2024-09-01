package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.world.WorldSettings;

@Module.ModuleInterface(name = "Flight", type = Type.Movement, description = "Allows you to fly")
public class Flight extends Module {

    public Flight() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Vulcan));

    private double startY;

    /**
     * @see dev.px.leapfrog.ASM.Network.MixinNetHandlerPlayServer
     * TODO: Create vanilla fly event and cancael it using NetHandlerPlayerServer and changing flying ticks
     */
    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case NCP:

                    break;

                case NoFall:
                    mc.thePlayer.motionY = 0;
                    event.setOnGround(true);
                    break;
                case Vulcan:
                    if (mc.thePlayer.posY < startY) {
                        if (mc.thePlayer.fallDistance > 2) {
                            event.setOnGround(true);
                            mc.thePlayer.fallDistance = 0;
                        }
                        if (mc.thePlayer.ticksExisted % 3 != 0) {
                            mc.thePlayer.motionY = -0.0991;
                        } else {
                            mc.thePlayer.motionY += 0.026;
                        }
                    }
                    break;
            }
        } else {
            switch (mode.getValue()) {
                case NoFall:

                    break;

                case NCP:

                    break;
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        MoveUtil.resetMotion();
    }

    @Override
    public void onEnable() {
        this.startY = mc.thePlayer.posY;
        super.onEnable();
    }

    private enum Mode {
        Vulcan,
        NoFall,
        NCP
    }

}
