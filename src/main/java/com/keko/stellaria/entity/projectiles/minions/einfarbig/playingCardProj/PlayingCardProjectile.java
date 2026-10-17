package com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj;

import com.keko.stellaria.helper.GameplayUtilHelper;
import com.keko.stellaria.helper.Trail;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class PlayingCardProjectile extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    Trail trail;
    private final int STRIP_LENGTH = 20;


    public PlayingCardProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
        setSoundEvent(ModSounds.PAPER_HIT);
        trail = new Trail(STRIP_LENGTH, this);

    }

    public Trail getTrail() {
        return trail;
    }


    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (inGround){
                GameplayUtilHelper.createCustomExplosion(level(), getOwner(), position(), 20.2f, 10.0f, ParticleTypes.EXPLOSION, 20);
                discard();
            }

            if (tickCount > 4 * 20) discard();
        }else {
            getTrail().update(1.0f);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!entityHitResult.getEntity().equals(getOwner()))
            super.onHitEntity(entityHitResult);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
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
}
