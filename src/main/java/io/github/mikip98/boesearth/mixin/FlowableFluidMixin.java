package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mikip98.boesearth.blockstates.DistanceToShore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(FlowableFluid.class)
public class FlowableFluidMixin {

    @Inject(at = @At("HEAD"), method = "appendProperties")
    public void appendProperties(CallbackInfo info, @Local(argsOnly = true) StateManager.Builder<Block, BlockState> builder) {
        builder.add(DistanceToShore.DISTANCE_TO_SHORE);
    }

    @Inject(at = @At("RETURN"), method = "getUpdatedState", cancellable = true)
    protected void getUpdatedState(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<FluidState> cir) {
        Optional<Integer> value = state.getOrEmpty(DistanceToShore.DISTANCE_TO_SHORE);
        value.ifPresent(integer -> cir.setReturnValue(cir.getReturnValue().withIfExists(DistanceToShore.DISTANCE_TO_SHORE, integer)));
    }
}
