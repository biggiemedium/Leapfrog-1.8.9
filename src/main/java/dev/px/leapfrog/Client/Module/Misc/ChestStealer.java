package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.RotationUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

@Module.ModuleInterface(name = "Chest Stealer", type = Type.Misc, description = "Steals chest contents")
public class ChestStealer extends Module {

    public ChestStealer() {

    }

    private Setting<Boolean> aura = create(new Setting<>("Aura", false));
    private Setting<Integer> auraRange = create(new Setting<>("Aura Range", 3, 1, 6, v -> aura.getValue()));
    private Setting<Integer> delay = create(new Setting<>("Delay", 80, 0, 300));
    private Setting<Boolean> random = create(new Setting<>("Random", false));
    private Setting<Integer> randomMin = create(new Setting<>("Random Min", 50, 0, 1000, v -> random.getValue()));
    private Setting<Integer> randomMax = create(new Setting<>("Random Max", 50, 0, 1000, v -> random.getValue()));
    public Setting<Boolean> silent = create(new Setting<>("Silent", false));


    private ArrayList<BlockPos> openedPos = new ArrayList<>();
    private TimerUtil timer = new TimerUtil();
    public boolean silentStealing = false;

    @Override
    public void onEnable() {
        openedPos.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {

            if(aura.getValue()) {
                int radius = auraRange.getValue();
                for (int x = -radius; x < radius; x++) {
                    for (int y = -radius; y < radius; y++) {
                        for (int z = -radius; z < radius; z++) {
                            BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                            if (mc.theWorld.getBlockState(pos).getBlock() == Blocks.chest && !openedPos.contains(pos)) {
                                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, new Vec3(pos))) {
                                    mc.thePlayer.swingItem();
                                    float[] rotations = RotationUtil.getBlockRotations(pos.getX(), pos.getY(), pos.getZ());
                                    event.setYaw(rotations[0]);
                                    event.setPitch(rotations[1]);
                                    openedPos.add(pos);
                                }
                            }
                        }
                    }
                }
            }

            if(mc.thePlayer.openContainer instanceof ContainerChest) {
                ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                IInventory inv = chest.getLowerChestInventory();

                // checking if player inventory is full
                boolean full = true;
                for (ItemStack item : mc.thePlayer.inventory.mainInventory) {
                    if (item == null) {
                        full = false;
                        break;
                    }
                }

                boolean containsItems = false;

                if (!full) {
                    for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); index++) {
                        ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                        if (stack != null && !isBad(stack)) {
                            containsItems = true;
                            break;
                        }
                    }

                    if (containsItems) {
                        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); index++) {
                            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                            if (stack != null && timer.passed(delay.getValue() +
                                    (random.getValue() ? MathUtil.getRandomInRange(randomMin.getValue(), randomMax.getValue()) : 0)) && !isBad(stack)) {
                                mc.playerController.windowClick(chest.windowId, index, 0, 1, mc.thePlayer);
                                timer.reset();
                            }
                        }
                    } else {
                        mc.thePlayer.closeScreen();
                        if (silent.getValue()) {
                            ChatUtil.sendClientSideMessage("Stealing...");
                        }
                    }

                }
            }
        }
    });



    /**
     * @param item
     * @author PandaWare
     */
    private boolean isBad(ItemStack item) {
        return item != null &&
                ((item.getItem().getUnlocalizedName().contains("tnt")) ||
                        (item.getItem().getUnlocalizedName().contains("stick")) ||
                        (item.getItem().getUnlocalizedName().contains("egg") && !item.getItem().getUnlocalizedName().contains("leg")) ||
                        (item.getItem().getUnlocalizedName().contains("string")) ||
                        (item.getItem().getUnlocalizedName().contains("flint")) ||
                        (item.getItem().getUnlocalizedName().contains("compass")) ||
                        (item.getItem().getUnlocalizedName().contains("feather")) ||
                        (item.getItem().getUnlocalizedName().contains("snow")) ||
                        (item.getItem().getUnlocalizedName().contains("fish")) ||
                        (item.getItem().getUnlocalizedName().contains("enchant")) ||
                        (item.getItem().getUnlocalizedName().contains("exp")) ||
                        (item.getItem().getUnlocalizedName().contains("shears")) ||
                        (item.getItem().getUnlocalizedName().contains("anvil")) ||
                        (item.getItem().getUnlocalizedName().contains("torch")) ||
                        (item.getItem().getUnlocalizedName().contains("seeds")) ||
                        (item.getItem().getUnlocalizedName().contains("leather")) ||
                        ((item.getItem() instanceof ItemGlassBottle)) ||
                        (item.getItem().getUnlocalizedName().contains("piston")) ||
                        ((item.getItem().getUnlocalizedName().contains("potion"))
                                && (isBadPotion(item))));
    }

    /**
     * @param stack
     * @author PandaWare
     */
    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
