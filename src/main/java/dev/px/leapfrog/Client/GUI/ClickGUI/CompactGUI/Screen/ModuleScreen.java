package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Screen;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.ClickGUI;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Panels.ModulePanel;

import java.awt.*;
import java.io.IOException;

public class ModuleScreen extends Screen {

    private ModulePanel panel;
    private Type type;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();

    public ModuleScreen(int x, int y, int width, int height, Type type, ClickGUI clickGUI) {
        super(type.name(), x, y, width, height, clickGUI);
        this.type = type;
        this.panel = new ModulePanel(getX() + 8, getY() + 36, getWidth() - 16, getHeight() - 45, type);
    }

    @Override
    public void initGUI() {
        panel.initGUI();
    }

    @Override
    public void render(int mouseX, int mouseY) {

        // Top bar
        RoundedShader.drawRound(getX() + 8, getY() + 8, getWidth() - 16, 22, 2, new Color(26, 26, 26));
        FontUtil.regular_bold26.drawString(type.name(), getX() + 12, getY() + 13, -1);
        froggy.renderT(getX() + getWidth() - 38, getY() + 4, 30, 30);

        // Module buttons retigga
        //RoundedShader.drawRound(getX() + 8, getY() + 36, getWidth() - 16, getHeight() - 45, 4, new Color(26, 26, 26));
        panel.setX(getX() + 8);
        panel.setY(getY() + 36);
        panel.setWidth(getWidth() - 16);
        panel.setHeight(getHeight() - 45);
        //stack.pushScissor(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
        panel.render(mouseX, mouseY);
        //stack.popScissor();
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        panel.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        panel.onRelease(mouseX, mouseY, button);
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {
        panel.onType(typedChar, keyCode);
    }
}
