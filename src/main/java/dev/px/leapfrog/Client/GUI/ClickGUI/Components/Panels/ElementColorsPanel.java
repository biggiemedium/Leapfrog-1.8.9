package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting.ThemeComponents.ColorSlider;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ColorsScreen;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class ElementColorsPanel implements Component {

    private int x, y, width, height;
    private Color color;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();
    private double scrollX = 0;
    private ColorsScreen screen;

    private ColorSlider<Float> opacity;
    private ColorSlider<Integer> radius;

    public ElementColorsPanel(int x, int y, int width, int height, Color color, ColorsScreen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.screen = screen;
        opacity = new ColorSlider<>(getX() + getWidth() - 90, getY() + 5, 70, 6, LeapFrog.colorManager.getOpacity());
        radius = new ColorSlider<>(getX() + getWidth() - 90, getY() - 25, 70, 6, LeapFrog.colorManager.getRadius());

    }

    @Override
    public void render(int mouseX, int mouseY) {
        RoundedShader.drawRound(x, y, width, height, 4, color);
        stack.pushScissor(getX(), getY(), getWidth() * (int) screen.getClickGUI().getCloseAnimation().getAnimationFactor(), getHeight() * (int) screen.getClickGUI().getCloseAnimation().getAnimationFactor());

        //RoundedShader.drawRound(getX() + getWidth() - 60, getY() + (getHeight() / 2), 55, 30, 4, new Color(30, 30, 30));
        FontUtil.regular_bold18.drawString("HUD Colors", getX() + 5, getY() + 4, -1);

        // Sliders
        opacity.setX(getX() + getWidth() - 90);
        opacity.setY(getY() + getHeight() - (opacity.getHeight() + 8));
        opacity.render(mouseX, mouseY);

        radius.setX(getX() + getWidth() - 90);
        radius.setY(getY() + getHeight() - (opacity.getHeight() * 4 + 8));
        radius.render(mouseX, mouseY);

        stack.popScissor();
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        opacity.onClick(mouseX, mouseY, button);
        radius.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        opacity.onRelease(mouseX, mouseY, button);
        radius.onRelease(mouseX, mouseY, button);
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RenderUtil.ScissorStack getStack() {
        return stack;
    }

    public void setStack(RenderUtil.ScissorStack stack) {
        this.stack = stack;
    }

    public double getScrollX() {
        return scrollX;
    }

    public void setScrollX(double scrollX) {
        this.scrollX = scrollX;
    }

    public ColorsScreen getScreen() {
        return screen;
    }

    public void setScreen(ColorsScreen screen) {
        this.screen = screen;
    }
}
