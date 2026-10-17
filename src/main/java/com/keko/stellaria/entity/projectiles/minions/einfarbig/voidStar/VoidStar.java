package com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar;

import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.helper.Trail;
import com.keko.stellaria.particles.ModParticles;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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

public class VoidStar extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private LivingEntity target = null;
    private boolean friendly = false;
    private int trackingTime = 20;
    Trail trail;
    private final int STRIP_LENGTH = 20;

    public VoidStar(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.DISALLOWED;
        setSoundEvent(ModSounds.MESSIER_STAR_IMPACT);
        trail = new Trail(STRIP_LENGTH, this);

    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
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
        if (target != null && !friendly){
            if(--trackingTime > 0)
                trackTarget();
        }

        if (friendly){
            if (target == null) {
                for (LivingEntity entity : level().getEntitiesOfClass(Monster.class, SpaceHelpers.createEvenBox(getPosition(1.0f), 40))) {
                    target = entity;
                }
            }else {
                trackTarget();
            }
        }

        if (tickCount > 80 ) discard();
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
            for (Player player : level().players()){
                ((ServerLevel) level()).sendParticles(
                        (ServerPlayer) player,
                        ParticleTypes.END_ROD,
                        true,
                        getPosition(1.0f).x, getPosition(1.0f).y, getPosition(1.0f).z,
                        200,
                        0,0,0,
                        1
                );
            }

        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() != getOwner()){
            super.onHitEntity(entityHitResult);
            if (!level().isClientSide){
                for (Player player : level().players()) {
                    ((ServerLevel) level()).sendParticles(
                            (ServerPlayer) player,
                            ParticleTypes.END_ROD,
                            true,
                            getPosition(1.0f).x, getPosition(1.0f).y, getPosition(1.0f).z,
                            200,
                            0, 0, 0,
                            1
                    );
                }
            }
            discard();
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


}
