package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.API.Util.Render.Color.AccentColor;

import java.awt.*;
import java.util.ArrayList;

public class ColorManager {

    private ArrayList<AccentColor> themeColors = new ArrayList<>();
    private ArrayList<AccentColor> accentColors = new ArrayList<>();
    private AccentColor clientColor;

    public ColorManager() {
        this.themeColors.add(new AccentColor("Light", new Color(245, 233, 233), new Color(196, 192, 192)));
        this.themeColors.add(new AccentColor("Dark", new Color(31, 27, 27), new Color(70, 69, 69)));

        Add(new AccentColor("Tenacity", new Color(39, 179, 206), new Color(236, 133, 209)));
        Add(new AccentColor("Deep Ocean", new Color(61, 79, 143), new Color(1, 19, 63)));
        Add(new AccentColor("Melon", new Color(173, 247, 115), new Color(128, 243, 147)));
        Add(new AccentColor("Neon Red", new Color(210, 39, 48), new Color(184, 27, 45)));
        Add(new AccentColor("Pink Blood", new Color(226, 0, 70), new Color(255, 166, 200)));
        Add(new AccentColor("Lemon", new Color(252, 248, 184), new Color(255, 243, 109)));
        Add(new AccentColor("Blaze Orange", new Color(254, 169, 76), new Color(253, 130, 0)));
        Add(new AccentColor("Sunset Pink", new Color(253, 145, 21), new Color(245, 106, 230)));
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
}
