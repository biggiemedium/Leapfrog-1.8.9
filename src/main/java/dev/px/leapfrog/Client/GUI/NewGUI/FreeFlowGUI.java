package dev.px.leapfrog.Client.GUI.NewGUI;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.GLUtils;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.NewGUI.Screens.ModuleScreen.ModuleScreen;
import dev.px.leapfrog.Client.GUI.NewGUI.Screens.SettingsScreen;
import dev.px.leapfrog.Client.GUI.NewGUI.Screens.ThemeScreen;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class FreeFlowGUI extends GuiScreen {

    private int x, y, width, height;
    private Animation closeAnimation;
    private boolean dragging, close;
    private int dragX, dragY;

    private Color white = Color.decode("#f7f7f7");
    private Color lightGrey = Color.decode("#b0b0b0");
    private Color darkGrey = Color.decode("#121212");
    private Color grey = Color.decode("#444444");

    private ArrayList<ScreenHandler> screens;
    private ScreenHandler currentScreen;

    private Animation spotifyAnimation = new Animation(300,(LeapFrog.spotifyManager.getAPI() != null && LeapFrog.spotifyManager.getAPI().isPlaying()), Easing.LINEAR);
    //private SpotifyHandler spotifyHandler;

    public FreeFlowGUI() {
        this.x = 100;
        this.y = 100;
        this.width = 370 + 30; // Old 370
        this.height = 260 + 30; // Old 260
        this.close = false;
        this.dragging = false;
        this.closeAnimation = new Animation(250, true, Easing.TENACITY_EASEBACKIN);

        //this.spotifyHandler = new SpotifyHandler(getX(), getY() - 25, getWidth(), 25, new Color(23, 23, 23));
        this.screens = new ArrayList<>();

        this.screens.add(new ModuleScreen(getX() + 100, getY(), getWidth() - 100, getHeight(), this, ScreenHandler.ScreenType.Game));
        this.screens.add(new SettingsScreen(getX() + 100, getY(), getWidth() - 100, getHeight(), this, ScreenHandler.ScreenType.Game));
        this.screens.add(new ThemeScreen(getX() + 100, getY(), getWidth() - 100, getHeight(), this, ScreenHandler.ScreenType.Appearance));
        this.currentScreen = screens.get(0);
    }

    @Override
    public void initGui() {
        this.closeAnimation = new Animation(250, true, Easing.TENACITY_EASEBACKIN);
        this.close = false;
        closeAnimation.setState(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.spotifyAnimation.setState(LeapFrog.spotifyManager.getAPI() != null && LeapFrog.spotifyManager.getAPI().isPlaying());

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
        // Entire GUI
        RenderUtil.drawBlurredShadow(getX(), getY(), getWidth(), getHeight(), 10, new Color(30, 30, 30));
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, new Color(30, 30, 30));

        // Left side bar
        RoundedShader.drawRound(getX(), getY(), 80, getHeight(), 4, new Color(28, 28, 28));

        // Top bar displaying screen name
        RoundedShader.drawRound(getX(), getY(), getWidth(), 25, 4, new Color(28, 28, 28));

        // Spotify bar
        //spotifyHandler.setX(getX());
        //spotifyHandler.setY(getY() + getHeight() - 25);
        //spotifyHandler.setHeight(30);
        //spotifyHandler.render(mouseX, mouseY);

        // white bar dividing stuff
        RenderUtil.drawRect(getX() + 80, getY() + 1, 0.4f, getHeight() - 1, new Color(190, 190, 190));
        RenderUtil.drawRect(getX() + 82, getY() + 25, getWidth() - 83, 0.4f, new Color(190, 190, 190));

        // Left side logo
        FontRenderer.sans24_bold.drawStringWithClientColor(LeapFrog.NAME, getX() + (80 / 2 - (FontRenderer.sans24.getStringWidth("Leapfrog") / 2)) - 6, getY() + 18 - (FontRenderer.sans24_bold.getHeight() / 2), false);
        FontRenderer.sans24_bold.drawStringWithClientColor(currentScreen.getName(), getX() + 87, getY() + 18 - FontRenderer.sans18.getHeight(), false);

        int offsetY = 0;
        for(ScreenHandler s : this.screens) {
            s.setX(x + 80);
            s.setY(y + 25);
            s.setWidth(width - 80);
            s.setHeight(height - 50);

            if(s == currentScreen) {
               // RoundedShader.drawGradientCornerRL(getX() + 5, getY() + 45 + offsetY, 70, 14, 4,
               //         new Color(LeapFrog.colorManager.getClientColor().getMainColor().getRed(), LeapFrog.colorManager.getClientColor().getMainColor().getGreen(), LeapFrog.colorManager.getClientColor().getMainColor().getBlue(), 190),
               //         new Color(LeapFrog.colorManager.getClientColor().getAlternativeColor().getRed(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getGreen(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getBlue(), 190));

                RoundedShader.drawGradientRound(getX() + 5, getY() + 45 + offsetY, 70, 14, 4,
                        ColorUtil.getClientColor(0, 190),
                        ColorUtil.getClientColor(90, 190),
                        ColorUtil.getClientColor(180, 190),
                        ColorUtil.getClientColor(270, 190));
            }

            currentScreen.render(mouseX, mouseY);

            FontRenderer.sans20_bold.drawString(s.getName(), getX() + 8, getY() + 50 + offsetY, -1);
            GL11.glPushMatrix();
            GL11.glColor3d(1, 1, 1);

            /*
            Texture t = new Texture(s.getIcon());
            if(t.getLocation() != null) {
                t.renderT(getX() + 55, getY() + 45 + offsetY, 15, 15);
            }
             */

            GL11.glPopMatrix();
            offsetY += 18;
        }

        GLUtils.stopScale();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        //this.spotifyHandler.onClick(mouseX, mouseY, mouseButton);

        int offsetY = 0;
        for(ScreenHandler s : screens) {
            if(isMouseOver(x + 5, y + 45 + offsetY, 70, 15, mouseX, mouseY)) {
                if(mouseButton == 0) {
                    currentScreen = s;
                }
            }
            currentScreen.onClick(mouseX, mouseY, mouseButton);
            offsetY += 18;
        }

        if(isMouseOver(x, y, width, 25, mouseX, mouseY)) {
            if(mouseButton == 0) {
                this.dragging = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
        for(ScreenHandler s : screens) {
            currentScreen.onRelease(mouseX, mouseY, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            close = true;
        }
        if(keyCode == Keyboard.KEY_UP) {

        }
        if(keyCode == Keyboard.KEY_DOWN) {

        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
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
}
