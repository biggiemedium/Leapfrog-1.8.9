package dev.px.leapfrog.API.Util;

import dev.px.leapfrog.API.Account.Account;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerTeleportEvent;
import dev.px.leapfrog.API.Event.Player.PlayerTickEvent;
import dev.px.leapfrog.API.Event.Render.Overlays.RenderFireOverlayEvent;
import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Event.Render.RenderPlayerLighting;
import dev.px.leapfrog.API.Module.Setting.Bind;
import dev.px.leapfrog.API.Module.Toggleable;
import dev.px.leapfrog.Client.GUI.AltManager.AltManagerGui;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.GUI.HUD.UI.GuiHUDEditor;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Render.HUD;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

public class EventProcessor implements Listenable {

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
        LeapFrog.EVENT_BUS.subscribe(this);
    }

    private Minecraft mc = Minecraft.getMinecraft();
    private boolean isTeleporting = false;

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if(event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            Render2DEvent e = new Render2DEvent(event.partialTicks);
            LeapFrog.EVENT_BUS.post(e);

            for(Element element : LeapFrog.elementManager.getElements()) {
                if(!(mc.currentScreen instanceof GuiHUDEditor)) {
                    if(LeapFrog.moduleManager.isModuleToggled(HUD.class)) {
                        if (element.isToggled()) {
                            element.onRender(e);
                            element.renderDummy(e);
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if(mc.thePlayer == null || mc.theWorld == null) return;

        mc.mcProfiler.startSection("leapfrog");
        Render3DEvent render3dEvent = new Render3DEvent(event.partialTicks);
        LeapFrog.EVENT_BUS.post(render3dEvent);
        mc.mcProfiler.endSection();
    }

    @SubscribeEvent
    public void onRenderBlockOverlayEvent(RenderBlockOverlayEvent event) {
        if(event.overlayType == RenderBlockOverlayEvent.OverlayType.FIRE) {
            RenderFireOverlayEvent e = new RenderFireOverlayEvent();
            LeapFrog.EVENT_BUS.post(e);
            if(e.isCancelled()) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        LeapFrog.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void renderPlayerPre(RenderPlayerEvent.Pre event) {
        LeapFrog.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void renderTickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        LeapFrog.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        PlayerTickEvent e = new PlayerTickEvent(Event.Stage.Pre);
        LeapFrog.EVENT_BUS.post(e);
        LeapFrog.EVENT_BUS.post(event);
    }

    @EventHandler
    private Listener<RenderPlayerLighting> lightingListener = new Listener<>(event -> {
        if(LeapFrog.settingsManager.PLAYERLIGHTING.getValue()) { // TODO: fix
            event.cancel();
        }
    });

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        ServerData serverData = mc.getCurrentServerData();
        if (serverData != null) {
            String serverIP = serverData.serverIP;
            if (serverIP.endsWith("hypixel.net") || serverIP.endsWith("hypixel.io")) {
                LeapFrog.accountManager.load();
                for (Account account : LeapFrog.accountManager.accounts) {
                    if (mc.getSession().getUsername().equals(account.getUsername())) {
                        account.setUnban(0L);
                    }
                }
                LeapFrog.accountManager.save();
            }
        }
    }

    @SubscribeEvent
    public void initGuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiSelectWorld || event.gui instanceof GuiMultiplayer) {
            event.buttonList.add(new GuiButton(69, event.gui.width - 106, 6, 100, 20, "Accounts"));
        }

        if (event.gui instanceof GuiDisconnected) {
            try {
                Field f = ReflectionHelper.findField(GuiDisconnected.class, "message", "field_146304_f");
                IChatComponent message = (IChatComponent) f.get(event.gui);
                String text = message.getFormattedText().split("\n\n")[0];
                if (text.equals("§r§cYou are permanently banned from this server!")) {
                    LeapFrog.accountManager.load();
                    for (Account account : LeapFrog.accountManager.accounts) {
                        if (mc.getSession().getUsername().equals(account.getUsername())) {
                            account.setUnban(-1L);
                        }
                    }
                    LeapFrog.accountManager.save();
                    return;
                }

                if (text.matches("§r§cYou are temporarily banned for §r§f.*§r§c from this server!")) {
                    String unban = StringUtils.substringBetween(text, "§r§f", "§r§c");
                    if (unban != null) {
                        long time = System.currentTimeMillis();
                        for (String duration : unban.split(" ")) {
                            String type = duration.substring(duration.length() - 1);
                            long value = Long.parseLong(duration.substring(0, duration.length() - 1));
                            switch (type) {
                                case "d": {
                                    time += value * 86400000L;
                                }
                                break;
                                case "h": {
                                    time += value * 3600000L;
                                }
                                break;
                                case "m": {
                                    time += value * 60000L;
                                }
                                break;
                                case "s": {
                                    time += value * 1000L;
                                }
                                break;
                            }
                        }

                        LeapFrog.accountManager.load();
                        for (Account account : LeapFrog.accountManager.accounts) {
                            if (mc.getSession().getUsername().equals(account.getUsername())) {
                                account.setUnban(time);
                            }
                        }
                        LeapFrog.accountManager.save();
                    }
                }
            } catch (Exception e) { LeapFrog.LOGGER.error(e.getMessage()); }
        }
    }

    @SubscribeEvent
    public void onClick(GuiScreenEvent.ActionPerformedEvent event) {
        if (event.gui instanceof GuiSelectWorld || event.gui instanceof GuiMultiplayer) {
            if (event.button.id == 69) {
                mc.displayGuiScreen(new AltManagerGui(event.gui));
            }
        }
    }

    @EventHandler
    private Listener<PacketReceiveEvent> packetrEventListener = new Listener<>(event -> {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                for(Module m : LeapFrog.moduleManager.getModules()) {
                    if(m.isSafeToggle()) {
                        m.safeToggle((S08PacketPlayerPosLook) event.getPacket(), isTeleporting);
                    }

                }
            }
    });

    @EventHandler
    private Listener<InputEvent> inputEventListener = new Listener<>(event -> {
        for(Toggleable t : LeapFrog.moduleManager.getModules()) {
            if(t.isToggled()) {
                for(Setting s : t.getSettings()) {
                    if(s != null) {
                        if(s.getValue() instanceof Bind) {
                            Bind bind = (Bind) s.getValue();
                            if(Keyboard.getEventKey() == bind.getBind()) {

                            }
                        }
                    }
                }
            }
        }
    });

    @EventHandler
    private Listener<PlayerTeleportEvent> teleportEventListener = new Listener<>(event -> {
        isTeleporting = true;
    });
}
