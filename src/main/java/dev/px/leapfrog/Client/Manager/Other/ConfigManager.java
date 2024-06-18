package dev.px.leapfrog.Client.Manager.Other;

import dev.px.leapfrog.API.Config.Config;
import dev.px.leapfrog.LeapFrog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private ArrayList<Config> configs = new ArrayList<>();

    public ConfigManager() {
        this.loadConfigs();
    }

    public void loadConfigs() {
        configs.clear();

        File[] configDirs = LeapFrog.fileManager.getConfigDir().listFiles(File::isDirectory);

        if (configDirs != null) {
            for (File configDir : configDirs) {
                Config config = new Config(configDir.getName());
                configs.add(config);
            }
        }
    }

    public void saveConfigs() throws IOException {
        for (Config config : configs) {
            File configDir = new File(LeapFrog.fileManager.getConfigDir(), config.getName());
            if (!configDir.exists()) {
                configDir.mkdirs(); // Create directory if it doesn't exist
            }
        }
    }

    public void addConfig(String folderName) {
        Config newConfig = new Config(folderName);
        configs.add(newConfig);

        File configDir = new File(LeapFrog.fileManager.getConfigDir(), folderName);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        newConfig.save();
        try {
            this.saveConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(String name) {
        for(Config c : configs) {
            if(c.getName().equalsIgnoreCase(name)) {
                c.load();
            }
        }
    }

}
