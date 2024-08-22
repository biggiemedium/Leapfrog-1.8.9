package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface IMixinEntity {

    @Accessor("isInWeb")
    boolean isInWeb();

    @Accessor("isInWeb")
    void setInWeb(boolean value);

}
