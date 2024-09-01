package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Setting;

import dev.px.leapfrog.API.Module.Setting.BetweenInteger;
import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;

public class BetweenIntegerButton<T extends Number & Comparable<T>> extends SettingButton<BetweenInteger<T>> {

    private Setting<BetweenInteger<T>> setting;
    private boolean draggingFirstKnob;
    private boolean draggingSecondKnob;
    private double firstKnobPos;
    private double secondKnobPos;
    private SimpleAnimation slide = new SimpleAnimation(0.0f);

    public BetweenIntegerButton(ModuleButton button, float x, float y, Setting<BetweenInteger<T>> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
        this.draggingFirstKnob = false;
        this.draggingSecondKnob = false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        BetweenInteger<T> range = setting.getValue();
        double min = range.getMin().doubleValue();
        double max = range.getMax().doubleValue();
        double l = getWidth() / 4;

        firstKnobPos = (l) * (range.getMin().doubleValue() - min) / (max - min);
        secondKnobPos = (l) * (range.getMax().doubleValue() - min) / (max - min);

        // Draw the range and knobs
        RoundedShader.drawGradientCornerRL((float) (getX() + getWidth()) - (int) (secondKnobPos + 5), (float) getY() + 3, (float) (secondKnobPos - firstKnobPos) + 7, 4, 2, LeapFrog.colorManager.getClientColor().getMainColor(), LeapFrog.colorManager.getClientColor().getAlternativeColor());
        RoundedShader.drawRound((float) (getX() + getWidth()) - (int) (firstKnobPos + 5) + slide.getValue(), (float) getY() + 3 - 1.5f, 7, 7, 3.5f, new Color(255, 255, 255));
        RoundedShader.drawRound((float) (getX() + getWidth()) - (int) (secondKnobPos + 5) + slide.getValue(), (float) getY() + 3 - 1.5f, 7, 7, 3.5f, new Color(255, 255, 255));

        FontRenderer.sans16_bold.drawString(setting.getName() + ": " + range.getMin() + " - " + range.getMax(), (int) getX() + 5, getY() + (getHeight() / 2 - 3), -1);
        mouseDragged(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        //mouseDragged(mouseX, mouseY);
        if (button == 0) {
            double l = getWidth() / 4;
            if (isMouseOver(getX() + getWidth() - (float) firstKnobPos + 5, getY(), (float) l, getHeight(), mouseX, mouseY)) {
                draggingFirstKnob = true;
            } else if (isMouseOver(getX() + getWidth() - (float) secondKnobPos + 5, getY(), (float) l, getHeight(), mouseX, mouseY)) {
                draggingSecondKnob = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        draggingFirstKnob = false;
        draggingSecondKnob = false;
    }

    public void mouseDragged(int mouseX, int mouseY) {
        if (draggingFirstKnob || draggingSecondKnob) {
            double min = setting.getValue().getMin().doubleValue();
            double max = setting.getValue().getMax().doubleValue();
            double l = getWidth() / 4;

            double pos = mouseX - (getX() + getWidth() - 5);
            double newPos = Math.min(l, Math.max(0, pos));
            double newValue = ((newPos / l) * (max - min) + min);

            BetweenInteger<T> range = setting.getValue();
            if (draggingFirstKnob) {
                if (newValue <= range.getMax().doubleValue()) {
                    range.setMin((T) Integer.valueOf((int) Math.round(newValue)));
                }
            } else if (draggingSecondKnob) {
                if (newValue >= range.getMin().doubleValue()) {
                    range.setMax((T) Integer.valueOf((int) Math.round(newValue)));
                }
            }
            setting.setValue(range);
        }
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
