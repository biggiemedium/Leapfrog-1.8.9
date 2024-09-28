package dev.px.leapfrog.ASM.Render;

import dev.px.leapfrog.API.Event.Render.RenderChestEvent;
import dev.px.leapfrog.API.Util.EventProcessor;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntityChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityChestRenderer.class)
public class MixinTileChestEntityRenderer {

}
