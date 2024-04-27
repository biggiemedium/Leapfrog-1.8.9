package dev.px.leapfrog.API.Util.Render;

import dev.px.leapfrog.API.Util.Render.Blur.GaussianFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

public class RenderUtil {

    private static final HashMap<Integer, Integer> shadowCache = new HashMap<Integer, Integer>();


    public static void drawBlurredShadow(float x, float y, float width, float height, int blurRadius, Color color) {
        GL11.glPushMatrix();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
        width = width + blurRadius * 2;
        height = height + blurRadius * 2;
        x = x - blurRadius;
        y = y - blurRadius;

        float _X = x - 0.25f;
        float _Y = y + 0.25f;

        int identifier = (int) (width * height + width + color.hashCode() * blurRadius + blurRadius);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableBlend();

        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);

            GlStateManager.bindTexture(texId);
        } else {
            if (width <= 0) width = 1;
            if (height <= 0) height = 1;
            BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);

            Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int) (width - blurRadius * 2), (int) (height - blurRadius * 2));
            g.dispose();

            GaussianFilter op = new GaussianFilter(blurRadius);

            BufferedImage blurred = op.filter(original, null);


            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);

            shadowCache.put(identifier, texId);
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); // top left
        GL11.glVertex2f(_X, _Y);

        GL11.glTexCoord2f(0, 1); // bottom left
        GL11.glVertex2f(_X, _Y + height);

        GL11.glTexCoord2f(1, 1); // bottom right
        GL11.glVertex2f(_X + width, _Y + height);

        GL11.glTexCoord2f(1, 0); // top right
        GL11.glVertex2f(_X + width, _Y);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    public static void setDockIcon(String path) {
        if (net.minecraft.util.Util.getOSType() != net.minecraft.util.Util.EnumOS.OSX) return;  // Redundant check
        InputStream icon = RenderUtil.class.getResourceAsStream(path); // you could probably use resource location but users can then change it with a TexturePack
        if (icon != null) {
            try {
                Class<?> appClass = Class.forName("com.apple.eawt.Application");
                appClass.getMethod("setDockIconImage", Image.class).invoke(appClass.getMethod("getApplication").invoke(null), ImageIO.read(icon));
            } catch (Exception e) {
                System.err.println("[ MacOS Utils ] Error setting dock icon: " + e.getMessage());
            }
        } else {
            System.err.println("[ MacOS Utils ] Icon file could not be found");
        }
    }

    /**
     * @author GiantNuker
     * @since 01/29/2022
     */
    public static class ScissorStack {
        // scissor stack
        private final LinkedList<Rectangle> scissorStack = new LinkedList<>();

        /**
         * Applies scissor test to a specified section of the screen
         * @param x Lower x
         * @param y Lower y
         * @param width Upper x
         * @param height Upper y
         */
        public void pushScissor(int x, int y, int width, int height) {
            Rectangle scissor;

            // resolution
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

            // scaled
            int sx = x * resolution.getScaleFactor();
            int sy = (resolution.getScaledHeight() - (y + height)) * resolution.getScaleFactor();
            int sWidth = ((x + width) - x) * resolution.getScaleFactor();
            int sHeight = ((y + height) - y) * resolution.getScaleFactor();

            if (!scissorStack.isEmpty()) {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);

                Rectangle last = scissorStack.getLast();

                int nx = Math.max(sx, last.x);
                int ny = Math.max(sy, last.y);

                int hDiff = sx - nx;
                int nWidth = Math.min(Math.min(last.width + (last.x - sx), last.width), sWidth + hDiff);

                int diff = sy - ny;
                int nHeight = Math.min(Math.min(last.height + (last.y - sy), last.height), hDiff + diff);

                scissor = new Rectangle(nx, ny, nWidth, nHeight);
            }

            else {
                scissor = new Rectangle(sx, sy, sWidth, sHeight);
            }

            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            if (scissor.width > 0 && scissor.height > 0) {
                GL11.glScissor(scissor.x, scissor.y, scissor.width, scissor.height);
            }

            else {
                GL11.glScissor(0, 0, 0, 0);
            }

            scissorStack.add(scissor);
        }

        /**
         * Disables scissor test
         */
        public void popScissor() {
            if (!scissorStack.isEmpty()) {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                scissorStack.removeLast();

                if (!scissorStack.isEmpty()) {
                    Rectangle scissor = scissorStack.getLast();
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                    GL11.glScissor(scissor.x, scissor.y, scissor.width, scissor.height);
                }
            }
        }
    }

}
