package com.keko.stellaria.entity.projectiles.boss.zregos;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.particles.ModParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

import static com.keko.stellaria.entity.projectiles.boss.zregos.ZregosProjectileLaser.LASER_LEN_AFTER;

public class ZregosProjectile extends AbstractArrow implements GeoAnimatable {
    private int delay = 0;
    private int remTime = 4;
    private boolean markForRem = false; //that's gonna leave a https://imgur.com/gallery/reaction-looking-imgur-today-RfoAkMi#/t/markiplier
    private Vec3 targetPos = null;
    private UUID targetUUID = null;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Integer> TARGET_ID =
            SynchedEntityData.defineId(ZregosProjectile.class, EntityDataSerializers.INT);

    public ZregosProjectile(EntityType<? extends ZregosProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }


    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }




    @Override
    public boolean canCollideWith(Entity entity) {
        return entity instanceof Player;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!(entityHitResult.getEntity() instanceof Player)) return; //i dont trust that canCollideWith for the shit of my life

        super.onHitEntity(entityHitResult);
        entityHitResult.getEntity().invulnerableTime = 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()){
            if (markForRem) {
                remTime--;
                if (remTime <=0)
                    discard();
            }
            int shootTime = 40;

            if (getTarget() != null){
                if (tickCount < shootTime + getDelay()) {
                    targetPos = getTarget().getPosition(1.0f);
                    addDeltaMovement(new Vec3(0f,0.05f,0f));
                    hurtMarked = true;
                }
                if (tickCount >= shootTime + getDelay() + 1 ) {
                    shootLaser();
                    discard();
                }



            }


            if (tickCount > 100)
                discard();
        }
    }

    private void shootLaser() {
        ZregosProjectileLaser zregosProjectileLaser = new ZregosProjectileLaser(ModProjectiles.ZREGOS_PROJECTILE_LASER, level());
        zregosProjectileLaser.setPos(getPosition(1.0f));
        zregosProjectileLaser.setTargetPos(targetPos);
        Vec3 shootPos = targetPos.add(
                targetPos.subtract(getPosition(1.0f)).normalize()
                        .scale(LASER_LEN_AFTER)
        );
        zregosProjectileLaser.setShootPos(shootPos);
        level().addFreshEntity(zregosProjectileLaser);
        playSound(SoundEvents.AMETHYST_BLOCK_HIT, 1000, .5f + random.nextFloat());
        playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1000, .5f + random.nextFloat());
        for (Player player : level().players())
            ((ServerLevel)level()).sendParticles((ServerPlayer) player,
                ModParticles.ZREGOS_STAR_LASER,true, getX(), getY(), getZ(), 1,0,0,0,1);
        damagePlayers(shootPos);
    }

    private void damagePlayers(Vec3 shootPos) {
        Vec3 start = position();
        Vec3 end = shootPos;

        Vec3 dir =  end.subtract(start);
        double length = dir.length();

        dir = dir.normalize();

        double step = 0.5d;

        for (double d = 0; d <= length; d += step){
            Vec3 point = start.add(dir.scale(d));

            AABB box = SpaceHelpers.createEvenBox(point, 0.5d);
            List<Player> players = level().getEntitiesOfClass(Player.class, box);

            for (Player player : players) {
                player.hurt(damageSources().thrown(getOwner(), player), 12.0f);
                player.invulnerableTime = 0;
            }

        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }



    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET_ID, -1);
    }

    public void setTarget(Entity entity) {
        if (entity == null) {
            this.targetUUID = null;
            this.entityData.set(TARGET_ID, -1);
        } else {
            this.targetUUID = entity.getUUID();
            this.entityData.set(TARGET_ID, entity.getId());
        }
    }

    public Entity getTarget() {
        int id = this.entityData.get(TARGET_ID);
        if (id != -1) {
            Entity entity = level().getEntity(id);
            if (entity != null) return entity;
        }

        if (targetUUID != null && level() instanceof ServerLevel serverLevel) {
            Entity e = serverLevel.getEntity(targetUUID);
            if (e != null) {
                this.entityData.set(TARGET_ID, e.getId());
                return e;
            }
        }
        return null;
    }



    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("target")) {
            this.targetUUID = compoundTag.getUUID("target");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        if (targetUUID != null) {
            compoundTag.putUUID("target", targetUUID);
        }
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
