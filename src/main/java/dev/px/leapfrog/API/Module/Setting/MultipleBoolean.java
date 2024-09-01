package dev.px.leapfrog.API.Module.Setting;

import dev.px.leapfrog.Client.Module.Setting;

import java.util.ArrayList;

public class MultipleBoolean {

    private ArrayList<Setting<Boolean>> settings;

    public MultipleBoolean(ArrayList<Setting<Boolean>> settings) {
        this.settings = settings;
    }

    public MultipleBoolean add(Setting<Boolean> b) {
        this.settings.add(b);
        return this;
    }

    public ArrayList<Boolean> getValues() {
        ArrayList<Boolean> values = new ArrayList<>();
        for (Setting<Boolean> setting : settings) {
            values.add(setting.getValue());
        }
        return values;
    }

    public void toggleAll() {
        for (Setting<Boolean> setting : settings) {
            setting.setValue(!setting.getValue());
        }
    }

    public MultipleBoolean build() {
        return this;
    }
}
