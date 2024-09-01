package dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Math.ADT.Pair;
import dev.px.leapfrog.Client.Module.Setting;

import java.awt.*;

public abstract class DropdownSettingButton<T> implements Component {

    private String name, description;
    private float x, y, width, height;
    public Pair<Color, Color> clientColors;
    private Setting<T> setting;

    public DropdownSettingButton(Setting<T> setting) {
        this.setting = setting;
    }

}
