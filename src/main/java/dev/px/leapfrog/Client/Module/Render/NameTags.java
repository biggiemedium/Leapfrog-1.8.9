package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.ASM.Listeners.IMixinRenderManager;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@Module.ModuleInterface(name = "NameTags", type = Type.Visual, description = "Custom Nametags")
public class NameTags extends Module {

    public NameTags() {

    }

    public Setting<Integer> range = create(new Setting<>("Range", 30, 5, 100));
    public Setting<Float> scale = create(new Setting<>("Scale", 0.5f, 0.1f, 2.0f));

    /**
     * @see net.minecraft.client.renderer.entity.RendererLivingEntity#renderName(EntityLivingBase, double, double, double)
     * Interpolation - mc.thePlayer.posX, (mc.thePlayer.posX - mc.thePlayer.lastPosX), partialTicks
     */
    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        for(EntityPlayer player : mc.theWorld.playerEntities) {
            if(player == null) {
                continue;
            }
            if(player.isDead || !(player instanceof EntityLivingBase)) {
                continue;
            }
            if(mc.thePlayer.getDistance(player.posX, player.posY, player.posZ) > range.getValue()) {
                continue;
            }

            this.renderTag(
                    RenderUtil.renderInterpolations(player, event.getPartialTicks())[0],
                    RenderUtil.renderInterpolations(player, event.getPartialTicks())[1],
                    RenderUtil.renderInterpolations(player, event.getPartialTicks())[2], player);
        }
    });

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void renderTag(double x, double y, double z, EntityPlayer entity) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + entity.height + (entity.isSneaking() ? 0.5F : 0.7F), (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) -((IMixinRenderManager) mc.getRenderManager()).getViewerPosY(), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) -((IMixinRenderManager) mc.getRenderManager()).getViewerPosX(), mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0F, 0.0F);
        GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
        GlStateManager.translate(0.0F, 9.374999F, 0.0F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = (int) FontRenderer.sans18.getStringWidth(displayName(entity)) / 2;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)(-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        FontRenderer.sans18.drawString(displayName(entity), -FontRenderer.sans18.getStringWidth(displayName(entity)) / 2, 0, 553648127);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private String displayName(EntityPlayer player) {
        String name = "";

        name += player.getName();
        name += " | ";
        name += PlayerUtil.getHealth(player);

        return name;
    }
}
