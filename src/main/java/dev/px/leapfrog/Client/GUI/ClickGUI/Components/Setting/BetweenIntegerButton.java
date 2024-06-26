package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting;

import dev.px.leapfrog.API.Module.BetweenInteger;
import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class BetweenIntegerButton <T extends Number & Comparable<T>> extends SettingButton<BetweenInteger<T>> {

    private boolean dragMin;
    private boolean dragMax;
    private double renderWidthMin, renderWidthMax;
    private SimpleAnimation slideMin = new SimpleAnimation(0.0f);
    private SimpleAnimation slideMax = new SimpleAnimation(0.0f);
    private Setting<BetweenInteger<T>> setting;

    public BetweenIntegerButton(ModuleButton button, float x, float y, Setting<BetweenInteger<T>> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
        this.dragMax = false;
        this.dragMin = false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        BetweenInteger<T> value = setting.getValue();
        if (setting.getValue().getMin() != null && setting.getValue().getMax() != null) {
            T min = setting.getValue().getMin();
            T max = setting.getValue().getMax();
            T valueRange = getValueRange(min, max);

            double sliderLength = getWidth() - 10;
            renderWidthMin = (sliderLength) * ((toDouble(setting.getValue().getMin()) - toDouble(min)) / toDouble(valueRange));
            renderWidthMax = (sliderLength) * ((toDouble(setting.getValue().getMax()) - toDouble(min)) / toDouble(valueRange));

            // Draw slider track
            RoundedShader.drawGradientCornerRL((float) getX() + 5, (float) getY() + 3, (float) renderWidthMax + 2, 4, 2, LeapFrog.colorManager.getClientColor().getMainColor(), LeapFrog.colorManager.getClientColor().getAlternativeColor());

            // Draw min slider knob
            RoundedShader.drawRound((float) getX() + 5 + (float) renderWidthMin + slideMin.getValue(), (float) getY() + 3 - 1.5f, 7, 7, 3.5f, new Color(255, 255, 255));

            // Draw max slider knob
            RoundedShader.drawRound((float) getX() + 5 + (float) renderWidthMax + slideMax.getValue(), (float) getY() + 3 - 1.5f, 7, 7, 3.5f, new Color(255, 255, 255));

            // Display setting name and current values
            FontRenderer.sans16_bold.drawString(setting.getName() + ": " + formatValue(setting.getValue().getMin()) + " - " + formatValue(setting.getValue().getMax()), (int) getX() + 5, getY() + (getHeight() / 2 - 3), -1);
            if (dragMin) {
                updateValue(mouseX, renderWidthMin, true);
            } else if (dragMax) {
                updateValue(mouseX, renderWidthMax, false);
            }
        }
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        int sliderXMin = (int) getX() + 5 + (int) renderWidthMin;
        int sliderXMax = (int) getX() + 5 + (int) renderWidthMax;

        if (isMouseOver(sliderXMin, getY(), 7, 7, mouseX, mouseY)) {
            dragMin = true;
        } else if (isMouseOver(sliderXMax, getY(), 7, 7, mouseX, mouseY)) {
            dragMax = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragMax = false;
        this.dragMin = false;
        //super.mouseReleased(mouseX, mouseY);
    }

    private void updateValue(int mouseX, double renderWidth, boolean isMin) {
        T min = setting.getValue().getMin();
        T max = setting.getValue().getMax();
        T valueRange = getValueRange(min, max);

        double sliderLength = getWidth() - 10;
        double diff = Math.min(sliderLength, Math.max(0, mouseX - (getX() + 5)));

        if (isMin) {
            double newValue = (diff / sliderLength) * toDouble(valueRange) + toDouble(min);
            if (newValue < toDouble(setting.getValue().getMax())) {
                setting.getValue().setMin(fromDouble(newValue));
                slideMin.setAnimation((float) diff, 5);
            }
        } else {
            double newValue = (diff / sliderLength) * toDouble(valueRange) + toDouble(min);
            if (newValue > toDouble(setting.getValue().getMin())) {
                setting.getValue().setMax(fromDouble(newValue));
                slideMax.setAnimation((float) diff, 5);
            }
        }
    }

    private T getValueRange(T min, T max) {
        if (min instanceof Integer) {
            return (T) Integer.valueOf(max.intValue() - min.intValue());
        } else if (min instanceof Float) {
            return (T) Float.valueOf(max.floatValue() - min.floatValue());
        } else if (min instanceof Double) {
            return (T) Double.valueOf(max.doubleValue() - min.doubleValue());
        }
        return null;
    }

    private double toDouble(T value) {
        if (value instanceof Integer) {
            return (double) value.intValue();
        } else if (value instanceof Float) {
            return (double) value.floatValue();
        } else if (value instanceof Double) {
            return value.doubleValue();
        }
        return 0.0;
    }

    private T fromDouble(double value) {
        if (setting.getValue().getMin() instanceof Integer) {
            return (T) Integer.valueOf((int) Math.round(value));
        } else if (setting.getValue().getMin() instanceof Float) {
            return (T) Float.valueOf((float) value);
        } else if (setting.getValue().getMin() instanceof Double) {
            return (T) Double.valueOf(value);
        }
        return null;
    }

    private String formatValue(T value) {
        if (value instanceof Integer) {
            return String.valueOf(value.intValue());
        } else if (value instanceof Float || value instanceof Double) {
            return String.format("%.1f", value.doubleValue());
        }
        return "";
    }
}
