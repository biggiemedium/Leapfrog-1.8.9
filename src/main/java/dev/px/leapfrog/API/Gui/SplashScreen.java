package dev.px.leapfrog.API.Gui;

import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Impl.DecelerateAnimation;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Tenacity Client & LeapFrog Client
 */
public class SplashScreen {

    private static ResourceLocation splash;
    private static TextureManager textureManager;
    private static Minecraft mc = Minecraft.getMinecraft();
    private static int currentProgress, count;
    private static Framebuffer framebuffer;
    private static Animation progressAnim;
    private static Animation fadeAnim, moveAnim, versionAnim, progress2Anim;
    private static final ResourceLocation splashImage = new ResourceLocation("leapfrog/splashscreen.png");

    public static void update() {
        if (mc == null || mc.getLanguageManager() == null)
            return;

        drawSplash(mc.getTextureManager());
    }

    public static void continueCount() {
        continueCount(true);
    }

    public static void continueCount(boolean increment) {
        if (increment) {
            count++;
        }
        drawSplash(mc.getTextureManager());
    }


    public static void drawSplash(TextureManager mcTextureManager) {
        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = sr.getScaleFactor();

        framebuffer = RenderUtil.createFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (splash == null) {
            splash = new ResourceLocation("Leapfrog/Images/CreditSplash.png");
        }

        mcTextureManager.bindTexture(splash);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080, sr.getScaledWidth(), sr.getScaledHeight(), 1920, 1080);


        if (progressAnim == null) {
            progressAnim = new DecelerateAnimation(1000, 1);
        }

        // Smooth Progress Bar
        drawProgressBar(sr);

        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        RenderUtil.setAlphaLimit(1);
        mc.updateDisplay();
    }

    private static void drawProgressBar(ScaledResolution sr) {
        float progress = (float) count / 10.0f; // Assuming 10 steps for full loading
        float rectWidth = sr.getScaledWidth() * 0.6f;
        float rectHeight = 10;
        float roundX = (sr.getScaledWidth() / 2f - rectWidth / 2f);
        float roundY = sr.getScaledHeight() - 100;
        float progressWidth = (float) (rectWidth * progressAnim.getValue() * progress);

        RoundedShader.drawRound(roundX, roundY, rectWidth, rectHeight, rectHeight / 2f, new Color(60, 60, 60, 120));
        RoundedShader.drawRound(roundX, roundY, progressWidth, rectHeight, rectHeight / 2f, new Color(172, 249, 201, 130));

        String progressText = Math.min((int) (progress * 100), 100) == 100 ? "Authenticating..." : "Loading... " + Math.min((int) (progress * 100), 100) + "%";
        FontUtil.regular22.drawString(progressText, sr.getScaledWidth() / 2 - FontUtil.regular22.getStringWidth(progressText) / 2, (int) roundY - 15, new Color(60, 60, 60, 120).getRGB());
    }
}
