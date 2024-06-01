package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;

public class PlayerSpoofLadderEvent extends Event {

    private boolean spoofing;

    public PlayerSpoofLadderEvent() {
        this.spoofing = false;
    }

    public boolean isSpoofing() {
        return spoofing;
    }

    public void setSpoofing(boolean spoofing) {
        this.spoofing = spoofing;
    }
}
