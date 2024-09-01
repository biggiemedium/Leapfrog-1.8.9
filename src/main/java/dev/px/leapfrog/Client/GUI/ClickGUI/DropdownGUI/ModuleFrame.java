package dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.ADT.Pair;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;
import dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure.DropdownFrame;

import java.io.IOException;

public class ModuleFrame extends DropdownFrame {

    private Type type;

    public ModuleFrame(String name, int x, int y, int width, int height, Type type, Pair<Animation, Animation> animations) {
        super(name, x, y, width, height, animations);
        this.type = type;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        super.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        super.onRelease(mouseX, mouseY, button);
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {
        super.onType(typedChar, keyCode);
    }
}
