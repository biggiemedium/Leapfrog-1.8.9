package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting;

import dev.px.leapfrog.API.Module.Bind;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class KeybindButton extends SettingButton<Bind> {

    private Setting<Bind> setting;
    private boolean listening;

    public KeybindButton(ModuleButton button, float x, float y, Setting<Bind> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
        this.listening = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if(listening) {
            if (keyCode == 211 || keyCode == Keyboard.KEY_BACK) {
                this.setting.setValue(new Bind(-1)); // unbind
            } else {
                this.setting.setValue(new Bind(keyCode));
            }
        }
        this.listening = false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);
        String state = "";

            if(this.setting.getValue().getBind() <= 0) {
                state = "None";
            } else {
                state = Keyboard.getKeyName(this.setting.getValue().getBind());
            }

        FontRenderer.sans16_bold.drawString(listening ? "Listening..." : state, getX() + (getWidth() - FontRenderer.sans16_bold.getStringWidth(state)) - 5, getY() + (getHeight() / 2 - 3), -1);
        FontRenderer.sans16_bold.drawString(setting.getName(), getX() + 5, getY() + (getHeight() / 2 - 3), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                this.listening = true;
            }
        } else {
            if (button == 0) {
                this.listening = false;
            }
        }
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

}
