package dev.px.leapfrog.Client.GUI.ClickGUI.Screen;

import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;

import java.awt.*;

public class ClientSettingsScreen extends Screen {

    public ClientSettingsScreen(int x, int y, int width, int height) {
        super("Settings", x, y, width, height);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        // Top bar
        RoundedShader.drawRound(getX() + 8, getY() + 8, getWidth() - 16, 22, 2, new Color(26, 26, 26));
        FontUtil.regular_bold26.drawString("Settings", getX() + 12, getY() + 13, -1);

    }

}
