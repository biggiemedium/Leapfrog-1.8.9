package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.InventoryUtil;
import dev.px.leapfrog.API.Util.Math.BlockUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.API.Wrapper;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.PotionEffect;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel;

/**
 * @author Tenactiy Client
 * @author Rise Client
 *
 * Yes I skidding this what are you going to do
 *
 * TODO: Unskid but I dont really care for now
 */
@Module.ModuleInterface(name = "Auto Inventory", type = Type.Misc, description = "Manages Inventory")
public class AutoInventory extends Module {

    public AutoInventory() {

    }

    private Setting<Integer> delay = create(new Setting<>("Delay (MS)", 120, 0, 1000));
    private Setting<Boolean> armor = create(new Setting<>("Armor", true));

    // Slots
    private Setting<Integer> slotWeapon = create(new Setting<>("Weapon Slot", 1, 1, 9));
    private Setting<Integer> slotShovel = create(new Setting<>("Shovel Slot", 2, 1, 9));
    private Setting<Integer> slotBow = create(new Setting<>("Bow Slot", 3, 1, 9));
    private Setting<Integer> slotPick = create(new Setting<>("Pickaxe Slot", 4, 1, 9));
    private Setting<Integer> slotAxe = create(new Setting<>("Axe Slot", 5, 1, 9));
    private Setting<Integer> slotBlock = create(new Setting<>("Block Slot", 6, 1, 9));
    private Setting<Integer> slotGapple = create(new Setting<>("Gapple Slot", 7, 1, 9));

    // Tenacity client
    private final String[] blacklist = {"tnt", "stick", "egg", "string", "cake", "mushroom", "flint", "compass", "dyePowder", "feather", "bucket", "chest", "snow", "fish", "enchant", "exp", "anvil", "torch", "seeds", "leather", "reeds", "skull", "record", "snowball", "piston"};
    private final String[] serverItems = {"selector", "tracking compass", "(right click)", "tienda ", "perfil", "salir", "shop", "collectibles", "game", "profil", "lobby", "show all", "hub", "friends only", "cofre", "(click", "teleport", "play", "exit", "hide all", "jeux", "gadget", " (activ", "emote", "amis", "bountique", "choisir", "choose ", "recipe book", "click derecho", "todos", "teletransportador", "configuraci", "jugar de nuevo"};

    private TimerUtil timer = new TimerUtil();
    private boolean open = false;

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(mc.thePlayer.ticksExisted < 40) return;
            if (!(mc.currentScreen instanceof GuiInventory)) {
                return;
            }


        }
    });

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {

    });


    private float getProtection(ItemStack stack) {
        float protection = 0;

        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();

            protection += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * getEnchantmentLevel(
                    Enchantment.protection.effectId, stack) * 0.0075D;
            protection += getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100d;
            protection += getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50d;
            protection += getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100d;
        }

        return protection;
    }

    private boolean isBestArmor(ItemStack stack, int type) {
        String strType = "";

        switch (type) {
            case 1:
                strType = "helmet";
                break;
            case 2:
                strType = "chestplate";
                break;
            case 3:
                strType = "leggings";
                break;
            case 4:
                strType = "boots";
                break;
        }

        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }

        float protection = getProtection(stack);

        for (int i = 5; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > protection && is.getUnlocalizedName().contains(strType)) return false;
            }
        }

        return true;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
