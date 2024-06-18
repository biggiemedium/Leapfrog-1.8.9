package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.world.WorldSettings;

@Module.ModuleInterface(name = "Flight", type = Type.Movement, description = "Allows you to fly")
public class Flight extends Module {

    public Flight() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Vanilla));
    private Setting<Integer> speed = create(new Setting<>("Speed", 2, 1, 5));

    /**
     * @see dev.px.leapfrog.ASM.Network.MixinNetHandlerPlayServer
     * TODO: Create vanilla fly event and cancael it using NetHandlerPlayerServer and changing flying ticks
     */
    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case Vanilla:
                    //mc.thePlayer.capabilities.isFlying = false;
                    //mc.thePlayer.capabilities.allowFlying = false;
                    //mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? speed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -speed.getValue() : 0;
                    break;
                case NCP:

                    break;

                case NoFall:
                    mc.thePlayer.motionY = 0;
                    event.setOnGround(true);
                    break;
            }
        } else {
            switch (mode.getValue()) {
                case Vanilla:
                    if (this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.CREATIVE && this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
                        mc.thePlayer.capabilities.isFlying = true;
                        mc.thePlayer.capabilities.allowFlying = true;
                        mc.thePlayer.capabilities.setFlySpeed(speed.getValue() * 0.05f);
                    }
                    break;
                case NoFall:

                    break;

                case NCP:

                    break;
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        if(mode.getValue() == Mode.Vanilla) {
            if (this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.CREATIVE && this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.capabilities.allowFlying = false;
            }
            MoveUtil.resetMotion();
        }
    }

    private enum Mode {
        Vanilla,
        NoFall,
        NCP
    }

}
