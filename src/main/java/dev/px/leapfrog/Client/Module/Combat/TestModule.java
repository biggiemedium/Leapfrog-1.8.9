package dev.px.leapfrog.Client.Module.Combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Input.ClickMouseEvent;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.BetweenInteger;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.EntityUtil;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.Pair;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Util.Math.Vectors.Vec3d;
import dev.px.leapfrog.API.Util.Render.*;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.ContinualAnimation;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Direction;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import javafx.geometry.BoundingBox;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {

    });


    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if (event.getStage() == Event.Stage.Pre) {

        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> packetrEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            String s;
            ChatUtil.sendClientSideMessage(ChatFormatting.RED + "Warning: " + ChatFormatting.RESET + "You flagged the anti-cheat!");
        }
    });

    @EventHandler
    private Listener<PacketSendEvent> packetsEventListener = new Listener<>(event -> {

    });

    private Vec3 predictLandingPosition(EntityPlayer player) {
        Vec3 currentPosition = player.getPositionVector();
        Vec3 velocity = new Vec3(player.motionX, player.motionY, player.motionZ);
        double gravity = 0.08; // Minecraft's gravity constant

        Vec3 predictedPosition = currentPosition;

        while (velocity.yCoord > 0 || !isOnGround(predictedPosition)) {
            predictedPosition = predictedPosition.add(velocity);
            velocity = new Vec3(velocity.xCoord * 0.99, velocity.yCoord - gravity, velocity.zCoord * 0.99); // Apply friction and gravity
        }

        return predictedPosition;
    }

    private boolean isOnGround(Vec3 position) {
        BlockPos blockPos = new BlockPos(position.xCoord, position.yCoord - 1, position.zCoord);
        return !mc.theWorld.isAirBlock(blockPos);
    }

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
