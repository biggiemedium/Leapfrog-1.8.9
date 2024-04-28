package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Test Module", type = Type.Combat, description = "Balls")
public class TestModule extends Module {

    Setting<Boolean> setting = create(new Setting<>("Boolean setting", true));
    Setting<Boolean> setting2 = create(new Setting<>("Boolean setting 2", true));

    @EventHandler
    private Listener<PlayerUpdateEvent> onUpdate = new Listener<>(event -> {

    });

}
