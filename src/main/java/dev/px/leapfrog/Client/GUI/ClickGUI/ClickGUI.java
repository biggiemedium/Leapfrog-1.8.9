package dev.px.leapfrog.Client.GUI.ClickGUI;

import dev.px.leapfrog.API.Util.Render.*;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ClientSettingsScreen;
import dev.px.leapfrog.Client.GUI.ClickGUI.Screen.ColorsScreen;
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
    private Animation closeAnimation;
    private ArrayList<Screen> screens;
    private Screen currentScreen;
    private Animation moveAnimation = new Animation(100, false, Easing.LINEAR);
    private float moveY = 0;

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
        this.screens.add(new ModuleScreen(x + 80, y, width - 80, height, this));
        this.screens.add(new ClientSettingsScreen(x + 80, y, width - 80, height, this));
        this.screens.add(new ColorsScreen(x + 80, y, width - 80, height, this));
        this.currentScreen = screens.get(0);

        //this.closeAnimation.setState(true);
    }

    @Override
    public void initGui() {
        this.closeAnimation = new Animation(250, true, Easing.TENACITY_EASEBACKIN);
        this.close = false;
        closeAnimation.setState(true);

        for(Screen s : screens) {
            s.initGUI();
        }
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
        RenderUtil.drawBlurredShadow(x - 1, y, width + 2, height, 20, new Color(56, 56, 56, 200));
        RoundedShader.drawRound(x, y, width, height, 4, new Color(30, 30, 30));

        RoundedShader.drawRound(x, y, 80, height, 4, new Color(28, 28, 28));
        FontUtil.regular_bold20.drawStringWithClientColor("LeapFrog", x + 6, y + 8, false);
        FontUtil.regular_bold16.drawStringWithClientColor(LeapFrog.VERSION, x + 8 + FontUtil.regular20.getStringWidth("Leapfrog"), y + 8, false);
        RoundedShader.drawRound(x + 4, y + 10, 10, 0.2f, 4.0f, new Color(131, 131, 131, 150));

        int offsetY = 0;
        for(Screen s : this.screens) {
            s.setX(x + 80); // x + 80, y, width - 80, height
            s.setY(y);
            s.setWidth(width - 80);
            s.setHeight(height);

            s.selectedAnimation.setAnimation(currentScreen.equals(s) ? 0 : 30, 20);
            GLUtils.startTranslate(0, s.selectedAnimation.getValue());
            if(s == currentScreen) {
                currentScreen.render(mouseX, mouseY);
            }
            GLUtils.stopTranslate();

            if(s == currentScreen) { // getY() + 30 + offsetY
                this.moveY = offsetY;
                RoundedShader.drawGradientCornerRL(x + 5, getY() + 30 + (offsetY), 70, 15, 4,
                        LeapFrog.colorManager.getClientColor().getMainColor(),
                        LeapFrog.colorManager.getClientColor().getAlternativeColor());
            }

            FontUtil.regular_bold20.drawString(s.getName(), x + 15, y + 30 + 3 + offsetY, -1);

            offsetY += 18;
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

        int offsetY = 0;
        for(Screen s : screens) {
            if(isMouseOver(x + 5, y + 30 + offsetY, 70, 15, mouseX, mouseY)) {
                if(mouseButton == 0) {
                    currentScreen = s;
                    this.moveAnimation.setState(true);
                }
            }
            currentScreen.onClick(mouseX, mouseY, mouseButton);
            offsetY += 18;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;

        for(Screen s : screens) {
            currentScreen.onRelease(mouseX, mouseY, state);
        }
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
