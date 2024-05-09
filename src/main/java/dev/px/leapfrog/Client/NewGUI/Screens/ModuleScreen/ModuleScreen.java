package dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.Manager.InputManager;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.NewGUI.Components.Panel;
import dev.px.leapfrog.Client.NewGUI.FreeFlowGUI;
import dev.px.leapfrog.Client.NewGUI.ScreenHandler;
import dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl.CategoryPanel;
import dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl.ModuleButton;
import dev.px.leapfrog.Client.NewGUI.Screens.ModuleScreen.Impl.ModulePanel;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ModuleScreen extends ScreenHandler {

    private CategoryPanel categoryPanel;
    private ModulePanel modulePanel;

    public ModuleScreen(int x, int y, int width, int height, FreeFlowGUI clickGUI, ScreenType screenType) {
        super("Module", x, y, width, height, clickGUI, screenType, null); // new ResourceLocation("Leapfrog/Images/")
        categoryPanel = new CategoryPanel(getX() + 10, getY() + 5, 60, 30, new Color(28, 28, 28));
        modulePanel = new ModulePanel(getX() + 10, getY() + 45, 100, getHeight() - 55, new Color(28, 28, 28), categoryPanel.getCurrentType());
    }

    @Override
    public void render(int mouseX, int mouseY) {
        categoryPanel.updatePosition(getX() + 10, getY() + 5, 100, 25);
        categoryPanel.render(mouseX, mouseY);

        modulePanel.updatePosition(getX() + 10, getY() + 35, 100, getHeight() - 55);
        modulePanel.setType(categoryPanel.getCurrentType());
        modulePanel.render(mouseX, mouseY);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        categoryPanel.onClick(mouseX, mouseY, button);
        modulePanel.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {

    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }
}
