package com.keko.stellaria.entity.projectiles.minions.zregos;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class ZregosServantProjectile extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private float clientTick = 0;
    private Player target = null;
    private boolean shouldFollow = true;

    public ZregosServantProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public float getClientTick() {
        return clientTick;
    }

    public void addClientTick(float clientTick) {
        this.clientTick += clientTick;
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
    public void tick() {
        super.tick();

        if (!level().isClientSide){
            if (target == null || tickCount > 4 * 20) discard();
            else {
                if (distanceTo(target) > 6 && shouldFollow)
                    followPlayer();
                if (tickCount >  28) shouldFollow = false;
            }
        }
    }

    private void followPlayer() {
        Vec3 dir =  target.getEyePosition().subtract(position()).normalize();

        Vec3 curVel = getDeltaMovement().normalize();

        float turn = 0.16f + tickCount / 100f;
        float speed = 6.5f;

        Vec3 newDir = curVel.scale(1.0 - turn).add(dir.scale(turn)).normalize();

        setDeltaMovement(newDir.scale(speed));
        this.hurtMarked = true;
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
