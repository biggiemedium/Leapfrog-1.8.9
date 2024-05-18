package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.PreferenceButtons.BooleanPreference;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.PreferenceButtons.PreferenceButton;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ClientSettingsScreen;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsPanel implements Component {

    private int x, y, with, height;
    private int scrollY;
    private ClientSettingsScreen screen;
    private ArrayList<PreferenceButton> preferenceButtons;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();

    public SettingsPanel(int x, int y, int with, int height, ClientSettingsScreen screen) {
        this.x = x;
        this.y = y;
        this.with = with;
        this.height = height;
        this.screen = screen;
        this.preferenceButtons = new ArrayList<>();

        int offset = 0;
        for(Setting s : LeapFrog.settingsManager.getPreferences()) {
            if (s != null) {
                if (s.getValue() instanceof Boolean) {
                    preferenceButtons.add(new BooleanPreference(getX() + 4, getY() + 5 + offset, getWith() - 16, 25, s));
                    offset += 25;
                }
            }
        }

    }

    @Override
    public void render(int mouseX, int mouseY) {
        int offset = 0;
        RoundedShader.drawRound(getX(), getY(), getWith(), getHeight(), 4, new Color(26, 26, 26));
        if(isMouseOver(getX(), getY(), getWith(), getHeight(), mouseX, mouseY)) {
            this.scrollY += Mouse.getDWheel() * 0.02;
        }

        stack.pushScissor(getX(), getY(), getWith(), getHeight());
        for (PreferenceButton b : this.preferenceButtons) {
            if(b.getX() != getX() + 4) {
                b.setX(getX() + 4);
            }
            if(b.getY() != getY() + 5 + offset + (int) scrollY) {
                b.setY(getY() + 5 + offset + (int) scrollY);
            }

            if(b.getSetting().isVisible()) {
                b.draw(mouseX, mouseY);
                offset += b.getHeight() + 4;
            }
        }
        stack.popScissor();

        if(isMouseOver(getX(), getY(), getWith(), getHeight(), mouseX, mouseY)) {
            this.scrollY += Mouse.getDWheel() * 0.02;
        }

        if(scrollY > 0) {
            scrollY = 0;
        } else if(scrollY < -offset + 10) {
            scrollY = (-offset + 10);
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        for (PreferenceButton b : this.preferenceButtons) {
            b.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        for (PreferenceButton b : this.preferenceButtons) {
            b.mouseReleased(mouseX, mouseY);
        }
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

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ClientSettingsScreen getScreen() {
        return screen;
    }

    public void setScreen(ClientSettingsScreen screen) {
        this.screen = screen;
    }
}
