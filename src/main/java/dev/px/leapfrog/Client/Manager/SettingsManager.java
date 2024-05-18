package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.Client.Module.Setting;

import java.util.ArrayList;

public class SettingsManager {

    private ArrayList<Setting> preferences;

    // Preferences static
    public Setting<Boolean> BACKGROUND;
    public Setting<Boolean> CHATANIMATIONS;
    public Setting<Boolean> CHATCLEAR;
    public Setting<Boolean> NCPCluster;
    public Setting<Boolean> ESPCluster;
    public Setting<Boolean> FRIENDS;
    public Setting<Boolean> windowModifications;
    public Setting<Boolean> HACKERDETECTOR;

    public SettingsManager() {
        this.preferences = new ArrayList<>();

        BACKGROUND = Add(new Setting("Element Background", true))
                .setDescription("Element Background on HUD Elements");
        CHATANIMATIONS = Add(new Setting("Chat Animation", true)).setDescription("Chat Animations");
        CHATCLEAR = Add(new Setting("Clear Chat", true)).setDescription("No text background");
        NCPCluster = Add(new Setting<>("NCP Cluster", true)
                .setDescription("Doesnt Render NCP on nametags/ESP"));
        ESPCluster = Add(new Setting<>("ESP Cluster", true)
                .setDescription("Reduces amount of Players on ESP"));
        FRIENDS = Add(new Setting("Friends", true)
                .setDescription("Initalizes friend system"));
        windowModifications = Add(new Setting<>("Window Modifications", true)
                .setDescription("Fixes Fullscreen bug (1.8.9)"));
        HACKERDETECTOR = Add(new Setting("Hacker Detector", true)
                .setDescription("Finds other hackers in your lobby"));
    }

    public <T> Setting Add(Setting preference) {
        this.preferences.add(preference);
        return preference;
    }

    public ArrayList<Setting> getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList<Setting> preferences) {
        this.preferences = preferences;
    }

}
