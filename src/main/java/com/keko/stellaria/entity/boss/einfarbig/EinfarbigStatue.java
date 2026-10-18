package com.keko.stellaria.entity.boss.einfarbig;

import com.keko.stellaria.blocks.voidLamp.VoidLampEntity;
import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import com.keko.stellaria.particles.ModParticles;
import com.keko.stellaria.sounds.ModSounds;
import com.mojang.math.Axis;
import foundry.veil.api.client.registry.LightTypeRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.LightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EinfarbigStatue extends Entity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private AreaLightData areaLightData;
    LightRenderHandle<AreaLightData> handle;
    private static final Set<EinfarbigStatue> ACTIVE_AREA = new HashSet<>();

    public EinfarbigStatue(EntityType<?> entityType, Level level) {
        super(entityType, level);
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
        if (!level().isClientSide){
            for (Player player : level().players())
                if (player.distanceTo(this) < 40 && !player.isSpectator()){
                    for (ServerPlayer splayer : level().getServer().getPlayerList().getPlayers())
                        splayer.playNotifySound(ModSounds.STATUE_SUMMON, SoundSource.MASTER, 1.0f, 1.0f);

                    VoidDealer dealer = new VoidDealer(ModEntities.VOID_DEALER, level());
                    dealer.setPos(position());


                    level().addFreshEntity(dealer);
                    ((ServerLevel) level()).sendParticles(
                            (ServerPlayer) player,
                            ModParticles.ZREGOS_STAR_BURST_NORMAL,
                            true,
                            getPosition(1.0f).x, getPosition(1.0f).y, getPosition(1.0f).z,
                            300,
                            0,0,0,
                            6
                    );
                    discard();
                    return;
                }
        }
    }

    @Override
    public void onClientRemoval() {
        removeLight();
        super.onClientRemoval();
    }

    public void removeLight(){
        if (handle != null) {
            handle.free();
            handle = null;
        }
        ACTIVE_AREA.remove(this);
    }

    public void addLight(){
        if (handle == null){

            this.setAreaLightData(new AreaLightData()
                    .setBrightness(1.0f)
                    .setColor(1.0f, 1.0f, 1.0f)
                    .setSize(0.1, 0.1)
                    .setDistance(200.0f)
                    .setInscatteringStrength(400.0f));

            areaLightData.getPosition().set(position().x, position().y + 30.0f, position().z);
            areaLightData.setAngle(1.0f);
            areaLightData.getOrientationMutable().set(Axis.XN.rotation(Mth.PI/2.0f));


            handle = VeilRenderSystem.renderer().getLightRenderer().addLight(getAreaLightData());
            ACTIVE_AREA.add(this);
        }
    }

    public static void clearAllLights() {
        for (EinfarbigStatue statue : new ArrayList<>(ACTIVE_AREA)) {
            statue.removeLight();
        }
        ACTIVE_AREA.clear();
    }

    public void setAreaLightData(AreaLightData areaLightData) {
        this.areaLightData = areaLightData;
    }
    public AreaLightData getAreaLightData() {
        return areaLightData;
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
