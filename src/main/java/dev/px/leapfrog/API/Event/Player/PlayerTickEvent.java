package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;

public class PlayerTickEvent extends Event {

    private Stage stage;

    public PlayerTickEvent(Stage stage) {
        this.stage = stage;
        setStage(stage);
    }

}
