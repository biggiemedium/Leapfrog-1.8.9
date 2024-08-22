package dev.px.leapfrog.Client.Command.Impl;

import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.Client.Command.Command;
import dev.px.leapfrog.LeapFrog;

import java.io.IOException;

@Command.CommandInterface(name = "Config", description = "Loads config saves", arguments = {"config", "cfg", "configs"})
public class ConfigCommand extends Command {

    public ConfigCommand() {

    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            ChatUtil.sendClientSideMessage("Usage: config <load|save|add> <configName>");
            return;
        }

        String action = args[0];
        String configName = args[1];

        LeapFrog.LOGGER.info("Action: " + action);
        LeapFrog.LOGGER.info("Config Name: " + configName);

        switch (action.toLowerCase()) {
            case "load":
                loadConfig(configName);
                break;
            case "save":
                saveConfig(configName);
                break;
            case "add":
                addConfig(configName);
                break;
        }
    }

    private void loadConfig(String configName) {
        if (LeapFrog.configManager == null) {
            LeapFrog.LOGGER.error("ConfigManager is not initialized.");
            return;
        }

        LeapFrog.configManager.loadConfig(configName);
        LeapFrog.LOGGER.info("Loaded config: " + configName);
    }

    private void saveConfig(String configName) {
        if (LeapFrog.configManager == null) {
            LeapFrog.LOGGER.error("ConfigManager is not initialized.");
            return;
        }

        try {
            LeapFrog.configManager.saveConfigs();
            LeapFrog.LOGGER.info("Saved all configs.");
        } catch (IOException e) {
            LeapFrog.LOGGER.info("Failed to save configs: " + e.getMessage());
        }
    }

    private void addConfig(String configName) {
        if (LeapFrog.configManager == null) {
            LeapFrog.LOGGER.error("ConfigManager is not initialized.");
            return;
        }

        LeapFrog.configManager.addConfig(configName);
        LeapFrog.LOGGER.info("Added new config: " + configName);
    }
}
