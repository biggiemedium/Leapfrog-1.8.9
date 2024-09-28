package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Element.ElementInterface(name = "Radar", description = "Birds eye view radar of surrounding area")
public class RadarElement extends Element {

    public RadarElement() {
        super(25, 1);
    }

    Setting<Color> enemyColor = create(new Setting<>("Enemy Color", new Color(255, 50, 50, 255)));
    Setting<Color> friendColor = create(new Setting<>("Friend Color", new Color(50, 50, 255, 255)));

    private CopyOnWriteArrayList<EntityPlayer> players = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Entity> entityList = new CopyOnWriteArrayList<>();

    @Override
    public void onRender(Render2DEvent event) {
        this.setWidth(80);
        this.setHeight(80);

        players.clear();
        players.addAll(mc.theWorld.playerEntities);
        double psx = getX();
        double psy = getY();
        int sizeRect = 80;
        float xOffset = (float) psx;
        float yOffset = (float) psy;
        double playerPosX = mc.thePlayer.posX;
        double playerPosZ = mc.thePlayer.posZ;


        RenderUtil.drawBlurredShadow(xOffset, getY(), sizeRect, sizeRect, 17, new Color(61, 57, 57, 150));
        GL11.glPushMatrix();
        RoundedShader.drawGradientRound(xOffset, getY(), sizeRect, sizeRect, 7f, ColorUtil.getClientColorInterpolation()[0], ColorUtil.getClientColorInterpolation()[1], ColorUtil.getClientColorInterpolation()[2], ColorUtil.getClientColorInterpolation()[3]);
        GL11.glPopMatrix();

        this.drawRect(xOffset + (sizeRect / 2F - 0.5), yOffset + 3.5, xOffset + (sizeRect / 2F + 0.2), (yOffset + sizeRect) - 3.5, ColorUtil.tripleColor(200, 255).getRGB());

        this.drawRect(xOffset + 3.5, yOffset + (sizeRect / 2F - 0.2), (xOffset + sizeRect) - 3.5, yOffset + (sizeRect / 2F + 0.5), ColorUtil.tripleColor(200, 255).getRGB());


        for (EntityPlayer entityPlayer : players) {
            if (entityPlayer == mc.thePlayer)
                continue;

            float partialTicks = event.getPartialTicks();
            float posX = (float) (entityPlayer.posX + (entityPlayer.posX - entityPlayer.lastTickPosX) * partialTicks - playerPosX) * 2;
            float posZ = (float) (entityPlayer.posZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * partialTicks - playerPosZ) * 2;
            float cos = (float) Math.cos(mc.thePlayer.rotationYaw * 0.017453292);
            float sin = (float) Math.sin(mc.thePlayer.rotationYaw * 0.017453292);
            float rotY = -(posZ * cos - posX * sin);
            float rotX = -(posX * cos + posZ * sin);
            if (rotY > sizeRect / 2F - 6) {
                rotY = sizeRect / 2F - 6;
            } else if (rotY < -(sizeRect / 2F - 8)) {
                rotY = -(sizeRect / 2F - 8);
            }
            if (rotX > sizeRect / 2F - 5) {
                rotX = sizeRect / 2F - 5;
            } else if (rotX < -(sizeRect / 2F - 5)) {
                rotX = -(sizeRect / 2F - 5);
            }

            GL11.glPushMatrix();
            RoundedShader.drawRound((xOffset + sizeRect / 2F + rotX) - 2, (yOffset + sizeRect / 2F + rotY) - 2, 4, 4, 4f, LeapFrog.socialManager.isFriend(entityPlayer.getName()) ? friendColor.getValue() : enemyColor.getValue());
            GL11.glPopMatrix();
        }
    }

    private void drawRect(double x, double y, double width, double height, int color) {
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
}
