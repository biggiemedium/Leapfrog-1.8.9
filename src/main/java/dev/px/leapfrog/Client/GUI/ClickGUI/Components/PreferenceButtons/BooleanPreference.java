package dev.px.leapfrog.Client.GUI.ClickGUI.Components.PreferenceButtons;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Module.Setting;

import java.awt.*;
import java.io.IOException;

public class BooleanPreference extends PreferenceButton<Boolean> {

    private Setting<Boolean> setting;
    private Animation toggleAnimation;
    private SimpleAnimation hoverAnimation;

    public BooleanPreference(int x, int y, int width, int height, Setting<Boolean> setting) {
        super(x, y, width, height, setting);
        this.setting = setting;
        toggleAnimation = new Animation(200, setting.getValue(), Easing.LINEAR);
        hoverAnimation = new SimpleAnimation(0.0f);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        this.toggleAnimation.setState(this.setting.getValue());
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            RoundedShader.drawRoundOutline(getX(), getY(), getWidth(), getHeight(), 4, 0.5f, new Color(0, 0, 0, 0), new Color(255, 255, 255, 150));
        }

        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 2, new Color(35, 35, 35));
        if(!setting.getDescription().equalsIgnoreCase("")) {
            FontUtil.regular_bold18.drawString(setting.getName(), getX() + 4, getY() + 5, -1);
            FontUtil.regular12.drawString(setting.getDescription(), getX() + 4, getY() + (getHeight() - 3) - FontUtil.regular12.getHeight(), new Color(200, 200, 200).getRGB());
        } else {
            FontUtil.regular_bold20.drawString(setting.getName(), getX() + 4, getY() + 2, -1);
        }

        if(toggleAnimation.getAnimationFactor() > 0) {
           // RoundedShader.drawGradientCornerRL(getX() + getWidth() - 20 - (5 * (float) toggleAnimation.getAnimationFactor()), getY() + 10 - (5 * (float) toggleAnimation.getAnimationFactor()), 15 * (float) toggleAnimation.getAnimationFactor(), 15 * (float) toggleAnimation.getAnimationFactor(), 2,
           //         new Color(LeapFrog.colorManager.getClientColor().getMainColor().getRed(), LeapFrog.colorManager.getClientColor().getMainColor().getGreen(), LeapFrog.colorManager.getClientColor().getMainColor().getBlue()),
           //         new Color(LeapFrog.colorManager.getClientColor().getAlternativeColor().getRed(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getGreen(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getBlue()));
            RoundedShader.drawGradientRound(getX() + getWidth() - 20 - (5 * (float) toggleAnimation.getAnimationFactor()), getY() + 10 - (5 * (float) toggleAnimation.getAnimationFactor()), 15 * (float) toggleAnimation.getAnimationFactor(), 15 * (float) toggleAnimation.getAnimationFactor(), 2, ColorUtil.getClientColor(0, 255), ColorUtil.getClientColor(90, 255), ColorUtil.getClientColor(180, 255), ColorUtil.getClientColor(270, 255));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if(button == 0) {
            if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
                this.setting.setValue(!this.setting.getValue());
                this.toggleAnimation.setState(this.setting.getValue());
            }
        }
    }

    boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

}
