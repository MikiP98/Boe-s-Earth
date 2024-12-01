package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mikip98.boesearth.blockstates.IsOnLeaves;
import io.github.mikip98.boesearth.config.ModConfig;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(VineBlock.class)
public abstract class VineMixin extends Block {
    public VineMixin(Settings settings) {
        super(settings);
    }


    @Shadow
    public static final BooleanProperty NORTH = ConnectingBlock.NORTH;
    @Shadow
    public static final BooleanProperty EAST = ConnectingBlock.EAST;
    @Shadow
    public static final BooleanProperty SOUTH = ConnectingBlock.SOUTH;
    @Shadow
    public static final BooleanProperty WEST = ConnectingBlock.WEST;


    @Inject(at = @At("HEAD"), method = "appendProperties")
    private void addBlockState(CallbackInfo info, @Local(argsOnly = true) StateManager.Builder<Block, BlockState> builder) {
        builder.add(IsOnLeaves.IS_ON_LEAVES);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void correctDefaultState(CallbackInfo info) {
        VineBlock new_this = (VineBlock) (Object) this;
        setDefaultState(new_this.getDefaultState().with(IsOnLeaves.IS_ON_LEAVES, false));
    }

    @Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
    private void modifyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (ModConfig.vinesOnLeavesBlockstate) {
            BlockState currentState = cir.getReturnValue();
            if (currentState == null) return;

            boolean supportedOnLeaves = supportedOnLeaves(currentState, ctx.getWorld(), ctx.getBlockPos());
            cir.setReturnValue(currentState.with(IsOnLeaves.IS_ON_LEAVES, supportedOnLeaves));
        }
    }

    @Inject(at = @At("RETURN"), method = "randomTick")
    private void fixStateOnRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (ModConfig.doRandomTickVineUpdates && !(state.get(IsOnLeaves.IS_ON_LEAVES) && ModConfig.vinesOnLeavesBlockstate)) {
            if (ModConfig.vinesOnLeavesBlockstate) {
                boolean supportedOnLeaves = supportedOnLeaves(state, world, pos);
                world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, supportedOnLeaves));
            } else {
                world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, false));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate")
    private void updateStateOnNeighborChange(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (ModConfig.vinesOnLeavesBlockstate) {
            boolean supportedOnLeaves = supportedOnLeaves(state, world, pos);
            world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, supportedOnLeaves), 2);
        } else {
            world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, false), 2);
        }
    }

    @Unique
    private static boolean supportedOnLeaves(BlockState state, WorldAccess world, BlockPos pos) {
        boolean isOnBlock = false;
        boolean isOnLeaves = false;

        if (state.get(NORTH)) {
            BlockState blockNorth = world.getBlockState(pos.north());
            isOnBlock = checkIfOnBlock(blockNorth, world, pos.north());
            isOnLeaves = checkIfOnLeaves(blockNorth);
        }
        if (!isOnBlock || ModConfig.VinePrioritiseLeaves) {
            if (state.get(EAST)) {
                BlockState blockEast = world.getBlockState(pos.east());
                isOnBlock |= checkIfOnBlock(blockEast, world, pos.east());
                isOnLeaves |= checkIfOnLeaves(blockEast);
            }
        }
        if (!isOnBlock || ModConfig.VinePrioritiseLeaves) {
            if (state.get(SOUTH)) {
                BlockState blockSouth = world.getBlockState(pos.south());
                isOnBlock |= checkIfOnBlock(blockSouth, world, pos.south());
                isOnLeaves |= checkIfOnLeaves(blockSouth);
            }
        }
        if (!isOnBlock || ModConfig.VinePrioritiseLeaves) {
            if (state.get(WEST)) {
                BlockState blockWest = world.getBlockState(pos.west());
                isOnBlock |= checkIfOnBlock(blockWest, world, pos.west());
                isOnLeaves |= checkIfOnLeaves(blockWest);
            }
        }

        if (!isOnBlock || ModConfig.VinePrioritiseLeaves) {
            if (!isOnLeaves) {
                BlockState blockUp = world.getBlockState(pos.up());
                isOnLeaves = blockUp.getOrEmpty(IsOnLeaves.IS_ON_LEAVES).orElse(false);
            }
        }

        return isOnLeaves && !(isOnBlock && !ModConfig.VinePrioritiseLeaves);
    }
    @Unique
    private static boolean checkIfOnBlock(BlockState state, WorldAccess world, BlockPos pos) {
        if (state == null) return false;
        return (!(state.isIn(BlockTags.LEAVES) || state.getBlock() instanceof LeavesBlock)
                        && !state.isAir()
                        && (state.isFullCube(world, pos) && !(state.getProperties().contains(IsOnLeaves.IS_ON_LEAVES) && state.get(IsOnLeaves.IS_ON_LEAVES)))
                        && !(state.canPathfindThrough(NavigationType.AIR) || state.canPathfindThrough(NavigationType.WATER) || state.canPathfindThrough(NavigationType.LAND))
        );
    }
    @Unique
    private static boolean checkIfOnLeaves(BlockState state) {
        if (state == null) return false;
        return state.isIn(BlockTags.LEAVES) || (state.getProperties().contains(IsOnLeaves.IS_ON_LEAVES) && state.get(IsOnLeaves.IS_ON_LEAVES));
    }
}
