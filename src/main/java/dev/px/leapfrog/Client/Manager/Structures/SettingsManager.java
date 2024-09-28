package dev.px.leapfrog.Client.Manager.Structures;

import dev.px.leapfrog.Client.Module.Setting;

import java.util.ArrayList;

public class SettingsManager {

    private ArrayList<Setting> preferences;

    // Preferences static
    public Setting<Boolean> BACKGROUND;
    public Setting<Boolean> BLUR;
    public Setting<Boolean> FRIENDS;
    //public Setting<Boolean> RPC;
    public Setting<Boolean> WINDOWMODIFICATIONS;
    public Setting<Boolean> SCOREBOARD;
    public Setting<Boolean> TNTTIMER;
    public Setting<Boolean> HACKERDETECTOR;
    public Setting<Boolean> PLAYERLIGHTING;

    public SettingsManager() {
        this.preferences = new ArrayList<>();

        BACKGROUND = Add(new Setting("Element Background", true))
                .setDescription("Element Background on HUD Elements");
        BLUR = Add(new Setting("Blur", false))
                .setDescription("Blurs Minecrafts backgrounds");
        FRIENDS = Add(new Setting("Friends", true)
                .setDescription("Initalizes friend system"));
        //RPC = Add(new Setting("DiscordRPC", false)
        //        .setDescription("Displays Discord Rich Presence"));
        WINDOWMODIFICATIONS = Add(new Setting<>("Window Modifications", true)
                .setDescription("Fixes Fullscreen bug (1.8.9)"));
        SCOREBOARD = Add(new Setting("ScoreBoard", true))
                .setDescription("Scoreboard Modifcations");
        TNTTIMER = Add(new Setting("TNT Timer", true)
                .setDescription("Displays TNT Timer coundown above entity"));
        HACKERDETECTOR = Add(new Setting("Hacker Detector", true)
                .setDescription("Finds other hackers in your lobby"));
        PLAYERLIGHTING = Add(new Setting("Player Lighting", true)
                .setDescription("Removes Entity Lighting"));
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
