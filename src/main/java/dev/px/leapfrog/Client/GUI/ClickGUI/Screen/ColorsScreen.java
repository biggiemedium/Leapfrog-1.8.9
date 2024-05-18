package dev.px.leapfrog.Client.GUI.ClickGUI.Screen;

import dev.px.leapfrog.API.Util.Render.Color.AccentColor;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels.AccentColorsPanel;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels.ElementColorsPanel;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class ColorsScreen extends Screen {

    private AccentColorsPanel panel;
    private ElementColorsPanel elementPanel;

    public ColorsScreen(int x, int y, int width, int height, ClickGUI clickGUI) {
        super("Theme", x, y, width, height, clickGUI);
        this.panel = new AccentColorsPanel(getX() + 8, getY() + 90, getWidth() - 16, 65, new Color(26, 26, 26), this);
        this.elementPanel = new ElementColorsPanel(getX() + 8, getY() + 165, getWidth() - 16, 65, new Color(26, 26, 26), this);
    }

    @Override
    public void initGUI() {
        panel.initGUI();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        // Top bar
        RoundedShader.drawRound(getX() + 8, getY() + 8, getWidth() - 16, 22, 2, new Color(26, 26, 26));
        FontUtil.regular_bold26.drawString("Themes", getX() + 12, getY() + 13, -1);
        froggy.renderT(getX() + getWidth() - 38, getY() + 4, 30, 30);

        RoundedShader.drawRound(getX() + 8, getY() + 36, getWidth() - 16, 45, 4, new Color(26 ,26, 26));
        AccentColor color = LeapFrog.colorManager.getClientColor();
        Color color1 = ColorUtil.interpolateColorsBackAndForth(15, 0, color.getMainColor(), color.getAlternativeColor(), false);
        Color color2 = ColorUtil.interpolateColorsBackAndForth(15, 90, color.getMainColor(), color.getAlternativeColor(), false);
        Color color3 = ColorUtil.interpolateColorsBackAndForth(15, 180, color.getMainColor(), color.getAlternativeColor(), false);
        Color color4 = ColorUtil.interpolateColorsBackAndForth(15, 270, color.getMainColor(), color.getAlternativeColor(), false);

        RoundedShader.drawGradientRound((getX() + 8) + 4, (getY() + 36) + 4 , 35, 35, 4, color1, color2, color3, color4); // radius 10
        FontUtil.regular_bold24.drawString("Accent Color", (getX() + 8) + 46, (getY() + 36) + 6, -1);
        FontUtil.regular_bold20.drawString(color.getName(), (getX() + 8) + 46, (getY() + 36) + 10 + FontUtil.regular_bold26.getHeight(), -1);
        //FontUtil.regular_bold24.drawString("Opacity: " + Math.abs(-color.getOpactiy().getValue()), (getX() + 8) + 46, (getY() + 36) + 10 + FontUtil.regular_bold26.getHeight(), -1);

        panel.setX(getX() + 8);
        panel.setY(getY() + 90);
        panel.render(mouseX, mouseY);

        elementPanel.setX(getX() + 8);
        elementPanel.setY(getY() + 165);
        elementPanel.render(mouseX, mouseY);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        panel.onClick(mouseX, mouseY, button);
        elementPanel.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        panel.onRelease(mouseX, mouseY, button);
        elementPanel.onRelease(mouseX, mouseY, button);
    }
}
