package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Anti void", type = Type.Misc, description = "Prevents falling in void")
public class AntiVoid extends Module {

    public AntiVoid() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.NCP));

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case NCP:
                if (mc.thePlayer.fallDistance > 7 && !PlayerUtil.isBlockUnderPlayer(mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) && mc.thePlayer.posY + mc.thePlayer.motionY < Math.floor(mc.thePlayer.posY)) {
                    mc.thePlayer.motionY = Math.floor(mc.thePlayer.posY) - mc.thePlayer.posY;
                    if (mc.thePlayer.motionY == 0) {
                        mc.thePlayer.onGround = true;
                        event.setOnGround(true);
                    }
                }
                break;
            }
        }
    });

    private enum Mode {
        NCP
    }

}
