package com.keko.stellaria.entity.decour.zregosBoulders;

import com.keko.stellaria.entity.boss.zregos.ZregosAttack;
import com.keko.stellaria.particles.ModParticles;
import com.keko.stellaria.screenVfx.ScreenVfxHandler;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ZregosBoulderNormal extends Entity implements GeoEntity {
    private final int DETONATE_TIMER = 10 * 20;
    private int hits = 1;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private float clientTicker = 0;
    private final float ASCEND_TIMER = 20;

    public ZregosBoulderNormal(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        if (!level().isClientSide) hits = 0;
    }

    @Override
    public void onClientRemoval() {
        super.onClientRemoval();
    }



    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        ScreenVfxHandler.addBoulder(this);
    }

    @Override
    public void tick() {
        if (tickCount == 1) triggerAnim("controller", "zregos_boulders_normal.spin");

        if (tickCount == DETONATE_TIMER && !level().isClientSide && hits > 0){
            Player target = null;
            float distance = 999999999;
            for (Player player : level().players()) {
                if (player.distanceTo(this) < distance) {
                    target = player;
                    distance = player.distanceTo(this);
                }
            }
            if (target != null)
                ZregosAttack.punish(this, target);

            triggerAnim("controller", "zregos_boulders_normal.lunge");
        }
        if (hits == 0){
            playSound(ModSounds.ZREGOS_BOULDERS_BREAK, 10.0f, 0.9f + random.nextFloat());
            playSound(SoundEvents.AMETHYST_CLUSTER_BREAK, 10.0f, 0.9f + random.nextFloat());
            spawnParticles();
            discard();
        }


        if (tickCount > DETONATE_TIMER + 20) discard();
    }


    public float getASCEND_TIMER() {
        return ASCEND_TIMER;
    }

    public float getClientTicker() {
        return clientTicker;
    }

    public void setClientTicker(float clientTicker) {
        this.clientTicker = clientTicker;
    }

    public void setHits(int hits) {
        this.hits = hits;
        if (hits == 0)
            YSKYN();
    }


    private void YSKYN() {
    }

    public int getHits() {
        return hits;
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

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ZregosBoulderNormal> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("zregos_boulders_normal.spin", RawAnimation.begin().thenLoop("zregos_boulders_normal.spin"));
        controller.triggerableAnim("zregos_boulders_normal.lunge", RawAnimation.begin().thenPlayAndHold("zregos_boulders_normal.lunge"));

        controllers.add(controller);
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    public void spawnParticles(){
        for (Player player : level().players()){
            ((ServerLevel) level()).sendParticles(
                    (ServerPlayer) player,
                    ModParticles.ZREGOS_STAR_BURST_NORMAL,
                    true,
                    getPosition(1.0f).x, getPosition(1.0f).y + 2.0f, getPosition(1.0f).z,
                    200,
                    0,0,0,
                    0
            );
        }
    }
}
