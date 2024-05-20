package dev.px.leapfrog.ASM.Game;

import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Gui.CustomMainMenu;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.SplashScreen;
import dev.px.leapfrog.API.Wrapper;
import dev.px.leapfrog.Client.Module.Render.FPSBooster;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public int displayWidth;

    @Shadow
    public int displayHeight;

    @Shadow
    public abstract void displayGuiScreen(GuiScreen p_displayGuiScreen_1_);

    @Shadow
    private boolean fullscreen;

    private ResourceLocation shaders = new ResourceLocation("minecraft", "shaders/post/blur" + ".json");

    @Inject(method = "runTick", at = @At("TAIL"))
    private void onTick(final CallbackInfo ci) {
        PlayerUpdateEvent event = new PlayerUpdateEvent();
        LeapFrog.EVENT_BUS.post(event);

        if(Wrapper.getMC().currentScreen instanceof GuiMainMenu) {
            displayGuiScreen(new CustomMainMenu());
        }

        if(LeapFrog.settingsManager != null) {
            if(LeapFrog.settingsManager.BLUR.getValue()) {
                if(Minecraft.getMinecraft().theWorld != null) {
                    if(OpenGlHelper.shadersSupported && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
                        if(Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
                            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
                        }

                        Minecraft.getMinecraft().entityRenderer.loadShader(shaders);

                    }
                    if(Minecraft.getMinecraft().currentScreen == null) {
                        Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                }
            } else {
                if(Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
                    Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }
        }
    }

    @Inject(method = "run", at = @At("HEAD"))
    private void init(CallbackInfo callbackInfo) {
        if (displayWidth < 1100) {
            displayWidth = 1100;
        }

        if (displayHeight < 630) {
            displayHeight = 630;
        }
    }

    @Inject(method = "Lnet/minecraft/client/Minecraft;getLimitFramerate()I", at = @At("HEAD"), cancellable = true)
    public void preGetLimitFramerate(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        try {
            if (LeapFrog.moduleManager.isModuleToggled(FPSBooster.class)) {
                if(LeapFrog.moduleManager.getModuleByClass(FPSBooster.class).displayFocus.getValue()) {
                    if (!Display.isActive()) {
                        if (!(Minecraft.getMinecraft().currentScreen instanceof CustomMainMenu)) {
                            callbackInfoReturnable.setReturnValue(5);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {}
    }

    @Inject(method = "createDisplay", at = @At("RETURN"), cancellable = true)
    public void createDisplayPost(CallbackInfo ci) {
        Display.setTitle(LeapFrog.NAME + " | " + "V" + LeapFrog.VERSION);
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    private void setGameIcon(CallbackInfo c) {
        if(Util.getOSType() == Util.EnumOS.OSX) {
            RenderUtil.setDockIcon("/assets/minecraft/Leapfrog/Images/Froggy-transformed.png");
            c.cancel();
        } else if(Util.getOSType() == Util.EnumOS.WINDOWS) {
            // I am too lazy to make 32x32 and 16x16 images of logo
            //RenderUtil.setIcon("/assets/minecraft/Leapfrog/Images/Froggy-transformed.png");
            //c.cancel();
        }
    }

    @Overwrite
    public void drawSplashScreen(TextureManager textureManagerInstance) {
        SplashScreen.drawSplash(textureManagerInstance);
    }

}
