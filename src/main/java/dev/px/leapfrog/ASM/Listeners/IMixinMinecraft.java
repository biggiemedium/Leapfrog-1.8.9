package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface IMixinMinecraft {

    @Accessor("timer")
    Timer timer();

    @Accessor("rightClickDelayTimer")
    int getRightClickDelayTimer();

    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(int value);

    @Accessor("leftClickCounter")
    int getLeftClickCounter();

    @Accessor("leftClickCounter")
    void setLeftClickCounter(int counter);
}
