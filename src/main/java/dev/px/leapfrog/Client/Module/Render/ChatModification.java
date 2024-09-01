package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Event.Game.ChatReceiveEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.EnumChatFormatting;
import tv.twitch.chat.Chat;

@Module.ModuleInterface(name = "Chat Modifications", description = "Changes look of chat gui", type = Type.Visual, drawn = true, toggled = true)
public class ChatModification extends Module {

    public ChatModification() {

    }

    public Setting<ChatLookMode> chatMode = create(new Setting<>("Chat Mode", ChatLookMode.Clear));
    public Setting<Double> chatSpeed = create(new Setting<>("Speed", 4D, 1D, 10D));
    public Setting<Boolean> duplicates = create(new Setting<>("Remove Duplicates", true));

    private String lastMessage = "";
    private int line, amount;

    @EventHandler
    private Listener<ChatReceiveEvent> receiveEventListener = new Listener<>(event -> {
        if(duplicates.getValue()) {
            if (!event.isCancelled()) {
                GuiNewChat guiNewChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                if (lastMessage.equals(event.getMessage().getUnformattedText())) {
                    guiNewChat.deleteChatLine(line);
                    amount++;
                    lastMessage = event.getMessage().getUnformattedText();
                    event.getMessage().appendText(EnumChatFormatting.GRAY + " (" + amount + ")");
                } else {
                    amount = 1;
                    lastMessage = event.getMessage().getUnformattedText();
                }

                line++;
                if (!event.isCancelled()) {
                    guiNewChat.printChatMessageWithOptionalDeletion(event.getMessage(), line);
                }

                if (line > 256) {
                    line = 0;
                }

                event.cancel();
            }
        }
    });

    public enum ChatLookMode {
        Default,
        Clear
    }

}
