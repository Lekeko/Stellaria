package com.keko.stellaria.entity.projectiles.player.mage;

import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.helper.Trail;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;

public class FuryProjectile extends AbstractArrow implements GeoEntity {
    private float clientTick = 0;
    private float pierce = 2;
    private ArrayList<Entity> hitEntities = new ArrayList<>();
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final int STRIP_LENGTH = 20;
    Trail trail;

    public FuryProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
        trail = new Trail(STRIP_LENGTH, this);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (getOwner() != null) {

            if (!hitEntities.contains(entityHitResult.getEntity()) && entityHitResult.getEntity() != getOwner()) {
                entityHitResult.getEntity().hurt(level().damageSources().magic(), 5.0f);
                hitEntities.add(entityHitResult.getEntity());
                entityHitResult.getEntity().invulnerableTime = 0;
                pierce--;
            }
        }
    }

    public float getClientTick() {
        return clientTick;
    }

    public Trail getTrail() {
        return trail;
    }

    public void addClientTick(float clientTick) {
        this.clientTick += clientTick;
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);

        if (level().isClientSide){
            getTrail().update(1.0f);
        }else {
            if ( pierce <= 0 || tickCount > 20 * 6) {
                discard();
                return;
            }

            AABB entities = SpaceHelpers.createEvenBox(position(), 4);
            for (Entity entity : level().getEntitiesOfClass(LivingEntity.class, entities))
                this.onHitEntity(new EntityHitResult(entity));
        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<FuryProjectile> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("fury_projectile.spin", RawAnimation.begin().thenLoop("fury_projectile.spin"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
