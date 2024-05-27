package dev.px.leapfrog.Client.GUI.HUD;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiHUDEditor extends GuiScreen {

    private boolean backToGui;
    private ScaledResolution sr;

    public GuiHUDEditor(boolean backToGui) {
        this.backToGui = backToGui;
        sr = new ScaledResolution(Minecraft.getMinecraft());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(Element e : LeapFrog.elementManager.getElements()) {
            if(e.isVisible()) {
                e.editMode(mouseX, mouseY);
                e.onRender(new Render2DEvent(partialTicks));
            }
        }

        RoundedShader.drawGradientRound(sr.getScaledWidth() / 2 - 35 + 10, sr.getScaledHeight() / 2 - 7, 35, 15, 4,
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[0], 120),
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[1], 120),
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[2], 120),
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[3], 120)
        );
        FontRenderer.sans24_bold.drawString("GUI", sr.getScaledWidth() / 2 - (35 / 2), sr.getScaledHeight() / 2 - (7 / 2), -1);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(Element e : LeapFrog.elementManager.getElements()) {
            e.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if(isMouseOver(sr.getScaledWidth() / 2 - 35 + 10, sr.getScaledHeight() / 2 - 7, 35, 15, mouseX, mouseY)) {
            if(mouseButton == 0) {
                mc.displayGuiScreen(new ClickGUI());
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(Element e : LeapFrog.elementManager.getElements()) {
            e.mouseRelease(mouseX, mouseY, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            if(backToGui) {
                mc.displayGuiScreen(new ClickGUI());
            } else {
                mc.currentScreen = null;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
