package dev.px.leapfrog.Client.GUI.AltManager;

import dev.px.leapfrog.API.Gui.CustomMainMenu;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

public class AltManagerGui extends GuiScreen {

    private GuiScreen mainMenu;
    private GuiButton exit;

    private ScaledResolution sr = new ScaledResolution(mc);

    public AltManagerGui(GuiScreen menu) {
        this.mainMenu = menu;
        this.exit = new GuiButton(1, ((sr.getScaledWidth() / 2)), ((sr.getScaledHeight() / 2) + (mc.fontRendererObj.FONT_HEIGHT)), "Exit");
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.drawDefaultBackground();
        mc.fontRendererObj.drawString("Coming Soon!", (sr.getScaledWidth() / 2) - (mc.fontRendererObj.getStringWidth("Coming Soon!") / 2), sr.getScaledHeight() / 2, -1);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }

    @Override
    public void drawBackground(int p_drawBackground_1_) {
        super.drawBackground(p_drawBackground_1_);
    }

    @Override
    protected void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }

    @Override
    protected void actionPerformed(GuiButton p_actionPerformed_1_) throws IOException {
        switch (p_actionPerformed_1_.id) {
            case 1:
                mc.displayGuiScreen(new CustomMainMenu());
                break;
        }
        super.actionPerformed(p_actionPerformed_1_);
    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
    }
}
