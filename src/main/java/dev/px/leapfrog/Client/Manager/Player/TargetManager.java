package dev.px.leapfrog.Client.Manager.Player;

import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class TargetManager implements Listenable {

    private ConcurrentLinkedQueue<EntityPlayer> targets = new ConcurrentLinkedQueue<>();
    private Minecraft mc = Minecraft.getMinecraft();

    public TargetManager() {
        LeapFrog.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        if (mc.thePlayer.ticksExisted % 100 == 0) {
            LeapFrog.threadManager.submitRunnable(this::updateTargets);
        }
    });

    public void updateTargets() {
        targets.clear();
        for(EntityPlayer e : mc.theWorld.playerEntities) {
            if(e == null) {
                continue;
            }
            if(e == mc.thePlayer) {
                continue;
            }
            if(!(e instanceof EntityLivingBase)) {
                continue;
            }
            if(e.isDead || PlayerUtil.getHealth(e) <= 0) {
                continue;
            }

            this.targets.add(e);
        }
    }

    public List<EntityPlayer> getTargets(double range) {
        return targets.stream()
                .filter(entity -> entity instanceof EntityPlayer)
                .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) < range)
                .filter(entity -> mc.theWorld.loadedEntityList.contains(entity))
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))
                .collect(Collectors.toList());
    }
}
