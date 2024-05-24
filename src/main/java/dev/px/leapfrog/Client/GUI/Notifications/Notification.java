package dev.px.leapfrog.Client.GUI.Notifications;

import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class Notification {

    private String title, message;
    private int x, y;
    private int width, height;
    private float time;
    private TimerUtil timer;
    private NotificationType type;
    private ScaledResolution sr;
    private Animation animation = new Animation(300, false, Easing.LINEAR);

    public Notification(String title, String message, NotificationType type, int time) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.width = (int) FontRenderer.sans18_bold.getStringWidth(message) + 40;
        this.height = (int) FontRenderer.sans18_bold.getHeight() * 2;
        this.timer = new TimerUtil();
        this.timer.reset();
        this.time = (float) (time * 1000);
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
    }

    public void render() {
        GL11.glPushMatrix();



        switch (type) {
            case INFO:

                break;

            case Warning:

                break;

            case Error:

                break;
        }

        GL11.glPopMatrix();
    }

    public enum NotificationType {
        INFO,
        Warning,
        Error
    }
}
