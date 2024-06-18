package dev.px.leapfrog.Client.GUI.ClickGUI.Components;

import dev.px.leapfrog.API.Module.Bind;
import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Animation.Animation;
import dev.px.leapfrog.API.Util.Render.Animation.Easing;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.Panels.ModulePanel;
import dev.px.leapfrog.Client.GUI.ClickGUI.Components.Setting.*;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleButton implements Component {

    private Module module;
    private float x;
    private float y;
    private float width;
    private float height;
    private float featureHeight;
    private float settingOffset;
    private boolean open;
    private Color color;
    private ModulePanel panel;
    private Animation toggleAnimation = new Animation(200, false, Easing.LINEAR);
    private Animation openAnimation = new Animation(200, false, Easing.LINEAR);
    private Animation hover = new Animation(100, false, Easing.LINEAR);
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();
    private ArrayList<SettingButton<?>> settingButtons = new ArrayList<>();

    public ModuleButton(Module module, int x, int y, int width, Color color, ModulePanel modulePanel) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = 25;
        this.featureHeight = 0;
        this.open = false;
        this.color = color;
        this.panel = modulePanel;

        int offsetY = (int) getHeight();
        if(this.module.getSettings() != null) {
            for(Setting s : module.getSettings()) {
                if(s.getValue() instanceof Boolean) {
                    BooleanButton b = new BooleanButton(this, getX(), getY() + offsetY, s);
                    this.settingButtons.add(b);
                    offsetY += b.getHeight();
                }
                if(s.getValue() instanceof Number) {
                    SliderButton b = new SliderButton(this, getX(), getY() + offsetY, s);
                    this.settingButtons.add(b);
                    offsetY += b.getHeight();
                }
                if(s.getValue() instanceof Enum) {
                    EnumButton b = new EnumButton(this, getX(), getY() + offsetY, s);
                    this.settingButtons.add(b);
                    offsetY += b.getHeight();
                }


                // Keybind
                if(s.getValue() instanceof Bind) {
                    KeybindButton b = new KeybindButton(this, getX(), getY() + offsetY, s);
                    this.settingButtons.add(b);
                    offsetY += b.getHeight();
                }
            }
            DrawnButton b = new DrawnButton(this, getX(), getY() + offsetY, new Setting<>("Drawn", new AtomicBoolean(true)));
            this.settingButtons.add(b);
            offsetY += b.getHeight();
        }
    }

    public void initGUI() {
        this.toggleAnimation.setState(this.module.isToggled());
        this.openAnimation.setState(open);

        for(SettingButton s : this.settingButtons) {
            s.initGUI();
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        hover.setState(isMouseOver(x, y, width, height, mouseX, mouseY));
        this.toggleAnimation.setState(this.module.isToggled());
        this.openAnimation.setState(open);
        this.featureHeight = 0;

        // Featureheight handler
        // SettingButton position must be handled before ModuleButton rendering
        if(openAnimation.getAnimationFactor() > 0) {
            this.settingOffset = (float) 0;
            this.settingButtons.forEach(settingButton -> {
                if (settingButton.getX() != this.getX() + 4) {
                    settingButton.setX(getX() + 4); // fix this
                }

                if(settingButton.getWidth() != getWidth() - 8) {
                    settingButton.setWidth(getWidth() - 8);
                }
                if(settingButton.getY() != getY() + settingOffset) {
                    settingButton.setY(getY() + getHeight() + (settingOffset + 1));
                }

                if (settingButton.getSetting().isVisible()) {
                    this.featureHeight += (settingButton.getHeight() + 1) * openAnimation.getAnimationFactor();
                    this.settingOffset += (settingButton.getHeight() + 1);
                    this.panel.addFeatureOffset(featureHeight * (float) openAnimation.getAnimationFactor());
                }
            });
        }
        // Setting background
        RoundedShader.drawRound(getX() + 4, y, getWidth() - 8, height + (featureHeight * (float) openAnimation.getAnimationFactor()), 2, color);


        if(hover.getAnimationFactor() > 0) {
            RoundedShader.drawRoundOutline(getX() + 4, y, getWidth() - 8, height, 4, 0.5f, new Color(0, 0, 0, 0), new Color(255, 255, 255, 150));
        }

        if(toggleAnimation.getAnimationFactor() > 0) {
          //  RoundedShader.drawGradientCornerRL(getX() + getWidth() - 20 - (5 * (float) toggleAnimation.getAnimationFactor()), getY() + 10 - (5 * (float) toggleAnimation.getAnimationFactor()), 15 * (float) toggleAnimation.getAnimationFactor(), 15 * (float) toggleAnimation.getAnimationFactor(), 2,
          //          new Color(LeapFrog.colorManager.getClientColor().getMainColor().getRed(), LeapFrog.colorManager.getClientColor().getMainColor().getGreen(), LeapFrog.colorManager.getClientColor().getMainColor().getBlue()),
          //          new Color(LeapFrog.colorManager.getClientColor().getAlternativeColor().getRed(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getGreen(), LeapFrog.colorManager.getClientColor().getAlternativeColor().getBlue()));
            RoundedShader.drawGradientRound(getX() + getWidth() - 20 - (5 * (float) toggleAnimation.getAnimationFactor()), getY() + 10 - (5 * (float) toggleAnimation.getAnimationFactor()), 15 * (float) toggleAnimation.getAnimationFactor(), 15 * (float) toggleAnimation.getAnimationFactor(), 2, ColorUtil.getClientColor(0, 255), ColorUtil.getClientColor(90, 255), ColorUtil.getClientColor(180, 255), ColorUtil.getClientColor(270, 255));
        }

        //FontUtil.regular_bold18.drawString(module.getName(), getX() + 5, getY() + (getHeight() /2 - 3), -1);
        FontUtil.regular_bold18.drawString(module.getName(), getX() + 8, getY() + 5, -1);
        FontUtil.regular12.drawString(module.getDescription(), getX() + 8, getY() + (getHeight() - 3) - FontUtil.regular12.getHeight(), new Color(200, 200, 200).getRGB());

        // Button rendering
        // Button position handling must come before
        if(openAnimation.getAnimationFactor() > 0) {
          //  stack.pushScissor((int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) height + ((int)featureHeight * (int) openAnimation.getAnimationFactor()));
            this.settingButtons.forEach(settingButton -> {
                if (settingButton.getSetting().isVisible()) {
                    settingButton.draw(mouseX, mouseY);
                }
            });
          //  stack.popScissor();
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(x, y, width, height, mouseX, mouseY)) {
            if(button == 0) {
                this.module.toggle();
                this.toggleAnimation.setState(this.module.isToggled());
            }
            if(button == 1) {
                this.open = !this.open;
                this.openAnimation.setState(open);
            }
        }

        if(!this.isOpen()) return;

        for(SettingButton b : settingButtons) {
            b.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        if(!this.isOpen()) return;
        for(SettingButton b : settingButtons) {
            b.mouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {
        if(!this.isOpen()) return;
        for(SettingButton b : settingButtons) {
            b.keyTyped(typedChar, keyCode);
        }
    }

    private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public float getFeatureHeight() {
        return featureHeight;
    }

    public void setFeatureHeight(float featureHeight) {
        this.featureHeight = featureHeight;
    }

    public float getSettingOffset() {
        return settingOffset;
    }

    public void setSettingOffset(float settingOffset) {
        this.settingOffset = settingOffset;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ModulePanel getPanel() {
        return panel;
    }

    public void setPanel(ModulePanel panel) {
        this.panel = panel;
    }

    public Animation getToggleAnimation() {
        return toggleAnimation;
    }

    public void setToggleAnimation(Animation toggleAnimation) {
        this.toggleAnimation = toggleAnimation;
    }

    public Animation getOpenAnimation() {
        return openAnimation;
    }

    public void setOpenAnimation(Animation openAnimation) {
        this.openAnimation = openAnimation;
    }

    public Animation getHover() {
        return hover;
    }

    public void setHover(Animation hover) {
        this.hover = hover;
    }

    public RenderUtil.ScissorStack getStack() {
        return stack;
    }

    public void setStack(RenderUtil.ScissorStack stack) {
        this.stack = stack;
    }

    public ArrayList<SettingButton<?>> getSettingButtons() {
        return settingButtons;
    }

    public void setSettingButtons(ArrayList<SettingButton<?>> settingButtons) {
        this.settingButtons = settingButtons;
    }

            /*
        if(openAnimation.getAnimationFactor() > 0) {
            //stack.pushScissor((int) getX() + 4, (int) (getY()), (int) getWidth() - 8, (int)getHeight() + (int) getFeatureHeight());
            this.settingOffset = (float) 0;
            RoundedShader.drawRound(getX() + 4, getY() + getHeight(), getWidth() - 8, (settingOffset * (float) openAnimation.getAnimationFactor()) - getHeight(), 4, color);
            this.settingButtons.forEach(settingButton -> {
                if (settingButton.getX() != this.getX() + 4) {
                    settingButton.setX(getX() + 4); // fix this
                }

                if(settingButton.getWidth() != getWidth() - 8) {
                    settingButton.setWidth(getWidth() - 8);
                }
                if(settingButton.getY() != getY() + settingOffset) {
                    settingButton.setY(getY() + getHeight() + (settingOffset + 1));
                }

                if (settingButton.getSetting().isVisible()) {
                    settingButton.draw(mouseX, mouseY);
                    this.featureHeight += (settingButton.getHeight() + 1) * openAnimation.getAnimationFactor();
                    this.settingOffset += (settingButton.getHeight() + 1);
                    this.panel.addFeatureOffset(featureHeight * (float) openAnimation.getAnimationFactor());
                }
            });
            //this.panel.addFeatureOffset(featureHeight);
             //stack.popScissor();
        }
         */
}
