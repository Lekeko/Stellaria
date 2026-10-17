package com.keko.stellaria.entity.projectiles.boss.einfarbig;

import com.keko.stellaria.helper.GameplayUtilHelper;
import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.helper.Trail;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class TntBunny extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private LivingEntity target = null;
    private int trackingTime = 30;
    Trail trail;
    private final int STRIP_LENGTH = 20;
    private boolean friendly = false;
    private float damage = 20.0f;

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public TntBunny(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.DISALLOWED;
        setSoundEvent(SoundEvents.GENERIC_EXPLODE.value());
        trail = new Trail(STRIP_LENGTH, this);

    }

    public void setTrackingTime(int trackingTime) {
        this.trackingTime = trackingTime;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide)
            clientTicker();
        else
            serverTicker();
    }

    private void serverTicker() {


        if (friendly){
            if (inGround){
                discard();
                launchOwner();
                GameplayUtilHelper.createCustomExplosion(level(), getOwner(), position(), damage, 10.0f, ParticleTypes.EXPLOSION, 20);
                return;
            }
            if (target == null) {
                for (LivingEntity entity : level().getEntitiesOfClass(Monster.class, SpaceHelpers.createEvenBox(getPosition(1.0f), 40))) {
                    target = entity;
                }
            }else {
                trackTarget();
            }
        }else {
            if (target != null){
                if(--trackingTime > 0)
                    trackTarget();
            }
        }

        if (tickCount > 80) {
            GameplayUtilHelper.createCustomExplosion(level(), getOwner(), position(), damage, 10.0f, ParticleTypes.EXPLOSION, 20);
            discard();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    private void trackTarget() {
        Vec3 targetPos = target.position();
        float speed = 6.0f;
        Vec3 dir = targetPos.subtract(position()).normalize().scale(speed);

        float speedScale = 0.14f;
        Vec3 vel = getDeltaMovement().lerp(dir, speedScale);
        setDeltaMovement(vel);
        hurtMarked = true;
    }

    private void clientTicker() {
        getTrail().update(1.0f);
    }

    public Trail getTrail() {
        return trail;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!level().isClientSide)
            GameplayUtilHelper.createCustomExplosion(level(), getOwner(), position(), damage, 10.0f, ParticleTypes.EXPLOSION, 20);

        if (friendly)
            launchOwner();

        discard();
    }

    private void launchOwner() {
        if  (distanceTo(getOwner()) < 10.0f){
            Vec3 pos = position();
            Vec3 ownerPos = getOwner().position();
            Vec3 dir = ownerPos.subtract(pos);
            Vec3 dirNorm = dir.normalize();
            float speed = (float) dir.length();
            if (speed == 0)
                speed = 0.01f;
            float amp = 4.0f;

            getOwner().addDeltaMovement(dirNorm.scale(1 / speed * amp));
            getOwner().hurtMarked = true;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() != getOwner()) {
            discard();
            GameplayUtilHelper.createCustomExplosion(level(), getOwner(), position(), damage, 10.0f, ParticleTypes.EXPLOSION, 20);
        }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }
}
