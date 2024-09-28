package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;

public class PlayerReachEvent extends Event {

    private double range;
    private float expand;

    public PlayerReachEvent(double range, float expand) {
        this.range = range;
        this.expand = expand;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public float getExpand() {
        return expand;
    }

    public void setExpand(float expand) {
        this.expand = expand;
    }
}
