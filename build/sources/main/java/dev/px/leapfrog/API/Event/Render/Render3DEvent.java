package dev.px.leapfrog.API.Event.Render;

import dev.px.leapfrog.API.Event.Event;

public class Render3DEvent extends Event {

    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
