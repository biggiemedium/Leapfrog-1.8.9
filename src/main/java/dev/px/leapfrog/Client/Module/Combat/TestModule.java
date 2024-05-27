package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Event.Render.RenderNameTagEvent;
import dev.px.leapfrog.API.Event.World.WorldBlockAABBEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.API.Util.Render.RenderUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

@Module.ModuleInterface(name = "Test Module", type = Type.Combat, description = "Balls")
public class TestModule extends Module {

    public Setting<Mode> mode = create(new Setting<>("Mode", Mode.AAC));

    private Setting<Float> timer = create(new Setting<>("Timer", 1.0f, 0.0f, 5.0f));
    private Setting<Float> speedV = create(new Setting<>("Speed onGround", 1.0f, 0.0f, 10.0f));
    private Setting<Float> speedH = create(new Setting<>("Speed offGround", 1.0f, 0.0f, 10.0f));

    private double speed = 0.0D;
    private int stage = 0;
    private int movementTicks = 0;

    private Entity entity;

    @EventHandler
    private Listener<PlayerMotionEvent> updateEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {

        } else {

        }
    });

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {

    });

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        for(EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == null) {
                continue;
            }
            if (!(player instanceof EntityLivingBase)) {
                continue;
            }

            this.renderName(
                    RenderUtil.renderInterpolations(player, event.getPartialTicks())[0],
                    RenderUtil.renderInterpolations(player, event.getPartialTicks())[1],
                    RenderUtil.renderInterpolations(player, event.getPartialTicks())[2], player);
        }
    });

    private void renderName(double x, double y, double z, EntityPlayer player) {
        GL11.glPushMatrix();



        GL11.glPopMatrix();
    }

    @EventHandler
    private Listener<RenderNameTagEvent> tagListener = new Listener<>(event -> {
        event.cancel();
    });

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.entity = null;
        ((IMixinMinecraft) mc).setRightClickDelayTimer(4);
    }

    private void AAC() {

    }

    private void Grim() {

    }

    private void NCP() {

    }

    private enum Mode {
        AAC,
        Grim,
        NCP
    }

}
