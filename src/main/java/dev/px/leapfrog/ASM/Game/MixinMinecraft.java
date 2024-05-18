package dev.px.leapfrog.ASM.Game;

import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Gui.CustomMainMenu;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.SplashScreen;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "displayGuiScreen", at = @At("RETURN"), cancellable = true)
    public void displayGuiScreenInject(GuiScreen guiScreenIn, CallbackInfo ci) {
        if(guiScreenIn instanceof GuiMainMenu) {
            displayGuiScreen(new CustomMainMenu());
        }
    }

    @Inject(method = "runTick", at = @At("TAIL"))
    private void onTick(final CallbackInfo ci) {
        PlayerUpdateEvent event = new PlayerUpdateEvent();
        LeapFrog.EVENT_BUS.post(event);
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
