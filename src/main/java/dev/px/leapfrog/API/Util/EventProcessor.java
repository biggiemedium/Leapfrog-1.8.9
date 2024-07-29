package dev.px.leapfrog.API.Util;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.API.Event.Client.SettingUpdateEvent;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Game.KeyPressEvent;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerTeleportEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Event.Render.Overlays.RenderFireOverlayEvent;
import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.GUI.HUD.GuiHUDEditor;
import dev.px.leapfrog.Client.GUI.Notifications.Notification;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class EventProcessor implements Listenable {

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
        LeapFrog.EVENT_BUS.post(this);
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
                    if (element.isVisible()) {
                        element.onRender(e);
                        element.renderDummy(e);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent event) {
        try {
            if(Keyboard.isCreated()) {
                if(Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if(keyCode <= 0)
                        return;

                    if(mc.thePlayer != null && mc.theWorld != null) {
                        for(Module m : LeapFrog.moduleManager.getModules()) {
                            if(m.keybind.getValue().getBind() == keyCode) {
                                m.toggle();
                            }
                        }

                        if(keyCode == Keyboard.KEY_O) {
                            LeapFrog.notificationManager.pushNotification("Test", "ur mom", Notification.NotificationType.INFO, 5);
                            ChatUtil.sendClientSideMessage("Posted Notification");
                        }

                        KeyPressEvent e = new KeyPressEvent(keyCode);
                        LeapFrog.EVENT_BUS.post(e);
                    }
                }
            }
        } catch (Exception q) { q.printStackTrace(); }
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

        PlayerUpdateEvent e = new PlayerUpdateEvent(Event.Stage.Pre);
        LeapFrog.EVENT_BUS.post(e);
        LeapFrog.EVENT_BUS.post(event);
    }

    @EventHandler
    private Listener<PacketReceiveEvent> packetrEventListener = new Listener<>(event -> {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                if(LeapFrog.settingsManager.ANTICHEATFLAG.getValue()) {
                    if (!isTeleporting) {
                        ChatUtil.sendClientSideMessage(ChatFormatting.RED + "Warning: " + ChatFormatting.RESET + "You flagged the anti-cheat!");
                    }
                    isTeleporting = false;
                }
                for(Module m : LeapFrog.moduleManager.getModules()) {
                    if(m.isSafeToggle()) {
                        m.safeToggle((S08PacketPlayerPosLook) event.getPacket(), isTeleporting);
                    }

                }
            }
    });

    @EventHandler
    private Listener<PlayerTeleportEvent> teleportEventListener = new Listener<>(event -> {
        isTeleporting = true;
    });
}
