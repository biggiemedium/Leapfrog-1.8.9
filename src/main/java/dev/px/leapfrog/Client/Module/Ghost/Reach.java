package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerReachEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Reach", type = Type.Ghost, description = "Extends default player reach")
public class Reach extends Module {

    public Reach() {

    }

    public Setting<Float> reachDistance = create(new Setting<>("Distance", 4.5f, 0.0f, 6.0f));

    @EventHandler
    private Listener<PlayerAttackEvent> attackEventListener = new Listener<>(event -> {

    });

    @EventHandler
    private Listener<PlayerReachEvent> reachEventListener = new Listener<>(event -> {
        if(this.toggled) {
            event.setRange(reachDistance.getValue());
            event.cancel();
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
