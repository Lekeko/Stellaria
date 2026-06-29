package com.keko.stellaria.entity.projectiles.player.ranged;

import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.particles.ModParticles;
import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CosmicArrow extends AbstractArrow implements GeoEntity {
    private float clientTick = 0;
    private float pierce = 10;
    private ArrayList<Entity> hitEntities = new ArrayList<>();
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public CosmicArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;

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
                entityHitResult.getEntity().hurt(level().damageSources().magic(), 25.0f);
                hitEntities.add(entityHitResult.getEntity());
                pierce--;
            }
        }
    }

    public float getClientTick() {
        return clientTick;
    }

    public void addClientTick(float clientTick) {
        this.clientTick += clientTick;
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);

        if (level().isClientSide){

        }else {
            if ( pierce <= 0 || tickCount > 20 * 6) {
                discard();
                return;
            }

            AABB entities = SpaceHelpers.createEvenBox(position(), 4);
            for (Entity entity : level().getEntitiesOfClass(LivingEntity.class, entities)){
                this.onHitEntity(new EntityHitResult(entity));
            }

            if (level().dimension() == ModDimensions.ARTHA_LEVEL_KEY)
                for (Player player : level().players()) {
                    float x = (level().random.nextFloat() - 0.5f) * 2;
                    float y = (level().random.nextFloat() - 0.5f) * 2;
                    float z = (level().random.nextFloat() - 0.5f) * 2;

                    ((ServerLevel) level()).sendParticles((ServerPlayer) player,
                            ParticleTypes.END_ROD, true, getX() + x, getY() + y, getZ() + z, 1, 0, 0, 0, 1);
                }
        }
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
