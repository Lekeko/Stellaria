package com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.boss.minions.einfarbig.Messier;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar.VoidStar;
import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.helper.Vec3Helper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.UUID;

public class MessierStar extends AbstractArrow implements GeoEntity {
    private int order = -1;
    private float clientTicker = 0;
    private static EntityDataAccessor<Integer> STAR_PARENT =
            SynchedEntityData.defineId(MessierStar.class, EntityDataSerializers.INT);
    private static EntityDataAccessor<Float> CLI_TICK_GO =
            SynchedEntityData.defineId(MessierStar.class, EntityDataSerializers.FLOAT);
    private static EntityDataAccessor<Boolean> WANNA_GO =
            SynchedEntityData.defineId(MessierStar.class, EntityDataSerializers.BOOLEAN);

    private LivingEntity target;

    public MessierStar(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.DISALLOWED;
        if (level.isClientSide)
            order = level.getRandom().nextIntBetweenInclusive(1,4);
    }

    public float getClientTicker() {
        return clientTicker;
    }

    public void setClientTicker(float clientTicker) {
        this.clientTicker = clientTicker;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STAR_PARENT, -1);
        builder.define(WANNA_GO, false);
        builder.define(CLI_TICK_GO, 10.0f);

    }

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

    public AbstractArrow getParentStar() {
        int id = this.entityData.get(STAR_PARENT);
        if (id == -1) return null;

        Entity entity = level().getEntity(id);

        return entity instanceof AbstractArrow arrow ? arrow : null;
    }

    public void setParentStar(AbstractArrow parent) {
        this.entityData.set(STAR_PARENT, parent == null ? -1 : parent.getId());
    }




    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isNoPhysics() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide)
            clientTicker();
        else
            serverTicker();
    }

    private void serverTicker() {
        if (getWannaGo()) {
            setCliTickGo(getCliTickGo() - 1);
        }

        if (order == -1 || getCliTickGo() <= 0) discard();

        if (tickCount == 60 - 6 * order){
            destroy();
            summonSeekingStar();
        }

        if (tickCount == 6 && order > 0)
            createNewStar();
    }

    private void summonSeekingStar() {
        if (target == null) return;
        VoidStar voidStar = new VoidStar(ModProjectiles.VOID_STAR, level());
        voidStar.setPos(position());
        float initSpeed = 2.0f;
        voidStar.setTarget(getTarget());
        voidStar.setDeltaMovement(getTarget().position().subtract(position()).normalize().scale(initSpeed));
        level().addFreshEntity(voidStar);
    }

    private void destroy() {
        if (getWannaGo()) return;
        setWannaGo();
        if (getParentStar() instanceof MessierStar parent) {
            parent.destroy();
        }
    }

    private void createNewStar() {
        MessierStar messierStar = new MessierStar(ModProjectiles.MESSIER_STAR, level());
        Vec3 newPos = Vec3Helper.getRandomVec3(level().random, 100.0f).add(position());

        messierStar.setPos(newPos);
        messierStar.setTarget(getTarget());
        messierStar.setParentStar(this);
        messierStar.setOrder(getOrder() - 1);

        level().addFreshEntity(messierStar);
    }

    private void clientTicker() {
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
