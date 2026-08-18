package com.keko.stellaria.blocks.portal;

import com.keko.stellaria.blocks.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DraconicCrystalBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public DraconicCrystalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DRACONIC_CRYSTAL_BLOCK_ENTITY, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DraconicCrystalBlockEntity entity) {
        if (level.isClientSide) {
            return;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<DraconicCrystalBlockEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("draconic_crystal.idle", RawAnimation.begin().thenLoop("draconic_crystal.idle"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("draconic_crystal.idle"));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
