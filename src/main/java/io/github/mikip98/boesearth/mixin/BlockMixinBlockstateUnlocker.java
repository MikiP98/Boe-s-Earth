package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixinBlockstateUnlocker {
    @Shadow
    private BlockState defaultState;

    @Unique
    protected final void setDefaultState(BlockState state) {
        this.defaultState = state;
    }

    @Inject(at = @At("HEAD"), method = "appendProperties")
    public void addBlockState(CallbackInfo info, @Local(argsOnly = true) StateManager.Builder<Block, BlockState> builder) {}

    @Inject(at = @At("TAIL"), method = "<init>")
    public void correctDefaultState(CallbackInfo info) {}

    @Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
    public void modifyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {}

//    @Inject(at = @At("HEAD"), method = "randomTick")
//    private void fixStateOnRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {}
}
