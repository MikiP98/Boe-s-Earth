package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mikip98.boesearth.blockstates.IsOnLeaves;
import io.github.mikip98.boesearth.blockstates.SnowOnTop;
import io.github.mikip98.boesearth.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowBlock.class)
public abstract class SnowLayerMixin extends Block {
    public SnowLayerMixin(Settings settings) {
        super(settings);
    }


    @Inject(at = @At("HEAD"), method = "appendProperties")
    private void addBlockState(CallbackInfo info, @Local(argsOnly = true) Builder<Block, BlockState> builder) {
        builder.add(IsOnLeaves.IS_ON_LEAVES);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void correctDefaultState(CallbackInfo info) {
        SnowBlock new_this = (SnowBlock) (Object) this;
        setDefaultState(new_this.getDefaultState().with(IsOnLeaves.IS_ON_LEAVES, false));
    }

    @Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
    private void modifyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (ModConfig.snowOnLeavesBlockstate) {
            boolean isOnLeaves = checkIfOnLeaves(ctx.getWorld(), ctx.getBlockPos());
            cir.setReturnValue(cir.getReturnValue().with(IsOnLeaves.IS_ON_LEAVES, isOnLeaves));

            World world = ctx.getWorld();
            BlockPos down = ctx.getBlockPos().down();
            BlockState below = world.getBlockState(down);
            if (isOnLeaves && ModConfig.leavesWithSnowOnTopBlockstate && below.contains(SnowOnTop.SNOW_ON_TOP)) {
                world.setBlockState(down, below.with(SnowOnTop.SNOW_ON_TOP, true));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "randomTick")
    private void fixStateOnRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (ModConfig.doRandomTickSnowUpdates && !(state.get(IsOnLeaves.IS_ON_LEAVES) && ModConfig.snowOnLeavesBlockstate)) {
            if (ModConfig.snowOnLeavesBlockstate) {
                boolean isOnLeaves = checkIfOnLeaves(world, pos);
                world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, isOnLeaves));

                BlockState below = world.getBlockState(pos.down());
                if (isOnLeaves && ModConfig.leavesWithSnowOnTopBlockstate && below.contains(SnowOnTop.SNOW_ON_TOP)) {
                    world.setBlockState(pos.down(), below.with(SnowOnTop.SNOW_ON_TOP, true));
                }
            } else {
                world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, false));
            }
        }
    }

    @Unique
    private static boolean checkIfOnLeaves(World world, BlockPos pos) {
        BlockState blockBelow = world.getBlockState(pos.down());
        boolean isOnLeaves = blockBelow.isIn(BlockTags.LEAVES);

        if (!isOnLeaves) {
            isOnLeaves = blockBelow.getOrEmpty(IsOnLeaves.IS_ON_LEAVES).orElse(false);
        }

        return isOnLeaves;
    }
}
