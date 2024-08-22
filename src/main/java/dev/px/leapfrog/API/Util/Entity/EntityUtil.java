package dev.px.leapfrog.API.Util.Entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;

public class EntityUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isPassive(Entity entity) {

        if(entity instanceof EntityWolf && ((EntityWolf) entity).isAngry()) {
            return false;
        }

        if(entity instanceof EntityAnimal || entity instanceof EntityTameable || entity instanceof EntitySquid || entity instanceof EntityAgeable) {
            return true;
        }

        if(entity instanceof EntityIronGolem && !(((EntityIronGolem) entity).getRevengeTimer() > 0)) {
            return true;
        }

        return false;
    }

    // We love ChatGPT
    public static Entity getTarget(boolean playersOnly, int range) {
        if(mc.theWorld.loadedEntityList.isEmpty()) {
            return null;
        }

        Entity closestEntity = null;
        double closestDistance = range; // Use the specified range as the initial closest distance

        for(Entity e : mc.theWorld.loadedEntityList) {
            if (e == null || e == mc.thePlayer || e.isDead) {
                continue;
            }
            if (playersOnly && !(e instanceof EntityPlayer)) {
                continue;
            }

            double distance = mc.thePlayer.getDistanceToEntity(e);

            if (distance <= range && distance < closestDistance) {
                closestDistance = distance;
                closestEntity = e;
            }
        }

        return closestEntity;
    }

}
