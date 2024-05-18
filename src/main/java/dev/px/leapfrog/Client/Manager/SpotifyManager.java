package dev.px.leapfrog.Client.Manager;


import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.SpotifyAPIFactory;
import de.labystudio.spotifyapi.SpotifyListener;
import de.labystudio.spotifyapi.model.MediaKey;
import de.labystudio.spotifyapi.model.Track;
import de.labystudio.spotifyapi.open.OpenSpotifyAPI;
import dev.px.leapfrog.LeapFrog;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SpotifyManager {

    private SpotifyAPI API;
    private OpenSpotifyAPI openAPI;

    public SpotifyManager() {
        API = SpotifyAPIFactory.createInitialized();
        openAPI = API.getOpenAPI();
        if(API.isInitialized()) {
            init();
        } else {
            API.initialize();
        }
    }

    public void init() {
        API.registerListener(new SpotifyListener() {
            @Override
            public void onConnect() {
                LeapFrog.LOGGER.info("Connected to spotify");
            }

            @Override
            public void onTrackChanged(Track track) {
                LeapFrog.LOGGER.info("Changing tracks...");
            }

            @Override
            public void onPositionChanged(int position) {
                if (!API.hasTrack()) {
                    return;
                }


                int length = API.getTrack().getLength();
                float percentage = 100.0F / length * position;

                LeapFrog.LOGGER.info("Position changed: %s of %s (%d%%)\n", formatDuration(position), formatDuration(length), (int) percentage);
            }

            @Override
            public void onPlayBackChanged(boolean isPlaying) {
                LeapFrog.LOGGER.info(isPlaying ? "Song started playing" : "Song stopped");
            }

            @Override
            public void onSync() {

            }

            @Override
            public void onDisconnect(Exception exception) {
                LeapFrog.LOGGER.info("Disconnected from spotify");
            }
        });
    }

    public SpotifyAPI getAPI() {
        return API;
    }

    public OpenSpotifyAPI getOpenSpotifyAPI() {
        return openAPI;
    }

    public static String formatDuration(long ms) {
        Duration duration = Duration.ofMillis(ms);
        return String.format("%sm %ss", duration.toMinutes(), duration.getSeconds() - TimeUnit.MINUTES.toSeconds(duration.toMinutes()));
    }
}
