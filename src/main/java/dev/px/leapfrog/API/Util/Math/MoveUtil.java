package dev.px.leapfrog.API.Util.Math;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MoveUtil {

    private static Minecraft mc = Minecraft.getMinecraft();
    public static final double JUMP_HEIGHT = 0.42F;

    public static double jumpBoostMotion(final double motionY) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return motionY + (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        return motionY;
    }
}
