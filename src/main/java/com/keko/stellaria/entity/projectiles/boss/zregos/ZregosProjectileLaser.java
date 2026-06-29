package com.keko.stellaria.entity.projectiles.boss.zregos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ZregosProjectileLaser extends Entity{

    private Vec3 target;
    private Vec3 shootPos = null;
    private float clientTicker = 0;

    private static final EntityDataAccessor<Float> SHOOT_X =
            SynchedEntityData.defineId(ZregosProjectileLaser.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> SHOOT_Y =
            SynchedEntityData.defineId(ZregosProjectileLaser.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> SHOOT_Z =
            SynchedEntityData.defineId(ZregosProjectileLaser.class, EntityDataSerializers.FLOAT);

    private final int MAX_LIFE = 20;
    public static final float LASER_LEN_AFTER = 100;

    public ZregosProjectileLaser(EntityType<? extends ZregosProjectileLaser> entityType, Level level) {
        super(entityType, level);
    }

    public float getClientTicker() {
        return clientTicker;
    }

    public void setClientTicker(float clientTicker) {
        this.clientTicker = clientTicker;
    }

    public int getMAX_LIFE() {
        return MAX_LIFE;
    }


    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }


    public Vec3 getTarget() {
        return target;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){

            if (target == null) discard();
            if (tickCount > MAX_LIFE) discard();
        }
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SHOOT_X, 0f);
        builder.define(SHOOT_Y, 0f);
        builder.define(SHOOT_Z, 0f);
    }

    public Vec3 getShootPos() {
        return new Vec3(
                entityData.get(SHOOT_X),
                entityData.get(SHOOT_Y),
                entityData.get(SHOOT_Z)
        );
    }

    public void setShootPos(Vec3 pos) {
        entityData.set(SHOOT_X, (float) pos.x);
        entityData.set(SHOOT_Y, (float) pos.y);
        entityData.set(SHOOT_Z, (float) pos.z);
    }

    public void setTargetPos(Vec3 target) {
        this.target = target;
    }
}
