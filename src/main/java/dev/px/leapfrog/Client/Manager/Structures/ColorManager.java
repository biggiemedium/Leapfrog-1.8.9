package dev.px.leapfrog.Client.Manager.Structures;

import dev.px.leapfrog.API.Util.Render.Color.AccentColor;
import dev.px.leapfrog.Client.Module.Setting;

import java.awt.*;
import java.util.ArrayList;

public class ColorManager {

    private ArrayList<AccentColor> accentColors = new ArrayList<>();
    private AccentColor clientColor;

    private Setting<Integer> opacity = new Setting<>("Opacity", 150, 0, 255);
    private Setting<Integer> radius = new Setting<>("Radius", 4, 0, 8);
    private Setting<Color> fontColor = new Setting<>("Font Color", new Color(255, 255, 255));

    public ColorManager() {
        Add(new AccentColor("Froggy", new Color(173, 247, 115), new Color(128, 243, 147)));
        Add(new AccentColor("Tenacity", new Color(39, 179, 206), new Color(236, 133, 209)));
        Add(new AccentColor("Opal", new Color(39, 179, 206), new Color(219, 219, 229)));
        Add(new AccentColor("White", new Color(255, 255, 255), new Color(255, 255, 255)));
        Add(new AccentColor("ThunderHack", new Color(148, 25, 234), new Color(232, 12, 253)));
        Add(new AccentColor("Future", new Color(136, 15, 15), new Color(73, 73, 73)));
        Add(new AccentColor("Dark Blue", new Color(61, 79, 143), new Color(1, 19, 63)));
        Add(new AccentColor("Pink", new Color(226, 0, 70), new Color(255, 166, 200)));
        Add(new AccentColor("Sunset", new Color(253, 145, 21), new Color(245, 106, 230)));
        clientColor = accentColors.get(0);
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

    public Setting<Integer> getOpacity() {
        return opacity;
    }

    public void setOpacity(Setting<Integer> opacity) {
        this.opacity = opacity;
    }

    public Setting<Integer> getRadius() {
        return radius;
    }

    public void setRadius(Setting<Integer> radius) {
        this.radius = radius;
    }

    public Setting<Color> getFontColor() {
        return fontColor;
    }

    public void setFontColor(Setting<Color> fontColor) {
        this.fontColor = fontColor;
    }
}
