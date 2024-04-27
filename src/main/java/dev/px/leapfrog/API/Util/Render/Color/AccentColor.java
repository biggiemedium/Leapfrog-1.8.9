package dev.px.leapfrog.API.Util.Render.Color;

import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;

import java.awt.*;

public class AccentColor {

    private String name;
    private Color mainColor;
    private Color alternativeColor;

    public SimpleAnimation opactiy = new SimpleAnimation(0.0f);
    public SimpleAnimation zoom = new SimpleAnimation(0.0f);

    public AccentColor(String name, Color mainColor, Color alternativeColor) {
        this.name = name;
        this.mainColor = mainColor;
        this.alternativeColor = alternativeColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getMainColor() {
        return mainColor;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }

    public Color getAlternativeColor() {
        return alternativeColor;
    }

    public void setAlternativeColor(Color alternativeColor) {
        this.alternativeColor = alternativeColor;
    }

    public SimpleAnimation getOpactiy() {
        return opactiy;
    }

    public void setOpactiy(SimpleAnimation opactiy) {
        this.opactiy = opactiy;
    }

}
