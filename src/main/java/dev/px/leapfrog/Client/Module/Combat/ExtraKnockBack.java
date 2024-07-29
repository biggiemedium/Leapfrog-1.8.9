package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

@Module.ModuleInterface(name = "Extra Knockback", type = Type.Combat, description = "Makes enemy take more knockback")
public class ExtraKnockBack extends Module {

    public ExtraKnockBack() {

    }


    @EventHandler
    private Listener<PlayerAttackEvent> entityEventListener = new Listener<>(event -> {
        if(event.getEntity() != null) {
            if(mc.thePlayer.isSprinting()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        }
    });
}
