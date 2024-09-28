package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;

@Element.ElementInterface(name = "FPS", description = "Frames per second")
public class FPSElement extends Element {

    public FPSElement() {
        super(1, 35);
    }

    @Override
    public void onRender(Render2DEvent event) {
        font.drawString(getFPS(), getX() + font.getStringWidth("FPS: "), getY(), -1);
        font.drawStringWithClientColor("FPS: ", getX(), getY(), true);
        setWidth((float) font.getStringWidth("FPS: " + getFPS()));
    }

    private String getFPS() {
        return String.valueOf(Minecraft.getDebugFPS());
    }
}
