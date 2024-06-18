package dev.px.leapfrog.API.Config;

import dev.px.leapfrog.API.Util.System.FileUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.LeapFrog;

import java.io.*;
import java.util.UUID;

public class Config {

    private String name;
    private File directory;

    public Config(String name) {
        this.name = name;
        this.directory = new File(LeapFrog.fileManager.getConfigDir(), name);
    }

    public void save() {
        FileUtil.saveModules(directory);
    }

    public void load() {
        loadModules();
    }



    private void loadModules() {
        File modulesFile = new File(directory, "modules.txt");
        if (!modulesFile.exists()) {
            LeapFrog.LOGGER.error("Modules file does not exist.");
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(modulesFile))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    for(Module m : LeapFrog.moduleManager.getModules()) {

                    }
                    String name = parts[0];
                    boolean toggled = Boolean.parseBoolean(parts[1]);
                    boolean drawn = Boolean.parseBoolean(parts[3]);

                    Module m = LeapFrog.moduleManager.getModuleByName(name);
                    m.setToggled(toggled);
                    m.setDrawn(drawn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
