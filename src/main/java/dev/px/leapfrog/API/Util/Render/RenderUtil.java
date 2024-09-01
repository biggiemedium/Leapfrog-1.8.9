package dev.px.leapfrog.API.Util.Render;

import dev.px.leapfrog.API.Util.Render.Blur.GaussianFilter;
import dev.px.leapfrog.API.Wrapper;
import dev.px.leapfrog.ASM.Listeners.IMixinRenderManager;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;

public class RenderUtil {

    private static final HashMap<Integer, Integer> shadowCache = new HashMap<Integer, Integer>();
    private static Minecraft mc = Wrapper.getMC();

    /**
     * @param framebuffer
     * @return framebuffer
     * https://www.google.com/search?q=what+is+framebuffer+opengl+java&sca_esv=3cad9cc1a9da8ec0&rlz=1C1GEWG_enCA1109CA1109&biw=1536&bih=695&tbm=vid&sxsrf=ADLYWIIy0g6I3mwcVcSJQ7zt8WmEgAVmcw%3A1724170277753&ei=JcDEZoraLfWkptQPu9iN2AY&ved=0ahUKEwjKgMDn-oOIAxV1kokEHTtsA2sQ4dUDCA0&uact=5&oq=what+is+framebuffer+opengl+java&gs_lp=Eg1nd3Mtd2l6LXZpZGVvIh93aGF0IGlzIGZyYW1lYnVmZmVyIG9wZW5nbCBqYXZhMgUQIRigATIFECEYoAEyBRAhGKABSNAGUKoBWOcFcAB4AJABAJgBhQGgAaQEqgEDNS4xuAEDyAEA-AEBmAIGoAKwBMICBhAAGBYYHsICCxAAGIAEGIYDGIoFwgIIEAAYgAQYogSYAwCIBgGSBwM0LjKgB4IX&sclient=gws-wiz-video#fpstate=ive&vld=cid:a30ab9f2,vid:lALvR4j6RCM,st:0
     */
    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if(framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, false);
        }
        return framebuffer;
    }

    public static void resetColor() {
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static double[] renderInterpolations(EntityPlayer player, float ticks) {
        double pX = player.lastTickPosX + (player.posX - player.lastTickPosX) * ticks - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosX();
        double pY = player.lastTickPosY + (player.posY - player.lastTickPosY) * ticks - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosY();
        double pZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ticks - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosZ();
        return new double[] { pX, pY, pZ };
    }

    public static void drawRect(double x, double y, double width, double height, int color) {
        double d4;

        if (x < width) {
            d4 = x;
            x = width;
            width = d4;
        }

        if (y < height) {
            d4 = y;
            y = height;
            height = d4;
        }

        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f1, f2, f3, f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, height, 0.0D).endVertex();
        worldrenderer.pos(width, height, 0.0D).endVertex();
        worldrenderer.pos(width, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float x, float y, float width, float height, Color color) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glColor4f(0, 0, 0, 1);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void render(int mode, Runnable render){
        GL11.glBegin(mode);
        render.run();
        GL11.glEnd();
    }

    public static void setup2DRendering(Runnable f) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        f.run();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GlStateManager.disableBlend();
    }

    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }

    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }


    public static void fakeCircleGlow(float posX, float posY, float radius, Color color, float maxAlpha) {
        setAlphaLimit(0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        setup2DRendering(() -> render(GL11.GL_TRIANGLE_FAN, () -> {
            color(color.getRGB(), maxAlpha);
            GL11.glVertex2d(posX, posY);
            color(color.getRGB(), 0);
            for (int i = 0; i <= 100; i++) {
                double angle = (i * .06283) + 3.1415;
                double x2 = Math.sin(angle) * radius;
                double y2 = Math.cos(angle) * radius;
                GL11.glVertex2d(posX + x2, posY + y2);
            }
        }));
        GL11.glShadeModel(GL11.GL_FLAT);
        setAlphaLimit(1);
    }

    // This will set the alpha limit to a specified value ranging from 0-1
    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, (float) (limit * .01));
    }


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

    public static void setIcon(String path) {
        if (net.minecraft.util.Util.getOSType() != Util.EnumOS.WINDOWS) return;  // Redundant check
        InputStream inputstream = RenderUtil.class.getResourceAsStream(path);
        try {
            if (inputstream != null) {
                Display.setIcon(new ByteBuffer[] { readImageToBuffer(inputstream) });
            }
        } catch (IOException ioexception) {
            LeapFrog.LOGGER.error((String)"Couldn\'t set icon", (Throwable)ioexception);}
        finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    public static void scaleStart(float x, float y, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);
        GL11.glTranslatef(-x, -y, 0);
    }

    public static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(imageStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

        for (int i : aint)
        {
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
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
