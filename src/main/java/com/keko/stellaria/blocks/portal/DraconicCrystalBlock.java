package com.keko.stellaria.blocks.portal;

import com.keko.stellaria.blocks.ModBlockEntity;
import com.keko.stellaria.world.dimension.ModDimensions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DraconicCrystalBlock extends BaseEntityBlock {

    public DraconicCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DraconicCrystalBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(
                type,
                ModBlockEntity.DRACONIC_CRYSTAL_BLOCK_ENTITY,
                DraconicCrystalBlockEntity::tick
        );
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ServerLevel artha = player.getServer().getLevel(ModDimensions.ARTHA_LEVEL_KEY);

            if (artha != null) {
                serverPlayer.teleportTo(
                    artha,
                        0.0f, 2000.0d, 0.0f, 0.0f, 0.0f
                );
            }
        }
        return InteractionResult.SUCCESS;
    }
}
