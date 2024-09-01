package dev.px.leapfrog.API.Event.Player;

import dev.px.leapfrog.API.Event.Event;

public class PlayerMouseOverEvent extends Event {

    private double reach;
    private float expand;

    public PlayerMouseOverEvent(double reach, float expand) {
        this.reach = reach;
        this.expand = expand;
    }

    public double getReach() {
        return reach;
    }

    public void setReach(double reach) {
        this.reach = reach;
    }

    public float getExpand() {
        return expand;
    }

    public void setExpand(float expand) {
        this.expand = expand;
    }
}
