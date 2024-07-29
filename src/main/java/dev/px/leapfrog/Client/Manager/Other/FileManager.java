package dev.px.leapfrog.Client.Manager.Other;

import dev.px.leapfrog.API.Util.System.FileUtil;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileManager {

    private File directory;
    private File configDir;
    private File preferences;

    public FileManager() {
        directory = new File(Minecraft.getMinecraft().mcDataDir, "LeapFrog");

        configDir = new File(directory + File.separator + "Configs");
        preferences = new File(directory + File.separator + "Preferences");

        FileUtil.createFile(directory);
        FileUtil.createFile(configDir);
        FileUtil.createFile(preferences);
        load();
    }

    public void load() {
        FileUtil.loadModules(preferences);
        FileUtil.loadSettings(preferences);
    }

    public void save() {
        FileUtil.saveModules(preferences);
        FileUtil.saveSettings(preferences);
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public File getConfigDir() {
        return configDir;
    }

    public void setConfigDir(File configDir) {
        this.configDir = configDir;
    }
}
