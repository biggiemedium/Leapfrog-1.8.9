package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerStrafeEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.InventoryUtil;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.RotationUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Movement.Scaffold;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Module.ModuleInterface(name = "Kill aura", type = Type.Combat, description = "Auto targets enemies with sword")
public class KillAura extends Module {

    public KillAura() {

    }

    private Setting<AttackMode> attackMode = create(new Setting<>("Attack Mode", AttackMode.Range));
    private Setting<TargetMode> targetMode = create(new Setting<>("Target Mode", TargetMode.Single));
    private Setting<Integer> minCPS = create(new Setting<>("Min CPS", 8, 1, 20));
    private Setting<Integer> maxCPS = create(new Setting<>("Max CPS", 14, 1, 20));
    private Setting<Float> reach = create(new Setting<>("Reach", 4f, 3f, 6f));
    private Setting<Boolean> autoBlock = create(new Setting<>("Auto Block", true));
    private Setting<AutoBlockMode> autoBlockMode = create(new Setting<>("Auto Block Mode", AutoBlockMode.Watchdog, v -> autoBlock.getValue()));
    private Setting<Boolean> rotations = create(new Setting<>("Rotations", true));
    private Setting<RotationMode> rotationMode = create(new Setting<>("Rotation Mode", RotationMode.Normal));
    private Setting<Boolean> whileScaffold = create(new Setting<>("While Scaffold", false));
    private Setting<Boolean> rayCast = create(new Setting<>("RayCast", false));
    private Setting<Boolean> movementFix = create(new Setting<>("Movement Fix", false));

    // Target mode
    private Setting<Boolean> players = create(new Setting<>("Players", true));
    private Setting<Boolean> invisible = create(new Setting<>("Invisibles", true, v -> players.getValue()));
    private Setting<Boolean> throughWalls = create(new Setting<>("Through walls", false));
    private Setting<Boolean> animals = create(new Setting<>("Animals", false));
    private Setting<Boolean> mobs = create(new Setting<>("Mobs", false));


    public List<EntityLivingBase> targets = new ArrayList<>();
    public static boolean attacking;
    public static boolean blocking;
    public static boolean wasBlocking;
    public EntityLivingBase target;
    private float yaw = 0;
    private int cps;
    private final TimerUtil attackTimer = new TimerUtil();
    private final TimerUtil switchTimer = new TimerUtil();

    @Override
    public void onDisable() {
        super.onDisable();
        target = null;
        targets.clear();
        blocking = false;
        attacking = false;
        if(wasBlocking) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        wasBlocking = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        switch (event.getStage()) {
            case Pre:
                if(minCPS.getValue() > maxCPS.getValue()) {
                    minCPS.setValue(minCPS.getValue() - 1);
                }
                sortTargets();

                attacking = !targets.isEmpty() && (whileScaffold.getValue() || !LeapFrog.moduleManager.isModuleToggled(Scaffold.class));
                blocking = autoBlock.getValue() && attacking && InventoryUtil.isHoldingSword();
                if (attacking) {
                    target = targets.get(0);

                    if (rotations.getValue()) {
                        float[] rotations = new float[]{0, 0};
                        switch (rotationMode.getValue()) {
                            case Normal:
                                rotations = RotationUtil.getRotations(target);
                                break;
                            case Smooth:
                                rotations = RotationUtil.getSmoothRotations(target);
                                break;
                        }
                        yaw = event.getYaw();
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        //RotationUtils.setVisualRotations(event.getYaw(), event.getPitch());
                    }

                    if (rayCast.getValue() && !RotationUtil.isMouseOver(event.getYaw(), event.getPitch(), target, reach.getValue().floatValue()))
                        return;

                    if (attackTimer.passed(cps)) {
                        attackTimer.reset();
                        int maxValue = (int) ((minCPS.getMax() - maxCPS.getValue()) * 20);
                        int minValue = (int) ((minCPS.getMax() - minCPS.getValue()) * 20);
                        cps = MathUtil.getRandomInRange(minValue, maxValue);
                        if (targetMode.getValue() == TargetMode.Multi) {
                            for (EntityLivingBase entityLivingBase : targets) {
                                PlayerAttackEvent attackEvent = new PlayerAttackEvent(entityLivingBase);
                                LeapFrog.EVENT_BUS.post(attackEvent);

                                if (!attackEvent.isCancelled()) {
                                    mc.thePlayer.swingItem();
                                    mc.playerController.attackEntity(mc.thePlayer, entityLivingBase);
                                }
                            }
                        } else {
                            PlayerAttackEvent attackEvent = new PlayerAttackEvent(target);
                            LeapFrog.EVENT_BUS.post(attackEvent);

                            if (!attackEvent.isCancelled()) {
                                mc.thePlayer.swingItem();
                                mc.playerController.attackEntity(mc.thePlayer, target);
                            }
                        }
                    }
                }

                if (blocking) {
                    switch (autoBlockMode.getValue()) {
                        case Watchdog:
                            if (wasBlocking && mc.thePlayer.ticksExisted % 4 == 0) {
                                mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                wasBlocking = false;
                            }

                            break;
                        case Verus:
                            if (wasBlocking) {
                                mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.
                                        Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                            wasBlocking = true;

                            break;
                        case Fake:
                            break;
                    }
                } else if (wasBlocking && autoBlockMode.getValue() == AutoBlockMode.Watchdog) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.
                            Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    wasBlocking = false;
                }

                break;

            case Post:
                if (blocking) {
                    switch (autoBlockMode.getValue()) {
                        case Watchdog:
                            if (!wasBlocking) {
                                mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getHeldItem(), 255, 255, 255));
                                wasBlocking = true;
                            }
                            break;
                        case Verus:

                            break;
                        case Fake:
                            break;
                    }
                }

                break;
        }
    });

    @EventHandler
    private Listener<PlayerStrafeEvent> strafeEventListener = new Listener<>(event -> {
        if(movementFix.getValue()) {
            if(target != null) {
                event.setYaw(yaw);
            }
        }
    });

    private void sortTargets() {
        targets.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(entity) <= reach.getValue() && isValid(entity) && mc.thePlayer != entityLivingBase && !LeapFrog.socialManager.isFriend(entityLivingBase.getName())) {
                    targets.add(entityLivingBase);
                }
            }
        }

        switch (attackMode.getValue()) {
            case Range:
                targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
                break;
            case Hurt:
                targets.sort(Comparator.comparingInt(entity -> entity.hurtTime));
                break;
            case Health:
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            case Armor:
                targets.sort(Comparator.comparingInt(EntityLivingBase::getTotalArmorValue));
                break;
        }
    }

    public boolean isValid(Entity entity) {
        if (entity instanceof EntityPlayer && players.getValue() && !entity.isInvisible() && mc.thePlayer.canEntityBeSeen(entity))
            return true;

        if (entity instanceof EntityPlayer && invisible.getValue() && entity.isInvisible())
            return true;

        if(entity instanceof EntityPlayer && throughWalls.getValue() && !mc.thePlayer.canEntityBeSeen(entity))
            return true;

        if (entity instanceof EntityAnimal && animals.getValue())
            return true;

        if (entity instanceof EntityMob && mobs.getValue())
            return true;

        return false;
    }

    private enum AttackMode {
        Range,
        Hurt,
        Health,
        Armor
    }

    private enum RotationMode {
        Normal,
        Smooth
    }

    private enum TargetMode {
        Single,
        Multi
    }

    private enum AutoBlockMode {
        Fake,
        Watchdog,
        Verus
    }

    public EntityLivingBase getCurrentTarget() {
        return this.target;
    }
}
