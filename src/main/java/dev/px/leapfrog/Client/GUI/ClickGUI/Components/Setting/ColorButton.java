package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;

import java.awt.*;
import java.io.IOException;

public class ColorButton extends SettingButton<Color> {

    private Setting<Color> setting;

    public ColorButton(ModuleButton button, float x, float y, Setting<Color> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
    }
}
