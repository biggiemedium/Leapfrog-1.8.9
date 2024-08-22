package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Network.PacketUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;

@Module.ModuleInterface(name = "Fast Ladder", type = Type.Movement, description = "Go up ladders faster")
public class FastLadder extends Module {

    public FastLadder() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Motion));
    private Setting<Float> motionSpeed = create(new Setting<>("Motion Speed", 1.5F, 0F, 5F, v -> mode.getValue() == Mode.Motion));
    private Setting<Float> timerSpeed = create(new Setting<>("Timer Speed", 1.6F, 1F, 4F, v -> mode.getValue() == Mode.Timer));
    private Setting<Float> packetSpeed = create(new Setting<>("Packet Speed", 0.4F, 1F, 3F, v -> mode.getValue() == Mode.Packet));

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            switch (mode.getValue()) {
                case Timer:
                    if(mc.thePlayer.isOnLadder()) {
                        ((IMixinMinecraft) mc).timer().timerSpeed = timerSpeed.getValue();
                    }
                    break;
                case Motion:
                    if (mc.thePlayer.isOnLadder()) {
                        mc.thePlayer.motionY *= motionSpeed.getValue();
                    }
                    break;
                case Packet:
                    if(mc.thePlayer.isOnLadder()) {
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + packetSpeed.getValue(), mc.thePlayer.posZ, event.isOnGround()));
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + packetSpeed.getValue(), mc.thePlayer.posZ);
                    }
                    break;
            }
        } else {
            switch (mode.getValue()) {
                case Timer:
                    if(mc.thePlayer.isOnLadder()) {
                        ((IMixinMinecraft) mc).timer().timerSpeed = timerSpeed.getValue();
                    } else {
                        ((IMixinMinecraft) mc).timer().timerSpeed = 1.0F;
                    }
                    break;
                case Motion:

                    break;
                case Packet:

                    break;
            }
        }
    });

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ((IMixinMinecraft) mc).timer().timerSpeed = 1.0F;
    }

    private enum Mode {
        Motion,
        Timer,
        Packet
    }

}
