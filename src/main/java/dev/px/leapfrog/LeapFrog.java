package dev.px.leapfrog;

import dev.px.leapfrog.API.Gui.CustomMainMenu;
import dev.px.leapfrog.API.Util.EventProcessor;
import dev.px.leapfrog.API.Util.Render.Font.FontRenderer;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.Client.Manager.Other.*;
import dev.px.leapfrog.Client.Manager.Player.PositionManager;
import dev.px.leapfrog.Client.Manager.Structures.*;
import me.zero.alpine.fork.bus.EventBus;
import me.zero.alpine.fork.bus.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = LeapFrog.MODID, version = LeapFrog.VERSION)
public class LeapFrog {

    /*
    - Notifcation system
    - HUD (Draggable HUD, Grid system)
    - GUI Theme - HUD font color picker
    - Improve HUD Editor screen
    - Fix Main Menu
    - Rotation, anticheat, speed, position manager
    - Fix Keybinding
     */

    public static final String MODID = "leapfrog";
    public static final String VERSION = "1.0";
    public static final String NAME = "LeapFrog";

    public static EventBus EVENT_BUS = new EventManager();
    public static Logger LOGGER = LogManager.getLogger(NAME);

    public static ModuleManager moduleManager;
    public static EventProcessor eventProcessor;
    public static ColorManager colorManager;
    public static SettingsManager settingsManager;
    public static ElementManager elementManager;
    public static CapeManager capeManager;
    public static SpotifyManager spotifyManager;
    public static InputManager inputManager;
    //public static DiscordManager discordManager;
    public static MultiThreadingManager threadManager;
    public static ServerManager serverManager;
    public static NotificationManager notificationManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static PositionManager positionManager;

    public static CustomMainMenu menu;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        threadManager = new MultiThreadingManager();
        spotifyManager = new SpotifyManager();
        FontUtil.init();
        FontRenderer.init();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        menu = new CustomMainMenu();
        moduleManager = new ModuleManager();
        settingsManager = new SettingsManager(); // Settings manager before event processor
        eventProcessor = new EventProcessor();
        colorManager = new ColorManager(); // Color manager before element manager
        elementManager = new ElementManager();
        capeManager = new CapeManager();
        serverManager = new ServerManager();
        notificationManager  = new NotificationManager();
        //discordManager = new DiscordManager(); // MultiThread Manager must come before discord manager
        //discordManager.start();
        fileManager = new FileManager();
        configManager = new ConfigManager();
        positionManager = new PositionManager();
        // way down
        inputManager = new InputManager(); // put this after everything bc it calls on ColorManager, Module Manager, Settings Manager, Element Manager
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    public static void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //discordManager.shutDown();
            threadManager.shutDown();
        }));
    }
}
