package dev.px.leapfrog.Client.GUI.ClickGUI.Screen;

import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels.ModulePanel;

import java.awt.*;
import java.io.IOException;

public class ModuleScreen extends Screen {

    private ModulePanel panel;

    public ModuleScreen(int x, int y, int width, int height, ClickGUI clickGUI) {
        super("Mods", x, y, width, height, clickGUI);
        this.panel = new ModulePanel(getX() + 8, getY() + 36, getWidth() - 16, getHeight() - 45);
    }

    @Override
    public void initGUI() {
        panel.initGUI();
    }

    @Override
    public void render(int mouseX, int mouseY) {

        // Top bar
        RoundedShader.drawRound(getX() + 8, getY() + 8, getWidth() - 16, 22, 2, new Color(26, 26, 26));
        FontUtil.regular_bold26.drawString("Modules", getX() + 12, getY() + 13, -1);

        // Module buttons retigga
        //RoundedShader.drawRound(getX() + 8, getY() + 36, getWidth() - 16, getHeight() - 45, 4, new Color(26, 26, 26));
        panel.setX(getX() + 8);
        panel.setY(getY() + 36);
        panel.render(mouseX, mouseY);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        panel.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        panel.onRelease(mouseX, mouseY, button);
    }
}
