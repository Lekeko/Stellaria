package com.keko.stellaria.entity.boss.minions.einfarbig;

import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.helper.UtilHelper;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class VoidDealer extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final int MAX_HP = 400;
    private float damageTicker = 0;

    private Player target = null;
    private int changeTargetTimer = 0;
    private final int MAX_TARGET_TIEMR = 15 * 20;
    private boolean spawnednext = false;


    //Attack vars
    private boolean startedAttack = false;
    private int actualAttackTimer = 0;
    private int attack = 0;
    private final int ATTACK_NR = 4;
    private int attakTimers[] = {0, 15, 8, 20, 8};
    private final int MAX_CD = 40;
    private int cd = MAX_CD + 20;

    private final float MELEE_DMG = 22.0f;

    //attacks sub vars
        //hat
        private int remaininghatCharges = 0;
        private final int MAX_HAT_CHARGES = 3;
    //Movement vars

    private double orbitAngle;
    private double speed = 1.0d;



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
    public boolean isNoGravity() {
        return true;
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
        boolean anyoneAlive = false;
        for (Player player : level().players()) {
            if (player.isAlive() && !player.isSpectator()) {
                anyoneAlive = true;
                break;
            }
        }

        if (!anyoneAlive) {
            discard();
            return;
        }

        if (target == null || --changeTargetTimer <= 0 || target.isDeadOrDying()) {
            target = UtilHelper.getPlayerTarget(this, -1);
            changeTargetTimer = MAX_TARGET_TIEMR;
            if (target == null) {
                discard();
                return;
            }
        }

        if (isDeadOrDying())
            return;

        faceTarget();
        wanderAround();
        damageTargetNearby();

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
                        attack = 0;
                        startedAttack = false;
                        cd = MAX_CD + 40;
                        return;
                    }
                }

                attack = 0;
                startedAttack = false;
                cd = MAX_CD;

            }
        }
    }

    private void damageTargetNearby() {
        if (distanceTo(target) < 5.0f){
            target.hurt(level().damageSources().mobAttack(this), MELEE_DMG);
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!spawnednext && !level().isClientSide) {
            spawnednext = true;
            StarPhantom starPhantom = new StarPhantom(ModEntities.STAR_PHANTOM, level());
            starPhantom.setPos(position().add(0.0f, 5.0f, 0.0f));
            level().addFreshEntity(starPhantom);
            for (ServerPlayer player : level().getServer().getPlayerList().getPlayers()) {
                player.playNotifySound(ModSounds.STAR_PHANTOM_TP, SoundSource.MASTER, 1.0f, 1.0f);
            }
        }
        super.die(damageSource);
    }

    private void wanderAround() {

        double radius = 50.5f;
        double height = 10.5f;
        orbitAngle += 0.01f;
        Vec3 targetPos = target.position().add(
                Math.cos(orbitAngle) * radius,
                height + Math.sin(tickCount * 0.01),
                Math.sin(orbitAngle) * radius
        );

        Vec3 dir = targetPos.subtract(position()).scale(0.4);
        if (dir.length() > speed)
            dir = dir.normalize().scale(speed);

        Vec3 vel = getDeltaMovement().lerp(dir, 0.15);

        setDeltaMovement(vel);
        move(MoverType.SELF, vel);

    }

    private void faceTarget() {
        if (target == null) return;

        double dx = target.getX() - getX();
        double dy = target.getEyeY() - getEyeY();
        double dz = target.getZ() - getZ();
        double horiz = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Mth.atan2(dz, dx) * (180.0f / Math.PI)) - 90.0f;
        float pitch = (float) -(Mth.atan2(dy, horiz) * (180.0f / Math.PI));

        setYRot(yaw);
        setXRot(pitch);
        setYHeadRot(yaw);
        this.yBodyRot = yaw;
        this.yRotO = yaw;
        this.yBodyRotO = yaw;
        this.yHeadRotO = yaw;
    }

    private void evalAttack() {
        startedAttack = true;
        attack = random.nextIntBetweenInclusive(1, 3);
        actualAttackTimer = attakTimers[attack];

        beginAnimation();
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
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
