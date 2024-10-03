package dev.px.leapfrog.Client.Module.Combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Setting.BetweenInteger;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Util.Math.Vectors.Vec3d;
import dev.px.leapfrog.API.Util.Render.*;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Module.ModuleInterface(name = "Test Module", type = Type.Combat, description = "Balls")
public class TestModule extends Module {

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.NCP));
    private Setting<Float> littleNumber = create(new Setting<>("Little number", 100f, 0f, 500f));
    private Setting<Float> speedSetting = create(new Setting<>("Speed", 4.0f, 0.0f, 10.0f));

    private Setting<BetweenInteger<Integer>> betweenIntegerSetting = create(new Setting<>("Between Test", new BetweenInteger<Integer>(6, 10)));

    private double speed;
    private int ticks;

    private int packet;
    private boolean fixed;

    private TimerUtil time = new TimerUtil();

    @Override
    public void onEnable() {
        mc.thePlayer.setSneaking(false);
        speed = 0;
        ticks = 0;
        super.onEnable();
    }

    @EventHandler
    private Listener<PacketReceiveEvent> packetrEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            ChatUtil.sendClientSideMessage(ChatFormatting.RED + "Warning: " + ChatFormatting.RESET + "You flagged the anti-cheat!");
        }
    });

    @EventHandler
    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
        if(mode.getValue() == Mode.Watchdog_FastSpeed) {
            if (!MoveUtil.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
            MoveUtil.setSpeed(MoveUtil.isMoving() ? Math.max(MoveUtil.getBaseMoveSpeed(), MoveUtil.getMotionSpeed()) : 0, event);
        } else if(mode.getValue() == Mode.Watchdog_groundSpeed) {

        } else if(mode.getValue() == Mode.Watchdog_lowSpeed) {
            if (!MoveUtil.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
            final double value = MoveUtil.isMoving() ? Math.max(MoveUtil.getBaseMoveSpeed(), MoveUtil.getMotionSpeed()) : 0,
                    yaw = MoveUtil.getDirection(mc.thePlayer.rotationYaw),
                    x = -Math.sin(yaw) * value,
                    z = Math.cos(yaw) * value;
            //e.setX(x);
            //e.setZ(z);
            event.setX(x - (x - event.getX()) * (1.0 - 80F / 100));
            event.setZ(z - (z - event.getZ()) * (1.0 - 80F / 100));
            MoveUtil.setSpeed(MoveUtil.isMoving() ? Math.max(MoveUtil.getBaseMoveSpeed(), MoveUtil.getBaseMoveSpeed()) : 0, event);
        }
    });

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(mode.getValue() == Mode.Watchdog_FastSpeed) {
            if (mc.thePlayer.onGround) {
                if (MoveUtil.isMoving()) {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) return;
                    mc.thePlayer.jump();
                    MoveUtil.setSpeed(false, MoveUtil.getBaseMoveSpeed() * 1.5);
                    if (ticks > 0) MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() * 1.77);
                    ticks++;
                }
            } else if (MoveUtil.isMoving()) {
                if (mc.thePlayer.motionY > 0.05 && mc.thePlayer.motionY < 0.15) mc.thePlayer.motionY = (float) -0.01;
                if (mc.thePlayer.motionY > -0.07 && mc.thePlayer.motionY < 0.) mc.thePlayer.motionY = (float) -0.09;
            }
            if (!MoveUtil.isMoving()) {
                speed = 0;
            }
        } else if(mode.getValue() == Mode.Watchdog_groundSpeed) {
            if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                if (ticks < 15) {
                    ((IMixinMinecraft) mc).timer().timerSpeed = speedSetting.getValue();
                    ticks++;
                } else if (ticks < 22) {
                    ((IMixinMinecraft) mc).timer().timerSpeed = 1;
                    ticks++;
                } else {
                    ticks = 0;
                }
            } else {
                ((IMixinMinecraft) mc).timer().timerSpeed = 1.0f;
                ticks = 0;
            }
            if (!MoveUtil.isMoving()) {
                speed = 0;
            }
        } else if(mode.getValue() == Mode.Watchdog_lowSpeed) {
            event.setYaw((float) Math.toDegrees(MoveUtil.getDirection(mc.thePlayer.rotationYaw)));
            boolean doLowHop = !this.mc.thePlayer.isPotionActive(Potion.jump) &&
                    this.mc.thePlayer.fallDistance <= 0.75F &&
                    !this.mc.thePlayer.isCollidedHorizontally;
            if (mc.thePlayer.onGround) {
                if (MoveUtil.isMoving()) {
                    mc.thePlayer.jump();
                    if (!this.mc.thePlayer.isPotionActive(Potion.jump)) mc.thePlayer.motionY = 0.4;
                    MoveUtil.setSpeed(false, MoveUtil.getBaseMoveSpeed() * 1.65);
                    if (ticks > 0 && ticks % 2 == 0) {
                        MoveUtil.setSpeed(false, MoveUtil.getBaseMoveSpeed() * 1.72);
                    }
                    ticks++;
                }
            } else if (MoveUtil.isMoving() && doLowHop) {
                final double groundOffset = round(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3, 0.0001);
                if (groundOffset == round(0.4, 3, 0.0001)) {
                    mc.thePlayer.motionY = 0.32;
                } else if (groundOffset == round(0.71, 3, 0.0001)) {
                    mc.thePlayer.motionY = 0.04;
                } else if (groundOffset == round(0.75, 3, 0.0001)) {
                    mc.thePlayer.motionY = -0.2;
                } else if (groundOffset == round(0.55, 3, 0.0001)) {
                    mc.thePlayer.motionY = -0.15;
                } else if (groundOffset == round(0.41, 3, 0.0001)) {
                    mc.thePlayer.motionY = -0.2;
                }
            }
            if (!MoveUtil.isMoving()) speed = 0;
        }
    });

    public static double round(double value, int scale, double inc) {
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;

        if (value >= floored + halfOfInc)
            return new BigDecimal(Math.ceil(value / inc) * inc)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        else return new BigDecimal(floored)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);

        ((IMixinMinecraft) mc).setRightClickDelayTimer(4);
        mc.thePlayer.stepHeight = 0.6F;
        ((IMixinMinecraft) mc).timer().timerSpeed = 1;
        speed = 0;
        ticks = 0;

        MoveUtil.resetMotion();
    }

    private enum Mode {
        NCP,
        Watchdog_FastSpeed,
        Watchdog_groundSpeed,
        Watchdog_lowSpeed,
        Watchdog_mini2Speed,
        Watchdog_minSpeed,
        Watchdog_SemiStrafe,
        Watchdog_Speed
    }

    private ArrayList<Particle> particles = new ArrayList<>();

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        this.particles.forEach(particle -> particle.update());
        this.particles.removeIf(particle -> particle.remove);
        this.particles.forEach(particle -> particle.render());
    });

    @EventHandler
    private Listener<PlayerAttackEvent> attackEventListener = new Listener<>(event -> {
        if(event.getEntity() != mc.thePlayer) {
            Particle p = new Particle(new Vec3d(event.getEntity().posX, event.getEntity().posY + event.getEntity().getEyeHeight(), event.getEntity().posZ), "Hit!");
            this.particles.add(p);
        }
    });

    private class Particle {

        private Vec3d position;
        private TimerUtil timer;
        private boolean remove;
        private int time;
        private double verticalSpeed;
        private String text;

        public Particle(Vec3d position, String text) {
            this.position = position;
            this.timer = new TimerUtil();
            this.timer.reset();
            this.time = ThreadLocalRandom.current().nextInt(1000, 3001);
            this.remove = false;
            this.verticalSpeed = 0.02 + Math.random() * 0.03;
            this.text = text;
        }

        public void update() {
            if(this.timer.passed(time)) {
                this.remove = true;
            }

            position = position.addVector(0, verticalSpeed, 0);
            verticalSpeed *= 0.98;
        }

        public void render() {
            GL11.glPushMatrix();
            GL11.glTranslated(position.xCoord, position.yCoord, position.zCoord);

            Vec3d cameraPosition = new Vec3d(mc.getRenderViewEntity().getPositionEyes(1.0f).xCoord, mc.getRenderViewEntity().getPositionEyes(1.0f).yCoord, mc.getRenderViewEntity().getPositionEyes(1.0f).zCoord);
            Vec3d lookDirection = cameraPosition.subtract(position).normalize();

            float yaw = (float) Math.toDegrees(Math.atan2(lookDirection.zCoord, lookDirection.xCoord)) - 90;
            float pitch = (float) Math.toDegrees(Math.atan2(lookDirection.yCoord, Math.hypot(lookDirection.xCoord, lookDirection.zCoord)));
            GL11.glRotatef(-yaw, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);

            float scaleFactor = 0.01f; // Adjust this

            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
            Minecraft.getMinecraft().fontRendererObj.drawString(text, 0, 0, 0xFFFFFFFF);

            GL11.glPopMatrix();
        }

    }


}
