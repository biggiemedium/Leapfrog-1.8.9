package dev.px.leapfrog.API.Event;

import me.zero.alpine.fork.event.type.Cancellable;
import me.zero.alpine.fork.listener.Listenable;

public class Event extends Cancellable implements Listenable {

    private Stage stage;

    public Event() {
        this.stage = Stage.Pre;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public enum Stage {
        Pre,
        Post
    }

}
