package dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure;

import dev.px.leapfrog.API.Module.Toggleable;
import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Direction;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Impl.*;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.gui.Gui;

import java.awt.*;
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

    private Animation toggleAnimation = new SmoothStepAnimation(300, 1); // subjected to change
    private Animation hoverAnimation = new EaseOutSine(400, 1, Direction.BACKWARDS);

    private boolean gradient = false;
    private float alpha = 1;
    protected boolean open;

    public DropdownButton(String label, float x, float y, float width, float height, Toggleable toggleable) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.toggleable = toggleable;
        this.open = false;
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
        toggleAnimation.setDirection(toggleable.isToggled() ? Direction.FORWARDS : Direction.BACKWARDS);

        hoverAnimation.setDirection(isMouseOver(mouseX, mouseY) ? Direction.FORWARDS : Direction.BACKWARDS);
        hoverAnimation.setDuration(isMouseOver(mouseX, mouseY) ? 250 : 400);
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

    public ArrayList<DropdownSettingButton<?>> getSettingButtons() {
        return settingButtons;
    }

    public void setSettingButtons(ArrayList<DropdownSettingButton<?>> settingButtons) {
        this.settingButtons = settingButtons;
    }

    public Animation getToggleAnimation() {
        return toggleAnimation;
    }

    public void setToggleAnimation(Animation toggleAnimation) {
        this.toggleAnimation = toggleAnimation;
    }

    public Animation getHoverAnimation() {
        return hoverAnimation;
    }

    public void setHoverAnimation(Animation hoverAnimation) {
        this.hoverAnimation = hoverAnimation;
    }

    public boolean isGradient() {
        return gradient;
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
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
