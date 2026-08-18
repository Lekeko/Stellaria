package com.keko.stellaria.entity.boss.minions.zregos;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.minions.zregos.ZregosServantProjectile;
import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.packet.ZServPartS2C;
import com.keko.stellaria.particles.ModParticles;
import com.keko.stellaria.particles.QuasarParticlesHandler;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.data.ParticleSettings;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ZregosServant extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private Player target = null;

    private static final float BASE_HP = 85.0f;
    private static final float BASE_DMG = 8.0f;
    private static final float BASE_AR = 2.0f;

    private boolean wantsToAttack = false;
    private int attack = 0;
    private int atCd = 0;
    private int timeTillDamage = 0;
    private int missiles = 0;

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

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
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

            if (target == null){
                findTarget();
            }else {
                if (target != null || target.isDeadOrDying()) {
                    lookAt(target, 360.0F, 360.0F);
                }


                if (timeTillDamage > 0)
                    timeTillDamage--;

                if (missiles > 0 && timeTillDamage <= 0){
                    launchActualMissile();
                }


                if (attack == 2 )
                    if (timeTillDamage <= 0)
                        punchAround();
                    else{
                        Vec3 tpPos = target.position();
                        teleportTo(tpPos.x, tpPos.y, tpPos.z);
                    }


                if (atCd > 0){
                    atCd--;
                    return;
                }

                if (wantsToAttack){
                    switch (attack){
                        case 1 -> launchMissiles();
                        case 2 -> punchPlayer();
                    }
                }else {
                    chooseAttack();
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

    private void punchAround() {
        AABB aabb = SpaceHelpers.createEvenBox(position(), 7);
        for (Player player : level().getEntitiesOfClass(Player.class, aabb)){
            player.hurt(this.damageSources().mobAttack(this), 18.5f);
        }

        for (Player player : level().players())
            ((ServerLevel) level()).sendParticles(
                    (ServerPlayer) player,
                    ModParticles.ZREGOS_STAR_BURST_NORMAL,
                    true,
                    getPosition(1.0f).x, getPosition(1.0f).y + 2.0f, getPosition(1.0f).z,
                    50,
                    0,0,0,
                    0
            );
        attack = 0;

    }

    private void launchActualMissile() {
        missiles--;

        ZregosServantProjectile zregosServantProjectile = new ZregosServantProjectile(ModProjectiles.ZREGOS_SERVANT_PROJECTILE, level());
        zregosServantProjectile.setPos(position().add(0.0f, -1.0f, 0.0f));

        float speed = 9.3f;
        float dispersion = 10.0f;

        Vec3 vel = target.position().subtract(position()).normalize().scale(speed).add(
                (random.nextFloat()-.5f) * dispersion,
                -35.0f,
                (random.nextFloat()-.5f) * dispersion);

        zregosServantProjectile.setDeltaMovement(vel);
        zregosServantProjectile.setTarget(target);

        level().addFreshEntity(zregosServantProjectile);

        for (Player player : level().players())
            ServerPlayNetworking.send((ServerPlayer) player, new ZServPartS2C(getId()));

    }

    public static void spawnParticle(Entity entity, ResourceLocation id){
        try {
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setAttachedEntity(entity);
            float disp = 0.3f;
            float rx = (entity.getRandom().nextFloat() - .5f) * disp;
            float ry = (entity.getRandom().nextFloat() - .5f) * disp -  2.0f;
            float rz = (entity.getRandom().nextFloat() - .5f) * disp;
            emitter.setCount(1);
            emitter.setRate(100);
            Vector3fc initialDirection = entity.getViewVector(1.0f).add(rx, ry, rz).toVector3f();
            emitter.setParticleSettings(new ParticleSettings(1.0f, 1.2f, 2, 20, 1.0f,
                    initialDirection, false, false, true, true, true));
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }

    private void punchPlayer() {
        Vec3 tpPos = target.position().add(target.getViewVector(1.0f).scale(2));
        teleportTo(tpPos.x, tpPos.y, tpPos.z);
        triggerAnim("controller", "zregos_servant.punch");
        atCd = (int) (20 * 2.5);
        wantsToAttack = false;
    }

    private void launchMissiles() {
        Vec3 tpPos = target.position().add(
                random.nextIntBetweenInclusive(-200, 200),
                0,
                random.nextIntBetweenInclusive(-200, 200)
        );
        teleportTo(tpPos.x, tpPos.y, tpPos.z);
        triggerAnim("controller", "zregos_servant.fast_punching");
        lookAt(target, 360.0F, 360.0F);
        this.yBodyRot = getYRot();
        this.yHeadRot = getYRot();
        this.yBodyRotO = getYRot();
        this.yHeadRotO = getYRot();
        missiles = 15;
        atCd = 20 * 5;
        attack = 0;
        wantsToAttack = false;
    }

    private void chooseAttack() {
        attack = random.nextFloat() >= 0.5f ? 1 : 2;
        wantsToAttack = true;

        switch (attack){
            case 1 -> timeTillDamage = 35;
            case 2 -> timeTillDamage = 17;
        }
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

    public ZregosServant(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ZregosServant> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("zregos_servant.float", RawAnimation.begin().thenLoop("zregos_servant.float"));
        controller.triggerableAnim("zregos_servant.fast_punching", RawAnimation.begin().thenPlay("zregos_servant.fast_punching"));
        controller.triggerableAnim("zregos_servant.punch", RawAnimation.begin().thenPlay("zregos_servant.punch"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
