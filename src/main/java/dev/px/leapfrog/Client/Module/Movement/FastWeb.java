package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinEntity;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Fast Web", type = Type.Movement, description = "Increases web speed")
public class FastWeb extends Module {

    public FastWeb() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.NCP));

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if (event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case NCP:
                    if (((IMixinEntity) mc.thePlayer).isInWeb()) {
                        //mc.thePlayer.setSprinting(false); // speed flags on NCP
                        mc.thePlayer.onGround = false;
                        ((IMixinEntity) mc.thePlayer).setInWeb(false);
                        mc.thePlayer.motionX *= 0.84;
                        mc.thePlayer.motionZ *= 0.84;
                    }
                    break;
            }

        }
    });

    private enum Mode {
        NCP
    }

}
