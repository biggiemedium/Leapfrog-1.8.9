package dev.px.leapfrog.Client.NewGUI;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ScreenHandler implements Component {

    private String name;
    private int x, y, width, height;
    private FreeFlowGUI clickGUI;
    private ResourceLocation icon;
    private ScreenType screenType;

    public ScreenHandler(String name, int x, int y, int width, int height, FreeFlowGUI clickGUI, ScreenType type, ResourceLocation location) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.clickGUI = clickGUI;
        this.screenType = type;
        this.icon = location;
    }

    @Override
    public void render(int mouseX, int mouseY) {

    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {

    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public FreeFlowGUI getClickGUI() {
        return clickGUI;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public void setClickGUI(FreeFlowGUI clickGUI) {
        this.clickGUI = clickGUI;
    }

    protected enum ScreenType {
        Game,
        Appearance
    }
}
