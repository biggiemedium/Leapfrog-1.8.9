package dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.Manager.InputManager;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.NewGUI.Components.Panel;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.util.ArrayList;

public class ModulePanel extends Panel {

    private int x, y, width, height;
    private Color color;
    private Type type;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();
    private ArrayList<ModuleButton> modules;
    private InputManager.ScrollHandler scrollHandler = new InputManager.ScrollHandler(0);

    public ModulePanel(int x, int y, int width, int height, Color color, Type type) {
        super(x, y, width, height, color);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.type = type;
        this.modules = new ArrayList<>();
    }

    @Override
    public void render(int mouseX, int mouseY) {

    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
