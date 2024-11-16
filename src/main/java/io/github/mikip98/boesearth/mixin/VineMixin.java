package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mikip98.boesearth.blockstates.IsOnLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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
        BlockState currentState = cir.getReturnValue();
        if (currentState == null) return;

        boolean supportedOnLeaves = supportedOnLeaves(currentState, ctx.getWorld(), ctx.getBlockPos());
        cir.setReturnValue(currentState.with(IsOnLeaves.IS_ON_LEAVES, supportedOnLeaves));
    }

    @Inject(at = @At("TAIL"), method = "randomTick")
    private void fixStateOnRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        boolean supportedOnLeaves = supportedOnLeaves(state, world, pos);
        world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, supportedOnLeaves));
    }

    @Unique
    private static boolean supportedOnLeaves(BlockState state, World world, BlockPos pos) {
        boolean isOnBlock = false;
        boolean isOnLeaves = false;

        if (state.get(NORTH)) {
            BlockState blockNorth = world.getBlockState(pos.north());
            isOnBlock = checkIfOnBlock(blockNorth, world, pos.north());
            isOnLeaves = checkIfOnLeaves(blockNorth);
        }
        if (!isOnBlock) {
            if (state.get(EAST)) {
                BlockState blockEast = world.getBlockState(pos.east());
                isOnBlock = checkIfOnBlock(blockEast, world, pos.east());
                isOnLeaves |= checkIfOnLeaves(blockEast);
            }
        }
        if (!isOnBlock) {
            if (state.get(SOUTH)) {
                BlockState blockSouth = world.getBlockState(pos.south());
                isOnBlock = checkIfOnBlock(blockSouth, world, pos.south());
                isOnLeaves = checkIfOnLeaves(blockSouth);
            }
        }
        if (!isOnBlock) {
            if (state.get(WEST)) {
                BlockState blockWest = world.getBlockState(pos.west());
                isOnBlock = checkIfOnBlock(blockWest, world, pos.west());
                isOnLeaves = checkIfOnLeaves(blockWest);
            }
        }

        if (!isOnBlock) {
            if (!isOnLeaves) {
                BlockState blockUp = world.getBlockState(pos.up());
                isOnLeaves = blockUp.getProperties().contains(IsOnLeaves.IS_ON_LEAVES) && blockUp.get(IsOnLeaves.IS_ON_LEAVES);
            }
        }

        return isOnLeaves && !isOnBlock;
    }
    @Unique
    private static boolean checkIfOnBlock(BlockState state, World world, BlockPos pos) {
        if (state == null) return false;
        return (!state.isIn(BlockTags.LEAVES)
                        && !state.isAir()
                        && (state.isFullCube(world, pos) && !(state.getProperties().contains(IsOnLeaves.IS_ON_LEAVES) && state.get(IsOnLeaves.IS_ON_LEAVES)))
                        && !(state.canPathfindThrough(world, pos, NavigationType.AIR) || state.canPathfindThrough(world, pos, NavigationType.WATER) || state.canPathfindThrough(world, pos, NavigationType.LAND))
        );
    }
    @Unique
    private static boolean checkIfOnLeaves(BlockState state) {
        if (state == null) return false;
        return state.isIn(BlockTags.LEAVES) || (state.getProperties().contains(IsOnLeaves.IS_ON_LEAVES) && state.get(IsOnLeaves.IS_ON_LEAVES));
    }
}
