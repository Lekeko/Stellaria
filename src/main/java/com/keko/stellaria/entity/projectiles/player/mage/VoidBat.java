package com.keko.stellaria.entity.projectiles.player.mage;

import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.helper.Trail;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class VoidBat extends AbstractArrow implements GeoEntity {
    private final int STRIP_LENGTH = 20;
    Trail trail;

    private Entity target = null;
    public VoidBat(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.DISALLOWED;
        trail = new Trail(STRIP_LENGTH, this);
        setSoundEvent(SoundEvents.EMPTY);

    }

    public Trail getTrail() {
        return trail;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (!(getOwner() instanceof Player)) {
                discard();
                return;
            }

            if (inGround || tickCount > 80 ) {
                discard();
                return;
            }
            if (target == null){
                for (Entity entity : level().getEntitiesOfClass(LivingEntity.class, SpaceHelpers.createEvenBox(getPosition(1.0f), 20))) {
                    if (entity != getOwner()){
                        target = entity;
                    }
                }
            }else {
                if (((LivingEntity)target).isDeadOrDying()){
                    discard();
                    return;
                }
                trackTarget();
            }
        }else {
            if (level().isClientSide)
                trail.update(1.0f);
        }
    }

    private void trackTarget() {
        Vec3 targetPos = target.position();
        float speed = 6.0f;
        Vec3 dir = targetPos.subtract(position()).normalize().scale(speed);

        float speedScale = 0.54f;
        Vec3 vel = getDeltaMovement().lerp(dir, speedScale);
        setDeltaMovement(vel);
        hurtMarked = true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }


    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!entityHitResult.getEntity().is(getOwner())){
            entityHitResult.getEntity().hurt(level().damageSources().playerAttack((Player) getOwner()), 4.0f);
            entityHitResult.getEntity().invulnerableTime = 3;
            discard();
        }
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<VoidBat> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("void_bat.fly", RawAnimation.begin().thenLoop("void_bat.fly"));

        controllers.add(controller);
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("void_bat.fly"));
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
