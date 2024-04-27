package dev.px.leapfrog.Client.GUI.ClickGUI;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Direction;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Impl.EaseBackIn;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.GLUtils;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ClientSettingsScreen;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ModuleScreen;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.Screen;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {

    private int x, y, width, height;
    private boolean close, dragging;
    private int dragX, dragY;
    private Animation closeAnimation = new Animation(250, false, Easing.TENACITY_EASEBACKIN);
    private ArrayList<Screen> screens;
    private Screen currentScreen;

    public ClickGUI() {
        this.x = 100;
        this.y = 100;
        this.width = 350;
        this.height = 260;
        this.close = false;
        this.dragging = false;
        this.dragX = 0;
        this.dragY = 0;
        this.screens = new ArrayList<>();
        this.screens.add(new ModuleScreen(x + 80, y + 15, width - 80, height - 15));
        this.screens.add(new ClientSettingsScreen(x + 80, y + 15, width - 80, height - 15));
        this.currentScreen = screens.get(0);
    }

    @Override
    public void initGui() {
        this.close = false;
        closeAnimation.setState(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if(close) {
            this.closeAnimation.setState(false);
            if(closeAnimation.getAnimationFactor() <= 0) {
                mc.displayGuiScreen(null);
            }
        }

        if(dragging) {
            this.x = mouseX - dragX;
            this.y = mouseY - dragY;
        }

        GLUtils.startScale(((this.getX()) + (this.getX() + this.getWidth())) / 2, ((this.getY()) + (this.getY() + this.getHeight())) / 2, (float) closeAnimation.getAnimationFactor());
        RenderUtil.drawBlurredShadow(x, y, width, height, 20, new Color(30, 30, 30, 150));
        RoundedShader.drawRound(x, y, width, height, 4, new Color(30, 30, 30));

        RoundedShader.drawRound(x, y, 80, height, 4, new Color(26, 26, 26));
        RoundedShader.drawRound(x, y, width, 15, 4, new Color(26, 26, 26));
        FontUtil.regular20.drawStringWithClientColor("LeapFrog", x + 5, y + 5, false);
       // FontUtil.regular16.drawStringWithClientColor(LeapFrog.VERSION, x + 4 + FontUtil.regular16.getStringWidth("Leapfrog Client") - 5, y + 7 + FontUtil.regular22.getHeight(), false);

        int offsetY = 0;
        for(Screen s : this.screens) {


            if(s == currentScreen) {
                RoundedShader.drawGradientCornerRL(x + 5, y + 30 + offsetY, 60, 15, 4,
                        LeapFrog.colorManager.getClientColor().getMainColor(), LeapFrog.colorManager.getClientColor().getAlternativeColor());
            }

            FontUtil.regular16.drawString(s.getName(), x + 15, y + 30 + 7 + offsetY, -1);

            s.render(mouseX, mouseY);
            offsetY += 20 + 2;
        }

        GLUtils.stopScale();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(isMouseOver(x, y, width, 15, mouseX, mouseY)) {
            if(mouseButton == 0) {
                this.dragging = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            this.close = true;
        }

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

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public Animation getCloseAnimation() {
        return closeAnimation;
    }

    public void setCloseAnimation(Animation closeAnimation) {
        this.closeAnimation = closeAnimation;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
