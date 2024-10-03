package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;

public class PlayerUpdateEvent extends Event {

    public PlayerUpdateEvent(Stage stage) {
        this.setStage(stage);
    }

}
