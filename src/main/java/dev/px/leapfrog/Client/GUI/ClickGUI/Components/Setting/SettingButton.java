package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting;

import dev.px.leapfrog.Client.GUI.ClickGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;

public class SettingButton<T> {

    private float x;
    private float y;
    private float width;
    private float height;
    private Setting<T> setting;
    private ModuleButton button;
    protected Color color;
    protected Minecraft mc = Minecraft.getMinecraft();

    public SettingButton(ModuleButton button, float x, float y, Setting<T> setting) {
        this.x = x;
        this.y = y;
        this.button = button;
        this.setting = setting;
        this.width = button.getWidth();
        this.height = 20; // Module button height
        this.color = button.getColor();
    }

    public SettingButton(ModuleButton button, float x, float y, float width, float height, Setting<T> setting) {
        this.x = x;
        this.y = y;
        this.button = button;
        this.setting = setting;
        this.width = width;
        this.height = height;
        this.color = button.getColor();
    }

    public void initGUI() {

    }

    public void draw(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {

    }

    public void mouseReleased(int mouseX, int mouseY) {

    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void scroll(double in) {}

    public void setWidth(float width) {
        this.width = width;
    }

    public Setting<T> getSetting() {
        return setting;
    }

    public void setSetting(Setting<T> setting) {
        this.setting = setting;
    }

    public ModuleButton getButton() {
        return button;
    }

    public void setButton(ModuleButton button) {
        this.button = button;
    }

}
