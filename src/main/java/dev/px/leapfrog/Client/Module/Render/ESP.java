package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.ESPUtil;
import dev.px.leapfrog.API.Util.Render.GLUtils;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.GradientShader;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tenacity Client
 * @author Rise client
 * @author PX
 */
@Module.ModuleInterface(name = "ESP", type = Type.Visual, description = "See players through walls")
public class ESP extends Module {

    public ESP() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Box));
    private Setting<Colors> espColor = create(new Setting<>("Color", Colors.Client));
    private Setting<Color> customColor = create(new Setting<>("Custom Color", new Color(255, 255, 255), v -> espColor.getValue() == Colors.Custom));

    private Map<Entity, Vector4f> entityPosition = new HashMap<>();

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        entityPosition.clear();
        for(Entity e : mc.theWorld.playerEntities) {
            if(e == null) {
                continue;
            }
            if(e == mc.thePlayer) {
                continue;
            }
            if(e.isDead) {
                continue;
            }
            if(ESPUtil.isInView(e)) {
                entityPosition.put(e, ESPUtil.getEntityPositionsOn2D(e));
            }
        }
    });

    @EventHandler
    private Listener<Render2DEvent> render2DEventListener = new Listener<>(event -> {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Color firstColor = new Color(0, 0, 0);
        Color secondColor = new Color(0, 0, 0);
        Color thirdColor = new Color(0, 0, 0);
        Color fourthColor = new Color(0, 0, 0);

        switch (espColor.getValue()) {
            case Client:
                firstColor = ColorUtil.getClientColorInterpolation()[0];
                secondColor = ColorUtil.getClientColorInterpolation()[1];
                thirdColor = ColorUtil.getClientColorInterpolation()[2];
                fourthColor = ColorUtil.getClientColorInterpolation()[3];
                break;
            case Rainbow:
                firstColor = ColorUtil.rainbow(15, 0, .6f, 1, 1);
                secondColor = ColorUtil.rainbow(15, 90, .6f, 1, 1);
                thirdColor = ColorUtil.rainbow(15, 180, .6f, 1, 1);
                fourthColor = ColorUtil.rainbow(15, 270, .6f, 1, 1);
                break;

            case Custom:
                firstColor = customColor.getValue();
                secondColor = customColor.getValue();
                thirdColor = customColor.getValue();
                fourthColor = customColor.getValue();
                break;
        }

        for (Entity entity : entityPosition.keySet()) {
            Vector4f pos = entityPosition.get(entity);
            float x = pos.getX();
            float y = pos.getY();
            float right = pos.getZ();
            float bottom = pos.getW();

            switch (mode.getValue()) {
                case Box:
                    float outlineThickness = .3f;
                    RenderUtil.resetColor();
                    //top
                    GradientShader.drawGradientLR(x, y, (right - x), 1, 1, firstColor, secondColor);
                    //left
                    GradientShader.drawGradientTB(x, y, 1, bottom - y, 1, firstColor, fourthColor);
                    //bottom
                    GradientShader.drawGradientLR(x, bottom, right - x, 1, 1, fourthColor, thirdColor);
                    //right
                    GradientShader.drawGradientTB(right, y, 1, (bottom - y) + 1, 1, secondColor, thirdColor);

                    //Outline

                    //top
                    drawRect2(x - .5f, y - outlineThickness, (right - x) + 2, outlineThickness, Color.BLACK.getRGB());
                    //Left
                    drawRect2(x - outlineThickness, y, outlineThickness, (bottom - y) + 1, Color.BLACK.getRGB());
                    //bottom
                    drawRect2(x - .5f, (bottom + 1), (right - x) + 2, outlineThickness, Color.BLACK.getRGB());
                    //Right
                    drawRect2(right + 1, y, outlineThickness, (bottom - y) + 1, Color.BLACK.getRGB());


                    //top
                    drawRect2(x + 1, y + 1, (right - x) - 1, outlineThickness, Color.BLACK.getRGB());
                    //Left
                    drawRect2(x + 1, y + 1, outlineThickness, (bottom - y) - 1, Color.BLACK.getRGB());
                    //bottom
                    drawRect2(x + 1, (bottom - outlineThickness), (right - x) - 1, outlineThickness, Color.BLACK.getRGB());
                    //Right
                    drawRect2(right - outlineThickness, y + 1, outlineThickness, (bottom - y) - 1, Color.BLACK.getRGB());
                    break;
            }
        }
    });

    private void drawRect2(double x, double y, double width, double height, int color) {
        RenderUtil.resetColor();
        GLUtils.setup2DRendering(() -> GLUtils.render(GL11.GL_QUADS, () -> {
            RenderUtil.color(color);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x, y + height);
            GL11.glVertex2d(x + width, y + height);
            GL11.glVertex2d(x + width, y);
        }));
    }

    private enum Mode {
        Box,
        Rise,
        R6
    }

    private enum Colors {
        Client,
        Rainbow,
        Custom
    }
}
