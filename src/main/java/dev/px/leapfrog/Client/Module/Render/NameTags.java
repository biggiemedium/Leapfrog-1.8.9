package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Render.ESPUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
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
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Module.ModuleInterface(name = "NameTags", type = Type.Visual, description = "Custom Nametags")
public class NameTags extends Module {

    public NameTags() {

    }

    private Setting<Integer> range = create(new Setting<>("Range", 50, 5, 100));
    private Setting<Float> scale = create(new Setting<>("Scale", 0.5f, 0.0f, 1.5f));
    private Setting<Boolean> health = create(new Setting<>("Health", true));
    private Setting<Boolean> ping = create(new Setting<>("Ping", false));

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
            if(mc.thePlayer.getDistance(e.posX, e.posY, e.posZ) > range.getValue()) {
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

        for (Entity entity : entityPosition.keySet()) {
            Vector4f pos = entityPosition.get(entity);
            float x = pos.getX();
            float y = pos.getY();
            float right = pos.getZ();
            float bottom = pos.getW();
            if(entity instanceof EntityLivingBase) {
                EntityLivingBase e = (EntityLivingBase) entity;
                float middle = x + ((right - x) / 2);
                float textWidth = 0;
                Color healthColor = e.getHealth() / e.getMaxHealth() > .75 ? new Color(66, 246, 123) : e.getHealth() / e.getMaxHealth() > .5 ? new Color(228, 255, 105) : e.getHealth() / e.getMaxHealth() > .35 ? new Color(236, 100, 64) : new Color(255, 65, 68);

                double fontHeight;
                textWidth = (float) FontUtil.regular_bold20.getStringWidth(healthTag((EntityPlayer) e));
                middle -= (textWidth * scale.getValue()) / 2f;
                fontHeight = FontUtil.regular_bold20.getHeight() * scale.getValue();


                GL11.glPushMatrix();
                GL11.glTranslated(middle, y - (fontHeight + 2), 0);
                GL11.glScaled(scale.getValue(), scale.getValue(), 1);
                GL11.glTranslated(-middle, -(y - (fontHeight + 2)), 0);
                RoundedShader.drawRound(middle - 3, (float) (y - (fontHeight + 7)), (float) (textWidth + 6), (float) ((fontHeight / scale.getValue()) + 4), 4, new Color(35, 35, 35, 50));
                GlStateManager.bindTexture(0);
                RenderUtil.resetColor();
                FontUtil.regular_bold20.drawSmoothString(healthTag((EntityPlayer) e), middle, (float) (y - (fontHeight + 5)), new Color(255, 255, 255).getRGB());


                GL11.glPopMatrix();
            }
        }
    });

    private String healthTag(EntityPlayer e) {
        String s = "";
        s += e.getName() + " ";

        if(health.getValue()) {
            s += "[" + Math.floor(PlayerUtil.getHealth(e)) + "] ";
        }
        if(ping.getValue()) {
            try {
                int responseTime = Objects.requireNonNull(mc.getNetHandler()).getPlayerInfo(e.getUniqueID()).getResponseTime();
                s += responseTime + "ms ";
            } catch (Exception responseTime) {}
        }

        return s;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
