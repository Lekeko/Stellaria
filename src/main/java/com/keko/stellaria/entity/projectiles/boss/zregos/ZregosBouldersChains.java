package com.keko.stellaria.entity.projectiles.boss.zregos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ZregosBouldersChains extends Entity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private Player target = null;
    private final int animLen = 2 * 20;

    private static EntityDataAccessor<Float> rotationX =
            SynchedEntityData.defineId(ZregosBouldersChains.class, EntityDataSerializers.FLOAT);
    private static EntityDataAccessor<Float> rotationY =
            SynchedEntityData.defineId(ZregosBouldersChains.class, EntityDataSerializers.FLOAT);


    public ZregosBouldersChains(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public void handleDamageEvent(DamageSource damageSource) {

    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(rotationX, 0.0f);
        builder.define(rotationY, 0.0f);
    }



    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (target!= null){
                setPos(target.getPosition(1.0f));
            }

            if (tickCount > animLen){
                if (target != null){
                    target.hurt(level().damageSources().magic(), 20.0f);
                    target.invulnerableTime = 0;
                }
                discard();
            }
        }else {
            if (tickCount == 1)
                triggerAnim("controller","zregos_boulders_chains.destroy");
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("rotation")){
            this.setRotationX(compoundTag.getFloat("rotationX"));
            this.setRotationY(compoundTag.getFloat("rotationY"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("rotationX", getRotationX());
        compoundTag.putFloat("rotationY", getRotationY());
    }

    public void setRotationX(float rotation) {
        this.entityData.set(this.rotationX, rotation);
    }
    public void setRotationY(float rotation) {this.entityData.set(this.rotationY, rotation);}

    public float getRotationX() {
        return this.entityData.get(rotationX);
    }

    public float getRotationY() {
        return this.entityData.get(rotationY);
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Player getTarget() {
        return target;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ZregosBouldersChains> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("zregos_boulders_chains.destroy", RawAnimation.begin().thenPlayAndHold("zregos_boulders_chains.destroy"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
