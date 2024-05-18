package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.API.Event.Game.KeyPressEvent;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.API.Wrapper;
import dev.px.leapfrog.Client.GUI.ClickGUI.ClickGUI;
import dev.px.leapfrog.Client.GUI.HUD.GuiHUDEditor;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.NewGUI.FreeFlowGUI;
import me.zero.alpine.fork.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputManager implements Listenable {

    public InputManager() {
        MinecraftForge.EVENT_BUS.register(this);
        this.freeFlowGUI = new FreeFlowGUI();
        this.clickGUI = new ClickGUI();
    }

    private Minecraft mc = Wrapper.getMC();
    private FreeFlowGUI freeFlowGUI;
    private ClickGUI clickGUI;

    public static KeyBinding guiKey;
    public static KeyBinding guiKey2;

    public static void register() {
        guiKey = new KeyBinding("HUD Mod Compact GUI Key", Keyboard.KEY_RSHIFT, "Hud Mod");
        ClientRegistry.registerKeyBinding(guiKey);

        guiKey = new KeyBinding("HUD Mod Full GUI Key", Keyboard.KEY_P, "Hud Mod");
        ClientRegistry.registerKeyBinding(guiKey2);
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

                        if(keyCode == Keyboard.KEY_RSHIFT) {
                            if(clickGUI != null) {
                                mc.displayGuiScreen(clickGUI);
                            }
                        }

                        if(keyCode == Keyboard.KEY_P) {
                            mc.displayGuiScreen(new GuiHUDEditor());
                        }

                        if(keyCode == Keyboard.KEY_M) {
                            if(freeFlowGUI != null) {
                                mc.displayGuiScreen(freeFlowGUI);
                            }
                        }

                    }
                }
            }
        } catch (Exception q) { q.printStackTrace(); }
    }

    public static class ScrollHandler {

        private int scroll;

        public ScrollHandler(int scroll) {
            this.scroll = scroll;
        }

        public int getScroll() {
            return scroll;
        }

        public void setScroll(int scroll) {
            this.scroll = scroll;
        }

        public void increment(double amount) {
            scroll += Mouse.getDWheel() * amount;
        }

        private void setWithinBounds(int low, int high) {
            if(scroll < low) {
                scroll = low;
            } else if(scroll > high) {
                scroll = high;
            }
        }
    }

}
