package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.InventoryUtil;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.Client.Module.Misc.Timer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@Module.ModuleInterface(name = "Auto Pot", type = Type.Combat, description = "Automatically throws healing pot at feet")
public class AutoPot extends Module {

    public AutoPot() {

    }

    private Setting<Integer> delay = create(new Setting<>("Delay", 1000, 500, 2000));
    private Setting<Integer> health = create(new Setting<>("Health", 15, 0, 20));

    private TimerUtil timer = new TimerUtil();
    private int ticks = 0;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {

            if(PlayerUtil.getHealth(mc.thePlayer) > health.getValue()) return;
            if(mc.thePlayer.isOnLadder()) return;
            if(mc.currentScreen != null) return;
            if(!timer.passed(delay.getValue())) return;
            if(ticks < 10) return;
            int oldSlot = mc.thePlayer.inventory.currentItem;
            int newSlot = -1;
            for(int i = 0; i < 9; i++) {
                ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
                if(s == null) {
                    continue;
                }
                if (s.getItem() instanceof ItemPotion) {
                    ItemPotion potion = (ItemPotion) s.getItem();
                    if(!ItemPotion.isSplash(s.getMetadata())
                            || !InventoryUtil.goodPotion(potion.getEffects(s).get(0).getPotionID())
                            || PlayerUtil.getHealth(mc.thePlayer) > health.getValue()) {
                        continue;
                    }

                }



            }
        }
    });

    @EventHandler
    private Listener<PlayerAttackEvent> attackEventListener = new Listener<>(event -> {
        this.ticks = 0;
    });

}
