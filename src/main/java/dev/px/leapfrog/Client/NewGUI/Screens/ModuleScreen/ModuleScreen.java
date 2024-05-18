package dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen;

import dev.px.leapfrog.Client.NewGUI.FreeFlowGUI;
import dev.px.leapfrog.Client.NewGUI.ScreenHandler;
import dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl.ModulePanel;

import java.awt.*;
import java.io.IOException;

public class ModuleScreen extends ScreenHandler {

    private ModulePanel modulePanel;

    public ModuleScreen(int x, int y, int width, int height, FreeFlowGUI clickGUI, ScreenType screenType) {
        super("Module", x, y, width, height, clickGUI, screenType, null); // new ResourceLocation("Leapfrog/Images/")
        modulePanel = new ModulePanel(getX() + 10, getY() + 45, 100, getHeight() - 55, new Color(28, 28, 28));
    }

    @Override
    public void render(int mouseX, int mouseY) {
        modulePanel.updatePosition(getX() + 10, getY() + 10, getWidth() - 20, getHeight() - 20);
        modulePanel.render(mouseX, mouseY);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        modulePanel.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {

    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
