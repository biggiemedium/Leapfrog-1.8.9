package dev.px.leapfrog.API.Util;

import dev.px.leapfrog.API.Event.Game.KeyPressEvent;
import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.GUI.HUD.GuiHUDEditor;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class EventProcessor implements Listenable {

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
        LeapFrog.EVENT_BUS.post(this);
    }

    private Minecraft mc = Minecraft.getMinecraft();

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
                            if(m.getKeyBind() == keyCode) {
                                m.toggle();
                            }
                        }
                        if(keyCode == Keyboard.KEY_RSHIFT) {
                            mc.displayGuiScreen(new ClickGUI());
                        }
                        if(keyCode == Keyboard.KEY_P) {
                            mc.displayGuiScreen(new GuiHUDEditor());
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
}
