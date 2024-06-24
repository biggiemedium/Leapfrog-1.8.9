package dev.px.leapfrog.API.Gui;

import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.GLSLSandboxShader;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.API.Util.Render.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.IOException;

/**
 * @see net.minecraft.client.gui.GuiMainMenu
 */
public class CustomMainMenu extends GuiScreen {

    private GLSLSandboxShader backgroundShader;
    private Texture texture;
    private MenuButton modButton;
    private long time = -1;

    public CustomMainMenu() {
        try {
            //this.backgroundShader = new GLSLSandboxShader("/assets/minecraft/Leapfrog/Shaders/MenuShaders/background.fsh");
            this.backgroundShader = new GLSLSandboxShader("/assets/minecraft/Leapfrog/Shaders/MenuShaders/wave.fsh");
            this.texture = new Texture(new ResourceLocation("Leapfrog/Images/Froggy.png"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background shader", e);
        }
    }

    @Override
    public void initGui() {
        time = System.currentTimeMillis();
        int y = this.height / 4 + 55;

        ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();

        this.buttonList.clear();
        this.buttonList.add(new MenuButton(0, this.width / 2 - 100, y, I18n.format("menu.singleplayer")));
        this.buttonList.add(new MenuButton(1, this.width / 2 - 100, y + 26 * 1, I18n.format("menu.multiplayer")));
        this.buttonList.add(new MenuButton(2, this.width / 2 - 100, y + 26 * 2, "Settings"));
        this.buttonList.add(modButton = new MenuButton(3, this.width / 2 - 100, y + 26 * 3, 98 * 2 + 4, 20, "Mods"));
        this.buttonList.add(new MenuButton(4, this.width / 2 - 100, y + 26 * 4, 98 * 2 + 4, 20, "Exit"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int y = this.height / 4 + 55;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.disableCull();
        this.backgroundShader.useShader((int) (sr.getScaledWidth() * 2.0f), (int) (sr.getScaledHeight() * 2.0f), (float) mouseX, (float) mouseY, (float) (System.currentTimeMillis() - time) / 1000.0F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        texture.renderT(this.width / 2, y - 50, 50, 50);
        super.drawScreen(mouseX, mouseY, partialTicks);
        //FontRenderer.sans40_bold.drawString("LeapFrog", this.width / 2 - FontRenderer.sans40_bold.getStringWidth("LeapFrog"), y - FontRenderer.sans40_bold.getHeight(), -1);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if(button.id == 1) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if(button.id == 2) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }
        if(button.id == 3) {
            mc.displayGuiScreen(new GuiModList(this));
        }

        if(button.id == 4) {
            mc.shutdown();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public static class MenuButton extends GuiButton  {

        private Animation hoverAnimation;

        public MenuButton(int buttonId, int x, int y, int width, int height, String buttonText) {
            super(buttonId, x, y, width, height, buttonText);
            this.hoverAnimation = new Animation(300, false, Easing.LINEAR);
        }

        public MenuButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, 200, 20, buttonText);
            this.hoverAnimation = new Animation(300, false, Easing.LINEAR);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                FontRenderer.sans24_bold.drawString(this.displayString,
                        this.xPosition + (width / 2D) - (FontRenderer.sans24_bold.getStringWidth(this.displayString) / 2),
                        this.yPosition + (height / 2D) - (FontRenderer.sans24_bold.getHeight() / 2), -1);
                RoundedShader.drawRoundOutline(xPosition, yPosition, width, height, 4, 1,
                        this.hovered ? new Color(200, 200, 200, 100) : new Color(255, 255, 255, 100), new Color(255, 255, 255));
                hoverAnimation.setState(this.hovered);
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        }

        private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
            return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
        }
    }
}
