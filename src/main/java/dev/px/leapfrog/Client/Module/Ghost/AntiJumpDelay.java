package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

import java.util.Random;

/**
 * https://github.com/KadTheHunter/NoJumpDelay/blob/main/src/main/java/com/kadalyst/nojumpdelay/NoJumpDelay.java
 */
@Module.ModuleInterface(name = "Anti Jump Delay", type = Type.Ghost, description = "Prevents jump delay when spamming space")
public class AntiJumpDelay extends Module {

    public AntiJumpDelay() {

    }

    private int ticks = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        this.ticks = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        Random random = new Random();
        ++this.ticks;
        if (mc.thePlayer != null) {
            int randomNumber = random.nextInt(3) + 1;
            if (this.ticks > randomNumber) {
                if (mc != null && mc.thePlayer != null && mc.gameSettings.keyBindJump.isPressed() && mc.thePlayer.onGround && !mc.playerController.isInCreativeMode() && !mc.thePlayer.isSpectator()) {
                    mc.thePlayer.jump();
                }

                this.ticks = 0;
            }
        }
    });

}
