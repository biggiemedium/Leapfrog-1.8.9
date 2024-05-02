package dev.px.leapfrog.Client.NewGUI.Components;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.Client.NewGUI.FreeFlowGUI;

import java.io.IOException;

public class CategoryButton implements Component {

    private String name;
    private int x, y, width, height;
    private FreeFlowGUI clickGUI;

    public CategoryButton(String name, int x, int y, int width, int height, FreeFlowGUI clickGUI) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.clickGUI = clickGUI;
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

}
