package dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.ModuleButton;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.LeapFrog;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ModulePanel implements Component {

    private int x, y, width, height;
    private ArrayList<ModuleButton> buttons;
    private int scrollY = 0;
    private float featureOffset;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();

    public ModulePanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttons = new ArrayList<>();

        for(Module m : LeapFrog.moduleManager.getModules()) {
            buttons.add(new ModuleButton(m, x + 8, y, width - 8, new Color(35, 35, 35), this));
        }
    }

    public void initGUI() {
        for(ModuleButton b : buttons) {
            b.initGUI();
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RoundedShader.drawRound(x, y, width, height, 4, new Color(26, 26, 26));
        this.featureOffset = 0;
        int offsetY = 0;
        stack.pushScissor(x, y, width, height);
        this.featureOffset = 0;
        for(ModuleButton b : buttons) {
            if(b.getX() != getX()) {
                b.setX(getX());
            }

            if(b.getY() != getY() + 5 + offsetY + scrollY) { // im going to shoot myself
                b.setY(getY() + 5 + offsetY + scrollY);
            }

            offsetY += b.getHeight() + b.getFeatureHeight() + 4;
            b.render(mouseX, mouseY);
        }
        stack.popScissor();

        if(isMouseOver(x, y, width, height, mouseX, mouseY)) {
            this.scrollY += Mouse.getDWheel() * 0.02;
        }
        if(scrollY > 0) {
            scrollY = 0;
        } else if(scrollY < -offsetY + 10) {
            scrollY = (-offsetY + 10);
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        for(ModuleButton b : buttons) {
            b.onClick(mouseX, mouseY, button);
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        for(ModuleButton b : buttons) {
            b.onRelease(mouseX, mouseY, button);
        }
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public void addFeatureOffset(double featureOffset) {
        this.featureOffset += featureOffset;
    }

    public double getFeatureOffset() {
        return featureOffset;
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
