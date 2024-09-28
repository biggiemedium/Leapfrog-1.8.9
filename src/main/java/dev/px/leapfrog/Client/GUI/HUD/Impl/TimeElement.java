package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Element.ElementInterface(name = "Time", description = "Time of day")
public class TimeElement extends Element {

    public TimeElement() {
        super(50, 1);
    }

    Setting<Boolean> tfHour = create(new Setting<>("24 Hr", false));


    @Override
    public void onRender(Render2DEvent event) {
        DateFormat dateFormat = tfHour.getValue() ? new SimpleDateFormat("kk:mm") : new SimpleDateFormat("hh:mm a");
        font.drawStringWithClientColor(dateFormat.format(new Date()), getX(), getY(), true);
        setWidth((float) font.getStringWidth(dateFormat.format(new Date())));
    }
}
