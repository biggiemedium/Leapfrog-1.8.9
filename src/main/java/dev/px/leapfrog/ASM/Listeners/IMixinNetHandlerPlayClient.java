package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetHandlerPlayClient.class)
public interface IMixinNetHandlerPlayClient {

    @Accessor("doneLoadingTerrain")
    boolean isDoneLoadingTerrain();

}
