package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import tv.twitch.chat.Chat;

@Module.ModuleInterface(name = "Chat Modifications", description = "Changes look of chat gui", type = Type.Visual, drawn = true, toggled = true)
public class ChatModification extends Module {

    public ChatModification() {

    }

    public Setting<ChatLookMode> chatMode = create(new Setting<>("Chat Mode", ChatLookMode.Clear));
    public Setting<Double> chatSpeed = create(new Setting<>("Speed", 4D, 1D, 10D));

    public enum ChatLookMode {
        Default,
        Clear
    }

}
