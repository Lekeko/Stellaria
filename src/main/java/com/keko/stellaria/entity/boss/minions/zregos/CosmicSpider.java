package com.keko.stellaria.entity.boss.minions.zregos;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosProjectileLaser;
import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.particles.ModParticles;
import com.keko.stellaria.particles.QuasarParticlesHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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

import java.util.List;

import static com.keko.stellaria.entity.projectiles.boss.zregos.ZregosProjectileLaser.LASER_LEN_AFTER;

public class CosmicSpider extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final float BASE_HP = 35.0f;
    private static final float BASE_DMG = 8.0f;
    private static final float BASE_AR = 2.0f;

    private Player target = null;

    private int laserPhase = 0;
    private int laserTimer = 0;
    private int laserTimeStamps[] = {
            20,
            5,
            5,
            5,
            5
    };
    private int atCd = 0;

    private boolean readyToFire = false;

    private Vec3 targetPos = null;

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
    public boolean removeWhenFarAway(double d) {
        return false;
    }



    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (level().isClientSide){
            setDamagedTicker(MAX_DMG_TICKER);
        }
        return super.hurt(damageSource, f);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide){
            invulnerableTime = 0;

            setDeltaMovement(getDeltaMovement().scale(0.5f));
            hurtMarked = true;

            if (isDeadOrDying()) {
                discard();
            }

            if (target == null){
                findTarget();
            }else {
                followTarget();

                if (readyToFire){
                    lookAt(target, 360.0F, 360.0F);
                    this.yBodyRot = getYRot();
                    this.yHeadRot = getYRot();
                    this.yBodyRotO = getYRot();
                    this.yHeadRotO = getYRot();

                    if (atCd > 0)
                        atCd --;
                    shootLaser();

                }else {

                    if (targetPos == null)
                        setTargetPosNearPlayer();
                    else {
                        if (position().distanceTo(targetPos) < 1.5) {
                            engage();
                        } else {
                            travelToPos();
                        }
                    }
                }
            }
        }else {
            if (isDeadOrDying()) {
                QuasarParticlesHandler.renderZregosMinionsParticlesDeath(this);
            }
            if (getDamagedTicker() > 0)
                decreaseDamagedTicker();
        }
    }

    private void engage() {
        readyToFire = true;

    }

    private void travelToPos() {
        float speed = 1.3f;

        Vec3 vel = targetPos.subtract(position()).normalize().scale(speed);
        hurtMarked = true;
        addDeltaMovement(vel);
    }

    private void setTargetPosNearPlayer() {
        int dispersion = 90;
        targetPos = target.position().add(
                (random.nextIntBetweenInclusive(0, dispersion) + dispersion/2.5f) * (random.nextBoolean() ? -1 : 1),
                (random.nextIntBetweenInclusive(0, dispersion/3) + dispersion/6.0f) * (random.nextBoolean() ? -1 : 1),
                (random.nextIntBetweenInclusive(0, dispersion) + dispersion/2.5f) * (random.nextBoolean() ? -1 : 1)

        );
    }

    private void followTarget() {
        if (target == null)
            return;
        if (targetPos != null)
            lookAtPosition(targetPos, 360.0F, 360.0F);

    }

    public void lookAtPosition(Vec3 pos, float f, float g) {
        double d = pos.x - this.getX();
        double e = pos.z - this.getZ();
        double h = pos.y - this.getEyeY();

        double i = Math.sqrt(d * d + e * e);
        float j = (float)(Mth.atan2(e, d) * (double)(180F / (float)Math.PI)) - 90.0F;
        float k = (float)(-(Mth.atan2(h, i) * (double)(180F / (float)Math.PI)));
        this.setXRot(rotlerp(this.getXRot(), k, g));
        this.setYRot(rotlerp(this.getYRot(), j, f));
    }

    private float rotlerp(float f, float g, float h) {
        float i = Mth.wrapDegrees(g - f);
        if (i > h) {
            i = h;
        }

        if (i < -h) {
            i = -h;
        }

        return f + i;
    }

    private void shootLaser() {
        if (atCd > 0) return;
        stopTriggeredAnim("controller", "cosmic_spider.shoot");

        if (laserPhase >= laserTimeStamps.length) {
            reset();
            return;
        }

        setDeltaMovement(Vec3.ZERO);
        hurtMarked = true;

        triggerAnim("controller", "cosmic_spider.shoot");
        atCd = laserTimeStamps[laserPhase];
        laserPhase++;

        ZregosProjectileLaser zregosProjectileLaser = new ZregosProjectileLaser(ModProjectiles.ZREGOS_PROJECTILE_LASER, level());
        zregosProjectileLaser.setPos(getPosition(1.0f));
        Vec3 targetPosition = target.position().subtract(target.getDeltaMovement());

        zregosProjectileLaser.setTargetPos(targetPosition);
        Vec3 shootPos = targetPosition.add(
                targetPosition.subtract(getPosition(1.0f)).normalize()
                        .scale(LASER_LEN_AFTER)
        );
        zregosProjectileLaser.setShootPos(shootPos);
        level().addFreshEntity(zregosProjectileLaser);
        playSound(SoundEvents.AMETHYST_BLOCK_HIT, 1000, .5f + random.nextFloat());
        playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1000, .5f + random.nextFloat());
        for (Player player : level().players())
            ((ServerLevel)level()).sendParticles((ServerPlayer) player,
                    ModParticles.ZREGOS_STAR_LASER,true, getX(), getY(), getZ(), 1,0,0,0,1);
        damagePlayers(shootPos);
    }

    private void damagePlayers(Vec3 shootPos) {
        Vec3 start = position();
        Vec3 end = shootPos;

        Vec3 dir =  end.subtract(start);
        double length = dir.length();

        dir = dir.normalize();

        double step = 0.5d;

        for (double d = 0; d <= length; d += step){
            Vec3 point = start.add(dir.scale(d));

            AABB box = SpaceHelpers.createEvenBox(point, 0.5d);
            List<Player> players = level().getEntitiesOfClass(Player.class, box);

            for (Player player : players)
                player.hurt(damageSources().thrown(this, player), 5f); // TODO damage this

        }
    }

    private void reset() {

        readyToFire = false;
        targetPos = null;
        target = null;
        laserPhase = 0;
        atCd = 0;
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

    public CosmicSpider(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<CosmicSpider> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("cosmic_spider.walk", RawAnimation.begin().thenLoop("cosmic_spider.walk"));
        controller.triggerableAnim("cosmic_spider.shoot", RawAnimation.begin().thenPlay("cosmic_spider.shoot"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (Vec3Helper.getSpeedOnVel(getDeltaMovement()) > 1) {
            tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("cosmic_spider.walk"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void knockback(double d, double e, double f) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
