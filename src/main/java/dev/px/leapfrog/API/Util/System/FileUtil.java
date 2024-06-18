package dev.px.leapfrog.API.Util.System;

import dev.px.leapfrog.API.Module.Bind;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.LeapFrog;

import java.io.*;

public class FileUtil {

    public static void createFile(File file) {
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    public static void saveModules(File directory) {
        File modules = new File(directory, "modules.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(modules));
            for (Module module : LeapFrog.moduleManager.getModules()) {
                try {
                    if (!module.getName().matches("null")
                            && !module.getName().equalsIgnoreCase("fakeplayer")) {
                        out.write(module.getName() + ":" + module.isToggled() + ":" + module.isDrawn() + ":" + module.keybind.getValue().getBind());
                        out.write("\r\n");
                    }
                } catch(Exception e) {}
            }
            out.close();
        }
        catch (Exception ignored) {}
    }

    public static void loadModules(File directory) { // WHY WONT THIS WORK WHY WHY WHY WHY WHY
        try {
            File file = new File(directory, "modules.txt");
            if (!file.exists()) {
                LeapFrog.LOGGER.error("modules.txt not found at: " + file.getAbsolutePath());
                return;
            }

            FileInputStream fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    try {
                        String name = parts[0].trim();
                        boolean toggled = Boolean.parseBoolean(parts[1].trim());
                        boolean drawn = Boolean.parseBoolean(parts[2].trim());
                        int bind = Integer.parseInt(parts[3].trim());

                        LeapFrog.LOGGER.info("Debug 1");
                        Module m = LeapFrog.moduleManager.getModuleByName(name);
                        LeapFrog.LOGGER.info("Debug 2");
                        if (m != null) {
                            LeapFrog.LOGGER.info("Debug 3: " + m.isToggled());
                            m.setToggled(toggled);
                            LeapFrog.LOGGER.info("Debug 4: " + m.isToggled());
                            m.setDrawn(drawn);
                            m.keybind.setValue(new Bind(bind));
                        } else {
                            LeapFrog.LOGGER.error("Module '" + name + "' not found in module manager.");
                        }
                    } catch (Exception e) {
                        LeapFrog.LOGGER.error(e.getMessage());
                    }
                } else {
                    LeapFrog.LOGGER.error("Invalid line format in modules.txt: " + line);
                }
            }
            br.close();
            LeapFrog.LOGGER.info("Debug 5");
        } catch (FileNotFoundException e) {
            LeapFrog.LOGGER.error("File not found: " + e.getMessage());
        } catch (IOException e) {
            LeapFrog.LOGGER.error("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            LeapFrog.LOGGER.error("Unexpected error: " + e.getMessage());
        }
    }

}
