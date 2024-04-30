package dev.px.leapfrog.Client.GUI.ClickGUI.Screen;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.Texture;
import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class Screen implements Component {

    private String name;
    private int x, y, width, height;
    private ClickGUI clickGUI;
    public SimpleAnimation selectedAnimation = new SimpleAnimation(0.0f);
    protected Texture froggy = new Texture(new ResourceLocation("Leapfrog/Images/Froggy.png"));


    public Screen(String name, int x, int y, int width, int height, ClickGUI clickGUI) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.clickGUI = clickGUI;
    }

    public void initGUI() {

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

    public ClickGUI getClickGUI() {
        return clickGUI;
    }

    public void setClickGUI(ClickGUI clickGUI) {
        this.clickGUI = clickGUI;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
