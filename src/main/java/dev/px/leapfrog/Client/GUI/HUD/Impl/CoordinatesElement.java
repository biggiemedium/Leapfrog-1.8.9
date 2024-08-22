package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Setting;

@Element.ElementInterface(name = "Coordinates", description = "Displays XYZ Coordinates", visible = true)
public class CoordinatesElement extends Element {

    public CoordinatesElement() {
        super(1, 50);
    }

    private Setting<Boolean> round = create(new Setting("Round", true));

    @Override
    public void onRender(Render2DEvent event) {
        String x = String.valueOf(MathUtil.round(mc.thePlayer.posX, round.getValue() ? 0 : 1));
        String y = String.valueOf(MathUtil.round(mc.thePlayer.posY, round.getValue() ? 0 : 1));
        String z = String.valueOf(MathUtil.round(mc.thePlayer.posZ, round.getValue() ? 0 : 1));
       font.drawString(" " + x + " " + y + " " + z, getX() + font.getStringWidth("XYZ "), getY(), -1);
       font.drawStringWithClientColor("XYZ: ", getX(), getY(), true);

       setWidth((float) font.getStringWidth("XYZ: " + " " + x + " " + y + " " + z));
    }

}
