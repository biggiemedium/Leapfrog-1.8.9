package dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Module.Module;

import java.awt.*;
import java.io.IOException;

public class ModuleButton implements Component {

    private Module module;
    public Color color;
    private int x, y, width, height, extendedHeight;

    public ModuleButton(Module module, int x, int y, int width, int height, Color color) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.extendedHeight = 0;
        this.color = color;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {

    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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

    public int getExtendedHeight() {
        return extendedHeight;
    }

    public void setExtendedHeight(int extendedHeight) {
        this.extendedHeight = extendedHeight;
    }
}
