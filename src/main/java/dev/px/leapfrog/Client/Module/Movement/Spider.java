package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Spider", type = Type.Movement, description = "Climb walls")
public class Spider extends Module {

    public Spider() {

    }

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.MMC));

    private boolean versusFlag = false;
    private boolean spoofFlag = false;

    @EventHandler
    private Listener<PlayerMotionEvent> updateEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case MMC:
                    if(mc.thePlayer.isCollidedHorizontally && !versusFlag && mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.thePlayer.motionY = MoveUtil.jumpBoostMotion(MoveUtil.JUMP_HEIGHT);
                    }

                    if(mc.thePlayer.isCollidedVertically) {
                        this.versusFlag = !mc.thePlayer.onGround;
                    }
                    break;

                case Vulcan:
                    if(mc.thePlayer.isCollidedHorizontally) {
                        if(mc.thePlayer.ticksExisted % 3 == 0) {
                            event.setOnGround(true);
                            mc.thePlayer.jump();
                        }
                    }
                    break;
                case Vanilla:
                    if(mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.jump();
                    }
                    break;
                case Verus:
                    if(mc.thePlayer.isCollidedHorizontally) {
                        if(mc.thePlayer.ticksExisted % 2 == 0) {
                            mc.thePlayer.jump();
                        }
                    }
                    break;
            }

        } else {
            switch (mode.getValue()) {
                case Vulcan:
                    event.setOnGround(mc.thePlayer.onGround);
                    break;
            }
        }
    });

    @Override
    public String arrayDetails() {
        return this.mode.getValue().name();
    }

    private enum Mode {
        MMC,
        Vulcan,
        Vanilla,
        Verus
    }

}
