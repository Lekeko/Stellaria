package com.keko.stellaria.entity.projectiles.boss.zregos;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class MaceProjectile extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private Player target = null;

    public MaceProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
        this.setSoundEvent(SoundEvents.GLASS_FALL);

    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof Player && getOwner() != null){
            entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 15.0f);
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (tickCount > 5 * 20) discard();
            if (target == null){
                searchTarget();
            }else {
                followTarget();
            }
        }
    }

    private void followTarget() {
        Vec3 dir =  target.getEyePosition().subtract(position()).normalize();

        Vec3 curVel = getDeltaMovement().normalize();

        float turn = 0.26f;
        float speed = 1.5f;

        Vec3 newDir = curVel.scale(1.0 - turn).add(dir.scale(turn)).normalize();

        setDeltaMovement(newDir.scale(speed));
        this.hurtMarked = true;
    }

    private void searchTarget() {
        float distance = 999999999;
        for (Player player : level().players()) {
            if (player.distanceTo(this) < distance) {
                target = player;
                distance = player.distanceTo(this);
            }
        }
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
