package com.keko.stellaria.entity.projectiles.player.melee;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.VoidSword;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar.VoidStar;
import com.keko.stellaria.logic.StellariaConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class MeleeSlash extends AbstractArrow implements GeoAnimatable {
    int enemiesHit = 0;
    float rotation;
    float baseDamage = 10.0f;
    float damageMult = 1.0f;

    float clientTickerINeed = 0;

    private static EntityDataAccessor<Integer> slashType =
            SynchedEntityData.defineId(MeleeSlash.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(slashType, 0);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_BLOCK_CHIME;
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        compoundTag.putInt("slashType", getSlashType());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (compoundTag.contains("slashType")){
            setSlashType(compoundTag.getInt("slashType"));
        }
    }

    public void setSlashType(int slashType) {
        this.entityData.set(this.slashType, slashType);
    }

    public int getSlashType() {
        return this.entityData.get(slashType);
    }

    public float getClientTickerINeed() {
        return clientTickerINeed;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public MeleeSlash(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
        rotation = (level.random.nextFloat() - 0.5f) * 90;
        this.setSoundEvent(SoundEvents.AMETHYST_BLOCK_CHIME);
    }

    public void setClientTickerINeed(float clientTickerINeed) {
        this.clientTickerINeed = clientTickerINeed;
    }

    public void setDamageMult(float damageMult) {
        this.damageMult = damageMult;
    }

    public float getRotation() {
        return rotation;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        if (getOwner() != null)
            if (entity == getOwner())
                return false;

        return super.canCollideWith(entity);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity().isAttackable()){
            enemiesHit++;
            entityHitResult.getEntity().hurt(level().damageSources().cactus(), baseDamage * damageMult); //TODO DAMAGE
            entityHitResult.getEntity().invulnerableTime = 0;

        }
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            addDeltaMovement(getDeltaMovement().scale(-1.0d).scale(0.2f));
            if (enemiesHit >= 3 || tickCount > 20 || inGround)
                discard();
            if (getSlashType() == StellariaConstants.VOID_GLAIVE.ordinal()){
                if (tickCount % 4 == 0){

                    VoidStar voidStar = new VoidStar(ModProjectiles.VOID_STAR, level());
                    voidStar.setOwner(getOwner());
                    voidStar.setPos(position());
                    voidStar.setFriendly(true);
                    level().addFreshEntity(voidStar);
                }
            }
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.DIRT.getDefaultInstance();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }
}
