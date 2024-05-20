package dev.px.leapfrog.Client.GUI.NewGUI.Screens.ModuleScreen.Impl;

import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Manager.Other.InputManager;
import dev.px.leapfrog.Client.GUI.NewGUI.Components.Panel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ModulePanel extends Panel {

    private int x, y, width, height;
    private Color color;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();
    private InputManager.ScrollHandler scrollHandler = new InputManager.ScrollHandler(0);

    private ArrayList<ModuleButton> modules;

    public ModulePanel(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.modules = new ArrayList<>();

    }

    @Override
    public void render(int mouseX, int mouseY) {
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(getX(), getY(), getWidth(), 30, mouseX, mouseY)) {
            if(button == 0) {

            }
        }

    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
