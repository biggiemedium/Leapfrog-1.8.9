package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;

@Element.ElementInterface(name = "Spotify", description = "Displays currently playing")
public class SpotifyElement extends Element {

    public SpotifyElement() {
        super(75, 2);
    }

    private Setting<Mode> mode = create(new Setting("Mode", Mode.Text));

    @Override
    public void onRender(Render2DEvent event) {
        switch (mode.getValue()) {
            case Text:
                font.drawStringWithClientColor(getSongText(), getX(), getY(), true);
                setWidth((float) font.getStringWidth(getSongText()));
                setHeight((int) font.getHeight());
                break;
            case Box:

                drawBackground(getX(), getY(), getWidth(), getHeight());
                font.drawString(LeapFrog.spotifyManager.getAPI().getTrack() == null
                        ? "No song.." : LeapFrog.spotifyManager.getAPI().getTrack().getName(), getX() + 2, getY() + 5,
                        LeapFrog.colorManager.getClientColor() == LeapFrog.colorManager.getAccentColorByName("White") ? new Color(20, 20, 20).getRGB() : 255);
                setWidth(75);
                setHeight(75);
                break;
        }
    }

    private String getSongText() {
        if(LeapFrog.spotifyManager.getAPI().isPlaying()) {
            return "[" +
                    LeapFrog.spotifyManager.getAPI().getTrack().getName() + " - " +
                    LeapFrog.spotifyManager.getAPI().getTrack().getArtist() + "]";
        } else {
            return "No song playing...";
        }
    }

    private enum Mode {
        Text,
        Box
    }

}
