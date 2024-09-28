package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Module;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

@Element.ElementInterface(name = "CPS", description = "Displays Clicks per second")
public class CPSElement extends Element {

    public CPSElement() {
        super(50, 20);
    }

    private boolean leftPressed;
    private boolean rightPressed;
    private long lastClickTime;
    private List<Long> leftClickList = new ArrayList<>();
    private List<Long> rightClickList = new ArrayList<>();

    @Override
    public void onRender(Render2DEvent event) {
        // Track left clicks
        if (Mouse.isButtonDown(0) != leftPressed) {
            lastClickTime = System.currentTimeMillis();
            leftPressed = Mouse.isButtonDown(0);
            if (leftPressed) {
                leftClickList.add(lastClickTime);
            }
        }

        // Track right clicks
        if (Mouse.isButtonDown(1) != rightPressed) {
            lastClickTime = System.currentTimeMillis();
            rightPressed = Mouse.isButtonDown(1);
            if (rightPressed) {
                rightClickList.add(lastClickTime);
            }
        }

        // Display CPS for both left and right clicks
        font.drawStringWithClientColor("CPS [" + getCPS(leftClickList) + ":" + getCPS(rightClickList) + "]", getX(), getY(), true);
        setWidth((float) font.getStringWidth("CPS [" + getCPS(leftClickList) + ":" + getCPS(rightClickList) + "]"));
    }

    private int getCPS(List<Long> clickList) {
        long currentTime = System.currentTimeMillis();
        clickList.removeIf(time -> time + 1000 < currentTime);
        return clickList.size();
    }
}
