package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "HUD", type = Type.Visual, description = "Displays on screen HUD")
public class HUD extends Module {

    public HUD() {

    }

    public Setting<Float> snapStrength = create(new Setting<>("Snap Strength", 1.0f, 0.0f, 5.0f));
    public Setting<Integer> gridDistance = create(new Setting<>("Grid Distance", 20, 0, 50));

    @EventHandler
    private Listener<Render2DEvent> render2DEventListener = new Listener<>(event -> {

    });
}
