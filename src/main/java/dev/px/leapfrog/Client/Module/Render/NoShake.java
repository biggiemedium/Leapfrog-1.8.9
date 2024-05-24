package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.CameraShakeEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "No Shake", description = "Prevents client side camera shake", type = Type.Visual)
public class NoShake extends Module {

    public NoShake() {

    }

    @EventHandler
    private Listener<CameraShakeEvent> shakeListener = new Listener<>(event -> {
        event.cancel();
    });

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
