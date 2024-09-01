package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinC17PacketCustomPayload;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import io.netty.buffer.Unpooled;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@Module.ModuleInterface(name = "Client Spoofer", type = Type.Misc, description = "Spoofs if you are playing on a modified client")
public class ClientSpoofer extends Module {

    public ClientSpoofer() {

    }

    public Setting<ClientType> type = create(new Setting<>("Mode", ClientType.Vanilla));

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof C17PacketCustomPayload) {
            C17PacketCustomPayload packet = (C17PacketCustomPayload) event.getPacket();

            switch (type.getValue()) {
                case Forge: {
                    ((IMixinC17PacketCustomPayload) packet).setData(createPacketBuffer("FML", true));
                    break;
                }

                case Lunar: {
                    ((IMixinC17PacketCustomPayload) packet).setChannel("REGISTER");
                    ((IMixinC17PacketCustomPayload) packet).setData(createPacketBuffer("Lunar-Client", false));

                    break;
                }
            }
        }
    });

    private PacketBuffer createPacketBuffer(final String data, final boolean string) {
        if (string) {
            return new PacketBuffer(Unpooled.buffer()).writeString(data);
        } else {
            return new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
        }
    }

    @Override
    public void onEnable() {
        ChatUtil.sendClientSideMessage("Leave server and rejoin to use");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public enum ClientType {
        Vanilla,
        Forge,
        Lunar
    }
}

