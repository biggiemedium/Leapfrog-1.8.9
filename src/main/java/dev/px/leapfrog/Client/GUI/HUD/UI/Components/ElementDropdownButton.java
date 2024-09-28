package dev.px.leapfrog.Client.GUI.HUD.UI.Components;

import dev.px.leapfrog.API.Module.Toggleable;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Direction;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure.DropdownButton;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.IOException;

public class ElementDropdownButton extends DropdownButton {

    public ElementDropdownButton(String label, float x, float y, float width, float height, Toggleable toggleable) {
        super(label, x, y, width, height, toggleable);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY); // this must come first bc it does all the animation handling
        // I have no clue why this works just don't fuck with it
        RenderUtil.resetColor();
        RenderUtil.drawRect(getX(), getY(), (getWidth() + 0.75f), getHeight(),  new Color(26, 26, 26, (int) (255 * (1.0f - (float) getToggleAnimation().getValue()))));
        RenderUtil.resetColor();
        FontRenderer.sans22.drawString(getToggleable().getName(), this.getX() + 5, this.getY() + FontRenderer.sans22.getMiddleOfBox(this.getHeight()), ColorUtil.applyOpacity(Color.WHITE, 0.5f + (.4f * (float) getToggleAnimation().getValue())).getRGB());
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(mouseX, mouseY)) {
            if(button == 0) {
                getToggleAnimation().setDirection(!getToggleable().isToggled() ? Direction.FORWARDS : Direction.BACKWARDS);
                getToggleable().toggle();
            } else if(button == 1) {
                this.open = !this.open;
            }
        }
        super.onClick(mouseX, mouseY, button);
    }


}
