package dev.px.leapfrog.Client.GUI.HUD;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.Font.MinecraftFontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.API.Wrapper;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

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
    protected MinecraftFontRenderer font = FontRenderer.sans20_bold;

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

    public Element(int x, int y) {
        this.name = getElement().name();
        this.description = getElement().description();
        this.x = x;
        this.y = y;
        this.width = (float) font.getStringWidth(getElement().name()) + 3;
        this.height = (float) font.getHeight() + 3;
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
            FontUtil.regular12.drawString("x " + getX() + " y " + getY(), getX() - 2, getY() + getHeight() + 5, -1);
        }

        RoundedShader.drawRoundOutline(getX() - 1, getY() - 1, getWidth() + 2, getHeight() + 2, 4, 0.1f, new Color(0, 0, 0, 0), new Color(255, 255, 255));

        // ill do this stuff later
        if(isMouseOver(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY)) {
            if(Mouse.getEventDWheel() != 0) {
                scaling = true;
            }
            if(scaling) {
                if(scale <= 1) {
                    scale = 1;
                } else if(scale <= 5) {
                    scale = 5; // cannot be bigger than 5x what the element is
                }
                scale += Mouse.getDWheel() * 0.01d;
            }
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

        if(button == 1) {
            if(isHovered(mouseX, mouseY)) {
                if(mc.currentScreen != null) {
                    this.visible = !this.visible;
                }
            }
        }
    }

    public void drawBackground(float x, float y, float width, float height) {
        int radius = LeapFrog.colorManager.getRadius().getValue();
        int opacity = LeapFrog.colorManager.getOpacity().getValue();

        if(LeapFrog.settingsManager.BACKGROUND.getValue()) {
            RoundedShader.drawGradientRound(x, y, width, height, radius,
                    ColorUtil.getClientColor(0, opacity),
                    ColorUtil.getClientColor(90, opacity),
                    ColorUtil.getClientColor(180, opacity),
                    ColorUtil.getClientColor(270, opacity));
        }
    }

    public int getFontColor() {
        return LeapFrog.colorManager.getFontColor().getValue().getRGB();
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
