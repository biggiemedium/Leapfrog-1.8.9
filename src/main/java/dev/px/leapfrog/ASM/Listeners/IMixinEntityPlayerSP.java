package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public interface IMixinEntityPlayerSP {

    @Accessor("serverSprintState")
    boolean getServerSprintState();

    @Accessor("serverSprintState")
    void setServerSprintState(boolean value);

    @Accessor("serverSneakState")
    boolean getServerSneakState();

    @Accessor("serverSneakState")
    void setServerSneakState(boolean value);


}
