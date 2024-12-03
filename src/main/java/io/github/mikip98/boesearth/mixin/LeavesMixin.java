package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mikip98.boesearth.blockstates.IsOnLeaves;
import io.github.mikip98.boesearth.blockstates.SnowOnTop;
import io.github.mikip98.boesearth.config.ModConfig;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public class LeavesMixin extends Block implements Waterloggable {
    public LeavesMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "appendProperties")
    private void addBlockState(CallbackInfo info, @Local(argsOnly = true) StateManager.Builder<Block, BlockState> builder) {
        builder.add(SnowOnTop.SNOW_ON_TOP);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void correctDefaultState(CallbackInfo info) {
        LeavesBlock new_this = (LeavesBlock) (Object) this;
        // `.withIfExists` instead of `.with` for `Biomes'o Plenty` (`GlichCore`) compat (and probably more)
        setDefaultState(new_this.getDefaultState().withIfExists(SnowOnTop.SNOW_ON_TOP, false));
    }

    @Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
    private void modifyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState above = ctx.getWorld().getBlockState(ctx.getBlockPos().up());

        if (ModConfig.isOnLeavesBlockstate && above.contains(IsOnLeaves.IS_ON_LEAVES) && !above.get(IsOnLeaves.IS_ON_LEAVES)) {
            ctx.getWorld().setBlockState(ctx.getBlockPos().up(), above.with(IsOnLeaves.IS_ON_LEAVES, true), 3);
        }

        BlockState currentState = cir.getReturnValue();

        boolean isSnowOnTop = checkIfSnowOnTop(above);
        cir.setReturnValue(currentState.with(SnowOnTop.SNOW_ON_TOP, isSnowOnTop));
    }

    @Inject(at = @At("RETURN"), method = "hasRandomTicks", cancellable = true)
    private void hasRandomTicks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.doRandomTickLeavesUpdates) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("RETURN"), method = "randomTick")
    private void fixStateOnRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (ModConfig.doRandomTickLeavesUpdates) {  // && !(state.get(SnowOnTop.SNOW_ON_TOP) && ModConfig.leavesWithSnowOnTopBlockstate)
            BlockState above = world.getBlockState(pos.up());

            if (ModConfig.isOnLeavesBlockstate && above.contains(IsOnLeaves.IS_ON_LEAVES) && !above.get(IsOnLeaves.IS_ON_LEAVES) && ModConfig.carpetOnLeavesBlockstate && ModConfig.snowOnLeavesBlockstate) {
                world.setBlockState(pos.up(), above.with(IsOnLeaves.IS_ON_LEAVES, true), 3);
            }

            boolean isSnowOnTop = checkIfSnowOnTop(above);
            world.setBlockState(pos, state.with(SnowOnTop.SNOW_ON_TOP, isSnowOnTop));
        }
    }

    @Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate")
    private void updateStateOnNeighborChange(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (ModConfig.updateLeavesOnNeighborChange && (!ModConfig.updateLeavesOnNeighborChangeOnlyAbove || Direction.UP == direction)) {
            if (ModConfig.snowOnLeavesBlockstate) {
                BlockState above = world.getBlockState(pos.up());
                boolean isSnowOnTop = checkIfSnowOnTop(above);
                if (state.get(SnowOnTop.SNOW_ON_TOP) != isSnowOnTop) {
                    world.setBlockState(pos, state.with(SnowOnTop.SNOW_ON_TOP, isSnowOnTop), 3, ModConfig.maxLeavesUpdateChain);
                }
            } else if (state.get(SnowOnTop.SNOW_ON_TOP) && !ModConfig.leavesWithSnowOnTopBlockstate) {
                world.setBlockState(pos, state.with(SnowOnTop.SNOW_ON_TOP, false), 3, ModConfig.maxLeavesUpdateChain);
            }
        }
    }

    @Unique
    private static boolean checkIfSnowOnTop(BlockState above) {
        return ModConfig.leavesWithSnowOnTopBlockstate && (above.isIn(BlockTags.SNOW) || above.getBlock() instanceof SnowBlock || above.getSoundGroup() == BlockSoundGroup.SNOW);
    }
}
