package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.InventoryUtil;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.Client.Module.Misc.Timer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Movement.Scaffold;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Tenacity client
 * Not like this code is being put to use anymore... may as well take it
 * RIP Tenacity
 */
@Module.ModuleInterface(name = "Auto Pot", type = Type.Combat, description = "Automatically throws healing pot at feet")
public class AutoPot extends Module {

    public AutoPot() {

    }

    private Setting<Integer> delay = create(new Setting<>("Delay", 750, 50, 2_000));
    private Setting<Integer> health = create(new Setting<>("Health", 12, 1, 20));
    private Setting<Boolean> frogPot = create(new Setting<>("Frog pot", false));

    private TimerUtil timer = new TimerUtil();
    public static boolean isPotting;
    private float prevPitch;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {

        if (LeapFrog.moduleManager.isModuleToggled(Scaffold.class) || mc.currentScreen instanceof GuiChest)
            return;
        int prevSlot = mc.thePlayer.inventory.currentItem;
        if(event.getStage() == Event.Stage.Pre) {
            if (MoveUtil.isOnGround(1.0E-5)
                    && !(mc.theWorld.getBlockState(new BlockPos(event.getX(), event.getY() - 1, event.getZ())).getBlock() instanceof BlockGlass)
                    && (!mc.thePlayer.isPotionActive(Potion.moveSpeed)
                    || mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getDuration() < 30)
                    && timer.passed(delay.getValue().longValue()) && !mc.thePlayer.isUsingItem()) {
                if (isSpeedPotsInHotbar()) {
                    for (int i = 36; i < 45; i++) {
                        if (isSpeedPot(mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
                            isPotting = true;
                            prevPitch = mc.thePlayer.rotationPitch;
                            throwPot(prevSlot, i);
                            event.setPitch(-90 + ThreadLocalRandom.current().nextFloat());
                            break;
                        }
                    }
                    timer.reset();
                    isPotting = false;
                } else {
                    moveSpeedPots();
                }
            }

            if (!mc.thePlayer.isPotionActive(Potion.regeneration) && mc.thePlayer.getHealth() <= health.getValue()
                    && timer.passed(delay.getValue().longValue())) {
                if (isRegenPotsInHotbar()) {
                    for (int i = 36; i < 45; i++) {
                        if (isRegenPot(mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
                            isPotting = true;
                            prevPitch = mc.thePlayer.rotationPitch;
                            throwPot(prevSlot, i);
                            event.setPitch(-90 + ThreadLocalRandom.current().nextFloat());
                            break;
                        }
                    }
                    timer.reset();
                    isPotting = false;
                } else {
                    moveRegenPots();
                }
            }

            if (mc.thePlayer.getHealth() <= health.getValue() && timer.passed(delay.getValue().longValue())) {
                if (isHealthPotsInHotbar()) {
                    for (int i = 36; i < 45; i++) {
                        if (isHealthPot(mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
                            isPotting = true;
                            prevPitch = mc.thePlayer.rotationPitch;
                            throwPot(prevSlot, i);
                            event.setPitch(-90 + ThreadLocalRandom.current().nextFloat());
                            break;
                        }
                    }
                    timer.reset();
                    isPotting = false;
                } else {
                    moveHealthPots();
                }
            }
        } else if(event.getStage() == Event.Stage.Post) {
            isPotting = false;
        }
    });

    @EventHandler
    private Listener<PlayerAttackEvent> attackEventListener = new Listener<>(event -> {

    });

    private void throwPot(int prevSlot, int index) {
        double x = mc.thePlayer.posX, y = mc.thePlayer.posY, z = mc.thePlayer.posZ;
        float yaw = mc.thePlayer.rotationYaw;
        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(
                x, y, z, yaw, 88.8F + ThreadLocalRandom.current().nextFloat(), mc.thePlayer.onGround));

        mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(index - 36));

        mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));

        mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(prevSlot));

        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw, prevPitch, mc.thePlayer.onGround));
    }

    private boolean isSpeedPotsInHotbar() {
        for (int index = 36; index < 45; index++) {
            if (isSpeedPot(mc.thePlayer.inventoryContainer.getSlot(index).getStack())) return true;
        }
        return false;
    }

    private boolean isHealthPotsInHotbar() {
        for (int index = 36; index < 45; index++) {
            if (isHealthPot(mc.thePlayer.inventoryContainer.getSlot(index).getStack())) return true;
        }
        return false;
    }

    private boolean isRegenPotsInHotbar() {
        for (int index = 36; index < 45; index++) {
            if (isRegenPot(mc.thePlayer.inventoryContainer.getSlot(index).getStack())) return true;
        }
        return false;
    }

    private int getPotionCount() {
        int count = 0;
        for (int index = 0; index < 45; index++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (isHealthPot(stack) || isHealthPot(stack) || isRegenPot(stack))
                count++;
        }
        return count;
    }

    private void moveSpeedPots() {
        if (mc.currentScreen instanceof GuiChest) return;
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) continue;
            if (!frogPot.getValue() && stack.getDisplayName().contains("Frog")) continue;
            if (isSpeedPot(stack)) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private void moveHealthPots() {
        if (mc.currentScreen instanceof GuiChest) return;
        for (int index = 9; index < 36; index++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (isHealthPot(stack)) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private void moveRegenPots() {
        if (mc.currentScreen instanceof GuiChest) return;
        for (int index = 9; index < 36; index++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (isRegenPot(stack)) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private boolean isSpeedPot(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            if (!frogPot.getValue() && stack.getDisplayName().contains("Frog")) return false;
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect e : ((ItemPotion) stack.getItem()).getEffects(stack)) {
                    if (e.getPotionID() == Potion.moveSpeed.id && e.getPotionID() != Potion.jump.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isHealthPot(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect e : ((ItemPotion) stack.getItem()).getEffects(stack)) {
                    if (e.getPotionID() == Potion.heal.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isRegenPot(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect e : ((ItemPotion) stack.getItem()).getEffects(stack)) {
                    if (e.getPotionID() == Potion.regeneration.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
