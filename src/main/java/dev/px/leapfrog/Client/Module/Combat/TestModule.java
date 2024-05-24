package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Movement.Strafe;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Test Module", type = Type.Combat, description = "Balls")
public class TestModule extends Module {

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.AAC));

    private Setting<Float> timer = create(new Setting<>("Timer", 1.0f, 0.0f, 5.0f));
    private Setting<Float> speedV = create(new Setting<>("Speed onGround", 1.0f, 0.0f, 10.0f));
    private Setting<Float> speedH = create(new Setting<>("Speed offGround", 1.0f, 0.0f, 10.0f));

    private double speed = 0.0D;
    private int stage = 0;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        ((IMixinMinecraft) mc).timer().timerSpeed = timer.getValue();
        if(PlayerUtil.isMoving()) {
            if(event.isOnGround()) {
                mc.thePlayer.jump();
            } else {
                speed = speedH.getValue();
                PlayerUtil.setSpeed(PlayerUtil.getBaseMoveSpeed() * speed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
            }
        } else {
            mc.thePlayer.motionX = 0.0d;
            mc.thePlayer.motionZ = 0.0d;
        }
    });

    @Override
    public void onDisable() {
        ((IMixinMinecraft) mc).timer().timerSpeed = 1.0f;
    }

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
