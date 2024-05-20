package dev.px.leapfrog.ASM.GUI.Chat;

import dev.px.leapfrog.API.Event.Game.ChatReceiveEvent;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Render.ChatModification;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;
import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat extends Gui {
    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    public abstract int getLineCount();

    @Shadow
    private boolean isScrolled;

    @Shadow
    public abstract float getChatScale();

    private float percentComplete;
    private int newLines;
    private long prevMillis = System.currentTimeMillis();
    private float animationPercent;
    private int lineBeingDrawn;

    private void updatePercentage(long diff) {
        if (percentComplete < 1) {
            percentComplete += (4 / 1000) * (float) diff;
        }
        percentComplete = clamp(percentComplete, 0, 1);
    }

    @Inject(method = "printChatMessage", at = @At("TAIL"), cancellable = true)
    public void onChatReceive(IChatComponent chatComponent, CallbackInfo ci) {
        ChatReceiveEvent event = new ChatReceiveEvent(chatComponent);
        if(event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "drawChat", at = @At("HEAD"), cancellable = true)
    private void modifyChatRendering(CallbackInfo ci) {
        long current = System.currentTimeMillis();
        long diff = current - prevMillis;
        prevMillis = current;
        this.updatePercentage(diff);
        float t = percentComplete;
        animationPercent = clamp(1 - (--t) * t * t * t, 0, 1);
    }

    @Inject(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal = 0, shift = At.Shift.AFTER))
    private void translate(CallbackInfo ci) {
        float y = 0;
        if(LeapFrog.moduleManager.isModuleToggled(ChatModification.class) && LeapFrog.moduleManager.getModuleByClass(ChatModification.class).animations.getValue() && !this.isScrolled) {
            y += (9 - 9 * animationPercent) * this.getChatScale();
        }
        GlStateManager.translate(0, y, 0);
    }

    @ModifyArg(method = "drawChat", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, remap = false), index = 0)
    private int getLineBeingDrawn(int line) {
        lineBeingDrawn = line;
        return line;
    }

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"))
    private void printChatMessageWithOptionalDeletion(CallbackInfo ci) {
        percentComplete = 0;
    }

    @ModifyArg(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"), index = 3)
    private int modifyTextOpacity(int original) {
        if(LeapFrog.moduleManager.isModuleToggled(ChatModification.class) && LeapFrog.moduleManager.getModuleByClass(ChatModification.class).animations.getValue() && lineBeingDrawn <= newLines) {
            int opacity = (original >> 24) & 0xFF;
            opacity *= animationPercent;
            return (original & ~(0xFF << 24)) | (opacity << 24);
        } else {
            return original;
        }
    }

    @ModifyVariable(method = "setChatLine", at = @At("STORE"), ordinal = 0)
    private List<IChatComponent> setNewLines(List<IChatComponent> original) {
        newLines = original.size() - 1;
        return original;
    }

    @Inject(method = "getChatComponent", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiNewChat;scrollPos:I"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getChatComponent(int mouseX, int mouseY, CallbackInfoReturnable<IChatComponent> cir, ScaledResolution scaledresolution, int i, float f, int j, int k, int l) {
        int line = k / mc.fontRendererObj.FONT_HEIGHT;
        if (line >= getLineCount()) cir.setReturnValue(null);
    }

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void overrideChatBackgroundColour(int left, int top, int right, int bottom, int color) {
            if(LeapFrog.moduleManager.getModuleByClass(ChatModification.class).chatMode.getValue() == ChatModification.ChatLookMode.Clear) {
                Gui.drawRect(left, top, right, bottom, new Color(0, 0, 0, 0).getRGB());
            } else {
                Gui.drawRect(left, top, right, bottom, color);
            }
    }

    public float clamp(float number, float min, float max) {
        return number < min ? min : Math.min(number, max);
    }

    @Redirect(method = "deleteChatLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ChatLine;getChatLineID()I"))
    private int deleteChatLine(ChatLine instance) {
        if (instance == null) return -1;
        return instance.getChatLineID();
    }

}
