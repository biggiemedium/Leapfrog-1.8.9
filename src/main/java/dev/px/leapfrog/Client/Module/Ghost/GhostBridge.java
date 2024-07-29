package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Ghost Bridge", type = Type.Ghost, description = "Fake Legit bridge")
public class GhostBridge extends Module { // god bridging is at yaw 45

    public GhostBridge() {

    }

    private boolean sneak;

    @EventHandler
    private Listener<PlayerMotionEvent> playerMotionEventListener = new Listener<>(event -> {

    });

}
