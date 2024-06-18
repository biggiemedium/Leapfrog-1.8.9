package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.World.WorldBlockAABBEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

@Module.ModuleInterface(name = "Air Jump", type = Type.Movement, description = "Lets you run on air (only use vanilla servers bans fast)")
public class AirJump extends Module {

    public AirJump() {

    }

    private boolean fall = true;

    @EventHandler
    private Listener<WorldBlockAABBEvent> bb = new Listener<>(event -> {
        if (event.getBlock() instanceof BlockAir
                && !mc.gameSettings.keyBindSneak.isKeyDown()
                && event.getBlockPos().equals(mc.thePlayer.getPosition().down())
                && !fall){
            int x = event.getBlockPos().getX();
            int y = event.getBlockPos().getY();
            int z = event.getBlockPos().getZ();

            event.setBoundingBox(AxisAlignedBB.fromBounds(x, y, z, x + 1, y + 1F, z + 1));
        }
    });

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            mc.thePlayer.onGround = true;
            mc.thePlayer.isAirBorne = false;
            mc.thePlayer.fallDistance = 0;
            fall = !PlayerUtil.isMoving();
            //event.setOnGround(!PlayerUtil.isMoving());
        } else {

        }
    });
}
