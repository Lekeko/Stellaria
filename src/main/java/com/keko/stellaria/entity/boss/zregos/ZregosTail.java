package com.keko.stellaria.entity.boss.zregos;

import com.keko.stellaria.helper.Vec3Helper;
import foundry.veil.api.client.util.Easing;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.UUID;

public class ZregosTail extends Monster implements GeoEntity {
    private static float baseHP = 1000.0f;
    private static float baseDMG = 8.0f;
    private static float baseArmor = 2.0f;

    private int index = 0;

    private static EntityDataAccessor<Optional<UUID>> bodyParent =
            SynchedEntityData.defineId(ZregosTail.class, EntityDataSerializers.OPTIONAL_UUID);
    private static EntityDataAccessor<Optional<UUID>> head =
            SynchedEntityData.defineId(ZregosTail.class, EntityDataSerializers.OPTIONAL_UUID);


    public ZregosTail(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAtributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, baseHP)
                .add(Attributes.MOVEMENT_SPEED, 4.0f)
                .add(Attributes.ATTACK_DAMAGE, baseDMG)
                .add(Attributes.ARMOR, baseArmor)
                .add(Attributes.ATTACK_KNOCKBACK, 4);

    }

    @Override
    public void checkDespawn() {

    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
        } else {
            if (getBodyParent() != null) {
                double parentDistance = Vec3Helper.ETEdistance(this, getBodyParent());
                if (parentDistance > 2.0d) {
                    followParent(parentDistance);
                }
            }
            if (getHead() != null)
                if (getHead().isDeadOrDying())
                    kill();

            if (getHead() == null)
                discard();
        }


        Vec3 vec3 = this.getDeltaMovement();

        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double horizontalDist = vec3.horizontalDistance();
            this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (180F / Math.PI)));
            this.setXRot((float) (Mth.atan2(vec3.y, horizontalDist) * (180F / Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        double d = vec3.x;
        double e = vec3.y;
        double f = vec3.z;
        double horizontalDist = vec3.horizontalDistance();

        float targetYaw = (float) (Mth.atan2(d, f) * (180F / Math.PI));
        float targetPitch = (float) (Mth.atan2(e, horizontalDist) * (180F / Math.PI));

        this.setXRot(Mth.rotLerp(0.2F, this.xRotO, targetPitch));
        this.setYRot(Mth.rotLerp(0.2F, this.yRotO, targetYaw));

        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

    }

    private void followParent(double parentDistance) {
        Vec3 direction = getBodyParent().position().subtract(position()).normalize();

        this.setDeltaMovement(direction.scale(parentDistance / 2));
        this.hurtMarked = true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(bodyParent, Optional.empty());
        builder.define(head, Optional.empty());

    }
    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        Monster head = getHead();
        if (head != null) {
            float dmgModif = 1 - ((float) getIndex() /Zregos.MAX_SEGMENTS);
            dmgModif = Math.max(0.05f, dmgModif);
            float easedDmgModif = Easing.EASE_IN_SINE.ease(dmgModif);

            head.hurt(damageSource, f * easedDmgModif);
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        this.entityData.get(bodyParent).ifPresent(uuid1 -> {
            compoundTag.putUUID("BodyParent", uuid1);
        });
        this.entityData.get(head).ifPresent(uuid1 -> {
            compoundTag.putUUID("head", uuid1);
        });
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

        if (compoundTag.hasUUID("BodyParent")) {
            UUID uuid = compoundTag.getUUID("BodyParent");
            this.entityData.set(bodyParent, Optional.of(uuid));
        }
        if (compoundTag.hasUUID("head")) {
            UUID uuid = compoundTag.getUUID("head");
            this.entityData.set(head, Optional.of(uuid));
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public Monster getBodyParent() {
        Optional<UUID> optional = this.entityData.get(bodyParent);
        if (optional.isEmpty()) return null;

        if (level() instanceof ServerLevel serverLevel) {
            return (Monster) serverLevel.getEntity(optional.get());
        }

        return null;
    }

    public void setBodyParent(UUID uuid) {
        this.entityData.set(bodyParent, Optional.ofNullable(uuid));
    }

    public Monster getHead() {
        Optional<UUID> optional = this.entityData.get(head);
        if (optional.isEmpty()) return null;

        if (level() instanceof ServerLevel serverLevel) {
            return (Monster) serverLevel.getEntity(optional.get());
        }

        return null;
    }

    public void setHead(UUID uuid) {
        this.entityData.set(head, Optional.ofNullable(uuid));
    }
}
