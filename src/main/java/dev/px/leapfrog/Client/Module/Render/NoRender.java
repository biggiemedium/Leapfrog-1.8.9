package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Render.Overlays.RenderFireOverlayEvent;
import dev.px.leapfrog.API.Event.Render.Overlays.RenderPortalOverlayEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Event.Render.Overlays.RenderPumpkinOverlayEvent;
import dev.px.leapfrog.API.Event.Render.RenderArmorLayerEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;

@Module.ModuleInterface(name = "No Render", type = Type.Visual, description = "Prevents client side rendering")
public class NoRender extends Module {

    public NoRender() {

    }

    private Setting<Boolean> fire = create(new Setting<>("Fire", true));
    private Setting<Boolean> pumpkin = create(new Setting<>("Pumpkin", true));
    private Setting<Boolean> portal = create(new Setting<>("Portal", false));
    private Setting<Boolean> weather = create(new Setting<>("Weather", true));
    private Setting<WeatherMode> mode = create(new Setting<>("Weather Mode", WeatherMode.Clear, v -> weather.getValue()));
    private Setting<Boolean> armor = create(new Setting<>("Armor", false));
    private Setting<Float> opacity = create(new Setting<>("Armor opacity", 0.0f, 0.0f, 1.0f, v -> armor.getValue()));

    @EventHandler
    private Listener<RenderArmorLayerEvent> renderArmorLayerEventListener = new Listener<>(event -> {
        if(armor.getValue()) {
            event.cancel();
        }
    });

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
        if(this.weather.getValue()) {
            switch (mode.getValue()) {
                case Clear:
                    mc.theWorld.setRainStrength(0);
                    mc.theWorld.setThunderStrength(0);
                    break;
                case Rain:
                    mc.theWorld.setRainStrength(1);
                    mc.theWorld.setThunderStrength(0);
                    break;
                case Thunder:
                    mc.theWorld.setRainStrength(1);
                    mc.theWorld.setThunderStrength(1);
                    break;
            }
        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> receiveEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S2BPacketChangeGameState) {
            S2BPacketChangeGameState packet = (S2BPacketChangeGameState) event.getPacket();

        }
    });

    private enum WeatherMode {
        Clear,
        Rain,
        Thunder
    }

}
