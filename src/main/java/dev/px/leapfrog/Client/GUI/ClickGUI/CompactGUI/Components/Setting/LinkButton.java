package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Setting;

import dev.px.leapfrog.API.Module.Setting.Link;
import dev.px.leapfrog.API.Util.Network.NetworkUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;

import java.io.IOException;

public class LinkButton extends SettingButton<Link> {

    private Setting<Link> setting;

    public LinkButton(ModuleButton button, float x, float y, Setting<Link> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);

        FontRenderer.sans16_bold.drawString(setting.getValue().getDisplayName(), getX() + (getWidth() - FontRenderer.sans16_bold.getStringWidth(setting.getValue().getDisplayName())) - 5, getY() + (getHeight() / 2 - 3), -1);
        FontRenderer.sans16_bold.drawString(setting.getName(), getX() + 5, getY() + (getHeight() / 2 - 3), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                NetworkUtil.openLink(this.setting.getValue().getUrlString());
            }
        }
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
