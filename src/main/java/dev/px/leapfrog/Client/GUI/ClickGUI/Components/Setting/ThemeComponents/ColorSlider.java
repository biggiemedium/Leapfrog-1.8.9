package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting.ThemeComponents;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class ColorSlider<T extends Number> implements Component {

    private Setting<T> setting;
    private int x, y, width, height;
    private boolean dragging;
    private double renderWidth, renderWidth2;
    private SimpleAnimation slide = new SimpleAnimation(0.0f);

    public ColorSlider(int x, int y, int width, int height, Setting<T> setting) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.setting = setting;
        this.dragging = false;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if(setting.isNumberSetting()) {
            if(setting.getValue() instanceof Float) {
                double min = setting.getMin().floatValue();
                double max = setting.getMax().floatValue();
                double l = getWidth();
                renderWidth = (l) * (setting.getValue().doubleValue() - min) / (max - min);
                renderWidth2 = (l) * (setting.getMax().doubleValue() - min) / (max - min);
                slide.setAnimation((float) renderWidth, 14);
                double diff = Math.min(l, Math.max(0, mouseX - (getX()))); // fix 70 val
                if(dragging) {
                    if (diff == 0) {
                        setting.setValue((T) setting.getMin());
                    } else {
                        double newValue = MathUtil.roundToPlace(((diff / l) * (max - min) + min), 2);
                        setting.setValue((T) Float.valueOf((float) newValue));
                    }
                }

            } else if(setting.getValue() instanceof Integer) {
                int min = setting.getMin().intValue();
                int max = setting.getMax().intValue();
                double l = getWidth();
                renderWidth = (l) * (setting.getValue().doubleValue() - min) / (max - min);
                renderWidth2 = (l) * (setting.getMax().doubleValue() - min) / (max - min);
                slide.setAnimation((float) renderWidth, 14);
                double diff = Math.min(l, Math.max(0, mouseX - (getX()))); // fix 70 val
                if (dragging) {
                    if (diff == 0) {
                        setting.setValue((T) setting.getMin());
                    } else {
                        int newValue = (int) MathUtil.roundToPlace((diff / l) * (max - min) + min, 1);
                        setting.setValue((T) Integer.valueOf(newValue));
                    }
                }
            }
        }

        RoundedShader.drawGradientCornerRL((float) getX() + 1, (float) getY(), (float) (renderWidth2) + 2, 4, 2, LeapFrog.colorManager.getClientColor().getMainColor(), LeapFrog.colorManager.getClientColor().getAlternativeColor());
        RoundedShader.drawRound((float) getX() + slide.getValue(), (float) getY() - 1.5f, 7, 7, 3.5f, new Color(200, 200, 200));

        FontUtil.regular16.drawString(setting.getName() + ": " + (setting.getValue() instanceof Integer ? Math.round(setting.getValue().doubleValue()) : MathUtil.roundToPlace(setting.getValue().doubleValue(), 1)), (int) getX() + 5,(int) getY() + 8, -1);
    }



    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                this.dragging = true;
            }
        }

    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        this.dragging = false;
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public Setting<T> getSetting() {
        return setting;
    }

    public void setSetting(Setting<T> setting) {
        this.setting = setting;
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
}
