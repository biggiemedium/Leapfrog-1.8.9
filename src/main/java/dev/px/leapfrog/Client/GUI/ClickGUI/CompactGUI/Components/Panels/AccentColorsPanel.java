package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Panels;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Color.AccentColor;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.ColorComponent;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Screen.ColorsScreen;
import dev.px.leapfrog.LeapFrog;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class AccentColorsPanel implements Component {

    private int x, y, width, height;
    private ArrayList<ColorComponent> components;
    private Color color;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();
    private double scrollX = 0;
    private ColorsScreen screen;

    public AccentColorsPanel(int x, int y, int width, int height, Color color, ColorsScreen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.components = new ArrayList<>();
        this.color = color;
        this.screen = screen;
    }

    public void initGUI() {
        for(AccentColor c : LeapFrog.colorManager.getColors()) {
            this.components.add(new ColorComponent(c, getX(), getY()));
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RoundedShader.drawRound(x, y, width, height, 4, color);
        int offsetX = 0;
        if(scrollX > 0) {
            scrollX = 0;
        } else if(scrollX > -offsetX) {
            scrollX = -offsetX;
        }

        stack.pushScissor(getX(), getY(), getWidth() * (int) screen.getClickGUI().getOpenAnimation().getAnimationFactor(), getHeight() * (int) screen.getClickGUI().getOpenAnimation().getAnimationFactor());
        FontUtil.regular_bold18.drawString("Accent Colors", getX() + 5, getY() + 4, -1);
        for(ColorComponent c : this.components) {
            if(c.getX() != getX() + 5 + offsetX + scrollX) {
                c.setX(getX() + 5 + offsetX + (int) scrollX);
            }

            if(c.getY() != getY() + 16) {
                c.setY(getY() + 16);
            }
            offsetX += c.getWidth() + 6;
            c.render(mouseX, mouseY);
        }
        stack.popScissor();

        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            scrollX += Mouse.getDWheel() * 0.2D;
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        int offsetX = 0;
        for(ColorComponent c : this.components) {
            if(isMouseOver(getX() + 6 + offsetX + (int) scrollX, getY() + 6, c.getWidth(), c.getHeight(), mouseX, mouseY)) {
                if(button == 0) {
                    LeapFrog.colorManager.setClientColor(c.getColor());
                }
            }
            offsetX += c.getWidth() + 6;
            c.onClick(mouseX, mouseY, button);
        }

    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
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
}
