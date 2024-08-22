package dev.px.leapfrog.API.Util.Entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.security.SecureRandom;

public class PlayerUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
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
}
