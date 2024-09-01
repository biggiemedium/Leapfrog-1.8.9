package dev.px.leapfrog.API.Module;

import dev.px.leapfrog.Client.Module.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class Toggleable {

    protected String name, description;
    protected boolean toggled;
    protected ArrayList<Setting<?>> settings = new ArrayList<>();
    protected Minecraft mc = Minecraft.getMinecraft();

    public Toggleable(String name, String description, boolean visible) {
        this.name = name;
        this.description = description;
        this.toggled = visible;
    }

    public void toggle() {
        this.toggled = !this.toggled;
    }

    protected <T> Setting<T> createSetting(Setting<T> setting) {
        this.settings.add(setting);
        return setting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public ArrayList<Setting<?>> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<Setting<?>> settings) {
        this.settings = settings;
    }
}
