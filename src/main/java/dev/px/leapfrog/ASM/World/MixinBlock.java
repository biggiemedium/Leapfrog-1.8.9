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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Shadow
    public abstract AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state);

    /**
     * @author Strikeless
     * @since 15.03.2022
     */
    @Inject(method = "addCollisionBoxesToList", at = @At("HEAD"), cancellable = true)
    public void onCollisionBoxAdded(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity, CallbackInfo ci) {
        ci.cancel();
        AxisAlignedBB bb = this.getCollisionBoundingBox(worldIn, pos, state);
        WorldBlockAABBEvent event = new WorldBlockAABBEvent(worldIn, (Block) (Object) this, pos, bb, mask); // (Block) (Object)
        LeapFrog.EVENT_BUS.post(event);

        if(event.isCancelled()) {
            return;
        }

        if(event.getBoundingBox() != null && event.getMaskBoundingBox().intersectsWith(event.getBoundingBox())) {
            list.add(event.getBoundingBox());
        }
    }


    /*
    @Redirect(method = "addCollisionBoxesToList", at = @At("HEAD"))
    public void addCollision(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB bb = this.getCollisionBoundingBox(worldIn, pos, state);
        WorldBlockAABBEvent event = new WorldBlockAABBEvent(worldIn, (Block) (Object) this, pos, bb, mask);

        if(event.isCancelled()) {
            return;
        }

        if(event.getBoundingBox() != null && event.getMaskBoundingBox().intersectsWith(event.getBoundingBox())) {
            list.add(event.getBoundingBox());
        }
    }
     */

}
