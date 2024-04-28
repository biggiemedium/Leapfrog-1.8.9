package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.Client.Module.Setting;

import java.util.ArrayList;

public class SettingsManager {

    private ArrayList<Setting> preferences;

    // Preferences static
    public Setting<Boolean> BACKGROUND;
    public Setting<Boolean> NCPCluster;
    public Setting<Boolean> ESPCluster;
    public Setting<Boolean> FRIENDS;
    public Setting<Boolean> windowModifications;
    public Setting<Boolean> HACKERDETECTOR;

    public SettingsManager() {
        this.preferences = new ArrayList<>();

        BACKGROUND = Add(new Setting("Element Background", false));
        NCPCluster = Add(new Setting<>("NCP Cluster", true));
        ESPCluster = Add(new Setting<>("ESP Cluster", true));
        FRIENDS = Add(new Setting("Friends", true));
        windowModifications = Add(new Setting<>("Window Modifications", true));
        HACKERDETECTOR = Add(new Setting("Hacker Detector", true));
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
