package dev.px.leapfrog.Client.GUI.Notifications;

import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Notification {

    private String messsage;
    private long start;

    private long fadedIn;
    private long fadeOut;
    private long end;
    private int length;
    private final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    public Notification(String messsage, int length) {
        this.messsage = messsage;
        this.length = length;

        fadedIn = 200 * length;
        fadeOut = fadedIn + 500 * length;
        end = fadeOut + fadedIn;
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render() {
        double offset;
        int width = (int) FontRenderer.sans20_bold.getStringWidth(messsage) + 20;
        int height = (int) FontRenderer.sans20_bold.getHeight() + 10;
        long time = getTime();

        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 3.0) * 10;
        } else if (time > fadeOut) {
            offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * 10);
        } else {
            offset = 10;
        }

        int x = (sr.getScaledWidth() / 2) - (width / 2);
        int y = (int) offset;
        RoundedShader.drawRound(x, y, width, height, 7, new Color(0, 0, 0, 120));
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        FontRenderer.sans20_bold.drawString(messsage, x + 10, (int) y + 6, Color.WHITE.getRGB());
    }

    public enum NotificationType {
        INFO,
        Warning,
        Error
    }
}
