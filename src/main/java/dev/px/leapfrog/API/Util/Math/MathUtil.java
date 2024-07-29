package dev.px.leapfrog.API.Util.Math;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtil {

    public static final DecimalFormat DF_0 = new DecimalFormat("0");
    private static Minecraft mc = Minecraft.getMinecraft();

    private static float changeRotation(float currentRotation, float targetRotation, float maxChange) {
        float rotationDifference = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);

        if (rotationDifference > maxChange) {
            rotationDifference = maxChange;
        }
        if (rotationDifference < -maxChange) {
            rotationDifference = -maxChange;
        }

        return currentRotation + rotationDifference;
    }

    private float[] faceTarget(Entity target, final float yawSpeed, final float pitchSpeed, final boolean miss) {
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

        float adjustedPitch = changeRotation(mc.thePlayer.rotationPitch, pitch, pitchSpeed);
        float adjustedYaw = changeRotation(mc.thePlayer.rotationYaw, yaw, yawSpeed);

        return new float[]{adjustedYaw, adjustedPitch};
    }

    public static double[] faceTargetNoDelay(double px, double py, double pz, EntityPlayer me) { // bannable bc its not smooth
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
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

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }

    public static double getRandom(double min, double max) {
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
