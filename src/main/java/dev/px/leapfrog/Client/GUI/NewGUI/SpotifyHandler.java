package dev.px.leapfrog.Client.GUI.NewGUI;

import de.labystudio.spotifyapi.model.MediaKey;
import dev.px.leapfrog.API.Util.Listener.Component;
import dev.px.leapfrog.API.Util.Render.Color.ColorUtil;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Manager.Structures.SpotifyManager;
import dev.px.leapfrog.LeapFrog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SpotifyHandler implements Component {

    private int x, y, width, height;
    private Color color;
    private RenderUtil.ScissorStack stack = new RenderUtil.ScissorStack();
    private RenderUtil.ScissorStack stack2 = new RenderUtil.ScissorStack();
    private boolean hoverRW = false, hoverFF = false;

    public SpotifyHandler(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    private void renderNull() {
        FontRenderer.sans18_bold.drawString("--/--", (getX() + 80) + 5, getY() + (3 + FontRenderer.sans18_bold.getHeight()), new Color(255, 255, 255).getRGB());
        FontRenderer.sans12.drawString("--/--", (getX() + 80) + 5, getY() + getHeight() - (2 + FontRenderer.sans18.getHeight()), new Color(162, 161, 161, 240).getRGB());
        FontRenderer.sans12.drawString("--", getX() + 40 + ((getWidth()) - 80) / 2 + 130, getY() + getHeight() - 10, -1);
        FontRenderer.sans12.drawString("--", getX() + 40 + ((getWidth()) - 80) / 2 - 10, getY() + getHeight() - 10, -1);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        //stack.pushScissor(getX(), getY(), getWidth(), getHeight());
        RoundedShader.drawRound(getX(), getY(), getWidth(), getHeight(), 4, color);

        if(LeapFrog.spotifyManager.getAPI().isInitialized() && LeapFrog.spotifyManager.getAPI().isConnected()) {
            if(LeapFrog.spotifyManager.getAPI().hasPosition()) {
                try {
                    stack2.pushScissor(getX() + 80, getY(), (int) (((getWidth()) - 120) / 2 - (20 + FontRenderer.sans12.getStringWidth(SpotifyManager.formatDuration(LeapFrog.spotifyManager.getAPI().getPosition())))), getHeight());
                    FontRenderer.sans18_bold.drawString(LeapFrog.spotifyManager.getAPI().getTrack().getName(), (getX() + 80) + 5, getY() + (3 + FontRenderer.sans18_bold.getHeight()),   new Color(255, 255, 255).getRGB());
                    FontRenderer.sans12.drawString(LeapFrog.spotifyManager.getAPI().getTrack().getArtist(), (getX() + 80) + 5, getY() + getHeight() - (2 + FontRenderer.sans18.getHeight()), new Color(162, 161, 161, 240).getRGB());
                    stack2.popScissor();
                    FontRenderer.sans12.drawString("" + SpotifyManager.formatDuration(LeapFrog.spotifyManager.getAPI().getTrack().getLength()), getX() + 40 + ((getWidth()) - 80) / 2 + 130, getY() + getHeight() - 10, -1);
                    FontRenderer.sans12.drawString("" + SpotifyManager.formatDuration(LeapFrog.spotifyManager.getAPI().getPosition()),
                            getX() + 40 + ((getWidth()) - 80) / 2 - (10 + FontRenderer.sans12.getStringWidth(SpotifyManager.formatDuration(LeapFrog.spotifyManager.getAPI().getPosition()))),
                            getY() + getHeight() - 10, -1);
                } catch (IllegalStateException ignored) {
                    ignored.printStackTrace();
                }
            } else {
                renderNull();
            }
        }

        RoundedShader.drawRound(getX() + 40 + ((getWidth() - 80) / 2), getY() + getHeight() - 10, 120, 2, 1, color.darker());

        if(LeapFrog.spotifyManager.getAPI().hasPosition()) {
            double completionPercentage = (double) LeapFrog.spotifyManager.getAPI().getPosition() / LeapFrog.spotifyManager.getAPI().getTrack().getLength();
            int progressBarWidth = (int) (completionPercentage * 120);

            RenderUtil.drawBlurredShadow(getX() + 40 + ((getWidth() - 80) / 2), getY() + getHeight() - 10, progressBarWidth, 2, 5, ColorUtil.interpolateColorC(
                    ColorUtil.getClientColor(0, 190), ColorUtil.getClientColor(180, 190), 0.5f));

            RoundedShader.drawGradientRound(getX() + 40 + ((getWidth() - 80) / 2), getY() + getHeight() - 10, progressBarWidth, 2, 1,
                    ColorUtil.getClientColor(0, 190),
                    ColorUtil.getClientColor(90, 190),
                    ColorUtil.getClientColor(180, 190),
                    ColorUtil.getClientColor(270, 190));
        }

        stack.pushScissor(getX() + 40 + ((getWidth()) - 80) / 2, getY(), 120, getHeight());
        FontRenderer.sans20_bold.drawString(
                LeapFrog.spotifyManager.getAPI().isPlaying() ? LeapFrog.spotifyManager.getAPI().getTrack().getName() : "",
                getX() + 40 + ((getWidth()) - 80) / 2, getY() + getHeight() - (12 + FontRenderer.sans24_bold.getHeight()), -1);
        stack.popScissor();

        // Rewind
        FontUtil.icon24.drawString("E", getX() + 10, getY() + 12, -1);

        // Pause/Play
        FontUtil.icon24.drawString(LeapFrog.spotifyManager.getAPI().isPlaying() ? "B" : "C", getX() + (80 / 2) - (FontUtil.icon24.getStringWidth((LeapFrog.spotifyManager.getAPI().isPlaying() ? "B" : "C")) / 2), getY() + 12, -1);

        // Fast Forward
        FontUtil.icon24.drawString("D", getX() + 80 - (20), getY() + 12, -1);

        // Shadow over buttons
        hoverRW = isMouseOver(getX() + 6, getY() + 7, 25, 15, mouseX, mouseY);
        hoverFF = isMouseOver((getX() + 80 - (20)) - 4, getY() + 7, 25, 15, mouseX, mouseY);
        if(hoverFF) {
            RenderUtil.drawBlurredShadow((float) getX() + 80 - (20), getY() + 12, 7, 7, 14, new Color(255, 255, 255, 100));

        } else if(hoverRW) {
            RenderUtil.drawBlurredShadow((float) getX() + 10, getY() + 12, 7, 7, 14, new Color(255, 255, 255, 100));
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) throws IOException {
        if(isMouseOver(getX() + 6, getY() + 7, 25, 15, mouseX, mouseY)) {
            LeapFrog.spotifyManager.getAPI().pressMediaKey(MediaKey.PREV);
        }
        if(isMouseOver((float) (getX() + (80 / 2) - (FontUtil.icon24.getStringWidth((LeapFrog.spotifyManager.getAPI().isPlaying() ? "B" : "C")) / 2)) + 4, getY() + 7, 15, 15, mouseX, mouseY)) {
            LeapFrog.spotifyManager.getAPI().pressMediaKey(MediaKey.PLAY_PAUSE);
        }
        if(isMouseOver((getX() + 80 - (20)) - 4, getY() + 7, 25, 15, mouseX, mouseY)) {
            LeapFrog.spotifyManager.getAPI().pressMediaKey(MediaKey.NEXT);
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onType(char typedChar, int keyCode) throws IOException {

    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
