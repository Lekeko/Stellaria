package com.keko.stellaria.entity.boss.minions.einfarbig;

import com.keko.stellaria.entity.boss.minions.zregos.Crystalius;
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

public class StarPhantom extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private boolean sleeping = true;
    private static final int MAX_HP = 700;

    private float damageTicker = 0;

    public float getDamageTicker() {
        return damageTicker;
    }

    public void decreaseDamageTicker(){
        this.damageTicker--;
    }

    public StarPhantom(EntityType<? extends Monster> entityType, Level level) {
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
        AnimationController<StarPhantom> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("star_phantom.spin_and_attack", RawAnimation.begin().thenPlay("star_phantom.spin_and_attack"));
        controller.triggerableAnim("star_phantom.fly", RawAnimation.begin().thenPlay("star_phantom.fly"));
        controller.triggerableAnim("star_phantom.slice", RawAnimation.begin().thenPlay("star_phantom.slice"));
        controller.triggerableAnim("star_phantom.sleep", RawAnimation.begin().thenPlay("star_phantom.sleep"));

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

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("star_phantom.sleep"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
