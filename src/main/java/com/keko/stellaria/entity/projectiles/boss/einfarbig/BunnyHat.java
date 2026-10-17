package com.keko.stellaria.entity.projectiles.boss.einfarbig;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.helper.Vec3Helper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class BunnyHat extends AbstractArrow implements GeoEntity {
    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final int UNTIL_SPAWN = 25;
    private final int SPAWN_RATE = 5;
    private int spawns = 8;
    private int atTickCount = 0;

    private static final EntityDataAccessor<Integer> PHASE =
            SynchedEntityData.defineId(BunnyHat.class, EntityDataSerializers.INT);

    public BunnyHat(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.DISALLOWED;
    }

    public void setSpawns(int spawns) {
        this.spawns = spawns;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PHASE, 1);
    }

    public void setPhase(int phase) {
        this.entityData.set(PHASE, phase);
    }

    public int getPhase() {
        return this.entityData.get(PHASE);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (getOwner() == null) {
                discard();
                return;
            }

            if (tickCount > UNTIL_SPAWN){
                if (getOwner() instanceof Einfarbig eini){
                    if (getPhase() == 1){
                        if (tickCount - atTickCount > SPAWN_RATE && spawns > 0) {
                            spawns--;
                            spawnBunny();
                            atTickCount = tickCount;
                        }
                    }else {
                        if (tickCount  > SPAWN_RATE ) {
                            spawns--;
                            eini.spawnSword(position().add(0.0f, 20.0f, 0.0f));
                        }
                    }
                }
                if (spawns <= 0) discard();
            }
        }
    }



    @Override
    public boolean isNoGravity() {
        return true;
    }

    private void spawnBunny() {
        if (getOwner() instanceof Einfarbig eini){
            for (int i = 0; i < 10; i++){
                TntBunny tntBunny = new TntBunny(ModProjectiles.TNT_BUNNY, level());
                tntBunny.setPos(position().add(0.0f, 8.0f, 0.0f));
                tntBunny.setDeltaMovement(Vec3Helper.getRandomVec3(random, 10.2f).add(0.0f, 9.0f, 0.0f));
                tntBunny.setOwner(getOwner());
                tntBunny.setTarget(eini.getTarget());
                level().addFreshEntity(tntBunny);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<BunnyHat> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("bunny_hat.spawn", RawAnimation.begin().thenPlay("bunny_hat.spawn"));

        controllers.add(controller);
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenPlayAndHold("bunny_hat.spawn"));
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
