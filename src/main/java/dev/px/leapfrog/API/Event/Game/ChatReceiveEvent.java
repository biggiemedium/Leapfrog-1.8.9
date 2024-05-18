package dev.px.leapfrog.API.Event.Game;

import dev.px.leapfrog.API.Event.Event;
import net.minecraft.util.IChatComponent;

public class ChatReceiveEvent extends Event {

    private IChatComponent message;

    public ChatReceiveEvent(IChatComponent message) {
        this.message = message;
    }

    public IChatComponent getMessage() {
        return message;
    }

    public void setMessage(IChatComponent message) {
        this.message = message;
    }
}
