package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EnumButton extends SettingButton<Enum> {

    private Setting<Enum> setting;

    public EnumButton(ModuleButton button, float x, float y, Setting<Enum> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);

        FontRenderer.sans16_bold.drawString(setting.getValue().name(), getX() + (getWidth() - FontRenderer.sans16_bold.getStringWidth(setting.getValue().name())) - 5, getY() + (getHeight() / 2 - 3), -1);
        FontRenderer.sans16_bold.drawString(setting.getName(), getX() + 5, getY() + (getHeight() / 2 - 3), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            List<Enum> values = Arrays.asList(getValues().getEnumConstants());
            int in = values.indexOf(setting.getValue());
            if(button == 0) {
                setting.setValue(in + 1 < values.size() ? values.get(in + 1) : values.get(0));
            } else if(button == 1) {
            }
        }
    }

    private Class<Enum> getValues() {
        return setting.getValue().getDeclaringClass();
    }

    boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
