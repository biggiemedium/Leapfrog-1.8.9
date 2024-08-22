package dev.px.leapfrog.API.Util.Math;

import dev.px.leapfrog.API.Util.Render.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class GridSystem {

    private double width, height;
    private float strength;
    private int distance;

    public GridSystem(double width, double height, float strength, int distance) {
        this.width = width;
        this.height = height;
        this.strength = strength;
        this.distance = distance;
    }

    public void render() {
        GL11.glPushMatrix();

        // Enable line smoothing for better visuals
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        // Enable blending for transparency (if needed)
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Set line color and width
        GL11.glColor3f(0.5f, 0.5f, 0.5f); // Gray color
        GL11.glLineWidth(strength);

        GL11.glBegin(GL11.GL_LINES);

        // Draw vertical lines
        for (int x = 0; x <= width; x += distance) {
            GL11.glVertex2d(x, 0);
            GL11.glVertex2d(x, height);
        }

        // Draw horizontal lines
        for (int y = 0; y <= height; y += distance) {
            GL11.glVertex2d(0, y);
            GL11.glVertex2d(width, y);
        }

        GL11.glEnd();

        // Restore OpenGL states
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
