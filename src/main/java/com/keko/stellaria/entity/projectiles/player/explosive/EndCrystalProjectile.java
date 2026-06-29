package com.keko.stellaria.entity.projectiles.player.explosive;

import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.particles.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class EndCrystalProjectile extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);



    public EndCrystalProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
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
    public boolean canCollideWith(Entity entity) {
        if (getOwner() != null)
            return entity != getOwner();
        else
            return super.canCollideWith(entity);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        explode();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        explode();
    }

    private void explode(){
        if (!level().isClientSide){
            AABB aabb = SpaceHelpers.createEvenBox(getPosition(1.0f), 15);
            int i = 12;
            for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, aabb)) {
                if (i <= 0) break;
                if (getOwner() != null) {
                    if (livingEntity != getOwner())
                        livingEntity.hurt(level().damageSources().explosion(getOwner(), livingEntity), 20.0f);
                } else {
                    livingEntity.hurt(level().damageSources().explosion(this, livingEntity), 20.0f);
                }
                i--;
            }

            for (Player player : level().players())
                ((ServerLevel) level()).sendParticles(
                        (ServerPlayer) player,
                        ModParticles.ZREGOS_STAR_BURST_NORMAL,
                        true,
                        getPosition(1.0f).x, getPosition(1.0f).y + 2.0f, getPosition(1.0f).z,
                        100,
                        0,0,0,
                        0
                );
            discard();
        }
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (tickCount == 1)
                triggerAnim("controller", "ecp.spin");

            if (inGround) explode();
            if (tickCount > 5 * 20) discard();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<EndCrystalProjectile> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("ecp.spin", RawAnimation.begin().thenLoop("ecp.spin"));

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
