package dev.px.leapfrog.Client.GUI.HUD;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.API.Wrapper;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

public class Element {

    private String name, description;
    private float x, y, width, height;
    private float dragX, dragY;
    private boolean dragging, scaling;
    private boolean visible;
    private float scale;

    private ArrayList<Setting<?>> settings = new ArrayList<>();
    protected Minecraft mc = Wrapper.getMC();

    public Element(int x, int y, int width, int height) {
        this.name = getElement().name();
        this.description = getElement().description();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = getElement().visible();
        this.scale = 1.0f;
        this.dragging = false;
        this.scaling = false;
    }

    protected Element.ElementInterface getElement() {
        return getClass().getAnnotation(Element.ElementInterface.class);
    }

    public void onRender(Render2DEvent event) {

    }

    public String renderDummy(Render2DEvent event) {
        return "";
    }

    public <T> Setting Add(Setting preference) {
        this.settings.add(preference);
        return preference;
    }

    public void editMode(int mouseX, int mouseY) {
        if (dragging) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }


    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(isHovered(mouseX, mouseY)) {
            if (button == 0) {
                dragging = true;
                dragX = x - mouseX;
                dragY = y - mouseY;
            }
        }

        if(button == 2 || button == 1) {
            if(isHovered(mouseX, mouseY)) {
                if(mc.currentScreen != null) {
                    this.visible = !this.visible;
                    //ChatFormatting togglecolor = this.toggled ? ChatFormatting.GREEN : ChatFormatting.RED;
                   // Util.sendClientSideMessage(this.getName() + togglecolor + " Toggled" + ChatFormatting.RESET, true);
                }
            }
        }

        if(button == 1) {
            if(isMouseOver(getX() + getWidth() - 1, getY() + getHeight() - 1, 5, 5, mouseX, mouseY)) {
                scaling = true;
            }
        }
    }

    public void drawBackground() {
        int radius = LeapFrog.colorManager.getRadius().getValue();
        float opacity = LeapFrog.colorManager.getOpacity().getValue();
        switch (LeapFrog.colorManager.getCurrentMode()) {
            case Client:
                RoundedShader.drawGradientRound(getX(), getY(), getWidth(), getHeight(), radius,
                        ColorUtil.getClientColorInterpolation()[0],
                        ColorUtil.getClientColorInterpolation()[1],
                        ColorUtil.getClientColorInterpolation()[2],
                        ColorUtil.getClientColorInterpolation()[3]);
                break;

            case Glass:
                break;
        }
    }

    public void mouseRelease(int mouseX, int mouseY, int state) {
        this.dragging = false;
        this.scaling = false;
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height;
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getDragX() {
        return dragX;
    }

    public void setDragX(float dragX) {
        this.dragX = dragX;
    }

    public float getDragY() {
        return dragY;
    }

    public void setDragY(float dragY) {
        this.dragY = dragY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ElementInterface {

        String name();

        String description() default "";

        boolean visible() default false;

    }

}
