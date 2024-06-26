package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Anti ClickDelay", type = Type.Ghost, description = "Removes click delay")
public class AntiClickDelay extends Module {

    public AntiClickDelay() {
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(mc.thePlayer == null || mc.theWorld == null) {
                return;
            }

            ((IMixinMinecraft) mc).setLeftClickCounter(0);
        }
    });

}
