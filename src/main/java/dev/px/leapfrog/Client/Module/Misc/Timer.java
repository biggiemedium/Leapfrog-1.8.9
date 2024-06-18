package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import javax.swing.plaf.multi.MultiOptionPaneUI;

@Module.ModuleInterface(name = "Timer", type = Type.Misc, description = "Set client side timer")
public class Timer extends Module {

    public Timer() {

    }

    private Setting<Float> min = create(new Setting<>("Min Speed", 1F, 0F, 2F));
    private Setting<Float> max = create(new Setting<>("Max Speed", 2F, 0F, 5F));
    private Setting<Boolean> rotateCheck = create(new Setting<>("Rotate Check", true));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ((IMixinMinecraft) mc).timer().timerSpeed = 1F;
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            ((IMixinMinecraft) mc).timer().timerSpeed = (float) MathUtil.getRandom(min.getValue().floatValue(), max.getValue().floatValue());
        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> packetRecieveEvent = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            if(rotateCheck.getValue()) {
                MoveUtil.resetMotion();
                ((IMixinMinecraft) mc).timer().timerSpeed = 1F;
            }
        }
    });
}
