package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.API.Util.Render.Color.AccentColor;
import dev.px.leapfrog.Client.Module.Setting;

import java.awt.*;
import java.util.ArrayList;

public class ColorManager {

    private ArrayList<AccentColor> themeColors = new ArrayList<>();
    private ArrayList<AccentColor> accentColors = new ArrayList<>();
    private AccentColor clientColor;

    private Setting<Float> opacity = new Setting<>("Opacity", 0.8f, 0.1f, 1.0f);
    private Setting<Integer> radius = new Setting<>("Radius", 4, 0, 10);
    private ColorMode currentMode;

    public enum ColorMode {
        Client,
        Glass,
        Clear,
    }

    public ColorManager() {
        this.themeColors.add(new AccentColor("Light", new Color(245, 233, 233), new Color(196, 192, 192)));
        this.themeColors.add(new AccentColor("Dark", new Color(31, 27, 27), new Color(70, 69, 69)));

        Add(new AccentColor("Froggy", new Color(173, 247, 115), new Color(128, 243, 147)));
        Add(new AccentColor("Tenacity", new Color(39, 179, 206), new Color(236, 133, 209)));
        Add(new AccentColor("Dark Blue", new Color(61, 79, 143), new Color(1, 19, 63)));
        Add(new AccentColor("Angry birds", new Color(225, 37, 46), new Color(164, 27, 43)));
        Add(new AccentColor("Pank", new Color(226, 0, 70), new Color(255, 166, 200)));
        Add(new AccentColor("Sunset", new Color(253, 145, 21), new Color(245, 106, 230)));
        clientColor = accentColors.get(0);
        currentMode = ColorMode.Client;
    }

    public void Add(AccentColor accentColor) {
        this.accentColors.add(accentColor);
    }

    public AccentColor getClientColor() {
        return clientColor;
    }

    public void setClientColor(AccentColor clientColor) {
        this.clientColor = clientColor;
    }

    public ArrayList<AccentColor> getColors() {
        return accentColors;
    }

    public AccentColor getAccentColorByName(String name) {
        return accentColors.stream().filter(color -> color.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public AccentColor getAccentColorByClass(Class<AccentColor> modClass) {
        return accentColors.stream().filter(color -> color.getClass().equals(modClass)).findFirst().orElse(null);
    }

    public ColorMode getCurrentMode() {
        return currentMode;
    }

    public Setting<Float> getOpacity() {
        return opacity;
    }

    public void setOpacity(Setting<Float> opacity) {
        this.opacity = opacity;
    }

    public Setting<Integer> getRadius() {
        return radius;
    }

    public void setRadius(Setting<Integer> radius) {
        this.radius = radius;
    }
}
