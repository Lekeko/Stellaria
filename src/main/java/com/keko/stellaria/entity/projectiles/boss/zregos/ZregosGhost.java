package com.keko.stellaria.entity.projectiles.boss.zregos;

import com.keko.stellaria.helper.Vec3Helper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ZregosGhost extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private boolean playedAnim = false;
    private boolean damager = false;

    private static EntityDataAccessor<Float> rotation =
            SynchedEntityData.defineId(ZregosGhost.class, EntityDataSerializers.FLOAT);


    public ZregosGhost(EntityType<? extends ZregosGhost> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }


    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        triggerAnim("controller", "zregos_ghost.dash");


    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(rotation, 0.0f);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putFloat("rotation", getRotation());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("rotation")){
            this.setRotation(compoundTag.getFloat("rotation"));
        }
    }

    public void setDamager(boolean damager) {
        this.damager = damager;
    }

    public void setRotation(float rotation) {
        this.entityData.set(this.rotation, rotation);
    }

    public float getRotation() {
        return this.entityData.get(rotation);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide && !playedAnim) {
            playedAnim = true;
        }
        if (!level().isClientSide && tickCount == 7 && damager){
            damageNearby();
        }

        if (!level().isClientSide && tickCount > 50) discard();
    }

    private void damageNearby() {

        for (Player player : level().players()){
            if (Vec3Helper.ETEdistance(this, player) < 5){
                player.hurt(level().damageSources().mobAttack((LivingEntity) getOwner()), 10.0f);
                player.invulnerableTime = 5;
            }
        }

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ZregosGhost> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("zregos_ghost.dash", RawAnimation.begin().thenPlayAndHold("zregos_ghost.dash"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object entity) {
        return GeoEntity.super.getTick(entity);
    }
}
