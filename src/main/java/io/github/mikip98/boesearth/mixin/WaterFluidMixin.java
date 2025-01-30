package io.github.mikip98.boesearth.mixin;

import io.github.mikip98.boesearth.blockstates.DistanceToShore;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.fluid.WaterFluid.class)
public class WaterFluidMixin {

    @Inject(at = @At("RETURN"), method = "toBlockState", cancellable = true)
    public void toBlockState(FluidState state, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(cir.getReturnValue().with(DistanceToShore.DISTANCE_TO_SHORE, state.get(DistanceToShore.DISTANCE_TO_SHORE)));
    }
}
