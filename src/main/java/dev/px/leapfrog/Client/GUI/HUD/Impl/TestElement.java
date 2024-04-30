package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Module;

@Element.ElementInterface(name = "TestElement", description = "For Testing purposes", visible = true)
public class TestElement extends Element {

    public TestElement() {
        super(1, 1, 25, 25);
    }

    @Override
    public void onRender(Render2DEvent event) {
        FontUtil.regular_bold20.drawString("Balls", getX(), getY(), -1);
    }


}
