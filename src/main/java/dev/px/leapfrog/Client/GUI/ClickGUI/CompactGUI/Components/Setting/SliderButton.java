package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Setting;

import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class SliderButton<T extends Number> extends SettingButton<Number> {

    private Setting<Number> setting;
    private boolean dragging;
    private double renderWidth, renderWidth2;
    private SimpleAnimation slide = new SimpleAnimation(0.0f);

    public SliderButton(ModuleButton button, float x, float y, Setting<Number> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
        this.dragging = false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(setting.isNumberSetting()) {
            if(setting.getValue() instanceof Float) {
                double min = setting.getMin().floatValue();
                double max = setting.getMax().floatValue();
                double l = getWidth() / 4;
                renderWidth = (l) * (setting.getValue().doubleValue() - min) / (max - min);
                renderWidth2 = (l) * (setting.getMax().doubleValue() - min) / (max - min);
                slide.setAnimation((float) renderWidth, 14);
                double diff = Math.min(l, Math.max(0, mouseX - ((getX() + getWidth()) - (int) (renderWidth2 + 5)))); // fix 70 val
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
                double l = getWidth() / 4;
                renderWidth = (l) * (setting.getValue().doubleValue() - min) / (max - min);
                renderWidth2 = (l) * (setting.getMax().doubleValue() - min) / (max - min);
                slide.setAnimation((float) renderWidth, 14);
                double diff = Math.min(l, Math.max(0, mouseX - ((getX() + getWidth()) - (int) (renderWidth2 + 5)))); // fix 70 val
                if (dragging) {
                    if (diff == 0) {
                        setting.setValue((T) setting.getMin());
                    } else {
                        int newValue = (int) MathUtil.roundToPlace((diff / l) * (max - min) + min, 1);
                        setting.setValue((T) Integer.valueOf(newValue));
                    }
                }
            } else if(setting.getValue() instanceof Double) {
                double min = setting.getMin().doubleValue();
                double max = setting.getMax().doubleValue();
                double l = getWidth() / 4;
                renderWidth = (l) * (setting.getValue().doubleValue() - min) / (max - min);
                renderWidth2 = (l) * (setting.getMax().doubleValue() - min) / (max - min);
                slide.setAnimation((float) renderWidth, 14);
                double diff = Math.min(l, Math.max(0, mouseX - ((getX() + getWidth()) - (int) (renderWidth2 + 5)))); // fix 70 val
                if(dragging) {
                    if (diff == 0) {
                        setting.setValue((T) setting.getMin());
                    } else {
                        double newValue = MathUtil.roundToPlace(((diff / l) * (max - min) + min), 2);
                        setting.setValue((T) Double.valueOf((float) newValue));
                    }
                }
            }
        }

        RoundedShader.drawGradientCornerRL((float) (getX() + getWidth()) - (int) (renderWidth2 + 5), (float) getY() + 3, (float) (renderWidth2) + 2, 4, 2, LeapFrog.colorManager.getClientColor().getMainColor(), LeapFrog.colorManager.getClientColor().getAlternativeColor());
        RoundedShader.drawRound((float)  (getX() + getWidth()) - (int) (renderWidth2 + 5) + slide.getValue(), (float) getY() + 3 - 1.5f, 7, 7, 3.5f, new Color(255, 255, 255));

        FontRenderer.sans16_bold.drawString(setting.getName() + ": " + (setting.getValue() instanceof Integer ? Math.round(setting.getValue().doubleValue()) : MathUtil.roundToPlace(setting.getValue().doubleValue(), 1)), (int) getX() + 5,getY() + (getHeight() / 2 - 3), -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver((getX() + getWidth()) - (int) (renderWidth2 + 5), getY(), getWidth() / 4, getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                this.dragging = true;
            } else {
                dragging = false;
            }
        } else {
            dragging = false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
