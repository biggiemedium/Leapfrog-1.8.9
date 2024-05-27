package dev.px.leapfrog.ASM.GUI.Chat;

import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.GLUtils;
import dev.px.leapfrog.Client.Module.Render.ChatModification;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {

    private SimpleAnimation animation = new SimpleAnimation(0.0f);

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void drawScreenPre(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {

        if(LeapFrog.moduleManager.getModuleByClass(ChatModification.class).isToggled()) {
            animation.setAnimation(30, 20);
            GLUtils.startTranslate(0, 29 - (int) animation.getValue());
            // Renderutil.drawOutlineRect(2, (float) this.height - (14 * animation.getValue()), (float) this.width - 2, (float) this.height - 2, 1, Integer.MIN_VALUE);
        }
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreenPost(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if(LeapFrog.moduleManager.getModuleByClass(ChatModification.class).isToggled()) {
            GLUtils.stopTranslate();
        }
    }

}
