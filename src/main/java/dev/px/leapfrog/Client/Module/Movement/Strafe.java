package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerStrafeEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.Mod;
import org.apache.http.util.EntityUtils;

@Module.ModuleInterface(name = "Strafe", type = Type.Movement, description = "Strafes")
public class Strafe extends Module {

    public Strafe() {

    }

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.NCP));
    private Setting<Float> vanillaMultipler = create(new Setting<>("Vanilla Speed", 1.5F, 0.0F, 2.0F, v -> mode.getValue() == Mode.Vanilla));
    private Setting<Boolean> watchDogOnGround = create(new Setting<>("On Ground", true, v -> mode.getValue() == Mode.WatchDog));
    // NCP
    private Setting<Boolean> NCPTimer = create(new Setting<>("NCP Timer", false, v -> mode.getValue() == Mode.NCP));

    // Vulcan
    private Setting<Boolean> vulcanTimer = create(new Setting<>("Vulcan Timer", false, v -> mode.getValue() == Mode.VulcanTimer));

    private double speed = 0.0D;
    private int stage = 0;
    private double jumpY = 0;
    private int NCPTicks = 0;
    private int vulcanTicks = 0;

    @EventHandler
    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
        if(mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
            return;
        }
        switch (mode.getValue()) {
            case NCP:

                break;
            case VulcanTimer:
                vulcanTimer(event);
                break;
        }
    });

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
                return;
            }
            switch (mode.getValue()) {
                case Vanilla:
                    vanilla(event);
                    break;
                case NCP:
                    NCP(event);
                    break;
                case Verus:
                    verus(event);
                    break;
                case Vulcan:
                    vulcan(event);
                    break;
                case VulcanTenacity:
                    vulcanTenacityPre(event);
                    break;
                case WatchDog:
                    watchDog(event);
                    break;
                case VulcanTimer:
                    vulcanTimer(event);
                    break;
            }
        } else {
            switch (mode.getValue()) {
                case VulcanTenacity:
                    vulcanTenacityPost(event);
                    break;
            }
            //((IMixinMinecraft) mc).timer().timerSpeed = 1.0f; // yes
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

    private void AAC(PlayerMotionEvent event) {

    }

    private void Grim(PlayerMotionEvent event) {

    }

    private void NCP(PlayerMotionEvent event) {
        switch (NCPTicks) {
            case 0:
            case 1:
                if (PlayerUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        NCPTicks++;
                    }
                }
                break;

            case 2:
                if (PlayerUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() + 0.2);
                        if(this.NCPTimer.getValue()) {
                            ((IMixinMinecraft) mc).timer().timerSpeed = 1.07f;
                        } else {
                            ((IMixinMinecraft) mc).timer().timerSpeed = 1.0f;
                        }
                    } else {
                        MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed());
                    }
                }
                break;
        }
    }

    private void verus(PlayerMotionEvent event) {
        if(event.getStage() == Event.Stage.Pre) {
            //if (!MoveUtil.isMoving()) return;\
            if(MoveUtil.isMoving()) {
                if(PlayerUtil.isBlockUnderEntity(mc.thePlayer, Blocks.slime_block, 2)) { // Verus does not check speed on slime
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }

                    MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() * 1.7);
                }else {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MoveUtil.strafe(0.55F);
                    } else {
                        MoveUtil.strafe(0.35F);
                    }
                }

            }
        }

    }

    private void vulcan(PlayerMotionEvent event) {
            if(MoveUtil.isMoving()) {
                if(mc.thePlayer.isSprinting()) {
                    mc.thePlayer.setSprinting(false);
                }
                if(MoveUtil.isOnGround(0.1)) {
                    MoveUtil.setMoveSpeed(Math.max(0.34, MoveUtil.getBaseMoveSpeed()));
                    mc.thePlayer.jump();
                } else {
                    MoveUtil.setMoveSpeed(Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
                    ((IMixinMinecraft) mc).timer().timerSpeed = 1.05F;
                }
            } else {
                ((IMixinMinecraft) mc).timer().timerSpeed = 1F;
            }
    }

    private void vulcanTenacityPre(PlayerMotionEvent event) {
        if (mc.thePlayer.onGround) {
            if (MoveUtil.isMoving()) {
                mc.thePlayer.jump();
                MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() * 1.6);
                vulcanTicks = 0;
            }
        }
    }

    private void vulcanTenacityPost(PlayerMotionEvent event) {
        vulcanTicks++;
        if (vulcanTicks == 1) {
            MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() * 1.16);
        }
    }

    private void watchDog(PlayerMotionEvent event) {
        if (MoveUtil.isMoving()) {
            if (MoveUtil.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                MoveUtil.setMoveSpeed(Math.max(0.275, MoveUtil.getBaseMoveSpeed() * 0.9));
                mc.thePlayer.jump();
            } else if(!this.watchDogOnGround.getValue()){
                MoveUtil.setMoveSpeed(MoveUtil.getMotionSpeed());
            }
        }
    }

    private void vulcanTimer(PlayerMoveEvent event) {
        if (MoveUtil.isMoving()) {
            if (mc.thePlayer.onGround) {
                if (vulcanTimer.getValue()) {
                    ((IMixinMinecraft) mc).timer().timerSpeed = 0.9f;
                }
                mc.thePlayer.jump();
                event.y = mc.thePlayer.motionY = 0.42f;
                MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() * (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.9 : 2.1));
            } else if (LeapFrog.positionManager.getOffGroundTicks() == 5) {
                if (vulcanTimer.getValue() && mc.thePlayer.fallDistance < 1) {
                    ((IMixinMinecraft) mc).timer().timerSpeed = 1.25f;
                }
                event.y = mc.thePlayer.motionY = -0.42f;
            } else {
                if (((IMixinMinecraft) mc).timer().timerSpeed == 1.3f && mc.thePlayer.fallDistance > 1) {
                    ((IMixinMinecraft) mc).timer().timerSpeed = 1;
                }
            }
        } else {
            ((IMixinMinecraft) mc).timer().timerSpeed = 1;
        }
    }

    private void vulcanTimer(PlayerMotionEvent event) {
        if(MoveUtil.isMoving()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }
        }
    }


    @Override
    public void onDisable() {
        super.onDisable();
        ((IMixinMinecraft) mc).timer().timerSpeed = 1.0f;
        NCPTicks = 0;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        NCPTicks = 0;
    }

    @Override
    public String arrayDetails() {
        return this.mode.getValue().name();
    }

    private enum Mode {
        Vanilla,
        //AAC,
        //Grim,
        NCP,
        Verus,
        Vulcan,
        VulcanTenacity,
        VulcanTimer,
        WatchDog
    }
}
