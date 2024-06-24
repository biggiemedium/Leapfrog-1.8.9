package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.BetweenInteger;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.*;

import java.util.ArrayList;

@Module.ModuleInterface(name = "Test Module", type = Type.Combat, description = "Balls")
public class TestModule extends Module {

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.AAC));

    private Setting<Boolean> trigger = create(new Setting<>("booleanSetting", false));
    private Setting<Float> timer = create(new Setting<>("Timer", 1.0f, 0.0f, 5.0f));
    private Setting<Float> speedV = create(new Setting<>("V speed", 1.0f, 0.0f, 10.0f));
    private Setting<Float> speedH = create(new Setting<>("H Speed", 1.0f, 0.0f, 10.0f));
    private Setting<BetweenInteger<Integer>> betweenn = create(new Setting<>("CPS", new BetweenInteger<>(1, 10)));

    private int ticks = 0;
    private double speed;

    private Entity entity;

    private TimerUtil playerTimer = new TimerUtil();
    private ArrayList<C03PacketPlayer> packetPlayers = new ArrayList<>();
    public Vec3 lastGroundPos = new Vec3(0, 0, 0);


    @Override
    public void onEnable() {
        super.onEnable();
        speed = 1.4f;
        ticks = 0;
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if (event.getStage() == Event.Stage.Pre) {

        }
    });



    @EventHandler
    private Listener<PacketReceiveEvent> packetrEventListener = new Listener<>(event -> {

    });

    @EventHandler
    private Listener<PacketSendEvent> packetsEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive) {
            event.cancel();
        }

    });

    @Override
    public void onDisable() {
        super.onDisable();
        this.entity = null;
        ((IMixinMinecraft) mc).setRightClickDelayTimer(4);
        mc.thePlayer.stepHeight = 0.6F;
        ((IMixinMinecraft) mc).timer().timerSpeed = 1;

        MoveUtil.resetMotion();

        speed = 0;
        ticks = 0;
    }

    private enum Mode {
        AAC,
        Grim,
        NCP
    }
}
