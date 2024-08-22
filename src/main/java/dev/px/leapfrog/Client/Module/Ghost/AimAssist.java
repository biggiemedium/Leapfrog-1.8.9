package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.RotationUtil;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Flux b39
 */
@Module.ModuleInterface(name = "Aim Assist", type = Type.Ghost, description = "Helps you aim at target better")
public class AimAssist extends Module {

    public AimAssist() {

    }

    private Setting<Boolean> swordOnly = create(new Setting<>("Sword Only", true));
    private Setting<Boolean> invisible = create(new Setting<>("Invisible", true));
    private Setting<Boolean> fov = create(new Setting<>("FOV", true));
    // vertical
    private Setting<Boolean> vertical = create(new Setting<>("Vertical", true));
    private Setting<Float> vSpeed = create(new Setting<>("Vertical Speed",  0.4f, 0.1f, 10.0f, v -> vertical.getValue()));

    // horizontal
    private Setting<Boolean> horizontal = create(new Setting<>("Horizontal", true));
    private Setting<Float> hSpeed = create(new Setting<>("Horizontal Speed",  0.4f, 0.1f, 10.0f, v -> horizontal.getValue()));

    private EntityPlayer target;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {

            this.target = getTarget();

            if(swordOnly.getValue()) {
                if(mc.thePlayer.getCurrentEquippedItem() == null || !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)) {
                    return;
                }
            }
            if(this.target == null) {
                return;
            }
            if(mc.currentScreen != null) {
                return;
            }
            if(mc.objectMouseOver.entityHit != null) { // we don't want to lock on to someone else in a fight
                return;
            }
            if(fov.getValue() && !RotationUtil.isVisibleFOV(target, mc.gameSettings.fovSetting)) {
                return;
            }

            if (horizontal.getValue()) {
                mc.thePlayer.rotationYaw = RotationUtil.faceTarget(target, vSpeed.getValue(), hSpeed.getValue(), false)[0];
            }
            if (vertical.getValue()) {
                mc.thePlayer.rotationPitch = RotationUtil.faceTarget(target, vSpeed.getValue(), hSpeed.getValue(), false)[1];
            }


        }
    });

    private EntityPlayer getTarget() {
        ArrayList<EntityPlayer> players = new ArrayList<>();
        for(EntityPlayer e : mc.theWorld.playerEntities) {
            if(e == null) {
                continue;
            }
            if(e == mc.thePlayer) {
                continue;
            }
            if(e.isDead || e.getHealth() <= 0) {
                continue;
            }
            if(mc.thePlayer.getDistanceToEntity(e) > 5) {
                continue;
            }
            if(!invisible.getValue() && e.isInvisible()) {
                continue;
            }

            players.add(e);
        }
        players.stream().sorted(Comparator.comparing(e -> e.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toList());
        if (players.size() <= 0) {
            return null;
        }
        return players.get(0);
    }

    public boolean canEntityBeSeenFixed(Entity entityIn) {
        return mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3(entityIn.posX, entityIn.posY + (double) entityIn.getEyeHeight(), entityIn.posZ)) == null
                || mc.theWorld.rayTraceBlocks(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3(entityIn.posX, entityIn.posY, entityIn.posZ)) == null;
    }

}
