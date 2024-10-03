package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Setting.Slot;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.InventoryUtil;
import dev.px.leapfrog.API.Util.Math.BlockUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.RandomUtils;


@Module.ModuleInterface(name = "Auto Inventory", type = Type.Misc, description = "Manages Inventory")
public class AutoInventory extends Module {

    public AutoInventory() {

    }


    private Setting<Boolean> sword = create(new Setting<>("Sword", true));
    private Setting<Slot> swordSlot = create(new Setting<>("Sword Slot", new Slot(1), v -> sword.getValue()));
    private Setting<Boolean> bow = create(new Setting<>("Bow", true));
    private Setting<Slot> bowSlot = create(new Setting<>("Bow Slot", new Slot(2), v -> bow.getValue()));
    private Setting<Boolean> food = create(new Setting<>("Food", true));
    //private Setting<Slot> foodSlot = create(new Setting<>("Food Slot", new Slot(3)));
    private Setting<Boolean> picaxe = create(new Setting<>("Pickaxe", false));
    private Setting<Slot> pickaxeSlot = create(new Setting<>("Pickaxe Slot", new Slot(4), v -> bow.getValue()));
    private Setting<Boolean> clean = create(new Setting<>("Clean", true));
    private Setting<CleanMode> cleanMode = create(new Setting<>("Clean Mode", CleanMode.Normal, v -> clean.getValue()));
    private Setting<Integer> blockCap = create(new Setting<>("Block Cap", 512, 64, 1024));
    private Setting<Integer> delay = create(new Setting<>("Delay", 75, 0, 1000));
    private Setting<Boolean> random = create(new Setting<>("Random", true));
    private Setting<Integer> randomMin = create(new Setting<>("Random Min", 50, 0, 1000, v -> random.getValue()));
    private Setting<Integer> randomMax = create(new Setting<>("Random Max", 50, 0, 1000, v -> random.getValue()));
    private Setting<Boolean> stopWhileCleaning = create(new Setting<>("Stop When Cleaning", false));

    private TimerUtil timer = new TimerUtil();
    private long lastClean;
    private int WEAPON_SLOT = 36, PICKAXE_SLOT = 37, AXE_SLOT = 38, SHOVEL_SLOT = 39, BLOCKS_SLOT = 44;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            long time = delay.getValue().intValue() + (random.getValue() ? RandomUtils.nextInt(randomMin.getValue().intValue(), randomMax.getValue().intValue()) : 0);

            if ((cleanMode.getValue() == CleanMode.Open && !(mc.currentScreen instanceof GuiInventory) &&
                    !stopWhileCleaning.getValue()) || stopWhileCleaning.getValue() &&
                    (MoveUtil.isMoving() || mc.thePlayer.moveForward > 0 || mc.thePlayer.moveStrafing > 0 ||
                            mc.gameSettings.keyBindJump.isPressed()))
                return;

            if (this.cleanMode.getValue() == CleanMode.Silent && (MoveUtil.isMoving() || mc.gameSettings.keyBindJump.isPressed()))
                return;

            if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) {
                if (timer.passed(time)) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(WEAPON_SLOT).getHasStack()) {
                        getBestWeapon(WEAPON_SLOT);
                    } else {
                        if (!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(WEAPON_SLOT).getStack())) {
                            getBestWeapon(WEAPON_SLOT);
                        }
                    }
                }

                if (timer.passed(time))
                    getBestPickaxe(PICKAXE_SLOT);

                if (timer.passed(time))
                    getBestShovel(SHOVEL_SLOT);

                if (timer.passed(time))
                    getBestAxe(AXE_SLOT);

                if (timer.passed(time)) {
                    for (int i = 9; i < 45; i++) {
                        if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                            if (shouldDrop(is, i)) {
                                InventoryUtil.drop(i);
                                timer.reset();
                                lastClean = System.currentTimeMillis();
                                if (time > 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        }
    });

    public boolean shouldDrop(ItemStack stack, int slot) {
        String itemName = stack.getItem().getUnlocalizedName().toLowerCase();
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("profil")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("§k||")) {
            return false;
        }

        if ((slot == WEAPON_SLOT && isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(WEAPON_SLOT).getStack())) ||
                (slot == PICKAXE_SLOT && isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(PICKAXE_SLOT).getStack()) && PICKAXE_SLOT >= 0) ||
                (slot == AXE_SLOT && isBestAxe(mc.thePlayer.inventoryContainer.getSlot(AXE_SLOT).getStack()) && AXE_SLOT >= 0) ||
                (slot == SHOVEL_SLOT && isBestShovel(mc.thePlayer.inventoryContainer.getSlot(SHOVEL_SLOT).getStack()) && SHOVEL_SLOT >= 0)) {
            return false;
        }

        if (stack.getItem() instanceof ItemBlock &&
                (getBlockCount() > blockCap.getValue() ||
                        BlockUtil.INVALID_BLOCKS.contains(((ItemBlock) stack.getItem()).getBlock()))) {
            return true;
        }

        if (stack.getItem() instanceof ItemPotion) {
            return isBadPotion(stack);
        }

        if (stack.getItem() instanceof ItemArmor)
            return false;

        if (stack.getItem() instanceof ItemFood && food.getValue() && !(stack.getItem() instanceof ItemAppleGold || stack.getItem() instanceof ItemFood)) {
            return true;
        }

        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword) {
            return true;
        }

        if ((stack.getItem() instanceof ItemBow || itemName.contains("arrow")) && !bow.getValue()) {
            return true;
        }

        if (stack.getItem() instanceof ItemDoublePlant) {
            return true;
        }

        if ((itemName.contains("bowl") ||
                (itemName.contains("bucket") && !itemName.contains("water") &&
                        !itemName.contains("lava") && !itemName.contains("milk")) ||
                (stack.getItem() instanceof ItemGlassBottle && !clean.getValue())) && !clean.getValue()) {
            return true;
        }
        return (itemName.contains("tnt")) ||
                (itemName.contains("stick")) ||
                (itemName.contains("egg")) ||
                (itemName.contains("string")) ||
                (itemName.contains("cake")) ||
                (itemName.contains("mushroom") && !itemName.contains("stew")) ||
                (itemName.contains("flint")) ||
                (itemName.contains("dyepowder")) ||
                (itemName.contains("feather")) ||
                (itemName.contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) ||
                (itemName.contains("snow")) ||
                (itemName.contains("fish")) ||
                (itemName.contains("enchant")) ||
                (itemName.contains("exp")) ||
                (itemName.contains("shears")) ||
                (itemName.contains("anvil")) ||
                (itemName.contains("torch")) ||
                (itemName.contains("seeds")) ||
                (itemName.contains("beacon")) ||
                (itemName.contains("flower")) ||
                (itemName.contains("leather")) ||
                (itemName.contains("reeds")) ||
                (itemName.contains("skull")) ||
                (itemName.contains("record")) ||
                (itemName.contains("snowball")) ||
                (itemName.contains("slab")) ||
                (itemName.contains("stair")) ||
                (itemName.contains("piston"));
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !BlockUtil.INVALID_BLOCKS.contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (potion.getEffects(stack) == null && !potion.hasEffect(stack)) {
                return true;
            }
            for (final Object o : potion.getEffects(stack)) {
                final PotionEffect effect = (PotionEffect) o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && (is.getItem() instanceof ItemSword || !sword.getValue()))
                    return false;
            }
        }
        return stack.getItem() instanceof ItemSword || !sword.getValue();
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword || !sword.getValue())) {
                    InventoryUtil.swap(i, slot - 36);
                    timer.reset();
                    break;
                }
            }
        }
    }

    public boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }

    private float getDamage(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return 0;

        float damage = 0;
        Item item = stack.getItem();

        if (item instanceof ItemSword) {
            damage += ((ItemSword) item).getDamageVsEntity();
        } else if (item instanceof ItemTool) {
            damage += item.getMaxDamage();
        }

        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F +
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.1F;

        return damage;
    }

    private float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool))
            return 0;
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool) item;
        float value;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else
            return 1f;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;
        return value;
    }

    private void getBestPickaxe(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestPickaxe(is) && PICKAXE_SLOT != i) {
                    if (!isBestWeapon(is)) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(PICKAXE_SLOT).getHasStack()) {
                            InventoryUtil.swap(i, PICKAXE_SLOT - 36);
                            timer.reset();
                            if (delay.getValue().longValue() > 0)
                                return;
                        } else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(PICKAXE_SLOT).getStack())) {
                            InventoryUtil.swap(i, PICKAXE_SLOT - 36);
                            timer.reset();
                            if (delay.getValue().longValue() > 0)
                                return;
                        }
                    }
                }
            }
        }
    }

    private void getBestShovel(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestShovel(is) && SHOVEL_SLOT != i) {
                    if (!isBestWeapon(is)) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(SHOVEL_SLOT).getHasStack()) {
                            InventoryUtil.swap(i, SHOVEL_SLOT - 36);
                            timer.reset();
                            if (delay.getValue().longValue() > 0)
                                return;
                        } else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(SHOVEL_SLOT).getStack())) {
                            InventoryUtil.swap(i, SHOVEL_SLOT - 36);
                            timer.reset();
                            if (delay.getValue().longValue() > 0)
                                return;
                        }
                    }
                }
            }
        }
    }

    private void getBestAxe(int slot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestAxe(is) && AXE_SLOT != i) {
                    if (!isBestWeapon(is)) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(AXE_SLOT).getHasStack()) {
                            InventoryUtil.swap(i, AXE_SLOT - 36);
                            timer.reset();
                            if (delay.getValue().longValue() > 0)
                                return;
                        } else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(AXE_SLOT).getStack())) {
                            InventoryUtil.swap(i, AXE_SLOT - 36);
                            timer.reset();
                            if (delay.getValue().longValue() > 0)
                                return;
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private enum CleanMode {
        Normal,
        Open,
        Silent
    }

}
