package dev.px.leapfrog.Client.GUI.ClickGUI.Components.PreferenceButtons;

import dev.px.leapfrog.Client.Module.Setting;

import java.io.IOException;

public class PreferenceButton<T> {

    private int x, y, width, height;
    private Setting<T> setting;

    public PreferenceButton(int x, int y, int width, int height, Setting<T> setting) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.setting = setting;
    }

    public void draw(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {

    }

    public void mouseReleased(int mouseX, int mouseY) {

    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

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

    public Setting<T> getSetting() {
        return setting;
    }

    public void setSetting(Setting<T> setting) {
        this.setting = setting;
    }

}
