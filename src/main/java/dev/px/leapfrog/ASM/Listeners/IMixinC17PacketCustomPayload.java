package dev.px.leapfrog.ASM.Listeners;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(C17PacketCustomPayload.class)
public interface IMixinC17PacketCustomPayload {

    @Accessor("channel")
    String getChannel();

    @Accessor("channel")
    void setChannel(String value);

    @Accessor("data")
    PacketBuffer getData();

    @Accessor("data")
    void setData(PacketBuffer value);

}
