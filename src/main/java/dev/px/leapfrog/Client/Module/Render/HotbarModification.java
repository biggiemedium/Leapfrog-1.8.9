package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @see dev.px.leapfrog.ASM.GUI.Game.MixinGuiInGame
 */
@Module.ModuleInterface(name = "Hotbar Modifications", description = "Hotbar Animations/look", type = Type.Visual, drawn = true, toggled = true)
public class HotbarModification extends Module {

    public HotbarModification() {

    }

    public Setting<Boolean> animation = create(new Setting<>("Animation", true));
    public Setting<Integer> speed = create(new Setting<>("Animation Speed", 15, 10, 30, v -> animation.getValue()));
    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.Default));

    public enum Mode {
        Default,
        Clear,
        Round
    }
}
