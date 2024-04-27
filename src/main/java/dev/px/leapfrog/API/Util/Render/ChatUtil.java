package dev.px.leapfrog.API.Util.Render;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static String prefix = ChatFormatting.GREEN + "[" + LeapFrog.NAME + "]" + ChatFormatting.RESET + " ";

    public static void sendMessageGlobal(String message) {
        mc.thePlayer.sendChatMessage(message);
    }
    public static void sendClientSideMessage(String message) {
        if(mc.thePlayer != null && mc.theWorld != null) {
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(prefix + message));
        }
    }

}
