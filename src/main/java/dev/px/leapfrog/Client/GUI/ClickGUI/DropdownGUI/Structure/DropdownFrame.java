package dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure;

import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Math.ADT.Pair;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;

import java.io.IOException;
import java.util.ArrayList;

public class DropdownFrame implements Component {

    public String name;
    public int x, y, width, height;
    public boolean dragging = false, open = true;
    public int dragX, dragY;
    public ArrayList<DropdownButton> dropdownButtons;
    private Pair<Animation, Animation> animations;

    public DropdownFrame(String name, int x, int y, int width, int height, Pair<Animation, Animation> animations) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dropdownButtons = new ArrayList<>();
        this.animations = animations;
    }

    public void render(int mouseX, int mouseY) {

    }

    public void onClick(int mouseX, int mouseY, int button) throws IOException {

    }

    public void onRelease(int mouseX, int mouseY, int button) {
        this.dragging = false;
    }

    public void onType(char typedChar, int keyCode) throws IOException {

    }

    public boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getDragX() {
        return dragX;
    }

    public void setDragX(int dragX) {
        this.dragX = dragX;
    }

    public int getDragY() {
        return dragY;
    }

    public void setDragY(int dragY) {
        this.dragY = dragY;
    }

    public void startDragging(int mouseX, int mouseY) {
        dragging = true;
        dragX = mouseX - x;
        dragY = mouseY - y;
    }

    public void stopDragging() {
        dragging = false;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    public void toggleOpen() {
        this.open = !this.open;
    }

}
