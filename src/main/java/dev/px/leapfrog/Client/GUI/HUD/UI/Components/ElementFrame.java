package dev.px.leapfrog.Client.GUI.HUD.UI.Components;

import dev.px.leapfrog.API.Util.Math.ADT.Pair;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.API.Util.Render.StencilUtil;
import dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure.DropdownButton;
import dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure.DropdownFrame;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.IOException;

public class ElementFrame extends DropdownFrame {

    private boolean gradient = false;
    private float tempHeight = 0;
    public Pair<Animation, Animation> openingAnimations;

    public ElementFrame(String name, int x, int y, int width, int height, Pair<Animation, Animation> openAnimations) {
        super(name, x, y, width, height, openAnimations);
        this.openingAnimations = openAnimations;

        for(Element e : LeapFrog.elementManager.getElements()) {
            this.dropdownButtons.add(new ElementDropdownButton(e.getName(), getX() - 0.5f, (int) (getY() + getHeight() + 1 * getHeight()), getWidth() - 10, (int) getHeight(), e));
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (openingAnimations == null) return;
        if(dragging) {
            this.x = mouseX - dragX;
            this.y = mouseY - dragY;
        }

        this.tempHeight = 0;
        float alpha = Math.min(1, (float) openingAnimations.getKey().getValue());
        float RH = Math.min(tempHeight, 300);
        RoundedShader.drawRound(x - .75f, y - .5f, getWidth() + 1.5f, RH + getHeight() + 1.5f, 5, ColorUtil.tripleColor(20, (alpha * alpha)));
        if (gradient) {
            RoundedShader.drawGradientVertical(x + 1, y + getHeight() + 1, getWidth() - 2, RH - 2, 4,  LeapFrog.colorManager.getClientColor().getMainColor(),  LeapFrog.colorManager.getClientColor().getAlternativeColor());
        } else {
            RoundedShader.drawRound(x + .8f, y + getHeight() + .8f, getWidth() - 1.6f, RH - 1.6f, 3.5f, LeapFrog.colorManager.getClientColor().getMainColor());
        }

        int scroll = 0;

        for(DropdownButton b : this.dropdownButtons) {
            b.setY((float) (y + getHeight() + (tempHeight * 14) + (Math.round(scroll * 2) / 2.0)));
            b.render(mouseX, mouseY);
            tempHeight += 1 + (b.getFeatureOffset() * (16 / 14f));
        }

        FontRenderer.sans22.drawString(this.name, x + ((getWidth() / 2) - (FontRenderer.sans22.getStringWidth(this.name) / 2)), y + FontRenderer.sans22.getMiddleOfBox(getHeight()), -1);
        super.render(mouseX, mouseY);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(this.getX(), this.getY(), this.getWidth(), this.getHeight(), mouseX, mouseY)) {
            if(button == 0) {
                this.dragging = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            }
        }
        super.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        this.stopDragging();
        super.onRelease(mouseX, mouseY, button);
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {
        super.onType(typedChar, keyCode);
    }
}
