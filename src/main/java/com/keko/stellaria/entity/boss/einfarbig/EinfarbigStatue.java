package com.keko.stellaria.entity.boss.einfarbig;

import com.mojang.math.Axis;
import foundry.veil.api.client.registry.LightTypeRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.LightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class EinfarbigStatue extends Entity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private AreaLightData areaLightData;
    LightRenderHandle<AreaLightData> handle;
    public EinfarbigStatue(EntityType<?> entityType, Level level) {
        super(entityType, level);
        if (level.isClientSide){
            areaLightData = new AreaLightData()
                    .setBrightness(12.480f)
                    .setColor(88/255.0f, 28/255.0f, 66/255.0f)
                    .setSize(0.1, 0.1)
                    .setDistance(60.0f)
                    .setInscatteringStrength(12.810f);
            areaLightData.getPosition().set(position().x, position().y + 30.0f, position().z);
            areaLightData.setAngle(1.0f);
            areaLightData.getOrientationMutable().set(Axis.XN.rotation(Mth.PI/2.0f));

            handle = VeilRenderSystem.renderer().getLightRenderer().addLight(areaLightData);
        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<EinfarbigStatue> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("einfarbig_statue.idle", RawAnimation.begin().thenLoop("einfarbig_statue.idle"));

        controllers.add(controller);
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("einfarbig_statue.idle"));
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide){
            if (areaLightData != null) {
                handle.getLightData().getPosition().set(position().x, position().y + 30.0f, position().z);
            }
        }
    }

    @Override
    public void onClientRemoval() {
        handle.free();

        super.onClientRemoval();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
