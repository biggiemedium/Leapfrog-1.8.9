package dev.px.leapfrog.Client.GUI.Notifications;

import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Impl.DecelerateAnimation;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Impl.EaseBackIn;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Tenacity Client
 */
public class Notification {

    private String name, description;
    private NotificationType type;
    private float time;
    private Animation animation;
    private TimerUtil timer;

    public Notification(String name, String description, NotificationType type, int time) {
        this.name = name;
        this.description = description;
        this.time = (long) (time * 1000);
        this.timer = new TimerUtil();
        this.type = type;
        this.animation = new DecelerateAnimation(250, 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public TimerUtil getTimer() {
        return timer;
    }

    public void setTimer(TimerUtil timer) {
        this.timer = timer;
    }

    public enum NotificationType {
        INFO,
        Warning,
        Error,
        Enable,
        Disable
    }
}
