package com.keko.stellaria.blocks.maceBlock;

import com.keko.stellaria.blocks.ModBlockEntity;
import com.keko.stellaria.entity.decour.zregosMace.ZregosMace;
import com.keko.stellaria.helper.SpaceHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashSet;
import java.util.Set;

public class ZregosMaceBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Set<ZregosMaceBlockEntity> connections = new HashSet<>();

    public ZregosMaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ZREGOS_MACE_BLOCK_ENTITY, pos, state);
    }
    private int searchCooldown = 0;

    private float clientTick = 0;

    public static void tick(Level level, BlockPos pos, BlockState state, ZregosMaceBlockEntity entity) {
        if (level.isClientSide){
            if (entity.clientTick < 1) {
                entity.searchConnections(level);
                entity.clientTick = 1;
            }
            entity.tickConnections(level);
        }
    }




    private void tickConnections(Level level) {
        if (searchCooldown-- <= 0) {
            searchCooldown = 100;
        }

        connections.removeIf(BlockEntity::isRemoved);
    }

    public void searchConnections(Level level) {
        if (level == null) return;
        connections.clear();

        BlockPos center = getBlockPos();
        int range = 25;

        BlockPos.betweenClosedStream(
                center.offset(-range, -range, -range),
                center.offset(range, range, range)
        ).forEach(pos -> {
            if (pos.equals(center)) return;

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ZregosMaceBlockEntity other) {
                if (!connections.contains(other)) {
                    connections.add(other);
                }
            }
        });

    }

    public float getClientTick() {
        return clientTick;
    }

    public void addClientTick(float clientTick) {
        this.clientTick += clientTick;
    }

    public Set<ZregosMaceBlockEntity> getConnections() {
        return connections;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ZregosMaceBlockEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("mace.loop", RawAnimation.begin().thenLoop("mace.loop"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("mace.loop"));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
