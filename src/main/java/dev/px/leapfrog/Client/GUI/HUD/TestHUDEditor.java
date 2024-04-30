package dev.px.leapfrog.Client.GUI.HUD;

import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class TestHUDEditor extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(Element e : LeapFrog.elementManager.getElements()) {
            e.editMode(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for(Element e : LeapFrog.elementManager.getElements()) {
            e.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(Element e : LeapFrog.elementManager.getElements()) {
            e.mouseRelease(mouseX, mouseY, state);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
