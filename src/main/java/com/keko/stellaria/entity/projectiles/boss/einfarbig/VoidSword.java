package com.keko.stellaria.entity.projectiles.boss.einfarbig;

import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class VoidSword extends AbstractArrow implements GeoEntity {
    private float launchTime = 2;
    private float speed = 10.0f;
    private float clientTicker = 0;
    public float getClientTicker() {
        return clientTicker;
    }

    public void setClientTicker(float clientTicker) {
        this.clientTicker = clientTicker;
    }

    private static final EntityDataAccessor<Vector3f> DIRECTION =
            SynchedEntityData.defineId(VoidSword.class, EntityDataSerializers.VECTOR3);


    public VoidSword(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() != getOwner())
            super.onHitEntity(entityHitResult);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (--launchTime == 0) {
                setDeltaMovement(getDirectionMine().scale(speed));
                hurtMarked = true;
            }else {
                if (launchTime > 0 && getOwner() instanceof Einfarbig eini && eini.getTarget() != null)
                    setDirection(eini.getTarget().position().add(eini.getTarget().getDeltaMovement().scale(9.0f)).subtract(position()).normalize());
            }

            if (tickCount > 2 * 20) discard();
        }
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
        AnimationController<VoidSword> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("void_sword.spin", RawAnimation.begin().thenLoop("void_sword.spin"));

        controllers.add(controller);
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (inGround) {
            tAnimationState.getController().stop();
            return PlayState.STOP;
        }
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("void_sword.spin"));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    public void setDirection(Vec3 dir) {
        this.entityData.set(DIRECTION, new Vector3f((float) dir.x, (float) dir.y, (float) dir.z));
    }

    public Vec3 getDirectionMine() {
        Vector3f v = this.entityData.get(DIRECTION);
        return new Vec3(v.x(), v.y(), v.z());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DIRECTION, new Vector3f(0.0f,0.0f,0.0f));
    }
}
