package com.keko.stellaria.entity.boss.minions.einfarbig;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class Messier extends Monster implements GeoEntity {
    private PointLightData pointLightData;
    LightRenderHandle<PointLightData> handle;

    public Messier(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        if (level.isClientSide) {
            pointLightData = new PointLightData()
                    .setBrightness(17.0f)
                    .setColor(1.0f, 1.0f, 1.0f)
                    .setRadius(20)
                    .setInscatteringStrength(25);
            handle = VeilRenderSystem.renderer().getLightRenderer().addLight(pointLightData);
        }
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide)
            clientTick();
        else
            serverTick();
    }

    private void clientTick() {
        if (pointLightData != null) {
            handle.getLightData().setPosition(position().x, position().y, position().z);
        }
    }

    private void serverTick() {

    }

    @Override
    public void onClientRemoval() {
        handle.free();

        super.onClientRemoval();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

}
