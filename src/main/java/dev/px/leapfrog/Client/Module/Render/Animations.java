package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.RenderItemEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinItemRenderer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * @see net.minecraft.client.renderer.ItemRenderer
 */

@Module.ModuleInterface(name = "Animations", type = Type.Visual, description = "Changes minecrafts default animations")
public class Animations extends Module {

    public Animations() {

    }

    private Setting<Mode> mode = create(new Setting<>("Block Mode", Mode.Old));
    //private Setting<SwingMode> swingMode = create(new Setting<>("Swing Mode", SwingMode.None));


    @EventHandler
    private Listener<RenderItemEvent> renderItemEventListener = new Listener<>(event -> {

        if(event.isUseItem() && event.getEnumAction() == EnumAction.BLOCK) {
            switch (mode.getValue()) {
                case None:
                    this.transformFirstPersonItem(event.getAnimationProgress(), 0.0F);
                    this.blockTransformation();
                    break;
                case Old:
                    this.transformFirstPersonItem(event.isUseItem() ? 0.0F : event.getAnimationProgress(), event.getSwingProgress());
                    this.blockTransformation();
                    break;

                case Exhibition:
                    this.transformFirstPersonItem(event.getAnimationProgress() / 2, 0);
                    GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(event.getSwingProgress()) * 3.1415927F) * 40.0F / 2.0F, MathHelper.sin(MathHelper.sqrt_float(event.getSwingProgress()) * 3.1415927F) / 2.0F, -0.0F, 9.0F);
                    GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(event.getSwingProgress()) * 3.1415927F) * 30.0F, 1.0F, MathHelper.sin(MathHelper.sqrt_float(event.getSwingProgress()) * 3.1415927F) / 2.0F, -0.0F);
                    blockTransformation();
                    GL11.glTranslatef(-0.05F, this.mc.thePlayer.isSneaking() ? -0.2F : 0.0F, 0.1F);
                    break;
                case Spin:
                    this.transformFirstPersonItem(event.getAnimationProgress(), 0.0F);
                    GlStateManager.translate(0, 0.2F, -1);
                    GlStateManager.rotate(-59, -1, 0, 3);
                    // Don't make the /2 a float it causes the animation to break
                    GlStateManager.rotate(-(System.currentTimeMillis() / 2 % 360), 1, 0, 0.0F);
                    GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case Smooth:
                    this.transformFirstPersonItem(event.getAnimationProgress(), 0.0F);
                    float y = -MathHelper.sin(MathHelper.sqrt_float(event.getSwingProgress()) * (float) Math.PI) * 2.0F;
                    GlStateManager.translate(0.0F, y / 10.0F + 0.1F, 0.0F);
                    GlStateManager.rotate(y * 10.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(250, 0.2F, 1.0F, -0.6F);
                    GlStateManager.rotate(-10.0F, 1.0F, 0.5F, 1.0F);
                    GlStateManager.rotate(-y * 20.0F, 1.0F, 0.5F, 1.0F);
                    break;
            }

            event.cancel();
        }
        /*
        else if (!event.isUseItem() && (event.getItemToRender().getItem() instanceof ItemSword || event.getItemToRender().getItem() instanceof ItemAxe)) {
            switch (swingMode.getValue()) {
                case None:
                    this.func_178105_d(event.getSwingProgress());
                    this.transformFirstPersonItem(event.getAnimationProgress(), event.getSwingProgress());
                    break;
                case Smooth:
                    this.func_178105_d(event.getAnimationProgress());
                    break;
                case Punch:
                    this.transformFirstPersonItem(event.getAnimationProgress(), event.getSwingProgress());
                    this.func_178105_d(event.getSwingProgress());
                    break;
            }
            event.cancel();
        }

         */
    });

    private void transformFirstPersonItem(float p_transformFirstPersonItem_1_, float p_transformFirstPersonItem_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_transformFirstPersonItem_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(p_transformFirstPersonItem_2_ * p_transformFirstPersonItem_2_ * 3.1415927F);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(p_transformFirstPersonItem_2_) * 3.1415927F);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    public void blockTransformation() {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

    private void func_178105_d(float p_178105_1_) {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927F);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927F * 2.0F);
        float f2 = -0.2F * MathHelper.sin(p_178105_1_ * 3.1415927F);
        GlStateManager.translate(f, f1, f2);
    }

    private enum Mode {
        None,
        Old,
        Exhibition,
        Spin,
        Smooth
    }

    private enum SwingMode {
        None,
        Smooth,
        Punch
    }
}
