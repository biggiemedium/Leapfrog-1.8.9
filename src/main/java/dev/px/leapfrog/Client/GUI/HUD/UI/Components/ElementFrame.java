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
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class ElementFrame extends DropdownFrame {

    private float tempHeight = 0;
    public Pair<Animation, Animation> openingAnimations;

    public ElementFrame(String name, int x, int y, int width, int height, Pair<Animation, Animation> openAnimations) {
        super(name, x, y, width, height, openAnimations);
        this.openingAnimations = openAnimations;

        for(Element e : LeapFrog.elementManager.getElements()) {
            this.dropdownButtons.add(new ElementDropdownButton(e.getName(), this.getX() - 0.5f, (int) (this.getY() + this.getHeight() + 1 * this.getHeight()), this.getWidth() - 10, (int) this.getHeight(), e));
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (openingAnimations == null) return;

        super.render(mouseX, mouseY);
        if(dragging) {
            this.x = mouseX - dragX;
            this.y = mouseY - dragY;
        }

        //this.tempHeight = 0;
        float alpha = Math.min(1, (float) openingAnimations.getKey().getValue());
        Color clientFirst = ColorUtil.applyOpacity(LeapFrog.colorManager.getClientColor().getMainColor(), alpha * alpha);
        Color clientSecond = ColorUtil.applyOpacity(LeapFrog.colorManager.getClientColor().getAlternativeColor(), alpha * alpha);
        Color firstColor = ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[0], alpha * alpha);
        Color secondColor = ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[1], alpha * alpha);
        Color thirdColor = ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[2], alpha * alpha);
        Color fourthColor = ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[3], alpha * alpha);
        float RH = Math.min(tempHeight, this.maxHeight);

        // black around frame
        RenderUtil.drawBlurredShadow(x - .75f, y - .5f, getWidth() + 1.5f, RH + getHeight() + 1.5f, 20, ColorUtil.tripleColor(20, (alpha * alpha)));
        RoundedShader.drawRound(x - .75f, y - .5f, getWidth() + 1.5f, RH + getHeight() + 1.5f, 5, ColorUtil.tripleColor(20, (alpha * alpha)));
        //RoundedShader.drawGradientVertical(x + 1, y + getHeight() + 1, getWidth() - 2, RH - 2, 4,  clientFirst,  clientSecond);
        RoundedShader.drawGradientRound(x + 1, y + getHeight() + 1, getWidth() - 2, RH - 2, 4,
                firstColor, secondColor, thirdColor, fourthColor);

        RenderUtil.resetColor(); // I think ?
        int scroll = 0;
        int count = 0;
        int h = (getHeight() - 1);
        for(DropdownButton b : this.dropdownButtons) {
                b.setX(this.getX() - 0.5f);
                b.setY((float) (y + getHeight() + (count * h) + (Math.round(scroll * 2) / 2.0)));
                b.setHeight(h);
                b.setWidth(getWidth());
                b.setAlpha(alpha);
                b.render(mouseX, mouseY);
                count += 1 + (b.getFeatureOffset() * (16 / 14f));
        }

        if(isMouseOver(getX(), getY() + getHeight(), getWidth(), this.maxHeight, mouseX, mouseY)) {
            scroll *= Mouse.getDWheel() * 0.1F;
        }
        tempHeight = (float) (count * h);

        RenderUtil.resetColor();
        FontRenderer.sans22.drawString(this.name, x + ((getWidth() / 2) - (FontRenderer.sans22.getStringWidth(this.name) / 2)), y + FontRenderer.sans22.getMiddleOfBox(getHeight()), ColorUtil.applyOpacity(-1, alpha));

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

        if(!isOpen()) {
            return;
        }
        for(DropdownButton b : this.dropdownButtons) {
            b.onClick(mouseX, mouseY, button);
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        this.stopDragging();
        super.onRelease(mouseX, mouseY, button);
        if(!isOpen()) {
            return;
        }
        for(DropdownButton b : this.dropdownButtons) {
            b.onRelease(mouseX, mouseY, button);
        }
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {
        super.onType(typedChar, keyCode);
    }
}
