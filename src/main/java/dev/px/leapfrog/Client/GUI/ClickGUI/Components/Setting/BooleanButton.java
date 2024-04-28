package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class BooleanButton extends SettingButton<Boolean> {

    private Setting<Boolean> setting;
    private boolean toggled;
    private Animation toggleAnimation = new Animation(300, false, Easing.LINEAR);

    public BooleanButton(ModuleButton button, float x, float y, Setting<Boolean> setting) {
        super(button, x, y, button.getWidth(), 15, setting);
        this.setting = setting;
        this.toggled = setting.getValue();
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            RoundedShader.drawRoundOutline(getX(), getY(), getWidth(), getHeight(), 4, 0.2f, new Color(0, 0, 0, 0), new Color(255, 255, 255, 150));
        }

        if(toggleAnimation.getAnimationFactor() > 0) {
            RoundedShader.drawGradientCornerRL(getX() + getWidth() - 20 - (5 * (float) toggleAnimation.getAnimationFactor()), getY() + 10 - (5 * (float) toggleAnimation.getAnimationFactor()), 15 * (float) toggleAnimation.getAnimationFactor(), 15 * (float) toggleAnimation.getAnimationFactor(), 2,
                    new Color(LeapFrog.colorManager.getClientColor().getMainColor().getRed(), LeapFrog.colorManager.getClientColor().getMainColor().getGreen(), LeapFrog.colorManager.getClientColor().getMainColor().getBlue()),
                    new Color(LeapFrog.colorManager.getClientColor().getAlternativeColor().getRed(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getGreen(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getBlue()));
        }
        FontUtil.regular16.drawString(setting.getName(), getX(), getY() + (getHeight() / 2 - 3), -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                this.setting.setValue(!this.setting.getValue());
            }
        }
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
