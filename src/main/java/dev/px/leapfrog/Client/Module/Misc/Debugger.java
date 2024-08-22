package dev.px.leapfrog.Client.Module.Misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinS12PacketEntityVelocity;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import jdk.nashorn.internal.runtime.Debug;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.EnumChatFormatting;

@Module.ModuleInterface(name = "Debugger", type = Type.Misc, description = "Shows transaction ID's and more")
public class Debugger extends Module {

    public Debugger() {

    }

    private Setting<Boolean> transactions = create(new Setting<>("Transactions", true));
    private Setting<Boolean> windowID = create(new Setting<>("Window ID", false));
    private Setting<Boolean> velocity = create(new Setting<>("Velocity", true));
    private Setting<Boolean> s08Packet = create(new Setting<>("S08 Lagback", true));

    /**
     * https://www.youtube.com/watch?v=aE69eCmhRRM&t=68s
     * How to detect AntiCheat
     */
    @EventHandler
    private Listener<PacketReceiveEvent> receiveEventListener = new Listener<>(event -> {
        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
            if(event.getPacket() instanceof S32PacketConfirmTransaction) {
                S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction) event.getPacket();
                if(transactions.getValue()) {
                    ChatUtil.sendClientSideMessage("ID: " + packet.getActionNumber());
                }
                if(windowID.getValue()) {
                    ChatUtil.sendClientSideMessage("Window ID: " + packet.getWindowId());
                }
            } else if(event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

                if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
                    ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Velocity " + EnumChatFormatting.RESET +
                                    "Delta X: " + ((IMixinS12PacketEntityVelocity) packet).getMotionX() / 8000D +
                                    "Delta Y: " + ((IMixinS12PacketEntityVelocity) packet).getMotionY() / 8000D +
                                    "Delta Z: " + ((IMixinS12PacketEntityVelocity) packet).getMotionZ() / 8000D);
                }
        } else if(event.getPacket() instanceof S08PacketPlayerPosLook) {
                if(s08Packet.getValue()) {
                    ChatUtil.sendClientSideMessage(ChatFormatting.RED + "Warning: " + ChatFormatting.RESET + "You flagged the anti-cheat! (S08)");
                }
            }
    });
}
