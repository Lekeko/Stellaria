package com.keko.stellaria.entity.boss.minions.einfarbig;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class VoidDealer extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private boolean sleeping = true;
    private static final int MAX_HP = 800;
    private float damageTicker = 0;

    public float getDamageTicker() {
        return damageTicker;
    }

    public void decreaseDamageTicker(){
        this.damageTicker--;
    }

    public VoidDealer(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAtributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HP)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0);

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<VoidDealer> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("void_dealer.idle", RawAnimation.begin().thenPlay("void_dealer.idle"));
        controller.triggerableAnim("void_dealer.double_range_attack", RawAnimation.begin().thenPlay("void_dealer.double_range_attack"));
        controller.triggerableAnim("void_dealer.swirl", RawAnimation.begin().thenPlay("void_dealer.swirl"));
        controller.triggerableAnim("void_dealer.swirl2", RawAnimation.begin().thenPlay("void_dealer.swirl2"));
        controller.triggerableAnim("void_dealer.laser", RawAnimation.begin().thenPlay("void_dealer.laser"));

        controllers.add(controller);
    }

    @Override
    public void checkDespawn() {

    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide)
            clientTick();
        else
            serverTick();
    }

    private void clientTick() {
        if (getDamageTicker() > 0)
            decreaseDamageTicker();
    }

    private void serverTick() {

    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (level().isClientSide)
            damageTicker = 10;
        return super.hurt(damageSource, f);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("void_dealer.idle"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
