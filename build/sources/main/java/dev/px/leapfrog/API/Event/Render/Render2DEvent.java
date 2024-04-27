package dev.px.leapfrog.API.Event.Render;

import dev.px.leapfrog.API.Event.Event;

public class Render2DEvent extends Event {

    private float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
