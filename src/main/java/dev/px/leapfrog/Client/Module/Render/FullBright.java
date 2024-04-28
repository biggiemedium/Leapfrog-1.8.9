package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.ModuleInterface(name = "FullBright", type = Type.Visual, description = "See in the dark")
public class FullBright extends Module {

    public FullBright() {

    }

    @EventHandler
    private Listener<PlayerUpdateEvent> event = new Listener<>(event -> {
        mc.gameSettings.gammaSetting = 1000;
    });

}
