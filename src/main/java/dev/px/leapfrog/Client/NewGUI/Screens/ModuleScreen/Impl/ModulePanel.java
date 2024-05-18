package dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Manager.InputManager;
import dev.px.leapfrog.Client.NewGUI.Components.Panel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ModulePanel extends Panel {

    private int x, y, width, height;
    private Color color;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();
    private ArrayList<ModuleButton> modules;
    private InputManager.ScrollHandler scrollHandler = new InputManager.ScrollHandler(0);

    private Type currentType = Type.Combat;

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

        int off = 0;
        for(Type t : Type.values()) {
            FontUtil.regular_bold18.drawString(t.name(), getX() + 5 + off, getY() + 5, -1);
            off += 30;
        }
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
