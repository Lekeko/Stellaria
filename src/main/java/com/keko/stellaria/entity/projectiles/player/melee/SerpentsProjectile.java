package com.keko.stellaria.entity.projectiles.player.melee;

import com.keko.stellaria.helper.SpaceHelpers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
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

public class SerpentsProjectile extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private Entity target;


    public SerpentsProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
        this.setSoundEvent(SoundEvents.AMETHYST_BLOCK_CHIME);

    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_BLOCK_CHIME;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return entity instanceof Monster;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity instanceof Monster;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    public Entity getTarget() {
        return target;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide){
            if (tickCount == 1)
                triggerAnim("controller", "serpents_projectile.spin");
            if (tickCount > 60 && target != null) discard();

            if (target != null){
                Vec3 currentDirection = this.getDeltaMovement().normalize();
                Vec3 targetDirection = this.getTarget().getPosition(1.0f).subtract(getPosition(1.0f)).normalize();

                currentDirection = currentDirection.lerp(targetDirection, 0.9);
                float speed = 3.0f;

                this.setDeltaMovement(currentDirection.scale(speed));
                this.hurtMarked = true;
            }else {
                    searchTarget();//i dont like this
                if (tickCount > 100) discard();
            }

        }
    }

    @Override
    public void setBaseDamage(double d) { //TODO modi this after rouge like mech is done
        super.setBaseDamage(4.0f);
    }

    private void searchTarget() {
        for (Entity entity : level().getEntitiesOfClass(Monster.class, SpaceHelpers.createEvenBox(getPosition(1.0f), 40))){
            target = entity;
            return;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().hurt(level().damageSources().magic(), 2.0f);
        discard();
        entityHitResult.getEntity().invulnerableTime = 0;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<SerpentsProjectile> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("serpents_projectile.spin", RawAnimation.begin().thenLoop("serpents_projectile.spin"));

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
