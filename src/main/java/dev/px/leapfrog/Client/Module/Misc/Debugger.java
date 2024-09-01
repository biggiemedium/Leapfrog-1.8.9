package dev.px.leapfrog.Client.Module.Misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Module.Setting.Link;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinS12PacketEntityVelocity;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import jdk.nashorn.internal.runtime.Debug;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.EnumChatFormatting;

@Module.ModuleInterface(name = "Debugger", type = Type.Misc, description = "Shows transaction ID's and more")
public class Debugger extends Module {

    public Debugger() {

    }

    private Setting<Link> anticheatLink = create(new Setting<>("Anti-Cheat Setup", new Link("https://www.youtube.com/watch?v=aE69eCmhRRM&t=68s")));
    private Setting<Boolean> transactions = create(new Setting<>("Transactions", true));
    private Setting<Boolean> windowID = create(new Setting<>("Window ID", false));
    private Setting<Boolean> velocity = create(new Setting<>("Velocity", true));
    private Setting<Boolean> s08Packet = create(new Setting<>("S08 Lagback", true));

    private Setting<Boolean> c02Packet = create(new Setting<>("C02 Use Entity", false));
    private Setting<Boolean> c02PacketCancel = create(new Setting<>("Cancel C02", false, visible -> c02Packet.getValue()));

    private Setting<Boolean> c03Packet = create(new Setting<>("C03 Player", false));
    private Setting<Boolean> c03PacketCancel = create(new Setting<>("Cancel C03", false, visible -> c03Packet.getValue()));

    private Setting<Boolean> c04Packet = create(new Setting<>("C04 Position", false));
    private Setting<Boolean> c04PacketCancel = create(new Setting<>("Cancel C04", false, visible -> c04Packet.getValue()));

    private Setting<Boolean> c05Packet = create(new Setting<>("C05 Player Look", false));
    private Setting<Boolean> c05PacketCancel = create(new Setting<>("Cancel C05", false, visible -> c05Packet.getValue()));

    private Setting<Boolean> c06Packet = create(new Setting<>("C06 Player Pos Look", false));
    private Setting<Boolean> c06PacketCancel = create(new Setting<>("Cancel C06", false, visible -> c06Packet.getValue()));

    private Setting<Boolean> c07Packet = create(new Setting<>("C07 Player Digging", false));
    private Setting<Boolean> c07PacketCancel = create(new Setting<>("Cancel C07", false, visible -> c07Packet.getValue()));

    private Setting<Boolean> c08Packet = create(new Setting<>("C08 Block Placement", false));
    private Setting<Boolean> c08PacketCancel = create(new Setting<>("Cancel C08", false, visible -> c08Packet.getValue()));

    private Setting<Boolean> c09Packet = create(new Setting<>("C09 Held Item Change", false));
    private Setting<Boolean> c09PacketCancel = create(new Setting<>("Cancel C09", false, visible -> c09Packet.getValue()));

    private Setting<Boolean> c0APacket = create(new Setting<>("C0A Packet Animation", false));
    private Setting<Boolean> c0APacketCancel = create(new Setting<>("Cancel C0A", false, visible -> c0APacket.getValue()));

    private Setting<Boolean> c0BPacket = create(new Setting<>("C0B Entity Action", false));
    private Setting<Boolean> c0BPacketCancel = create(new Setting<>("Cancel C0B", false, visible -> c0BPacket.getValue()));

    private Setting<Boolean> c0CPacket = create(new Setting<>("C0C Client Settings", false));
    private Setting<Boolean> c0CPacketCancel = create(new Setting<>("Cancel C0C", false, visible -> c0CPacket.getValue()));

    private Setting<Boolean> c0DPacket = create(new Setting<>("C0D Tab Complete", false));
    private Setting<Boolean> c0DPacketCancel = create(new Setting<>("Cancel C0D", false, visible -> c0DPacket.getValue()));

    private Setting<Boolean> c0EPacket = create(new Setting<>("C0E Click Window", false));
    private Setting<Boolean> c0EPacketCancel = create(new Setting<>("Cancel C0E", false, visible -> c0EPacket.getValue()));

    private Setting<Boolean> c0FPacket = create(new Setting<>("C0F Creative Inventory Action", false));
    private Setting<Boolean> c0FPacketCancel = create(new Setting<>("Cancel C0F", false, visible -> c0FPacket.getValue()));

    private Setting<Boolean> c10Packet = create(new Setting<>("C10 Enchant Item", false));
    private Setting<Boolean> c10PacketCancel = create(new Setting<>("Cancel C10", false, visible -> c10Packet.getValue()));

    private Setting<Boolean> c11Packet = create(new Setting<>("C11 Item Use", false));
    private Setting<Boolean> c11PacketCancel = create(new Setting<>("Cancel C11", false, visible -> c11Packet.getValue()));

    private Setting<Boolean> c12Packet = create(new Setting<>("C12 Player Try Use Item", false));
    private Setting<Boolean> c12PacketCancel = create(new Setting<>("Cancel C12", false, visible -> c12Packet.getValue()));
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

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {
        if (event.getPacket() instanceof C02PacketUseEntity) {
            if (c02Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Chat " + ChatFormatting.RESET + "C02PacketSent");
            }
            if (c02PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C03PacketPlayer) {
            if (c03Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Player " + ChatFormatting.RESET + "C03PacketSent");
            }
            if (c03PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            if (c04Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Position " + ChatFormatting.RESET + "C04PacketSent");
            }
            if (c04PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            if (c05Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Block Dig " + ChatFormatting.RESET + "C05PacketSent");
            }
            if (c05PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            if (c06Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Block Place " + ChatFormatting.RESET + "C06PacketSent");
            }
            if (c06PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C07PacketPlayerDigging) {
            if (c07Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Held Item Change " + ChatFormatting.RESET + "C07PacketSent");
            }
            if (c07PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            if (c08Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Block Placement " + ChatFormatting.RESET + "C08PacketSent");
            }
            if (c08PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C09PacketHeldItemChange) {
            if (c09Packet.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Use Entity " + ChatFormatting.RESET + "C09PacketSent");
            }
            if (c09PacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C0APacketAnimation) {
            if (c0APacket.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Player Abilities " + ChatFormatting.RESET + "C0APacketSent");
            }
            if (c0APacketCancel.getValue()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof C0BPacketEntityAction) {
            if (c0BPacket.getValue()) {
                ChatUtil.sendClientSideMessage(EnumChatFormatting.LIGHT_PURPLE + " Combat " + ChatFormatting.RESET + "C0BPacketSent");
            }
            if (c0BPacketCancel.getValue()) {
                event.cancel();
            }
        }
    });
}
