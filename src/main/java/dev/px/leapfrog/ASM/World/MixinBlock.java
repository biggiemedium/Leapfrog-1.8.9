package dev.px.leapfrog.ASM.World;

import dev.px.leapfrog.API.Event.World.WorldBlockAABBEvent;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "addCollisionBoxesToList", at = @At("HEAD"), cancellable = true)
    public void onCollisionBoxAdded(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity, CallbackInfo ci) {
        WorldBlockAABBEvent event = new WorldBlockAABBEvent(worldIn, (Block) (Object) this, pos, mask);
        LeapFrog.EVENT_BUS.post(event);

        if(event.isCancelled()) {
            ci.cancel();
        }
    }

}
