package com.keko.stellaria.entity.projectiles.minions.einfarbig.voidLaser;

import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.entity.boss.minions.einfarbig.Messier;
import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.HoleVortex;
import com.keko.stellaria.helper.Vec3Helper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VoidLaser extends AbstractArrow {

    private Vec3 clientDirection = Vec3.ZERO;
    private Vec3 clientDirectionPrev = Vec3.ZERO;
    private float spinAngle;
    private float spinAnglePrev;
    private float clientTick = 0;
    float goSizeTick = 10;

    public float getGoSizeTick() {
        return goSizeTick;
    }

    public void setGoSizeTick(float goSizeTick) {
        this.goSizeTick = goSizeTick;
    }

    private static EntityDataAccessor<Boolean> WANNA_GO =
            SynchedEntityData.defineId(VoidLaser.class, EntityDataSerializers.BOOLEAN);
    private static EntityDataAccessor<Float> CLI_TICK_GO =
            SynchedEntityData.defineId(VoidLaser.class, EntityDataSerializers.FLOAT);

    public float getCliTickGo() {
        return this.entityData.get(CLI_TICK_GO);
    }
    public void setCliTickGo(float tick) {
        this.entityData.set(CLI_TICK_GO, tick);
    }

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

    public VoidLaser(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        super.tick();
        if (getOwner() == null) return;
        if (level().isClientSide)
            clientTick();
        else
            serverTick();
    }

    public float getClientTick() {
        return clientTick;
    }

    private void clientTick() {
        if (getOwner() instanceof Messier){
            if (tickCount == 1) {
                setClientDirection(Vec3Helper.getRandomVec3(level().random, 1).normalize());
                if (getClientDirection(1.0f).y > 0.0f)
                    setClientDirection(getClientDirection(1.0f).multiply(1.0f, -1.0f, 1.0f));
            }

            spinAnglePrev = spinAngle;
            spinAngle = (float) (spinAngle + Math.toRadians(clientTick / 50.0f));
        }else {
            if (getOwner() instanceof VoidDealer dealer) {
                Vec3 target = dealer.getViewVector(1.0f).normalize();

                clientDirectionPrev = clientDirection;

                if (clientDirection.equals(Vec3.ZERO)) {
                    clientDirection = target;
                    clientDirectionPrev = target;
                } else {
                    Vec3 next = clientDirection.lerp(target, 0.11);
                    clientDirection = next.normalize();
                }
            }
        }

    }

    private void serverTick() {
        if (getOwner()instanceof Einfarbig einfarbig && tickCount > 25)
            setWannaGo();

        if (tickCount > (8 - (getOwner() instanceof VoidDealer ? 6 : 0)) * 20)
            setWannaGo();
        if (getOwner() instanceof Messier || getOwner() instanceof VoidDealer){
            setPos(getOwner().position());
        }

        if (getWannaGo()) {
            setCliTickGo(getCliTickGo() - 1);

            if (getCliTickGo() <= 0)
                discard();
        }

    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void setClientDirection(Vec3 clientDirection) {
        this.clientDirection = clientDirection;
    }

    public Vec3 getClientDirection(float partialTick) {
        if (getOwner() instanceof Messier) {
            float angle = Mth.lerp(partialTick, spinAnglePrev, spinAngle);
            return clientDirection.yRot(angle);
        }

        if (getOwner() instanceof VoidDealer dealer)
            return dealer.getViewVector(partialTick).normalize();

        if (getOwner() instanceof Einfarbig)
            return new Vec3(0.0, 1.0, 0.0);

        return Vec3.ZERO;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    public void addClientTick(float partialTick) {
        clientTick += partialTick;
    }
}
