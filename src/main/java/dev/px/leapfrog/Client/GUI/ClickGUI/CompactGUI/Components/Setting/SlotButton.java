package dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.Setting;

import dev.px.leapfrog.API.Module.Setting.Bind;
import dev.px.leapfrog.API.Module.Setting.Link;
import dev.px.leapfrog.API.Module.Setting.Slot;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Setting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class SlotButton extends SettingButton<Slot> {

    private Setting<Slot> setting;
    private boolean listening;

    public SlotButton(ModuleButton button, float x, float y, Setting<Slot> setting) {
        super(button, x, y, button.getWidth(), 20, setting);
        this.setting = setting;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (listening) {
            if (keyCode == 211 || keyCode == Keyboard.KEY_BACK) {
                this.setting.setValue(new Slot(-1)); // unbind
            } else if (keyCode >= Keyboard.KEY_1 && keyCode <= Keyboard.KEY_9) {
                int slotValue = keyCode - Keyboard.KEY_1 + 1; // Map to numbers 1-9
                this.setting.setValue(new Slot(slotValue));
            } else {
                this.setting.setValue(new Slot(-1)); // unbind for non-1-9 keys
            }
            this.listening = false;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);
        String state = "";

        if(this.setting.getValue().getSlot() <= 0) {
            state = "None";
        } else {
            state = (this.setting.getValue().getSlotName());
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
