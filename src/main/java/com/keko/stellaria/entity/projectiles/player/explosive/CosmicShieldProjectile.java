package com.keko.stellaria.entity.projectiles.player.explosive;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.player.melee.MeleeSlash;
import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class CosmicShieldProjectile extends AbstractArrow implements GeoEntity {
    private static EntityDataAccessor<Integer> power =
            SynchedEntityData.defineId(CosmicShieldProjectile.class, EntityDataSerializers.INT);

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private float clientTick = 0;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(power, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("power", getPower());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("power")){
            setPower(compoundTag.getInt("power"));
        }
    }

    public void setPower(int power) {
        this.entityData.set(this.power, power);
    }

    public int getPower() {
        return this.entityData.get(power);
    }

    public CosmicShieldProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;

    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        explodeLikeTheSassyBihYouAre();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        explodeLikeTheSassyBihYouAre();
    }

    private void explodeLikeTheSassyBihYouAre() {
        if(level().isClientSide) return;

        AABB aabb = SpaceHelpers.createEvenBox(position(), 5 + getPower() * 2);
        int MAX_ENTITIES = 10;
        int i = 0;

        for (Entity entity : level().getEntitiesOfClass(Entity.class, aabb)){
            if (i >= MAX_ENTITIES) break;
            i++;
            if (entity != getOwner() && !entity.is(this))
                entity.hurt(level().damageSources().magic(), 20 * getPower());
        }

        level().playSound(null, getOnPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 10, 0.5f + level().random.nextFloat());

        discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide){
            if (tickCount > 4 * 20) discard();
        }
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

    public void addClientTick(float partialTick) {
        clientTick += partialTick;
    }

    public float getClientTick() {
        return clientTick;
    }
}
