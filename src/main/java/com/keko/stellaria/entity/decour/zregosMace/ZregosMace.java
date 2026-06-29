package com.keko.stellaria.entity.decour.zregosMace;

import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderNormal;
import com.keko.stellaria.entity.projectiles.boss.zregos.MaceProjectile;
import com.keko.stellaria.helper.SpaceHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ZregosMace extends AbstractArrow implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final Set<ZregosMace> connections = new HashSet<>();

    private int searchCooldown = 0;

    private float clientTick = 0;

    public ZregosMace(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setInvulnerable(true);
        this.invulnerableTime = Integer.MAX_VALUE;
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
    protected void onHitEntity(EntityHitResult entityHitResult) {

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {

    }

    @Override
    public boolean isNoPhysics() {
        return true;
    }

    @Override
    protected void onHit(HitResult hitResult) {

    }


    public Set<ZregosMace> getConnections() {
        return connections;
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



        if (level().isClientSide){
            tickConnections();
        }else {
            if (tickCount == 1) triggerAnim("controller", "mace.loop");
            if (tickCount > 30 * 20) discard();
            if (tickCount % 120 == 0) shootZAFUCKER();
        }
    }

    private void shootZAFUCKER() {

        int maceNr = 3 + level().random.nextIntBetweenInclusive(1,3);
        for (int i = 0 ; i < maceNr; i++){
            MaceProjectile maceProjectile = new MaceProjectile(ModProjectiles.MACE_PROJECTILE, level());
            maceProjectile.setPos(position());
            float speed = 4;
            Vec3 vel = new Vec3(
                    level().random.nextFloat() - .5f,
                    level().random.nextFloat() - .5f,
                    level().random.nextFloat() - .5f
            ).scale(speed);

            maceProjectile.setDeltaMovement(vel);
            maceProjectile.setOwner(this);

            level().addFreshEntity(maceProjectile);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    private void tickConnections() {
        if (searchCooldown-- <= 0) {
            searchCooldown = 20;
            searchConnections();
        }

        connections.removeIf(Entity::isRemoved);
    }

    private void searchConnections() {

        AABB aabb = SpaceHelpers.createEvenBox(position(), 50);
        for (ZregosMace zregosMace : level().getEntitiesOfClass(ZregosMace.class, aabb)){
            if (zregosMace == this) continue;

            if (!connections.contains(zregosMace)) {
                connections.add(zregosMace);
                zregosMace.getConnections().add(this);
            }
        }

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ZregosMace> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("mace.loop", RawAnimation.begin().thenLoop("mace.loop"));

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
