package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Module;
import net.minecraft.util.MathHelper;

import java.text.DecimalFormat;

@Element.ElementInterface(name = "Speed", description = "Shows current player speed", visible = false)
public class SpeedElement extends Element {

    public SpeedElement() {
        super(1, 25);
    }

    @Override
    public void onRender(Render2DEvent event) {
        font.drawString(" " + getSpeed(), getX() + font.getStringWidth("BP/S: "), getY(), -1);
        font.drawStringWithClientColor("KM/H: ", getX(), getY(), true);
        setWidth((float) font.getStringWidth("KM/H: " + getSpeed()));
    }

    private float getSpeed() {
        double deltaX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double deltaZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;

        float distance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

        double floor = Math.floor(( distance/1000.0f ) / ( 0.05f/3600.0f ));

        String formatter = String.valueOf(floor);

        if (!formatter.contains("."))
            formatter += ".0";

        return Float.valueOf(formatter);
    }
}
