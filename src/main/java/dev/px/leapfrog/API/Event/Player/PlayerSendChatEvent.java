package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;

public class PlayerSendChatEvent extends Event {

    private String message;

    public PlayerSendChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
