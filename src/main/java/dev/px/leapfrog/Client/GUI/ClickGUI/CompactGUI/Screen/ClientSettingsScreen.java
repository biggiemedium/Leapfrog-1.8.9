package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Screen;

import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.ClickGUI;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Panels.SettingsPanel;

import java.awt.*;
import java.io.IOException;

public class ClientSettingsScreen extends Screen {

    private SettingsPanel panel;

    public ClientSettingsScreen(int x, int y, int width, int height, ClickGUI clickGUI) {
        super("Settings", x, y, width, height, clickGUI);
        panel = new SettingsPanel(getX() + 8, getY() + 36, getWidth() - 16, getHeight() - 45, this);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        // Top bar
        RoundedShader.drawRound(getX() + 8, getY() + 8, getWidth() - 16, 22, 2, new Color(26, 26, 26));
        FontUtil.regular_bold26.drawString("Settings", getX() + 12, getY() + 13, -1);
        froggy.renderT(getX() + getWidth() - 38, getY() + 4, 30, 30);

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
