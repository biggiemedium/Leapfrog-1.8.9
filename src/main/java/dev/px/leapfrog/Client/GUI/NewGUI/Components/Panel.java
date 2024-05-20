package dev.px.leapfrog.Client.GUI.NewGUI.Components;

import java.awt.*;
import java.io.IOException;

public class Panel {

    private int x, y, width, height;
    private Color color;

    public Panel(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void render(int mouseX, int mouseY) {

    }

    public void onClick(int mouseX, int mouseY, int button) throws IOException {

    }

    public void onRelease(int mouseX, int mouseY, int button) {

    }

    public void onType(char typedChar, int keyCode) throws IOException {

    }

    public void updatePosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
