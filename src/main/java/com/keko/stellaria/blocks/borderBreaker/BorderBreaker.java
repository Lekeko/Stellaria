package com.keko.stellaria.blocks.borderBreaker;

import com.keko.stellaria.blocks.ModBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BorderBreaker extends BaseEntityBlock {
    public BorderBreaker(Properties properties) {
        super(properties);
    }

    private static VoxelShape voxelShape = Block.box(0, 0, 0, 16, 16 * 3, 16);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BorderBreakerEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(
                type,
                ModBlockEntity.BORDER_BREAKER_ENTITY_BLOCK,
                BorderBreakerEntity::tick
        );
    }


    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(blockPos) instanceof BorderBreakerEntity entity) {
            if (player.isShiftKeyDown()) {
                changeSize(entity);
            } else {
                drill(entity);
            }
        }
        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    private void drill(BorderBreakerEntity entity) {
        if (!entity.isDrilling()) {
            entity.setDrilling(!entity.isDrilling());
        }
    }

    private void changeSize(BorderBreakerEntity entity) {
        if (entity.isDrilling()) return;
        entity.setSize(entity.getSize() + 2);
        if (entity.getSize() > 5)
            entity.setSize(1);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return voxelShape;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return voxelShape;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return voxelShape;
    }

}
