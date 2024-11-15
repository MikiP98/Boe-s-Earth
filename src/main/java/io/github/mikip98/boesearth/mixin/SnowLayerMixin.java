package io.github.mikip98.boesearth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(SnowBlock.class)
public abstract class SnowLayerMixin extends Block {
    @Unique
    private static final String PROPERTY_NAME = "is_on_leaves";
    @Unique
    private static final BooleanProperty IS_ON_LEAVES = BooleanProperty.of(PROPERTY_NAME);

    /// WHY?!
    public SnowLayerMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "appendProperties")
    private void addBlockState(CallbackInfo info, @Local(argsOnly = true) Builder<Block, BlockState> builder) {
        builder.add(IS_ON_LEAVES);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void correctDefaultState(CallbackInfo info) {
        SnowBlock new_this = (SnowBlock) (Object) this;
        setDefaultState(new_this.getDefaultState().with(IS_ON_LEAVES, false));
    }

    @Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
    private void modifyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState blockBelow = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
        AtomicBoolean isOnLeaves = new AtomicBoolean(blockBelow.isIn(BlockTags.LEAVES));

        if (!isOnLeaves.get()) {
            blockBelow.getProperties().forEach(property -> {
                if (property.getName().equals(PROPERTY_NAME)) {
                    isOnLeaves.set(blockBelow.get(IS_ON_LEAVES));
                }
            });
        }

        cir.setReturnValue(cir.getReturnValue().with(IS_ON_LEAVES, isOnLeaves.get()));
    }

    @Inject(at = @At("TAIL"), method = "randomTick")
    private void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!state.get(IS_ON_LEAVES)) {
            BlockState blockBelow = world.getBlockState(pos.down());
            if (blockBelow.isIn(BlockTags.LEAVES)) {
                world.setBlockState(pos, state.with(IS_ON_LEAVES, true));
            } else {
                blockBelow.getProperties().forEach(property -> {
                    if (property.getName().equals(PROPERTY_NAME)) {
                        world.setBlockState(pos, state.with(IS_ON_LEAVES, blockBelow.get(IS_ON_LEAVES)));
                    }
                });
            }
        }
    }
}
