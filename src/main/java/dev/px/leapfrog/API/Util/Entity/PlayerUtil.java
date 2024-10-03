package dev.px.leapfrog.API.Util.Entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    /**
     * @see Minecraft
     */
    public static void clickMouse() {
        try {
            Method method = Minecraft.class.getDeclaredMethod("clickMouse");
            method.setAccessible(true);
            method.invoke(mc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a click to Minecraft legitimately
     */
    public static void sendClick(final int button, final boolean state) {
        final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

        KeyBinding.setKeyBindState(keyBind, state);

        if (state) {
            KeyBinding.onTick(keyBind);
        }
    }

    /**
     * @return if player is moving
     */
    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
    }

    /**
     * Gets the block relative to the player from the offset
     *
     * @return block relative to the player
     */
    public static Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }

    public static boolean isFalseFlaggable(EntityPlayer player) {
        return player.isInWater() || player.isInLava() || player.isOnLadder() || player.ticksExisted < 10;
    }

    public static float getHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static boolean inLiquid() {
        boolean inLiquid = false;
        int y = (int) (mc.thePlayer.getEntityBoundingBox().minY + 0.02D);
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
                    if (!(block instanceof net.minecraft.block.BlockLiquid))
                        return false;
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    public static boolean isBlockUnderPlayer(double height) {
        for (int offset = 0; offset < height; offset += 2) {
            final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlockUnderEntity(EntityPlayer player, Block block, int range) {
        for (int i = 0; i < range; i++) {
            int blockX = MathHelper.floor_double(player.posX);
            int blockY = MathHelper.floor_double(player.getEntityBoundingBox().minY) - 1 - i;
            int blockZ = MathHelper.floor_double(player.posZ);
            BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
            if (mc.theWorld.getBlockState(blockPos).getBlock() == block) {
                return true;
            }
        }
        return false;
    }

    public static MovingObjectPosition rayTraceCustom(double blockReachDistance, float yaw, float pitch) {
        final Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
        final Vec3 vec31 = getVectorForRotation(yaw, pitch);
        final Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    public static boolean onLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = PlayerUtil.mc.thePlayer.getEntityBoundingBox();
        final WorldClient world = PlayerUtil.mc.theWorld;
        final int y = (int) playerBB.offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
                final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    /**
     * @author Rise client
     * @param offsetX
     * @param offsetZ
     * @return
     */
    // This methods purpose is to get block placement possibilities, blocks are 1 unit thick so please don't change it to 0.5 it causes bugs.
    public static Vec3 getPlacePossibility(double offsetX, double offsetZ) {
        final List<Vec3> possibilities = new ArrayList<>();
        final int range = (int) (5 + (Math.abs(offsetX) + Math.abs(offsetZ)));

        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = PlayerUtil.blockRelativeToPlayer(x, y, z);

                    if (!(block instanceof BlockAir)) {
                        for (int x2 = -1; x2 <= 1; x2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x + x2, mc.thePlayer.posY + y, mc.thePlayer.posZ + z));

                        for (int y2 = -1; y2 <= 1; y2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y + y2, mc.thePlayer.posZ + z));

                        for (int z2 = -1; z2 <= 1; z2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z + z2));
                    }
                }
            }
        }

        possibilities.removeIf(vec3 -> mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord) > 5 || !(mc.theWorld.getBlockState(new BlockPos(vec3.xCoord, vec3.yCoord, vec3.zCoord)).getBlock() instanceof BlockAir) || (vec3.yCoord > mc.thePlayer.posY));

        if (possibilities.isEmpty()) return null;

        possibilities.sort(Comparator.comparingDouble(vec3 -> {

            final double d0 = (mc.thePlayer.posX + offsetX) - vec3.xCoord;
            final double d1 = (mc.thePlayer.posY - 1) - vec3.yCoord;
            final double d2 = (mc.thePlayer.posZ + offsetZ) - vec3.zCoord;
            return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);

        }));

        return possibilities.get(0);
    }

    public static float getSpeedKMH() {
        double deltaX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double deltaZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;

        float distance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

        double floor = Math.floor(( distance/1000.0f ) / ( 0.05f/3600.0f ));

        String formatter = String.valueOf(floor);

        if (!formatter.contains("."))
            formatter += ".0";

        return Float.valueOf(formatter);
    }
}
