package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.fml.common.Mod;

@Module.ModuleInterface(name = "Strafe", type = Type.Movement, description = "Strafes")
public class Strafe extends Module {

    public Strafe() {

    }

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.AAC));

    private double speed = 0.0D;
    private int stage = 0;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {

    });

    private void AAC() {

    }

    private void Grim() {

    }

    private void NCP() {

    }

    private enum Mode {
        AAC,
        Grim,
        NCP
    }
}
