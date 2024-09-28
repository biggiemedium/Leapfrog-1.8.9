package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.Client.GUI.HUD.Element;

import java.util.Objects;

@Element.ElementInterface(name = "Ping", description = "Response time to server")
public class PingElement extends Element {

    public PingElement() {
        super(1, 40);
    }

    @Override
    public void onRender(Render2DEvent event) {
        font.drawString(getPing(), getX() + font.getStringWidth("Ping: "), getY(), -1);
        font.drawStringWithClientColor("Ping: ", getX(), getY(), true);
        setWidth((float) font.getStringWidth("Ping: " + getPing()));
    }

    private String getPing() {
        int ping = 0;
        try {
            int responseTime = Objects.requireNonNull(mc.getNetHandler()).getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
            ping = responseTime;
        } catch (Exception responseTime) {}

        return String.valueOf(ping);
    }

}
