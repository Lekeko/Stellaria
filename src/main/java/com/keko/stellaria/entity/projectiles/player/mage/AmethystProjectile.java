package com.keko.stellaria.entity.projectiles.player.mage;

import com.keko.stellaria.helper.SpaceHelpers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class AmethystProjectile extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private float clientTick = 0;

    private LivingEntity target = null;


    public AmethystProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
        this.setSoundEvent(SoundEvents.AMETHYST_BLOCK_CHIME);
        this.setBaseDamage(0.5d);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_BLOCK_CHIME;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (tickCount > 6 * 20 && target != null) discard();

            if (target == null){
                searchTarget();
            }else {
                trackTarget();
            }


            if (tickCount > 4 * 20 || inGround) discard();
        }
    }

    private void trackTarget() {
        if (target.isDeadOrDying()) {
            target = null;
            return;
        }

        Vec3 dir =  target.getEyePosition().subtract(position()).normalize();

        Vec3 curVel = getDeltaMovement().normalize();

        float turn = 0.96f;
        float speed = 4.5f;

        Vec3 newDir = curVel.scale(1.0 - turn).add(dir.scale(turn)).normalize();

        setDeltaMovement(newDir.scale(speed));
        this.hurtMarked = true;
    }

    private void searchTarget() {
        float distanceM = Float.MAX_VALUE;
        for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, SpaceHelpers.createEvenBox(position(), 40))){
            if (!(livingEntity instanceof Player)){
                float dis = this.distanceTo(livingEntity);
                if (dis < distanceM){
                    target = livingEntity;
                    distanceM = dis;
                }

            }
        }
    }

    public void addClientTIck(float clientTick) {
        this.clientTick += clientTick;
    }

    public float getClientTick() {
        return clientTick;
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
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
