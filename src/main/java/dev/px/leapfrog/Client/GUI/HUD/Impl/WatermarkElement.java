package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Util.Render.Texture;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.util.ResourceLocation;

@Element.ElementInterface(name = "Watermark", description = "Shows client logo", visible = true)
public class WatermarkElement extends Element {

    public WatermarkElement() {
        super(1, 2);
    }

    private Setting<Mode> mode = create(new Setting("Mode", Mode.Text));
    private Setting<Boolean> version = create(new Setting("Version", true, v -> mode.getValue() == Mode.Text));

    private Texture texture = new Texture(new ResourceLocation("Leapfrog/Images/Froggy.png"));

    @Override
    public void onRender(Render2DEvent event) {
        switch (mode.getValue()) {
            case Text:
                font.drawStringWithClientColor(LeapFrog.NAME + (version.getValue() ? " v " + LeapFrog.VERSION : ""), getX(), getY(), true);
                setWidth((float) font.getStringWidth(LeapFrog.NAME + (version.getValue() ? " v " + LeapFrog.VERSION : "")));
                setHeight((float) font.getHeight());
                break;
            case Logo:
                texture.renderT(getX(), getY(), 25, 25);
                setWidth(25);
                setHeight(25);
                break;
        }
    }

    private enum Mode {
        Text,
        Logo
    }
}
