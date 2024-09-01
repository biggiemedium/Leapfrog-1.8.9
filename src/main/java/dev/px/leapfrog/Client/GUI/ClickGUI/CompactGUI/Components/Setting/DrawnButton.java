package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Setting;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrawnButton extends SettingButton<AtomicBoolean> {

    private Setting<AtomicBoolean> setting;
    private Animation toggleAnimation;
    float switchWidth = 13;
    private ModuleButton button;

    public DrawnButton(ModuleButton button, float x, float y, Setting<AtomicBoolean> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.button = button;
        this.setting = setting;
        this.toggleAnimation = new Animation(300, button.getModule().isDrawn(), Easing.LINEAR);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        this.toggleAnimation.setState(button.getModule().isDrawn());
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        toggleAnimation.setState(button.getModule().isDrawn());

        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);
        /*
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            RoundedShader.drawRoundOutline(getX(), getY(), getWidth(), getHeight(), 4, 0.2f, new Color(0, 0, 0, 0), new Color(255, 255, 255, 150));
        }
         */

        RoundedShader.drawGradientCornerRL(getX() + getWidth() - 30, getY() + 7, 25, 10, 4,
                ColorUtil.interpolateColorC(new Color(26, 26, 26), LeapFrog.colorManager.getClientColor().getMainColor(), (float) toggleAnimation.getAnimationFactor()),
                ColorUtil.interpolateColorC(new Color(26, 26, 26), LeapFrog.colorManager.getClientColor().getAlternativeColor(), (float) toggleAnimation.getAnimationFactor()));

        RoundedShader.drawRound((getX() + getWidth() - 27) + (switchWidth * (float) toggleAnimation.getAnimationFactor()), getY() + 8f, 8f, 8f, 4, new Color(255, 255, 255));
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            RenderUtil.drawBlurredShadow((getX() + getWidth() - 24) + (switchWidth * (float) toggleAnimation.getAnimationFactor()), getY() + 8f, 4f, 4f, 12, new Color(255, 255, 255, 220));
        }

        FontUtil.regular_bold16.drawString(setting.getName(), getX() + 5, getY() + (getHeight() / 2 - 3), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                this.button.getModule().setDrawn(!this.button.getModule().isDrawn());
                toggleAnimation.setState(this.button.getModule().isDrawn());
            }
        }
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

}
