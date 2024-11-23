package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mikip98.boesearth.blockstates.IsOnLeaves;
import io.github.mikip98.boesearth.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CarpetBlock.class)
public class CarpetMixin extends BlockMixinForCarpetMixin {
    @Override
    public void addBlockState(CallbackInfo info, @Local(argsOnly = true) StateManager.Builder<Block, BlockState> builder) {
        builder.add(IsOnLeaves.IS_ON_LEAVES);
    }

    @Override
    public void correctDefaultState(CallbackInfo info) {
        CarpetBlock new_this = (CarpetBlock) (Object) this;
        setDefaultState(new_this.getDefaultState().withIfExists(IsOnLeaves.IS_ON_LEAVES, false));
    }

    @Override
    public void modifyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (ModConfig.carpetOnLeavesBlockstate) {
            boolean isOnLeaves = checkIfOnLeaves(ctx.getWorld(), ctx.getBlockPos());
            cir.setReturnValue(cir.getReturnValue().withIfExists(IsOnLeaves.IS_ON_LEAVES, isOnLeaves));
        }
    }

//    @Override
//    public void fixStateOnRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
//        if (ModConfig.correctSnowWithTime && !state.get(IsOnLeaves.IS_ON_LEAVES)) {
//            if (ModConfig.snowOnLeavesBlockstate) {
//                boolean isOnLeaves = checkIfOnLeaves(world, pos);
//                world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, isOnLeaves));
//            } else {
//                world.setBlockState(pos, state.with(IsOnLeaves.IS_ON_LEAVES, false));
//            }
//        }
//    }

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
