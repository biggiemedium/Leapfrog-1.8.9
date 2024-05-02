package dev.px.leapfrog.Client.NewGUI.Screens;

import dev.px.leapfrog.Client.NewGUI.FreeFlowGUI;
import dev.px.leapfrog.Client.NewGUI.ScreenHandler;
import net.minecraft.util.ResourceLocation;

public class ThemeScreen extends ScreenHandler {

    public ThemeScreen(int x, int y, int width, int height, FreeFlowGUI clickGUI, ScreenType screenType) {
        super("Theme", x, y, width, height, clickGUI, screenType, new ResourceLocation("Leapfrog/Images/pallette.png"));
    }


}
