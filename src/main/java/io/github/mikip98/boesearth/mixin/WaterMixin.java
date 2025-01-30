package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mikip98.boesearth.blockstates.DistanceToShore;
import io.github.mikip98.boesearth.blockstates.IsOnLeaves;
import io.github.mikip98.boesearth.blockstates.SnowOnTop;
import io.github.mikip98.boesearth.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;

@Mixin(FluidBlock.class)
public class WaterMixin extends Block {
    @Shadow @Final public static IntProperty LEVEL;

    public WaterMixin(Settings settings) { super(settings); }


    @Inject(at = @At("HEAD"), method = "appendProperties")
    public void addBlockState(CallbackInfo info, @Local(argsOnly = true) StateManager.Builder<Block, BlockState> builder) {
        builder.add(DistanceToShore.DISTANCE_TO_SHORE);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void correctDefaultState(CallbackInfo info) {
        FluidBlock new_this = (FluidBlock) (Object) this;
        setDefaultState(new_this.getDefaultState().withIfExists(DistanceToShore.DISTANCE_TO_SHORE, 15));  // withIfExists
    }

//    @Override
//    public void modifyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
//
//    }

    @Inject(at = @At("RETURN"), method = "getFluidState", cancellable = true)
    public void getFluidState(BlockState state, CallbackInfoReturnable<FluidState> cir) {
        cir.setReturnValue(cir.getReturnValue().with(DistanceToShore.DISTANCE_TO_SHORE, state.get(DistanceToShore.DISTANCE_TO_SHORE)));
//        cir.setReturnValue(cir.getReturnValue().withIfExists(DistanceToShore.DISTANCE_TO_SHORE, 1));
//        cir.setReturnValue(cir.getReturnValue().withIfExists(LEVEL, 1));
    }


    @Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate")
    private void updateStateOnNeighborChange(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        BlockState northernState = world.getBlockState(pos.north());
        BlockState easternState = world.getBlockState(pos.east());
        BlockState southernState = world.getBlockState(pos.south());
        BlockState westernState = world.getBlockState(pos.west());

        int distanceToShore = 15;

        if      (northernState.isFullCube(world, pos.north())) distanceToShore = 0;
        else if (easternState.isFullCube(world, pos.east()))   distanceToShore = 0;
        else if (southernState.isFullCube(world, pos.south())) distanceToShore = 0;
        else if (westernState.isFullCube(world, pos.west()))   distanceToShore = 0;

        if (distanceToShore == 0) {
            world.setBlockState(pos, state.with(DistanceToShore.DISTANCE_TO_SHORE, distanceToShore), 3, 16);
        }
        else {
            if (northernState.contains(DistanceToShore.DISTANCE_TO_SHORE)) distanceToShore = Math.min(distanceToShore, northernState.get(DistanceToShore.DISTANCE_TO_SHORE) + 1);
            if (easternState.contains(DistanceToShore.DISTANCE_TO_SHORE))   distanceToShore = Math.min(distanceToShore, easternState.get(DistanceToShore.DISTANCE_TO_SHORE) + 1);
            if (southernState.contains(DistanceToShore.DISTANCE_TO_SHORE)) distanceToShore = Math.min(distanceToShore, southernState.get(DistanceToShore.DISTANCE_TO_SHORE) + 1);
            if (westernState.contains(DistanceToShore.DISTANCE_TO_SHORE))   distanceToShore = Math.min(distanceToShore, westernState.get(DistanceToShore.DISTANCE_TO_SHORE) + 1);

            world.setBlockState(pos, state.with(DistanceToShore.DISTANCE_TO_SHORE, distanceToShore), 3, 16);
        }
    }





//    @Inject(at = @At("RETURN"), method = "getCollisionShape", cancellable = true)
//    public void getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
//        cir.setReturnValue(VoxelShapes.fullCube());
//    }

    @Inject(at = @At("RETURN"), method = "getOutlineShape", cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        cir.setReturnValue(VoxelShapes.cuboid(0.125D, 0.125D, 0.125D, 1.0D, 1.0D, 1.0D));
    }
}
