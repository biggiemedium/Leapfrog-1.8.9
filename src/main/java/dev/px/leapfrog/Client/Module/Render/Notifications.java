package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Direction;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.Notifications.Notification;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

@Module.ModuleInterface(name = "Notifications", type = Type.Visual, description = "Client side notifications and updates")
public class Notifications extends Module {

    public Notifications() {

    }

    private Setting<Float> time = create(new Setting<>("Display Time", 2.0F, 0.0F, 10.0F));
    public Setting<Boolean> toggle = create(new Setting<>("Toggle", true));
    public Setting<NotificationMode> mode = create(new Setting<>("Mode", NotificationMode.Client));

    @EventHandler
    private Listener<Render2DEvent> listener = new Listener<>(event -> {
        float yOffset = 0;
        int notificationHeight = 0;
        int notificationWidth;
        int actualOffset = 0;
        ScaledResolution sr = new ScaledResolution(mc);

        for (Notification notification : LeapFrog.notificationManager.getNotifications()) {
            Animation animation = notification.getAnimation();
            animation.setDirection(notification.getTimer().passed((long) notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);

            if (animation.finished(Direction.BACKWARDS)) {
                LeapFrog.notificationManager.getNotifications().remove(notification);
                continue;
            }

            float x, y;
            switch (mode.getValue()) {
                case Client:

                    break;

                case SuicideX:
                    animation.setDuration(200);
                    actualOffset = 3;
                    notificationHeight = 16;
                    String editTitle = notification.getName() + (notification.getName().endsWith(".") || notification.getName().endsWith("/") ? " " : ". ") + notification.getDescription();

                    notificationWidth = (int) FontRenderer.sans22.getStringWidth(editTitle) + 5;

                    x = sr.getScaledWidth() - (notificationWidth + 5);
                    y = sr.getScaledHeight() - (yOffset + 18 + notificationHeight + (15));

                    this.drawSuicideX(x, y, notificationWidth, notificationHeight, (float) animation.getValue(), notification.getName(), notification.getDescription());
                    break;
            }
        }
    });

    public void drawSuicideX(float x, float y, float width, float height, float animation, String name, String description) {
        float heightVal = height * animation <= 6 ? 0 : height * animation;
        float yVal = (y + height) - heightVal;

        String editTitle = name + (name.endsWith(".") || name.endsWith("/") ? " " : ". ") + description;

        FontRenderer.sans22.drawCenteredString(editTitle, x + width /2f,
                yVal + FontRenderer.sans22.getMiddleOfBox(heightVal), ColorUtil.applyOpacity(Color.WHITE, animation - .5f).getRGB());

    }

    public void blurSuicideX(float x, float y, float width, float height, float animation) {
        float heightVal = height * animation <= 6 ? 0 : height * animation;
        float yVal = (y + height) - heightVal;
        RoundedShader.drawRound(x, yVal, width, heightVal, 4, Color.BLACK);
    }

    public enum NotificationMode {
        Client,
        SuicideX
    }

}
