package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;

public class PlayerUpdateEvent extends Event {

    private Stage stage;

    public PlayerUpdateEvent(Stage stage) {
        this.stage = stage;
        setStage(stage);
    }

}
