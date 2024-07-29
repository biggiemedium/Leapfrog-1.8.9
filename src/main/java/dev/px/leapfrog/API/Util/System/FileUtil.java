package dev.px.leapfrog.API.Util.System;

import dev.px.leapfrog.API.Module.Bind;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static void saveSettings(File directory) {
        /*
        try {
            File f = new File(directory, "Boolean.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for(Module s : LeapFrog.moduleManager.getModules()) {
                for(Setting set : s.getSettings()) {
                    if(set.getValue() instanceof Boolean) {
                        String v = (Boolean) set.getValue() ? "true" : "false";
                        writer.write(s.getName() + ":" + set.getName() + ":" + v + "\r\n");
                    }
                }
            }
            writer.close();
        } catch (Exception ignored) {}

         */
        try {
            File boolFile = new File(directory, "Boolean.txt");
            File floatFile = new File(directory, "Float.txt");
            File doubleFile = new File(directory, "Double.txt");
            File intFile = new File(directory, "Integer.txt");
            File enumFile = new File(directory, "Enum.txt");
            File colorFile = new File(directory, "Color.txt");

            BufferedWriter boolWriter = new BufferedWriter(new FileWriter(boolFile));
            BufferedWriter floatWriter = new BufferedWriter(new FileWriter(floatFile));
            BufferedWriter doubleWriter = new BufferedWriter(new FileWriter(doubleFile));
            BufferedWriter intWriter = new BufferedWriter(new FileWriter(intFile));
            BufferedWriter enumWriter = new BufferedWriter(new FileWriter(enumFile));
            BufferedWriter colorWriter = new BufferedWriter(new FileWriter(colorFile));

            ArrayList<Module> modules = LeapFrog.moduleManager.getModules();

            for (Module s : modules) {
                for (Setting set : s.getSettings()) {
                    Object value = set.getValue();
                    String line = s.getName() + ":" + set.getName() + ":" + value + "\r\n";

                    if (value instanceof Boolean) {
                        boolWriter.write(line);
                    } else if (value instanceof Float) {
                        floatWriter.write(line);
                    } else if (value instanceof Double) {
                        doubleWriter.write(line);
                    } else if (value instanceof Integer) {
                        intWriter.write(line);
                    } else if (value instanceof Enum) {
                        enumWriter.write(line);
                    } else if (value instanceof Color) {
                        // Assuming Color has a proper toString method, otherwise customize as needed
                        colorWriter.write(line);
                    }
                }
            }

            boolWriter.close();
            floatWriter.close();
            doubleWriter.close();
            intWriter.close();
            enumWriter.close();
            colorWriter.close();
        } catch (Exception ignored) {}
    }

    public static void loadSettings(File directory) {
        try {
            loadSettingFile(new File(directory, "Boolean.txt"), Boolean.class);
            loadSettingFile(new File(directory, "Float.txt"), Float.class);
            loadSettingFile(new File(directory, "Double.txt"), Double.class);
            loadSettingFile(new File(directory, "Integer.txt"), Integer.class);
            loadSettingFile(new File(directory, "Enum.txt"), Enum.class);
            loadSettingFile(new File(directory, "Color.txt"), Color.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSettingFile(File file, Class<?> type) throws IOException {
        if (!file.exists()) {
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 3) {
                String moduleName = parts[0].trim();
                String settingName = parts[1].trim();
                String value = parts[2].trim();

                Module module = LeapFrog.moduleManager.getModuleByName(moduleName);
                if (module != null) {
                    for (Setting setting : module.getSettings()) {
                        if (setting.getName().equalsIgnoreCase(settingName) && type.isInstance(setting.getValue())) {
                            if (type == Boolean.class) {
                                setting.setValue(Boolean.parseBoolean(value));
                            } else if (type == Float.class) {
                                setting.setValue(Float.parseFloat(value));
                            } else if (type == Double.class) {
                                setting.setValue(Double.parseDouble(value));
                            } else if (type == Integer.class) {
                                setting.setValue(Integer.parseInt(value));
                            } else if (type == Enum.class) {
                                // Assuming Enum has a valueOf method, adjust if necessary
                                setting.setValue(Enum.valueOf((Class<Enum>) setting.getValue().getClass(), value));
                            } else if (type == Color.class) {
                                // Assuming Color has a method to parse from a string, adjust if necessary
                                //setting.setValue(parseColor(value));
                            }
                        }
                    }
                }
            }
        }
        reader.close();
    }

    private static Color parseColor(String value) {
        if (value.startsWith("#")) {
            return Color.decode(value);
        } else {
            throw new IllegalArgumentException("Invalid color format: " + value);
        }
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
