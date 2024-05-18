package dev.px.leapfrog.Client.GUI.ClickGUI.Components;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Color.AccentColor;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class ColorComponent implements Component {

    private AccentColor color;
    private int x, y, width, height;

    public ColorComponent(AccentColor color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = 60;
        this.height = 45;
    }

    @Override
    public void render(int mouseX, int mouseY) {

        Color color1 = ColorUtil.interpolateColorsBackAndForth(15, 0, color.getMainColor(), color.getAlternativeColor(), false);
        Color color2 = ColorUtil.interpolateColorsBackAndForth(15, 90, color.getMainColor(), color.getAlternativeColor(), false);
        Color color3 = ColorUtil.interpolateColorsBackAndForth(15, 180, color.getMainColor(), color.getAlternativeColor(), false);
        Color color4 = ColorUtil.interpolateColorsBackAndForth(15, 270, color.getMainColor(), color.getAlternativeColor(), false);

        RoundedShader.drawGradientRound(x, y , width, height, 4, color1, color2, color3, color4); // radius 10

        //RoundedShader.drawRound(x - 1, y + 1 + getHeight() - 13, width + 2, 14, 2, new Color(26, 26, 26));
        RenderUtil.drawRect(x - 1, y + 1 + getHeight() - 13, width + 2, 14, new Color(26, 26, 26));
        FontUtil.regular_bold16.drawString(this.color.getName(), x + 2, y + getHeight() - 10, -1);

        if(LeapFrog.colorManager.getClientColor().equals(this.color)) {
            RoundedShader.drawRoundOutline(x - 1, y - 1, width + 2, height + 2, 4, 0.5f, new Color(0, 0, 0, 0), new Color(255, 255, 255, 150));
        }
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

    public AccentColor getColor() {
        return color;
    }

    public void setColor(AccentColor color) {
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
