package dev.px.leapfrog.ASM.Listeners;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.resources.SkinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(SkinManager.class)
public interface IMixinSkinManager {

    @Accessor("sessionService")
    MinecraftSessionService getSessionService();

    @Accessor("skinCacheDir")
    File getSkinCacheDir();
}
