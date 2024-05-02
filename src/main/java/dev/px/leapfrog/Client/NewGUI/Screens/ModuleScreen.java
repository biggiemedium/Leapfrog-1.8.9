package dev.px.leapfrog.Client.NewGUI.Screens;

import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import dev.px.leapfrog.Client.NewGUI.FreeFlowGUI;
import dev.px.leapfrog.Client.NewGUI.ScreenHandler;
import net.minecraft.util.ResourceLocation;

public class ModuleScreen extends ScreenHandler {

    public ModuleScreen(int x, int y, int width, int height, FreeFlowGUI clickGUI, ScreenType screenType) {
        super("Module", x, y, width, height, clickGUI, screenType, null); // new ResourceLocation("Leapfrog/Images/")
    }


}
