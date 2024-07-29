package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Overlays.RenderFireOverlayEvent;
import dev.px.leapfrog.API.Event.Render.Overlays.RenderPortalOverlayEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Event.Render.Overlays.RenderPumpkinOverlayEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;

@Module.ModuleInterface(name = "No Render", type = Type.Visual, description = "Prevents client side rendering")
public class NoRender extends Module {

    public NoRender() {

    }

    private Setting<Boolean> fire = create(new Setting<>("Fire", true));
    private Setting<Boolean> pumpkin = create(new Setting<>("Pumpkin", true));
    private Setting<Boolean> portal = create(new Setting<>("Portal", false));

    @EventHandler
    private Listener<RenderPumpkinOverlayEvent> pumpkinOverlayEventListener = new Listener<>(event -> {
        if(this.pumpkin.getValue()) {
            event.cancel();
        }
    });


    @EventHandler
    private Listener<RenderPortalOverlayEvent> portalOverlayEventListener = new Listener<>(event -> {
        if(this.portal.getValue()) {
            event.cancel();
        }
    });

    @EventHandler
    private Listener<RenderFireOverlayEvent> fireOverlayEventListener = new Listener<>(event -> {
        if(this.fire.getValue()) {
            event.cancel();
        }
    });

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {

    });


}
