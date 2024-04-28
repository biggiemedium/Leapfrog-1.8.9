package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinRenderManager;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import java.awt.*;

@Module.ModuleInterface(name = "China Hat", type = Type.Visual, description = "CCP")
public class ChinaHat extends Module {

    Setting<Integer> sides = create(new Setting<>("Sides", 50, 1, 50));
    Setting<Integer> stack = create(new Setting<>("stack", 150, 1, 200));
    Setting<Boolean> self = create(new Setting<>("Self", true));

    @EventHandler
    public Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        for(EntityPlayer player : mc.theWorld.playerEntities) {
            if(player == null) {
                continue;
            }
            if(player.isDead) {
                continue;
            }
            if(!self.getValue() && player == mc.thePlayer) {
                continue;
            }
            if(player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0 && self.getValue()) {
                continue;
            }
            drawChinaHat(player, event);
        }
    });

    private void drawChinaHat(EntityLivingBase entity, Render3DEvent evt) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)evt.getPartialTicks() - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosX();
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)evt.getPartialTicks() - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosY();
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)evt.getPartialTicks() - ((IMixinRenderManager) mc.getRenderManager()).getRenderPosZ();
        int side = (int) sides.getValue();
        int stack = (int) this.stack.getValue();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + (mc.thePlayer.isSneaking() ? 2.0 : 2.2), z);

        GL11.glRotatef(-entity.width, 0.0f, 1.0f, 0.0f);

        Color col = ColorUtil.interpolateColorC(LeapFrog.colorManager.getClientColor().getMainColor(), LeapFrog.colorManager.getClientColor().getAlternativeColor(), 0.5f);
        GL11.glColor4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 0.2f);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(1.0f);

        Cylinder c = new Cylinder();
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        c.setDrawStyle(100011);
        c.draw(0.0f, 0.8f, 0.4f, side, stack);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glPopMatrix();
    }
}
