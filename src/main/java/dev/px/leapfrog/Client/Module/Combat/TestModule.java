package dev.px.leapfrog.Client.Module.Combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerAttackEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Event.World.WorldBlockAABBEvent;
import dev.px.leapfrog.API.Module.BetweenInteger;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Render.*;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.*;

@Module.ModuleInterface(name = "Test Module", type = Type.Combat, description = "Balls")
public class TestModule extends Module {

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.AAC));

    private Setting<Boolean> fall = create(new Setting<>("Web Fall", false));
    private Setting<Boolean> webspeed = create(new Setting<>("Web Speed", false));
    private Setting<Integer> packets = create(new Setting<>("Packets", 50, 1, 100));

    private Setting<BetweenInteger<Integer>> betweenIntegerSetting = create(new Setting<>("Between Test", new BetweenInteger<Integer>(6, 10)));

    private final boolean notInTheAir = true;
    private final boolean notDuringMove = false;
    private final boolean notDuringRegeneration = false;
    private final boolean stopInput = false;

    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);


    @Override
    public void onEnable() {
        mc.thePlayer.setSneaking(false);
        super.onEnable();
    }

    @EventHandler
    private Listener<WorldBlockAABBEvent> aabbEventListener = new Listener<>(event -> {
        if(fall.getValue()) {
            if(event.getBlock() instanceof BlockWeb) {
                int x = event.getBlockPos().getX();
                int y = event.getBlockPos().getY();
                int z = event.getBlockPos().getZ();

                event.setBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
            }
        }
    });

    @EventHandler
    private Listener<PacketReceiveEvent> packetrEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            ChatUtil.sendClientSideMessage(ChatFormatting.RED + "Warning: " + ChatFormatting.RESET + "You flagged the anti-cheat!");
        }
    });

    @EventHandler
    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
        if (mc.gameSettings.keyBindUseItem.isPressed() && stopInput) {

        }
    });

    @EventHandler
    private Listener<PacketSendEvent> packetsEventListener = new Listener<>(event -> {

    });

    @EventHandler
    private Listener<PacketReceiveEvent> packetsEventListener2 = new Listener<>(event -> {

    });

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {

        }
    });


    @EventHandler
    private Listener<PlayerAttackEvent> attackEventListener = new Listener<>(event -> {
        if(event.getEntity() != null) {

        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);

        ((IMixinMinecraft) mc).setRightClickDelayTimer(4);
        mc.thePlayer.stepHeight = 0.6F;
        ((IMixinMinecraft) mc).timer().timerSpeed = 1;

        MoveUtil.resetMotion();
    }

    private enum Mode {
        AAC,
        Grim,
        NCP
    }




}
