package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.PreferenceButtons.BooleanPreference;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.PreferenceButtons.PreferenceButton;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ClientSettingsScreen;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ElementScreen;
import dev.px.leapfrog.Client.GUI.NewGUI.Components.Panel;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

// I wrote this GUI in the least fucking optimal way
public class ElementPanel implements Component {

    private int x, y, width, height;

    public ElementPanel(int x, int y, int width, int height, ElementScreen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    @Override
    public void render(int mouseX, int mouseY) {

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

    boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
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
