package dev.px.leapfrog.API.Util.Math;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vector3d;

import java.util.Random;

/**
 * @see net.minecraft.entity.ai.EntityLookHelper
 */
public class RotationUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getNeededRotations(double p1, double p2, double p3, double t1, double t2, double t3) {
        final double diffX = t1 - p1;
        final double diffY = t2 - p2;
        final double diffZ = t3 - p3;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    public static float smoothRotate(float currentRotation, float targetRotation, float maxChange) {
        float rotationDifference = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);

        if (rotationDifference > maxChange) {
            rotationDifference = maxChange;
        }
        if (rotationDifference < -maxChange) {
            rotationDifference = -maxChange;
        }

        return currentRotation + rotationDifference;
    }

    public static boolean isVisibleFOV(final Entity e, final float fov) {
        return ((Math.abs(getRotations(e)[0] - mc.thePlayer.rotationYaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(getRotations(e)[0] - mc.thePlayer.rotationYaw) % 360.0f) : (Math.abs(getRotations(e)[0] - mc.thePlayer.rotationYaw) % 360.0f)) <= fov;
    }

    /**
     *
     * @param target
     * @param yawSpeed
     * @param pitchSpeed
     * @param miss
     *
     * @see net.minecraft.entity.ai.EntityLookHelper
     * setLookPositionWithEntity
     */
    public static float[] faceTarget(Entity target, float yawSpeed, float pitchSpeed, boolean miss) {
        final double deltaX = target.posX - mc.thePlayer.posX;
        final double deltaZ = target.posZ - mc.thePlayer.posZ;
        double deltaY;

        if (target instanceof EntityLivingBase) {
            final EntityLivingBase livingTarget = (EntityLivingBase) target;
            deltaY = livingTarget.posY + livingTarget.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        } else {
            deltaY = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }

        Random random = new Random();
        float offset = miss ? (random.nextInt(15) * 0.25f + 5.0f) : 0.0f;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        float yaw = (float) (Math.atan2(deltaZ + offset, deltaX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-(Math.atan2(deltaY - ((target instanceof EntityPlayer) ? 0.5f : 0.0f) + offset, distanceXZ) * 180.0 / Math.PI));

        float adjustedPitch = smoothRotate(mc.thePlayer.rotationPitch, pitch, pitchSpeed);
        float adjustedYaw = smoothRotate(mc.thePlayer.rotationYaw, yaw, yawSpeed);

        return new float[] {adjustedYaw, adjustedPitch};
    }

    public static float[] getRotations(Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - mc.thePlayer.posX;
        final double diffZ = entity.posZ - mc.thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase elb = (EntityLivingBase) entity;
            diffY = elb.posY + (elb.getEyeHeight()) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getBlockRotations(double x, double y, double z) {
        double var4 = x - mc.thePlayer.posX + 0.5;
        double var6 = z - mc.thePlayer.posZ + 0.5;
        double var8 = y - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight() - 1.0);
        double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
        float var12 = (float) (Math.atan2(var6, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[]{var12, (float) (-Math.atan2(var8, var14) * 180.0 / 3.141592653589793)};
    }

    public static double[] faceTargetNoDelay(double px, double py, double pz, EntityPlayer me) { // bannable bc its not smooth
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }

}
