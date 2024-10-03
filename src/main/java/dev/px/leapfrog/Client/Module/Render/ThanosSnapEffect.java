package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Player.PlayerTickEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.ThanosSnapRenderer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Module.ModuleInterface(name = "Thanos Snap", type = Type.Visual, description = "Thanos snap effect by Sk1er")
public class ThanosSnapEffect extends Module {

    public ThanosSnapEffect() {

    }

    Setting<Integer> distance = create(new Setting<>("Distance", 50, 0, 150));
    Setting<Double> speed = create(new Setting<>("Speed", 1D, 0D, 10D));
    Setting<SnapMode> snapMode = create(new Setting<>("Mode", SnapMode.SkinChange));
    Setting<Boolean> blending = create(new Setting<>("Blending", true));

    public ThanosSnapRenderer snap;

    @Override
    public void onEnable() {
        super.onEnable();
        snap = new ThanosSnapRenderer();
        snap.generate();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        snap = null;
    }

    @EventHandler
    private Listener<PlayerTickEvent> tickEvent = new Listener<>(event -> {
        if(snap != null) {
            snap.onTick(snapMode.getValue().getMode(), speed.getValue(), blending.getValue());
        }
    });

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        if(snap != null) {
            snap.renderWorld(event, distance.getValue());
        }
    });

    @EventHandler
    private Listener<WorldEvent.Unload> unloadListener = new Listener<>(event -> {
        if(snap != null) {
            snap.switchWorld(event);
        }
    });

    @EventHandler
    private Listener<RenderPlayerEvent.Pre> preListener = new Listener<>(event -> {
        if(snap != null) {
            snap.renderPlayer(event);
        }
    });

    @EventHandler
    private Listener<TickEvent.RenderTickEvent> renderTickEventListener = new Listener<>(event -> {
        if(snap != null) {
            snap.renderTick();
        }
    });

    private enum SnapMode {
        SkinChange(0),
        Twirl(1),
        DarkFade(2),
        Evaporate(3);

        int mode;
        SnapMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return this.mode;
        }
    }
}
