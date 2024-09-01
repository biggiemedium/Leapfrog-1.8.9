package dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure;

import dev.px.leapfrog.API.Module.Toggleable;
import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.Client.Module.Setting;

import java.io.IOException;
import java.util.ArrayList;

public class DropdownButton implements Component {

    private String label;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean hovered = false;
    private Toggleable toggleable;
    private float featureOffset = 0;
    private ArrayList<DropdownSettingButton<?>> settingButtons;

    public DropdownButton(String label, float x, float y, float width, float height, Toggleable toggleable) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.toggleable = toggleable;
        this.settingButtons = new ArrayList<>();
        if(this.toggleable.getSettings() != null) {
            for(Setting<?> s : this.toggleable.getSettings()) {
                if(s.getValue() instanceof Boolean) {

                }
            }
        }
    }


    @Override
    public void render(int mouseX, int mouseY) {

    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {

    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public Toggleable getToggleable() {
        return toggleable;
    }

    public void setToggleable(Toggleable toggleable) {
        this.toggleable = toggleable;
    }

    public float getFeatureOffset() {
        return featureOffset;
    }

    public void setFeatureOffset(float featureOffset) {
        this.featureOffset = featureOffset;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
