package dev.px.leapfrog.Client.Module.Combat;

import com.google.common.base.Predicates;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Event.World.WorldBlockAABBEvent;
import dev.px.leapfrog.API.Module.Setting.BetweenInteger;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Util.Render.*;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Random;

@Module.ModuleInterface(name = "Test Module", type = Type.Combat, description = "Balls")
public class TestModule extends Module {

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.AAC));

    private Setting<Boolean> fall = create(new Setting<>("Web Fall", false));
    private Setting<Boolean> webspeed = create(new Setting<>("Web Speed", false));
    private Setting<Float> delay = create(new Setting<>("Delay", 1f, 0f, 10f));
    private Setting<Integer> range = create(new Setting<>("Range", 5, 0, 10));

    private Setting<BetweenInteger<Integer>> betweenIntegerSetting = create(new Setting<>("Between Test", new BetweenInteger<Integer>(6, 10)));

    private final boolean notInTheAir = true;
    private final boolean notDuringMove = false;
    private final boolean notDuringRegeneration = false;
    private final boolean stopInput = false;

    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);
    private double speed;
    private int ticks;

    private C08PacketPlayerBlockPlacement placePacket;


    @Override
    public void onEnable() {
        mc.thePlayer.setSneaking(false);
        speed = 0;
        ticks = 0;
        super.onEnable();
    }

    @EventHandler
    private Listener<WorldBlockAABBEvent> aabbEventListener = new Listener<>(event -> {
        if(fall.getValue()) {
            if(event.getBlock() instanceof BlockWeb) {
                int x = event.getBlockPos().getX();
                int y = event.getBlockPos().getY();
                int z = event.getBlockPos().getZ();

                event.setBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
            }
        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> packetrEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            ChatUtil.sendClientSideMessage(ChatFormatting.RED + "Warning: " + ChatFormatting.RESET + "You flagged the anti-cheat!");
        }
    });

    @EventHandler
    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {

    });

    @SubscribeEvent
    public void onKey(InputEvent event) {
        if (Keyboard.getEventKey() == Keyboard.KEY_G) {

        }
    }

    @EventHandler
    private Listener<PacketSendEvent> packetsEventListener = new Listener<>(event -> {

    });

    @EventHandler
    private Listener<PacketReceiveEvent> packetsEventListener2 = new Listener<>(event -> {
        if (event.getPacket() instanceof S12PacketEntityVelocity) {

        }
    });

    private Random random = new Random();
    private final TimerUtil timer = new TimerUtil();

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if (event.getStage() == Event.Stage.Pre) {
            boolean usingItem = mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem() != null && MoveUtil.isMoving();
            if (usingItem &&
                    !(mc.thePlayer.posY % 0.015625 == 0) && mc.thePlayer.ticksExisted % 2 == 0) {
                event.setY(event.getY() + 0.05);
                event.setOnGround(false);
            }
        }
    });

    private boolean isHoldingSword() {
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        return heldItem != null && heldItem.getItem() instanceof ItemSword && mc.gameSettings.keyBindUseItem.isPressed();
    }

    private int findGoldenAppleSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() == Items.golden_apple) {
                return i; // Return the slot index where the golden apple is found
            }
        }
        return -1; // Return -1 if no golden apple is found
    }

    private void sendFakeEatingPackets(int slot) {
        int currentItem = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
                mc.thePlayer.getHeldItem()
        ));
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentItem));
    }

    public MovingObjectPosition rayCast(float yaw, float pitch, final double range, final float expand) {
        float partialTicks = ((IMixinMinecraft) mc).timer().renderPartialTicks;
        Entity entity = mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;

        if (entity != null && mc.theWorld != null) {
            objectMouseOver = rayTraceCustom(entity, range, yaw, pitch);
            double d1 = range;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = getVectorForRotation(yaw, pitch);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize() + expand;
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
            }

            return objectMouseOver;
        }

        return null;
    }

    private final Vec3 getVectorForRotation(float p_getVectorForRotation_1_, float p_getVectorForRotation_2_) {
        float f = MathHelper.cos(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.sin(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.cos(-p_getVectorForRotation_1_ * 0.017453292F);
        float f3 = MathHelper.sin(-p_getVectorForRotation_1_ * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public MovingObjectPosition rayTraceCustom(Entity player, double blockReachDistance, float yaw, float pitch) {
        final Vec3 vec3 = player.getPositionEyes(1.0F);
        final Vec3 vec31 = getVectorForRotation(yaw, pitch);
        final Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    private S12PacketEntityVelocity adjustVelocityToFacingDirection(S12PacketEntityVelocity packet) {
        float yaw = mc.thePlayer.rotationYaw;
        double yawRadians = Math.toRadians(yaw);
        double directionX = -Math.sin(yawRadians);
        double directionZ = Math.cos(yawRadians);
        double originalVelocityX = packet.getMotionX() / 8000.0;
        double originalVelocityZ = packet.getMotionZ() / 8000.0;
        double magnitude = Math.sqrt(originalVelocityX * originalVelocityX + originalVelocityZ * originalVelocityZ);
        double newVelocityX = directionX * magnitude;
        double newVelocityZ = directionZ * magnitude;
        int adjustedMotionX = (int) (newVelocityX * 8000);
        int adjustedMotionZ = (int) (newVelocityZ * 8000);
        return new S12PacketEntityVelocity(packet.getEntityID(), adjustedMotionX, packet.getMotionY(), adjustedMotionZ);
    }



    @EventHandler
    private Listener<PlayerAttackEvent> attackEventListener = new Listener<>(event -> {
        if(event.getEntity() != null) {

        }
    });

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
        AAC,
        Grim,
        NCP
    }




}
