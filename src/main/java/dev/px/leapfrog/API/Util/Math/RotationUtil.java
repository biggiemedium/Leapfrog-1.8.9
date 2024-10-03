package dev.px.leapfrog.API.Util.Math;

import com.google.common.base.Predicates;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

import java.util.List;
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

    public static Vec3 getVectorForRotation(float p_getVectorForRotation_1_, float p_getVectorForRotation_2_) {
        float f = MathHelper.cos(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.sin(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.cos(-p_getVectorForRotation_1_ * 0.017453292F);
        float f3 = MathHelper.sin(-p_getVectorForRotation_1_ * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = ((IMixinMinecraft) mc).timer().renderPartialTicks;
        final Entity entity = mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;
        Entity mcPointedEntity = null;

        if (entity != null && mc.theWorld != null) {

            mc.mcProfiler.startSection("pick");
            final double d0 = mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = getVectorForRotation(pitch, yaw);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
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

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mcPointedEntity = pointedEntity;
                }
            }

            mc.mcProfiler.endSection();

            return mcPointedEntity == target;
        }

        return false;
    }

    public static float[] getSmoothRotations(EntityLivingBase entity) {
        float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float fac = f1 * f1 * f1 * 256.0F;

        double x = entity.posX - mc.thePlayer.posX;
        double z = entity.posZ - mc.thePlayer.posZ;
        double y = entity.posY + entity.getEyeHeight()
                - (mc.thePlayer.getEntityBoundingBox().minY
                + (mc.thePlayer.getEntityBoundingBox().maxY
                - mc.thePlayer.getEntityBoundingBox().minY));

        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (MathHelper.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float) (-(MathHelper.atan2(y, d3) * 180.0 / Math.PI));
        yaw = smoothRotate(mc.thePlayer.prevRotationYawHead, yaw, fac * MathUtil.getRandomInRange(0.9F, 1));
        pitch = smoothRotate(mc.thePlayer.prevRotationPitch, pitch, fac * MathUtil.getRandomInRange(0.7F, 1));

        return new float[]{yaw, pitch};
    }

}
