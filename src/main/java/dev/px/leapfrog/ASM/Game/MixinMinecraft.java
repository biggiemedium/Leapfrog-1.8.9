package dev.px.leapfrog.ASM.Game;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Input.ClickMouseEvent;
import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Gui.CustomMainMenu;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Gui.SplashScreen;
import dev.px.leapfrog.Client.Module.Render.FPSBooster;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    @Shadow public GuiScreen currentScreen;
    @Shadow public MovingObjectPosition objectMouseOver;
    @Shadow public GuiIngame ingameGUI;
    @Shadow public EffectRenderer effectRenderer;
    private ResourceLocation shaders = new ResourceLocation("minecraft", "shaders/post/blur" + ".json");

    Executor threadpool = Executors.newFixedThreadPool(1);

    // Multi threading
    @Redirect(method = "runTick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;updateTick()V", ordinal = 0))
    public void updateTick1(GuiIngame ingameGUI) {
        threadpool.execute(() -> {
            ingameGUI.updateTick();
        });
    }

    // Multi threading
    @Inject(method = "runTick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void updateTick2(CallbackInfo ci) {
        threadpool.execute(() -> {
            ingameGUI.updateTick();
        });
    }

    // Multi threading
    @Redirect(method = "runTick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/EffectRenderer;updateEffects()V"))
    public void redirectParticles(EffectRenderer instance) {
        this.threadpool.execute(() -> {
            this.effectRenderer.updateEffects();
        });
    }


    @Inject(method = "displayGuiScreen", at = @At("RETURN"), cancellable = true)
    public void displayGuiScreenInject(GuiScreen guiScreenIn, CallbackInfo ci) {

    }

    @Inject(method = "setInitialDisplayMode", at = @At("HEAD"), cancellable = true)
    public void onDisplay(CallbackInfo ci) {
        try {
            Display.setFullscreen(false);
        if (fullscreen) {
            if (LeapFrog.settingsManager != null && LeapFrog.settingsManager.WINDOWMODIFICATIONS.getValue()) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            } else {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                Minecraft.getMinecraft().displayWidth = Math.max(1, displaymode.getWidth());
                Minecraft.getMinecraft().displayHeight = Math.max(1, displaymode.getHeight());
            }
        } else {
            if (LeapFrog.settingsManager != null && LeapFrog.settingsManager.WINDOWMODIFICATIONS.getValue()) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
            } else {
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        }

        Display.setResizable(false);
        Display.setResizable(true);

        ci.cancel();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "toggleFullscreen", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/opengl/Display;setVSyncEnabled(Z)V", shift = At.Shift.AFTER))
    private void toggleFullscreen(CallbackInfo ci) throws LWJGLException {
        if (LeapFrog.settingsManager != null && LeapFrog.settingsManager.WINDOWMODIFICATIONS.getValue()) {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        } else {
            Display.setFullscreen(fullscreen);
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        }

        Display.setResizable(false);
        Display.setResizable(true);
    }

    @Inject(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;swingItem()V", shift = At.Shift.BEFORE), cancellable = true)
    public void onMouseClick(CallbackInfo ci) {
        if(this.objectMouseOver.entityHit != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            PlayerAttackEvent event = new PlayerAttackEvent(this.objectMouseOver.entityHit);
            LeapFrog.EVENT_BUS.post(event);
            event.setStage(Event.Stage.Pre);
            if(event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;swingItem()V", shift = At.Shift.BEFORE), cancellable = true)
    public void onMouseClickHead(CallbackInfo ci) {
        ClickMouseEvent event = new ClickMouseEvent();
        LeapFrog.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "runTick", at = @At("TAIL"))
    private void onTick(final CallbackInfo ci) {
        if(this.currentScreen instanceof GuiMainMenu) {
            displayGuiScreen(LeapFrog.menu); // ?
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

    @Inject(method = "shutdown", at = @At("HEAD"), cancellable = true)
    public void onShutdown(CallbackInfo ci) {
        LeapFrog.fileManager.save();
    }

    // Splash screen vvvvv

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/IReloadableResourceManager;registerReloadListener(Lnet/minecraft/client/resources/IResourceManagerReloadListener;)V", ordinal = 1))
    public void languageManagerStartUp(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/IReloadableResourceManager;registerReloadListener(Lnet/minecraft/client/resources/IResourceManagerReloadListener;)V", ordinal = 2))
    public void soundStartUp(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/IReloadableResourceManager;registerReloadListener(Lnet/minecraft/client/resources/IResourceManagerReloadListener;)V", ordinal = 3))
    public void fontRendererObjStartUp(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    public void mouseObjStartUp(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/ProgressManager$ProgressBar;step(Ljava/lang/String;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public void step1(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/ProgressManager$ProgressBar;step(Ljava/lang/String;)V", shift = At.Shift.BEFORE, ordinal = 1))
    public void step2(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/ProgressManager$ProgressBar;step(Ljava/lang/String;)V", shift = At.Shift.BEFORE, ordinal = 3))
    public void step3(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/ProgressManager$ProgressBar;step(Ljava/lang/String;)V", shift = At.Shift.BEFORE, ordinal = 4))
    public void step4(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/ProgressManager;pop(Lnet/minecraftforge/fml/common/ProgressManager$ProgressBar;)V", shift = At.Shift.BEFORE))
    public void pop1(CallbackInfo ci) {
        SplashScreen.continueCount();
    }

    @Overwrite
    public void drawSplashScreen(TextureManager textureManagerInstance) {
        SplashScreen.drawSplash(textureManagerInstance);
    }

}
