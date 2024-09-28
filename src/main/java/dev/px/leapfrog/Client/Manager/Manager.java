package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;

public abstract class Manager {

    public abstract void onTick();

    public abstract void onThread();

    public abstract void onMotionUpdate(PlayerMotionEvent event);

}
