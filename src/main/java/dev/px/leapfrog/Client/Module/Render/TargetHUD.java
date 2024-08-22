package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Player.PlayerTeleportEvent;
import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.ADT.Pair;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.ContinualAnimation;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.ESPUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.API.Util.Render.StencilUtil;
import dev.px.leapfrog.Client.Module.Combat.KillAura;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module.ModuleInterface(name = "Target HUD", type = Type.Visual, description = "Additional information for PVP")
public class TargetHUD extends Module {

    public TargetHUD() {

    }

    private Setting<TrackingMode> trackingMode = create(new Setting<>("Tracking", TrackingMode.Right));
    private Setting<Boolean> killAuraOnly = create(new Setting<>("Kill Aura Target Only", true));
    //private Setting<Boolean> currentTarget = create(new Setting<>("Current Target", true, v -> killAuraOnly.getValue()));
    private Setting<Integer> range = create(new Setting<>("Range", 8, 1, 25));
    private Setting<Float> scale = create(new Setting<>("Scale", 1f, 0.1f, 2f));

    private Animation fadeAnimation = new Animation(300, false, Easing.LINEAR);
    private Vector4f vector;
    private EntityPlayer kaTarget = null;
    private ContinualAnimation healthbar = new ContinualAnimation();

    private Map<EntityPlayer, Animation> fadeAnimations = new HashMap<>();
    private Map<EntityPlayer, Vector4f> entityPositions = new HashMap<>();
    private List<EntityPlayer> targets = new ArrayList<>();


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.kaTarget = null;
        this.targets.clear();
        this.fadeAnimations.clear();
        super.onDisable();
    }

    @EventHandler
    private Listener<PlayerTeleportEvent> teleportEventListener = new Listener<>(event -> {
        this.fadeAnimations.clear();
        this.entityPositions.clear();
    });

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        if (killAuraOnly.getValue()) {
            kaTarget = null;
            if (LeapFrog.moduleManager.getModuleByClass(KillAura.class).getCurrentTarget() instanceof EntityPlayer) {
                kaTarget = (EntityPlayer) LeapFrog.moduleManager.getModuleByClass(KillAura.class).getCurrentTarget();
                if (kaTarget != null && kaTarget instanceof EntityPlayer) {
                    fadeAnimations.computeIfAbsent((EntityPlayer) kaTarget, k -> new Animation(300, false, Easing.LINEAR));
                    fadeAnimations.get(kaTarget).setState(true);
                    entityPositions.put((EntityPlayer) kaTarget, ESPUtil.getEntityPositionsOn2D(kaTarget));
                }
            }
        } else {
            targets.clear();
            entityPositions.clear();

            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer entityPlayer = (EntityPlayer) entity;
                    if (!killAuraOnly.getValue() || isKillAuraTarget(entityPlayer)) {
                        if(mc.thePlayer.getDistance(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ) > range.getValue()) {
                            continue;
                        }
                        targets.add(entityPlayer);
                        Vector4f vector = ESPUtil.getEntityPositionsOn2D(entity);
                        entityPositions.put(entityPlayer, vector);

                        fadeAnimations.computeIfAbsent(entityPlayer, k -> new Animation(300, false, Easing.LINEAR));
                        fadeAnimations.get(entityPlayer).setState(true);
                    }
                }
            }

            for (EntityPlayer player : fadeAnimations.keySet()) {
                if (!targets.contains(player)) {
                    fadeAnimations.get(player).setState(false);
                }
            }
        }
    });

    @EventHandler
    private Listener<Render2DEvent> render2DEventListener = new Listener<>(event -> {
        if (killAuraOnly.getValue()) {
            if (kaTarget != null) {
                renderTarget(kaTarget, entityPositions.get(kaTarget), fadeAnimations.get(kaTarget));
            }
        } else {
            for (EntityPlayer target : targets) {
                renderTarget(target, entityPositions.get(target), fadeAnimations.get(target));
            }
        }
    });

    private void renderTarget(EntityPlayer t, Vector4f vector, Animation fadeAnimation) {
        if (t != mc.thePlayer && vector != null) {
            float trackScale = 1;
            float x = 300, y = 300;
            float newWidth = (vector.getZ() - vector.getX()) * 1.4f;
            trackScale = Math.min(1, newWidth / Math.max(155, (int) FontRenderer.sans24.getStringWidth(t.getName()) + 75));

            Pair<Float, Float> coords = getTrackedCoords(t, vector);
            x = coords.getKey();
            y = coords.getValue();

            RenderUtil.scaleStart(x + 300 / 2f, y + 300 / 2f,
                    (float) (.5 + fadeAnimation.getAnimationFactor()) * trackScale * scale.getValue());
            float alpha = Math.min(1, (int) fadeAnimation.getAnimationFactor() * 2);

            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(GL11.GL_GREATER, (float) (0 * .01));
            float colorAlpha = .8f * alpha;
            RoundedShader.drawGradientRound(x, y, Math.max(155, (int) FontRenderer.sans22.getStringWidth(t.getName()) + 75), 50, 6,
                    ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[0], colorAlpha),
                    ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[1], colorAlpha),
                    ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[2], colorAlpha),
                    ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[3], colorAlpha));

            float size = 38;
            if (t instanceof AbstractClientPlayer) {
                StencilUtil.initStencil();
                this.circleNoSmoothRGB(x + 10, y + (50 / 2f) - (size / 2f), size, -1);
                StencilUtil.bindReadStencilBuffer(1);
                RenderUtil.resetColor();
                RenderUtil.setAlphaLimit(0);

                RenderUtil.color(ColorUtil.applyOpacity(-1, alpha));
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                mc.getTextureManager().bindTexture(((AbstractClientPlayer) t).getLocationSkin());
                Gui.drawScaledCustomSizeModalRect((int) x + 10, (int) y + (50 / 2) - (int) (size / 2f), (float) 8.0, (float) 8.0, 8, 8, Math.max(155, (int) FontRenderer.sans22.getStringWidth(t.getName()) + 75), 50, 64.0F, 64.0F);
                GlStateManager.disableBlend();
                StencilUtil.uninitStencilBuffer();
            } else {
                FontRenderer.sans24.drawCenteredStringWithShadow("?", x + 30, y + 25 - (int) FontRenderer.sans24.getHeight() / 2f, 255);
            }

            FontRenderer.sans20.drawCenteredString(t.getName(), x + 10 + size + ((Math.max(155, (int) FontRenderer.sans22.getStringWidth(t.getName()) + 75) - (10 + size)) / 2f), y + 10, ColorUtil.applyOpacity(-1, alpha));

            float healthPercentage = (PlayerUtil.getHealth(t)) / (t.getMaxHealth() + t.getAbsorptionAmount());

            float healthBarWidth = Math.max(155, (int) FontRenderer.sans22.getStringWidth(t.getName()) + 75) - (size + 30);

            float newHealthWidth = (healthBarWidth) * healthPercentage;
            healthbar.animate(newHealthWidth, 18);

            RoundedShader.drawRound(x + 20 + size, y + 25, healthBarWidth, 4, 2, ColorUtil.applyOpacity(Color.BLACK, .3f * alpha));
            RoundedShader.drawRound(x + 20 + size, y + 25, healthbar.getOutput(), 4, 2, ColorUtil.applyOpacity(Color.WHITE, alpha));
            DecimalFormat dc = new DecimalFormat("0");
            String healthText = dc.format(healthPercentage * 100) + "%";

            FontRenderer.sans16.drawCenteredString(healthText + " - " + Math.round(t.getDistanceToEntity(mc.thePlayer)) + "m",
                    x + 10 + size + ((Math.max(155, (int) FontRenderer.sans22.getStringWidth(t.getName()) + 75) - (10 + size)) / 2f), y + 35, ColorUtil.applyOpacity(-1, alpha));

            GL11.glPopMatrix();
        }
    }

    public void circleNoSmoothRGB(double x, double y, double radius, int color) {
        radius /= 2;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        RenderUtil.color(color);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (double i = 0; i <= 360; i++) {
            double angle = (i * (Math.PI * 2)) / 360;
            GL11.glVertex2d(x + (radius * Math.cos(angle)) + radius, y + (radius * Math.sin(angle)) + radius);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }


    private Pair<Float, Float> getTrackedCoords(EntityPlayer target, Vector4f vector) {
        ScaledResolution sr = new ScaledResolution(mc);
        float width = Math.max(155, (int) FontRenderer.sans22.getStringWidth(target.getName()) + 75), height = 50;
        float x = vector.getX(), y = vector.getY();
        float entityWidth = (vector.getZ() - vector.getX());
        float entityHeight = (vector.getW() - vector.getY());
        float middleX = x + entityWidth / 2f - width / 2f;
        float middleY = y + entityHeight / 2f - height / 2f;
        switch (trackingMode.getValue()) {
            case Middle:
                return Pair.of(middleX, middleY);
            case Right:
                return Pair.of(middleX, y - (height / 2f + height / 4f));
            case Left:
                return Pair.of(x - (width / 2f + width / 4f), middleY);
            default:
                return Pair.of(x + entityWidth - (width / 4f), middleY);
        }
    }

    private boolean isKillAuraTarget(EntityPlayer entityPlayer) {
        if(!LeapFrog.moduleManager.getModuleByClass(KillAura.class).isToggled() && killAuraOnly.getValue()) {
            return false;
        }
        return LeapFrog.moduleManager.getModuleByClass(KillAura.class).getCurrentTarget() == entityPlayer;
    }


    private enum TrackingMode{
        Middle,
        Left,
        Right
    }
}
