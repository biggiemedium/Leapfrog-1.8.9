package dev.px.leapfrog.API.Event.Entity;

import dev.px.leapfrog.API.Event.Event;

public class EntityCollisionBorderSize extends Event {

    private float borderSize;

    public EntityCollisionBorderSize() {
        this.borderSize = 0.1f;
    }

    public float getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }
}
