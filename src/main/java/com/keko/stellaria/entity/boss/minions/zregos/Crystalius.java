package com.keko.stellaria.entity.boss.minions.zregos;

import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.particles.QuasarParticlesHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class Crystalius extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private Player target = null;
    private int rollTimer = 20;
    private int atCd = 0;
    private final int MAX_AT_CD = 20;

    private static final float BASE_HP = 25.0f;
    private static final float BASE_DMG = 8.0f;
    private static final float BASE_AR = 2.0f;

    private float damagedTicker = 0;
    private final float MAX_DMG_TICKER = 5;

    public float getDamagedTicker() {
        return damagedTicker;
    }

    public void decreaseDamagedTicker() {
        this.damagedTicker -= 1;
    }

    public void setDamagedTicker(float damagedTicker) {
        this.damagedTicker = damagedTicker;
    }

    public static AttributeSupplier.Builder setAtributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, BASE_HP)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, BASE_DMG)
                .add(Attributes.ARMOR, BASE_AR)
                .add(Attributes.ATTACK_KNOCKBACK, 4);

    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (level().isClientSide){
            setDamagedTicker(MAX_DMG_TICKER);
        }
        return super.hurt(damageSource, f);
    }

    public Crystalius(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide){
            if (isDeadOrDying()) {
                discard();
            }
            invulnerableTime = 0;
            if (atCd > 0)
                atCd --;
            if (target == null){
                findTarget();
            }else {
                followTarget();
                if (distanceTo(target) < 4 && atCd <= 0 && !isDeadOrDying())
                    attackAround();
            }
        }else {
            if (isDeadOrDying()) {
                QuasarParticlesHandler.renderZregosMinionsParticlesDeath(this);
            }
            if (getDamagedTicker() > 0)
                decreaseDamagedTicker();
        }
    }

    private void followTarget() {
        if (target != null || target.isDeadOrDying()) {
            lookAt(target, 360.0F, 360.0F);
        }

        if (rollTimer-- <= 0){
            rollTimer = 20 + level().random.nextIntBetweenInclusive(3,8);
            triggerAnim("controller", "crystalius.roll");
            float speed = 2.0f;

            Vec3 vel = target.position().subtract(position()).normalize().scale(speed);
            hurtMarked = true;
            addDeltaMovement(vel);
        }
    }

    private void attackAround() {
        atCd = MAX_AT_CD;
        AABB aabb = SpaceHelpers.createEvenBox(position(), 6);
        for (Player player : level().getEntitiesOfClass(Player.class, aabb)){
            player.hurt(this.damageSources().mobAttack(this), 5.5f);
        }
        triggerAnim("controller", "crystalius.attack");
    }

    private void findTarget() {
        float distance = 999999999;
        for (Player player : level().players()) {
            if (player.distanceTo(this) < distance) {
                target = player;
                distance = player.distanceTo(this);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Crystalius> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("crystalius.attack", RawAnimation.begin().thenPlay("crystalius.attack"));
        controller.triggerableAnim("crystalius.roll", RawAnimation.begin().thenPlay("crystalius.roll"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public void knockback(double d, double e, double f) {
        super.knockback(d, e, f);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
