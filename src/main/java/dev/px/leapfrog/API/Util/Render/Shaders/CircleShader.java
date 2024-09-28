package dev.px.leapfrog.API.Util.Render.Shaders;

import dev.px.leapfrog.API.Util.Render.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CircleShader {

    public static ShaderUtil shader = new ShaderUtil("Leapfrog/Shaders/circle-arc.frag");

    public static void drawCircle(float x, float y, float radius, float progress, int change, Color color, float smoothness) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float borderThickness = 1;
        shader.init();
        shader.setUniformf("radialSmoothness", smoothness);
        shader.setUniformf("radius", radius);
        shader.setUniformf("borderThickness", borderThickness);
        shader.setUniformf("progress", progress);
        shader.setUniformi("change", change);
        shader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        float wh = radius + 10;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        shader.setUniformf("pos", (x + ((wh / 2f) - ((radius + borderThickness) / 2f))) * sr.getScaleFactor(),
                (Minecraft.getMinecraft().displayHeight - ((radius + borderThickness) * sr.getScaleFactor())) - ((y + ((wh / 2f) - ((radius + borderThickness) / 2f))) * sr.getScaleFactor()));
        ShaderUtil.drawQuads(x, y, wh, wh);
        shader.unload();
        GlStateManager.disableBlend();
    }
}
