package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.CircleShader;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Module;

import java.awt.*;

@Element.ElementInterface(name = "TestElement", description = "For Testing purposes", visible = true)
public class TestElement extends Element {

    public TestElement() {
        super(1, 60);
    }


    @Override
    public void onRender(Render2DEvent event) {
        this.drawBackground(getX(), getY(), getWidth(), getHeight());
        font.drawString("Balls", getX(), getY(), getFontColor());
        CircleShader.drawCircle(getX(), getY(), 40, 70, 1, new Color(255, 255, 255), 0.5f);
    }

}
