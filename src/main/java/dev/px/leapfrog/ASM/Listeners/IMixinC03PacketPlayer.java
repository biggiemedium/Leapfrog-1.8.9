package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(C03PacketPlayer.class)
public interface IMixinC03PacketPlayer {

    @Accessor("onGround")
    boolean isOnGround();

    @Accessor("onGround")
    void setOnGround(boolean value);

    @Accessor("x")
    double getX();

    @Accessor("x")
    void setX(double value);

    @Accessor("y")
    double getY();

    @Accessor("y")
    void setY(double value);

    @Accessor("z")
    double getZ();

    @Accessor("z")
    void setZ(double value);


}
