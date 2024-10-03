package dev.px.leapfrog.Client.Module.Movement;

import com.google.common.base.Predicates;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerStrafeEvent;
import dev.px.leapfrog.API.Event.Player.PlayerTickEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.EntityUtil;
import dev.px.leapfrog.API.Util.Entity.InventoryUtil;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.ADT.Pair;
import dev.px.leapfrog.API.Util.Math.BlockUtil;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.RotationUtil;
import dev.px.leapfrog.API.Util.Math.Vectors.Vec2f;
import dev.px.leapfrog.API.Util.Math.Vectors.Vec3d;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

import java.util.List;

@Module.ModuleInterface(name = "Scaffold", type = Type.Movement, description = "Places blocks under you to prevent falling")
public class Scaffold extends Module {

    public Scaffold() {

    }

    private Setting<Boolean> tower = create(new Setting<>("Tower", true));
    private Setting<TowerMode> towerMode = create(new Setting<>("Tower Mode", TowerMode.Normal, v -> tower.getValue()));
    private Setting<Boolean> scaffold = create(new Setting<>("Scaffold", true));
    private Setting<Integer> placeDelaymin = create(new Setting<>("Place delay min", 1, 0, 10, v -> scaffold.getValue()));
    private Setting<Integer> placeDelaymax = create(new Setting<>("Place delay max", 5, 0, 10, v -> scaffold.getValue()));
    private Setting<ScaffoldMode> scaffoldMode = create(new Setting<>("Scaffold Mode", ScaffoldMode.Normal, v -> scaffold.getValue()));
    private Setting<RayCast> raycastMode = create(new Setting<>("RayCast", RayCast.Normal, v -> scaffold.getValue()));
    private Setting<YawOffset> yawOffsetMode = create(new Setting<>("Yaw Offset", YawOffset.None, v -> scaffold.getValue()));
    private Setting<Integer> minRotationSpeed = create(new Setting<>("Min Rotation Speed", 5, 0, 10, v -> scaffold.getValue()));
    private Setting<Integer> maxRotationSpeed = create(new Setting<>("Max Rotation Speed", 10, 0, 10, v -> scaffold.getValue()));
    private Setting<Boolean> downwardRotation = create(new Setting<>("Downward Rotation", false, v -> scaffold.getValue()));
    private Setting<Boolean> debug = create(new Setting<>("Debug", true));
    private float targetYaw, targetPitch;
    private int ticksOnAir;
    private float forward, strafe;
    private BlockPos blockFace;
    private Pair<EnumFacing, Vec3d> enumFacing;
    private Vec3 targetBlock;

    @Override
    public void onEnable() {
        targetYaw = mc.thePlayer.rotationYaw - 180;
        targetPitch = 90;
        targetBlock = null;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private Listener<Render2DEvent> debugInfo = new Listener<>(event -> {
        if(debug.getValue()) {
            draw("Target block: " + targetBlock == null ? "null" : "Placing", 2, 5);
            draw("Enum facing: " + enumFacing.getValue(), 2, 5 + (14 * 1));
            draw("Ticks on air: " + ticksOnAir, 2, 5 + (14 * 2));
            draw("Target yaw: " + targetYaw, 2, 5 + (14 * 3));
            draw("Target pitch" + targetPitch, 2, 5 + (14 * 4));
            draw("BlockFace: " + blockFace == null ? "null" : "place", 2, 5 + (14 * 5));
        }
    });

    private void draw(String text, int x, int y) {
        mc.fontRendererObj.drawString(text, x, y, -1);
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionScaffold = new Listener<>(event -> {
        switch (event.getStage()) {
            case Pre:

                break;

            case Post:

                break;
        }
    });

    @EventHandler
    private Listener<PlayerUpdateEvent> scaffoldTick = new Listener<>(event -> { // leave as tick, We don't want to be faster than game tick

        if(placeDelaymin.getValue() > placeDelaymax.getValue()) {
            placeDelaymin.setValue(placeDelaymin.getValue() - 1);
        }
        if (PlayerUtil.blockRelativeToPlayer(0, -1, 0) instanceof BlockAir) {
                ticksOnAir++;
            } else {
                ticksOnAir = 0;
            }

            // stealing this from rise... If I end up rewriting scaffold just use this
            targetBlock = PlayerUtil.getPlacePossibility(0, 0);

            if (targetBlock == null) {
                return;
            }

            enumFacing = getEnumFacing(targetBlock);

            if(enumFacing == null) {
                return;
            }

            if(downwardRotation.getValue() && mc.gameSettings.keyBindSneak.isKeyDown() && mc.thePlayer.onGround) {
                enumFacing.setKey(EnumFacing.DOWN);
            }

            BlockPos position = new BlockPos(targetBlock.xCoord, targetBlock.yCoord, targetBlock.zCoord);
            blockFace = position.add(enumFacing.getValue().xCoord, enumFacing.getValue().yCoord, enumFacing.getValue().zCoord);

            if (blockFace == null || enumFacing == null) {
                return;
            }

            executeRotations();

            if (targetBlock == null || enumFacing == null || blockFace == null) {
                return;
            }

            if(InventoryUtil.getHeldItem() != null && !(InventoryUtil.getHeldItem() instanceof ItemBlock)) {
                return;
            }

            if (ticksOnAir > MathUtil.getRandom(placeDelaymin.getValue(), placeDelaymax.getValue()) &&
                    this.overBlock(enumFacing.getKey(), blockFace,
                            raycastMode.getValue() == RayCast.Strict || raycastMode.getValue() == RayCast.Off)) {

                Vec3 hitVec = new Vec3(blockFace.getX() + Math.random(), blockFace.getY() + Math.random(), blockFace.getZ() + Math.random());
                MovingObjectPosition movingObjectPosition = this.rayCast(
                        new Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch),
                        mc.playerController.getBlockReachDistance(), 0);

                // Correct the hitVec if the raycast position matches the blockFace
                if (movingObjectPosition != null && movingObjectPosition.getBlockPos().equals(blockFace)) {
                    hitVec = movingObjectPosition.hitVec;
                    ChatUtil.sendClientSideMessage("Debug: Hitvec");
                }

                // Handle player right-click action
                if (mc.playerController.onPlayerRightClick(
                        mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(),
                        blockFace, enumFacing.getKey(), hitVec)) {
                    ChatUtil.sendClientSideMessage("Debug: Right click action");
                    mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
                    ticksOnAir = 0;
                }

                // Reset right-click delay timer
                ((IMixinMinecraft) mc).setRightClickDelayTimer(0);

                ItemStack currentItem = mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem];

                if (currentItem != null) {
                    if (currentItem.stackSize == 0) {
                        mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                    }
                } else if (Math.random() > 0.85 && ((IMixinMinecraft) mc).getRightClickDelayTimer() <= 0) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(currentItem));
                    ((IMixinMinecraft) mc).setRightClickDelayTimer(0);
                }
            }

    });

    @EventHandler
    private Listener<PlayerStrafeEvent> scaffoldStrafe = new Listener<>(event -> {
        if(scaffold.getValue()) {
            if(scaffoldMode.getValue() == ScaffoldMode.Telly) {
                if(mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    mc.thePlayer.jump();
                }
            }
        }
    });

    @EventHandler
    private Listener<PlayerMotionEvent> towerMotion = new Listener<>(event -> {
        switch (event.getStage()) {
            case Pre:
                if(tower.getValue()) {
                    switch (towerMode.getValue()) {
                        case Normal:
                            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (mc.thePlayer.onGround) {
                                    mc.thePlayer.motionY = 0.42F;
                                }
                            }
                            break;

                        case Packet:
                            if(mc.gameSettings.keyBindJump.isKeyDown()) {
                                if(mc.thePlayer.onGround) {
                                   // mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer());
                                }
                            }
                            break;
                        case Vulcan:
                            if(mc.gameSettings.keyBindJump.isKeyDown()) {
                                if(LeapFrog.positionManager.getOffGroundTicks() > 3 && !BlockUtil.getBlockInSphere(Blocks.air, 2).isEmpty()) {
                                    ItemStack itemStack = mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem];

                                    if (itemStack == null || (itemStack.stackSize > 2)) {
                                        mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(null));
                                    }
                                    mc.thePlayer.motionY = 0.42F;
                                }
                            }
                            break;
                        case WatchDog:
                            // idk why but C08 will usually bypass watchdog movements
                            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (mc.thePlayer.onGround) {
                                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                                    mc.thePlayer.motionY = 0.42f;
                                }
                            }
                            break;
                    }
                }

                break;
            case Post:

                break;
        }
    });

    @EventHandler
    private Listener<PacketSendEvent> towerPacketEvent = new Listener<>(event -> {
        if(event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.getPacket();
            switch (towerMode.getValue()) {
                case Normal:
                    if(mc.thePlayer.motionY > -MoveUtil.GRAVITY_SPEED) {
                        if (packet.getPosition().equals(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.4, mc.thePlayer.posZ))) {
                            mc.thePlayer.motionY = -MoveUtil.GRAVITY_SPEED;
                        }
                    }
                    break;
            }
        }
    });

    private void executeRotations() {
        switch (scaffoldMode.getValue()) {
            case Normal:
                if (ticksOnAir > 0 && !this.overBlock(new Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch), enumFacing.getKey(), blockFace, raycastMode.getValue() == RayCast.Strict)) {
                    getRotations(Float.parseFloat(String.valueOf(this.yawOffsetMode.getValue().getAlias())));
                }
                break;

            case Telly:
                if (LeapFrog.positionManager.getOffGroundTicks() >= 3) {
                    if (!this.overBlock(new Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch), enumFacing.getKey(), blockFace, raycastMode.getValue() == RayCast.Strict)) {
                        getRotations(Float.parseFloat(String.valueOf(this.yawOffsetMode.getValue().getAlias())));
                    }
                } else {
                    getRotations(Float.parseFloat(String.valueOf(this.yawOffsetMode.getValue().getAlias())));
                    targetYaw = mc.thePlayer.rotationYaw;
                }
                break;
        }

        double minRotationSpeed = this.minRotationSpeed.getValue().doubleValue();
        double maxRotationSpeed = this.maxRotationSpeed.getValue().doubleValue();
        float rotationSpeed = (float) MathUtil.getRandom(minRotationSpeed, maxRotationSpeed);

        if (rotationSpeed != 0) {
            RotationUtil.smoothRotate(mc.thePlayer.rotationYaw, targetYaw, rotationSpeed * 18);
            RotationUtil.smoothRotate(mc.thePlayer.rotationPitch, targetPitch, rotationSpeed * 18);
        }
    }

    public boolean overBlock(final EnumFacing enumFacing, final BlockPos pos, final boolean strict) {
        final MovingObjectPosition movingObjectPosition = mc.objectMouseOver;

        if (movingObjectPosition == null) return false;

        final Vec3 hitVec = movingObjectPosition.hitVec;
        if (hitVec == null) return false;

        return movingObjectPosition.getBlockPos().equals(pos) && (!strict || movingObjectPosition.sideHit == enumFacing);
    }

    private ItemStack getItemStack() {
        if (mc.thePlayer == null || mc.thePlayer.inventoryContainer == null) {
            return null;
        }

        int itemIndex = 0;
        int inventorySlot = itemIndex + 36;

        if (inventorySlot < 0 || inventorySlot >= mc.thePlayer.inventoryContainer.inventorySlots.size()) {
            return null;
        }

        return mc.thePlayer.inventoryContainer.getSlot(inventorySlot).getStack();
    }

    public void getRotations(float yawOffset) {
        boolean found = false;
        for (float possibleYaw = mc.thePlayer.rotationYaw - 180 + yawOffset; possibleYaw <= mc.thePlayer.rotationYaw + 360 - 180 && !found; possibleYaw += 45) {
            for (float possiblePitch = 90; possiblePitch > 30 && !found; possiblePitch -= possiblePitch > 50 ? 1 : 10) {
                if (this.overBlock(new Vec2f(possibleYaw, possiblePitch), enumFacing.getKey(), blockFace, true)) {
                    targetYaw = possibleYaw;
                    targetPitch = possiblePitch;
                    found = true;
                }
            }
        }

        if (!found) {
            final Vec2f rotations = this.calculate(
                    new Vec3d(blockFace.getX(), blockFace.getY(), blockFace.getZ()), enumFacing.getKey());

            targetYaw = rotations.x;
            targetPitch = rotations.y;

        }
    }

    public boolean overBlock(Vec2f rotation, EnumFacing enumFacing, BlockPos pos, boolean strict) {
        MovingObjectPosition movingObjectPosition = PlayerUtil.rayTraceCustom(4.5f, rotation.x, rotation.y);

        if (movingObjectPosition == null) return false;

        final Vec3 hitVec = movingObjectPosition.hitVec;
        if (hitVec == null) return false;

        return movingObjectPosition.getBlockPos().equals(pos) && (!strict || movingObjectPosition.sideHit == enumFacing);
    }


    public Vec2f calculate(Vec3d position, final EnumFacing enumFacing) {
        double x = position.getX() + 0.5D;
        double y = position.getY() + 0.5D;
        double z = position.getZ() + 0.5D;

        x += (double) enumFacing.getDirectionVec().getX() * 0.5D;
        y += (double) enumFacing.getDirectionVec().getY() * 0.5D;
        z += (double) enumFacing.getDirectionVec().getZ() * 0.5D;
        return calculate(new Vec3d(x, y, z));
    }

    public Vec2f calculate(Vec3d from, Vec3d to) {
        final Vec3d diff = to.subtract(from);
        final double distance = Math.hypot(diff.getX(), diff.getZ());
        final float yaw = (float) (MathHelper.atan2(diff.getZ(), diff.getX()) * 180.0F / Math.PI) - 90.0F;
        final float pitch = (float) (-(MathHelper.atan2(diff.getY(), distance) * 180.0F / Math.PI));
        return new Vec2f(yaw, pitch);
    }

    public Vec2f calculate(final Vec3d to) {
        return calculate(new Vec3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).add(new Vec3d(0, mc.thePlayer.getEyeHeight(), 0)), to);
    }

    public Pair<EnumFacing, Vec3d> getEnumFacing(Vec3 position) {
        for (int x2 = -1; x2 <= 1; x2 += 2) {
            BlockPos checkPos = new BlockPos(position.xCoord + x2, position.yCoord, position.zCoord);
            if (!(mc.theWorld.getBlockState(checkPos).getBlock() instanceof BlockAir)) {
                return new Pair<>(x2 > 0 ? EnumFacing.WEST : EnumFacing.EAST, new Vec3d(x2, 0, 0));
            }
        }

        for (int y2 = -1; y2 <= 1; y2 += 2) {
            BlockPos checkPos = new BlockPos(position.xCoord, position.yCoord + y2, position.zCoord);
            if (!(mc.theWorld.getBlockState(checkPos).getBlock() instanceof BlockAir)) {
                return new Pair<>(y2 > 0 ? EnumFacing.DOWN : EnumFacing.UP, new Vec3d(0, y2, 0));
            }
        }

        for (int z2 = -1; z2 <= 1; z2 += 2) {
            BlockPos checkPos = new BlockPos(position.xCoord, position.yCoord, position.zCoord + z2);
            if (!(mc.theWorld.getBlockState(checkPos).getBlock() instanceof BlockAir)) {
                return new Pair<>(z2 > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH, new Vec3d(0, 0, z2));
            }
        }

        return new Pair<>(EnumFacing.UP, new Vec3d(0, 0, 0));
    }

    private MovingObjectPosition rayTraceCustom(Entity e, double blockReachDistance, float yaw, float pitch) {
        final Vec3 vec3 = e.getPositionEyes(1.0F);
        final Vec3 vec31 = PlayerUtil.getVectorForRotation(yaw, pitch);
        final Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public MovingObjectPosition rayCast(Vec2f rotation, final double range, final float expand) {
        float partialTicks = ((IMixinMinecraft) mc).timer().renderPartialTicks;
        Entity entity = mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;

        if (entity != null && mc.theWorld != null) {
            objectMouseOver = this.rayTraceCustom(entity, range, rotation.x, rotation.y);
            double d1 = range;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = PlayerUtil.getVectorForRotation(rotation.y, rotation.x);
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

    private enum TowerMode {
        Normal,
        Packet,
        Vulcan,
        WatchDog
    }

    private enum ScaffoldMode {
        Normal,
        Telly
    }

    private enum RayCast {
        Off,
        Normal,
        Strict
    }

    private enum YawOffset {
        None("0"),
        Positive("45"),
        negative("-45");

        String alias;

        YawOffset(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return this.alias;
        }
    }
}
