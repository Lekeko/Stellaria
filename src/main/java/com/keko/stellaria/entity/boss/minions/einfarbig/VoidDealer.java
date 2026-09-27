package com.keko.stellaria.entity.boss.minions.einfarbig;

import com.keko.stellaria.entity.boss.minions.EinMinAttacks;
import com.keko.stellaria.helper.UtilHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class VoidDealer extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final int MAX_HP = 800;
    private float damageTicker = 0;

    private Player target = null;
    private int changeTargetTimer = 0;
    private final int MAX_TARGET_TIEMR = 15 * 20;


    //Attack vars
    private boolean startedAttack = false;
    private int actualAttackTimer = 0;
    private int attack = 0;
    private final int ATTACK_NR = 4;
    private int attakTimers[] = {0, 30, 8, 40, 8};
    private int cd = 0;
    private final int MAX_CD = 40;

    //attacks sub vars
        //hat
        private int remaininghatCharges = 0;
        private final int MAX_HAT_CHARGES = 3;


    @Override
    public @Nullable Player getTarget() {
        return target;
    }

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
        if (target == null || --changeTargetTimer <= 0) {
            target = UtilHelper.getPlayerTarget(this, -1);
            changeTargetTimer = MAX_TARGET_TIEMR;

            if (target == null)
                return;
        }

        lookAt(target, 360.0F, 360.0F);
        this.hurtMarked = true;

        if (--cd >0) return;

        if (!startedAttack){
            evalAttack();
        }else {
            if (--actualAttackTimer <= 0){
                switch (attack){
                    case 1 -> {
                        EinMinAttacks.ripFabric(this);
                    }
                    case 2 -> {
                        EinMinAttacks.shootCards(this);
                        if (--remaininghatCharges > 0) {
                            actualAttackTimer = attakTimers[attack];
                            return;
                        }
                    }
                    case 3 -> {
                        EinMinAttacks.shootLaser(this);
                    }
                }

                attack = 0;
                startedAttack = false;
                cd = MAX_CD;

            }
        }
    }

    private void evalAttack() {
        startedAttack = true;
        attack = random.nextIntBetweenInclusive(1, ATTACK_NR - 1);
        actualAttackTimer = attakTimers[attack];
        lookAt(target, 360.0F, 360.0F);

        beginAnimation();
    }

    private void beginAnimation() {
        switch (attack){
            case 1 -> {
                triggerAnim("controller", "void_dealer.double_range_attack");
            }
            case 2 -> {
                remaininghatCharges = MAX_HAT_CHARGES;
                triggerAnim("controller", "void_dealer.swirl");
            }
            case 3 -> {
                triggerAnim("controller", "void_dealer.laser");
            }
            case 4 -> {
                triggerAnim("controller", "void_dealer.swirl2");
            }

        }
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
