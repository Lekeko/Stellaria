package com.keko.stellaria.entity.projectiles.boss.einfarbig;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar.MessierStar;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar.VoidStar;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.particles.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class HoleVortex extends AbstractArrow implements GeoEntity {
    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final int UNTIL_SPAWN = 6;
    private final int SPAWN_RATE = 1;
    private final int SPAWN_NR = 4;
    private int spawns = 1;
    private int atTickCount = 0;
    private float clientTicker = 0;
    private static EntityDataAccessor<Boolean> WANNA_GO =
            SynchedEntityData.defineId(HoleVortex.class, EntityDataSerializers.BOOLEAN);
    private static EntityDataAccessor<Float> CLI_TICK_GO =
            SynchedEntityData.defineId(HoleVortex.class, EntityDataSerializers.FLOAT);

    public boolean getWannaGo() {
        return this.entityData.get(WANNA_GO);
    }

    public void setWannaGo() {
        this.entityData.set(WANNA_GO, true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WANNA_GO, false);
        builder.define(CLI_TICK_GO, 10.0f);
    }
    public float getCliTickGo() {
        return this.entityData.get(CLI_TICK_GO);
    }
    public void setCliTickGo(float tick) {
        this.entityData.set(CLI_TICK_GO, tick);
    }
    public float getClientTicker() {
        return clientTicker;
    }

    public void setClientTicker(float clientTicker) {
        this.clientTicker = clientTicker;
    }
    public HoleVortex(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.DISALLOWED;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (tickCount == 1){
                for (Player player : level().players()){
                    ((ServerLevel) level()).sendParticles(
                            (ServerPlayer) player,
                            ModParticles.WHITE_BOOM_BIG,
                            true,
                            getPosition(1.0f).x, getPosition(1.0f).y, getPosition(1.0f).z,
                            1,
                            0,0,0,
                            0
                    );
                }
            }
            if (getWannaGo()) {
                setCliTickGo(getCliTickGo() - 1);
            }
            if (getOwner() == null) {
                discard();
                return;
            }
            if (tickCount > UNTIL_SPAWN){
                if (tickCount - atTickCount > SPAWN_RATE && spawns > 0){
                    spawns--;
                    spawnStar();
                    atTickCount = tickCount;
                }
                if (spawns <= 0) {
                    destroy();
                }
            }

            if (getCliTickGo() <= 0) discard();
        }
    }
    private void destroy() {
        setWannaGo();
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }

    private void spawnStar() {
        if (getOwner() instanceof Einfarbig eini){
            for (int i = 0; i < 2; i++){
                VoidStar voidStar = new VoidStar(ModProjectiles.VOID_STAR, level());
                Vec3 targetPos = eini.getTarget().position();
                Vec3 dir = targetPos.subtract(position()).normalize();
                Vec3 offset = Vec3Helper.getRandomVec3(random, 3.0f);
                float speed = 10.0f;
                Vec3 vel = dir.scale(speed).add(offset);

                voidStar.setPos(position());
                voidStar.setDeltaMovement(vel);
                voidStar.setOwner(getOwner());
                voidStar.setTrackingTime(40);
                voidStar.setTarget(eini.getTarget());

                level().addFreshEntity(voidStar);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
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
