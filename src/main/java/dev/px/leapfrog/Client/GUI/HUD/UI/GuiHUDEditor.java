package dev.px.leapfrog.Client.GUI.HUD.UI;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Math.ADT.Pair;
import dev.px.leapfrog.API.Util.Math.GridSystem;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Direction;
import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Impl.EaseBackIn;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.CompactGUI.ClickGUI;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.GUI.ClickGUI.DropdownGUI.Structure.DropdownFrame;
import dev.px.leapfrog.Client.GUI.HUD.UI.Components.ElementFrame;
import dev.px.leapfrog.Client.Module.Render.HUD;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiHUDEditor extends GuiScreen {

    private boolean backToGui;
    private ScaledResolution sr;
    private GridSystem grid;
    private DropdownFrame frame;
    private Pair<Animation, Animation> animations = Pair.of(
            new EaseBackIn(400, 1, 2f),
            new EaseBackIn(400, .4f, 2f));

    public GuiHUDEditor(boolean backToGui) {
        this.backToGui = backToGui;
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
        this.grid = new GridSystem(sr.getScaledWidth(), sr.getScaledHeight(), 0.5f, 10);
        this.frame = new ElementFrame("HUD Editor", 50, 50, 105, 15, animations);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.grid = new GridSystem(sr.getScaledWidth(), sr.getScaledHeight(), LeapFrog.moduleManager.getModuleByClass(HUD.class).snapStrength.getValue(), LeapFrog.moduleManager.getModuleByClass(HUD.class).gridDistance.getValue());
        animations.use((fade, opening) -> {
            fade.setDirection(Direction.FORWARDS);
            opening.setDirection(Direction.FORWARDS);
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        frame.render(mouseX, mouseY);
        for(Element e : LeapFrog.elementManager.getElements()) {
            if(e.isToggled()) {
                e.editMode(mouseX, mouseY);
                e.onRender(new Render2DEvent(partialTicks));
            }
        }
        grid.render();

        // chatGPT :)
        int boxWidth = 35;
        int boxHeight = 15;
        int boxX = sr.getScaledWidth() / 2 - boxWidth / 2;
        int boxY = sr.getScaledHeight() / 2 - boxHeight / 2;
        RoundedShader.drawGradientRound(boxX, boxY, boxWidth, boxHeight, 4,
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[0], 120),
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[1], 120),
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[2], 120),
                ColorUtil.applyOpacity(ColorUtil.getClientColorInterpolation()[3], 120)
        );

        double textWidth = FontRenderer.sans24_bold.getStringWidth("GUI");
        double textHeight = FontRenderer.sans24_bold.getHeight();
        double textX = sr.getScaledWidth() / 2 - textWidth / 2;
        double textY = sr.getScaledHeight() / 2 - textHeight / 2;

        FontRenderer.sans24_bold.drawString("GUI", textX, textY, -1);        grid.render();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(Element e : LeapFrog.elementManager.getElements()) {
            e.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if(isMouseOver(sr.getScaledWidth() / 2 - 35 + 10, sr.getScaledHeight() / 2 - 7, 35, 15, mouseX, mouseY)) {
            if(mouseButton == 0) {
                mc.displayGuiScreen(new ClickGUI());
            }
        }

        frame.onClick(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(Element e : LeapFrog.elementManager.getElements()) {
            e.mouseRelease(mouseX, mouseY, state);
        }

        frame.onRelease(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            if(backToGui) {
                mc.displayGuiScreen(new ClickGUI());
            } else {
                mc.currentScreen = null;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public boolean isBackToGui() {
        return backToGui;
    }

    public void setBackToGui(boolean backToGui) {
        this.backToGui = backToGui;
    }

    public ScaledResolution getSr() {
        return sr;
    }

    public void setSr(ScaledResolution sr) {
        this.sr = sr;
    }

    public GridSystem getGrid() {
        return grid;
    }

    public void setGrid(GridSystem grid) {
        this.grid = grid;
    }

    public DropdownFrame getFrame() {
        return frame;
    }

    public void setFrame(DropdownFrame frame) {
        this.frame = frame;
    }
}
