package dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.NewGUI.Components.Panel;

import java.awt.*;
import java.io.IOException;

public class CategoryPanel extends Panel {

    private int x, y, width, height;
    private Color color;
    private Type currentType;

    public CategoryPanel(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        currentType = Type.Combat;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);
        FontRenderer.sans24_bold.drawString(currentType.name(), getX() + ((getWidth() / 2) - (FontRenderer.sans24_bold.getStringWidth(currentType.name()) / 2)), getY() + (getHeight() / 2) - 4, -1);

        FontRenderer.sans24_bold.drawString("<", getX() + 5, getY() + (getHeight() / 2) - 4, new Color(190, 190, 190).getRGB());
        FontRenderer.sans24_bold.drawString(">", getX() + (getWidth() - 10), getY() + (getHeight() / 2) - 4, new Color(190, 190, 190).getRGB());
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {

        // back button
        if(isMouseOver(getX(), getY(), 25, getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                currentType = getNextType(currentType, false);
            }

        }

        // next button
        if(isMouseOver(getX() + (getWidth() - 25), getY(), 25, getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                currentType = getNextType(currentType, true);
            }
        }
    }

    public Type getNextType(Type e, boolean forward) {
        int index = e.ordinal();
        int nextIndex;
        Type[] cars = Type.values(); // Straight from stack overflow >:)
        if (forward) {
            nextIndex = (index + 1) % cars.length;
        } else {
            nextIndex = (index - 1 + cars.length) % cars.length;
        }
        return cars[nextIndex];
    }

    private Class<Type> getValues() {
        return currentType.getDeclaringClass();
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public Type getCurrentType() {
        return currentType;
    }

    public void setCurrentType(Type currentType) {
        this.currentType = currentType;
    }
}
