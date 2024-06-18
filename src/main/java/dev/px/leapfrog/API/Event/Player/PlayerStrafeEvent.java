package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import net.minecraft.client.Minecraft;

/**
 * @author alan
 */
public class PlayerStrafeEvent extends Event {

    private float forward;
    private float strafe;
    private float friction;
    private float yaw;

    public PlayerStrafeEvent(float forward, float strafe, float friction, float yaw) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
        this.yaw = yaw;
    }

    public void setSpeed(double speed, double motionMultiplier) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        Minecraft.getMinecraft().thePlayer.motionX *= motionMultiplier;
        Minecraft.getMinecraft().thePlayer.motionZ *= motionMultiplier;
    }

    public void setSpeed(double speed) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        MoveUtil.resetMotion();
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
