package dev.px.leapfrog;

import dev.px.leapfrog.API.Util.EventProcessor;
import dev.px.leapfrog.API.Util.Render.Font.FontUtil;
import dev.px.leapfrog.Client.Manager.ColorManager;
import dev.px.leapfrog.Client.Manager.ElementManager;
import dev.px.leapfrog.Client.Manager.ModuleManager;
import dev.px.leapfrog.Client.Manager.SettingsManager;
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
    - GUI (Draggable HUD, Grid system)
    - GUI - Fix scrollbar (Module and client settings), Add hud editor
    - Notifcation system
    -
     */

    public static final String MODID = "leapfrog";
    public static final String VERSION = "1.0";
    public static final String NAME = "LeapFrog";

    public static EventBus EVENT_BUS = new EventManager();
    public static Logger LOGGER = LogManager.getLogger("[" + NAME + "]");

    public static ModuleManager moduleManager;
    public static EventProcessor eventProcessor;
    public static ColorManager colorManager;
    public static SettingsManager settingsManager;
    public static ElementManager elementManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        moduleManager = new ModuleManager();
        eventProcessor = new EventProcessor();
        colorManager = new ColorManager();
        settingsManager = new SettingsManager();
        elementManager = new ElementManager();
        FontUtil.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
