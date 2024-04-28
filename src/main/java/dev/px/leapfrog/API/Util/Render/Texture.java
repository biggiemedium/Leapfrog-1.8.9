package dev.px.leapfrog.API.Util.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Texture {

    private ResourceLocation location;

    public Texture(ResourceLocation location) {
        this.location = location;
    }

    public void renderT(float x, float y, float width, float height) {
        renderT(x, y, width, height, 0F, 0F, 1F, 1F);
    }

    public void renderT(float x, float y, float width, float height, float u, float v, float t, float s) {
        bindTexture();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x + width, y, 0F).tex(t, v).endVertex();
        renderer.pos(x, y, 0F).tex(u, v).endVertex();
        renderer.pos(x, y + height, 0F).tex(u, s).endVertex();
        renderer.pos(x, y + height, 0F).tex(u, s).endVertex();
        renderer.pos(x + width, y + height, 0F).tex(t, s).endVertex();
        renderer.pos(x + width, y, 0F).tex(t, v).endVertex();
        tessellator.draw();

    }

    private void bindTexture() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableTexture2D();
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public void setLocation(ResourceLocation location) {
        this.location = location;
    }


}
