package dev.px.leapfrog.API.Event.Game;

import dev.px.leapfrog.API.Event.Event;

public class KeyPressEvent extends Event {

    private int keyCode;

    public KeyPressEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
