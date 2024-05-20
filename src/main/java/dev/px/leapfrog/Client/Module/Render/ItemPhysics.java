package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@Module.ModuleInterface(name = "Item Physics", description = "Gives Items 3d physics", type = Type.Visual)
public class ItemPhysics extends Module {

    public ItemPhysics() {

    }

    public long Field1898;
    private double Field1899;
    private Random Field1900 = new Random();

    public int Method2280(ItemStack itemStack) {
        int n = 1;
        if (itemStack.stackSize > 48) {
            n = 5;
        } else if (itemStack.stackSize > 32) {
            n = 4;
        } else if (itemStack.stackSize > 16) {
            n = 3;
        } else if (itemStack.stackSize > 1) {
            n = 2;
        }
        return n;
    }

    public void itemPhysic(Entity entity, double d, double d2, double d3) {
        EntityItem entityItem;
        ItemStack itemStack;
        Field1899 = (double) (System.nanoTime() - Field1898) / 2500000.0 * (double) 1.0f; // 1.0f rotation speed
        if (!mc.inGameHasFocus) {
            Field1899 = 0.0;
        }
        int n = (itemStack = (entityItem = (EntityItem) entity).getEntityItem()) != null && itemStack.getItem() != null ? Item.getIdFromItem(itemStack.getItem()) + itemStack.getMetadata() : 187;
        Field1900.setSeed(n);
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel iBakedModel = mc.getRenderItem().getItemModelMesher().getItemModel(itemStack);
        boolean bl = iBakedModel.isGui3d();
        boolean bl2 = iBakedModel.isGui3d();
        int n2 = Method2280(itemStack);
        GlStateManager.translate((float) d, (float) d2, (float) d3);
        if (iBakedModel.isGui3d()) {
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
        }
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(entityItem.rotationYaw, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.0, 0.0, bl2 ? -0.08 : -0.04);
        if (bl2 || mc.getRenderManager().options != null) {
            double d4;
            if (bl2) {
                if (!entityItem.onGround) {
                    d4 = Field1899 * 2.0;
                    entityItem.rotationPitch = (float) ((double) entityItem.rotationPitch + d4);
                }
            } else if (!(Double.isNaN(entityItem.posX) || Double.isNaN(entityItem.posY) || Double.isNaN(entityItem.posZ) || entityItem.worldObj == null)) {
                if (entityItem.onGround) {
                    entityItem.rotationPitch = 0.0f;
                } else {
                    d4 = Field1899 * 2.0;
                    entityItem.rotationPitch = (float) ((double) entityItem.rotationPitch + d4);
                }
            }
            GlStateManager.rotate(entityItem.rotationPitch, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i = 0; i < n2; ++i) {
            GlStateManager.pushMatrix();
            if (bl) {
                if (i > 0) {
                    float f = (Field1900.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float f2 = (Field1900.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float f3 = (Field1900.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(f, f2, f3);
                }
                mc.getRenderItem().renderItem(itemStack, iBakedModel);
                GlStateManager.popMatrix();
                continue;
            }
            mc.getRenderItem().renderItem(itemStack, iBakedModel);
            GlStateManager.popMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.05375f);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        Field1898 = System.nanoTime();
    });


}
