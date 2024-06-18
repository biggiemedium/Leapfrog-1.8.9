package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
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
    private Setting<Float> vanillaMultipler = create(new Setting<>("Vanilla Speed", 1.5F, 0.0F, 2.0F, v -> mode.getValue() == Mode.Vanilla));

    private double speed = 0.0D;
    private int stage = 0;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case Vanilla:
                    vanilla(event);
                    break;
                case NCP:

                    break;

                case Grim:

                    break;

                case AAC:

                    break;
            }
        }
    });

    private void vanilla(PlayerMotionEvent event) {
        if(MoveUtil.isMoving()) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }

            MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() * vanillaMultipler.getValue());
        }
    }

    private void AAC() {

    }

    private void Grim() {

    }

    private void NCP() {

    }

    private enum Mode {
        Vanilla,
        AAC,
        Grim,
        NCP
    }
}
