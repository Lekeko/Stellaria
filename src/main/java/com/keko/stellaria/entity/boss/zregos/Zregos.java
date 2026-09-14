package com.keko.stellaria.entity.boss.zregos;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.packet.AlertPlayerZregosBiteS2C;
import com.keko.stellaria.packet.RenderZregosLaserS2C;
import com.keko.stellaria.sounds.ModSounds;
import com.keko.stellaria.stellarEvent.artha.StellarEventHandler;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.data.ParticleSettings;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.Optional;
import java.util.UUID;

public class Zregos extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    private static final float BASE_HP = 1000.0f;
    private static final float BASE_DMG = 8.0f;
    private static final float BASE_AR = 2.0f;

    private int attackCooldown = 60;
    private int attack = 0;
    private int prepareAttack = 0;

    private static EntityDataAccessor<Optional<UUID>> bodyChild =
            SynchedEntityData.defineId(Zregos.class, EntityDataSerializers.OPTIONAL_UUID);

    private Player target;
    private Vec3 wanderingTarget = null;
    private int dashes = 0;
    private final int MAX_DASHES = 5;

    public static final int MAX_SEGMENTS = 70; //w/o tail but tail is still tail so tail tail

    public static final int ATTACK_CD = 80;
    public int dashTimer = 20;
    public final int MAX_LASER_TIME = 20 * 7;
    public int laserTimer = MAX_LASER_TIME;
    public boolean wantsToLaser = false;
    public boolean wantsToModifSky = false;
    public Vec3 laserDirection = Vec3.ZERO;
    private float laserScreenAmp = 0.0f;
    int intangibleTime = 0;
    private Vec3 dashDirection = null;
    private int maceTimer = 10 * 20;

    private float attackWeights[] = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private static final int ATTACK_COUNT = 4;

    private final ServerBossEvent bossBar;

    public Zregos(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.bossBar = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS));
    }
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (!level().isClientSide) {
            StellarEventHandler.onBossDied((ServerLevel) level());
        }    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.AMETHYST_BLOCK_HIT;
    }

    @Override
    protected void playHurtSound(DamageSource damageSource) {
        for (Player player : level().players()) {
            player.playSound(
                    getHurtSound(null),
                    1.0f,
                    1.0f
            );
        }
    }

    @Override
    public void knockback(double d, double e, double f) {

    }

    @Override
    public void tick() {
        this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
        super.tick();
        handleRotation();
        if (!level().isClientSide ) {

            if (getHealth() < getMaxHealth() / 1.8f){
                if (maceTimer-- <= 0) {
                    maceTimer = 10 * 20 + level().random.nextIntBetweenInclusive(5, 15);
                    ZregosAttack.maceAttack(this);
                }
            }

            if (target == null){
                wanderingTarget = null;
                float distance = 999999999;
                for (Player player : level().players()) {
                    if (player.distanceTo(this) < distance) {
                        target = player;
                        distance = player.distanceTo(this);
                    }
                }
            }else {
                evalAttack();

                for (Player player : level().players()) {
                    if (player.distanceTo(this) < 10) {
                        player.hurt(level().damageSources().mobAttack(this), 25.0f);
                    }
                }

                if (noPhysics && intangibleTime-- <= 0)
                    noPhysics = false;

                if (attack == 0 && dashTimer == 20)
                    moveAroundTarget();

                if (tickCount % (180 + random.nextIntBetweenInclusive(0,40)) == 0){
                    target = null;
                }

            }
        }else {
            if (wantsToLaser)
                handleThoseFuckingParticlesThatIHate();
        }
    }

    private void handleThoseFuckingParticlesThatIHate() {
        spawnParticle(this, Stellaria.id("star_burst"));
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    private void moveAroundTarget() {
        if (wanderingTarget == null || (Vec3Helper.ETPdistance(this, wanderingTarget) < 7.0f || tickCount % 40 == 0 || Vec3Helper.ETEdistance(this, target) > 200)) {
            float spread = 50.0f;
            float minDistSpread = 35.0f;

            double x = target.getX() + (random.nextBoolean() ? 1 : -1) * (minDistSpread + random.nextFloat() * spread);
            double y = target.getY() + (random.nextBoolean() ? 1 : -1) * (minDistSpread + random.nextFloat() * spread);
            double z = target.getZ() + (random.nextBoolean() ? 1 : -1) * (minDistSpread + random.nextFloat() * spread);


            wanderingTarget = new Vec3(x, y, z);
        }
        if (wanderingTarget != null && attack != 1)
            ZregosAttack.followPlayer(this, wanderingTarget);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof ZregosBody);
    }

    private void evalAttack() {

        noPhysics = true;
        if (target == null) return;

        if (attackCooldown > 0) attackCooldown--;
        if (prepareAttack > 0) prepareAttack--;


        if (attackCooldown == 0 && attack == 0) {
            chooseAttack();
        }

        //prep phase
        if (attack > 0 && prepareAttack > 0){
            switch (attack){
                case 1:{
                    addDeltaMovement(getDeltaMovement().scale(-1.0f).scale(0.3d));
                    break;
                }
                case 2:{
                    addDeltaMovement(getDeltaMovement().multiply(1.0f,0.0f,1.0f).scale(0.25f));
                    break;
                }
                case 3:{
                    for (Player player : level().players()) {
                        ServerPlayNetworking.send((ServerPlayer) player, new RenderZregosLaserS2C(getId(), true, true, 0.0f));
                    }
                }
                case 4:
                case 5:
            }
        }

        //attack the actual attacky attack atacat
        float hpPr = Mth.clamp(getHealth()/getMaxHealth(), 0.75f, 1.0f);
        int newAttackCD = (int) (ATTACK_CD * hpPr);

        if (attack > 0 && prepareAttack == 0 && attackCooldown == 0) {

            switch (attack){

                case 1: {


                    if (dashes == 0){
                        if (dashTimer > 10)
                            ZregosAttack.pierce(this, target);
                    }else {
                        ZregosAttack.ghostDash(this, target, dashes);
                    }
                    dashTimer--;

                    if (dashTimer <= 0) {
                        setDashDirection(null);

                        if (Vec3Helper.getSpeedOnVel(getDeltaMovement()) > 18.2d)
                            setDeltaMovement(getDeltaMovement().scale(0.2d));
                        if (dashes < MAX_DASHES && getHealth() < getMaxHealth()/2.0f){
                            announceAttack(1);
                            attack = 1;
                            prepareAttack = 6;
                            dashes++;

                        }else {
                            dashes = 0;
                            attack = 0;
                            dashTimer = 20;
                            attackCooldown = newAttackCD;
                        }
                    }
                    return;
                }
                case 2:{
                   ZregosAttack.glideAndShoot(this, target);

                    setDeltaMovement(getDeltaMovement().scale(1/Vec3Helper.getSpeedOnVel(getDeltaMovement())));
                    attack = 0;
                    attackCooldown = newAttackCD;
                    return;
                }
                case 3:{
                    if (laserTimer > 0){
                        //MIKU MIKU BEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMMMMMMMMMMMMMMMMMMMMM
                        if (laserTimer == MAX_LASER_TIME) {
                            triggerAnim("controller", "zregos.open");
                            for (Player player : level().players()){
                                level().playSound(null, player.getOnPos(), ModSounds.ZREGOS_LASER_BEAM, SoundSource.HOSTILE, 10, 1.0f);
                            }

                        }
                        laserTimer--;

                        ZregosAttack.followPlayer(this, target.getPosition(1.0f));
                        for (Player player : level().players()) {
                            ServerPlayNetworking.send((ServerPlayer) player, new RenderZregosLaserS2C(getId(), true, false, 0.0f));
                        }

                    }else {
                        for (Player player : level().players()) {
                            ServerPlayNetworking.send((ServerPlayer) player, new RenderZregosLaserS2C(getId(), false, false, 0.0f));
                        }

                        laserTimer = MAX_LASER_TIME;
                        attack = 0;
                        attackCooldown = newAttackCD;
                    }
                    return;

                }
                case 4:{
                    ZregosAttack.spawnBoulders(this, target);
                    attack = 0;
                    attackCooldown = newAttackCD;
                    return;
                }
                case 5:
            }
        }
    }


    private void chooseAttack() {

        float totalWeight = 0.0f;
        for (int i = 1; i <= ATTACK_COUNT; i++) {
            totalWeight += attackWeights[i];
        }

        float randAtt = random.nextFloat() * totalWeight;

        int attack = 1;
        float catt = 0.0f;

        for (int i = 1; i <= ATTACK_COUNT; i++){
            catt += attackWeights[i];

            if (randAtt <= catt){
                attack = i;
                break;
            }
        }

        attackWeights[attack] *= 0.35f;
        for (int i = 1; i <= ATTACK_COUNT; i++) {

            if (i != attack) {
                attackWeights[i] += 0.4f;
            }

            attackWeights[i] = Mth.clamp(attackWeights[i], 0.2f, 5.0f);
        }

        this.attack = attack;
        announceAttack(attack);

        prepareAttack = 15;

    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        invulnerableTime = 0;
        if (damageSource == level().damageSources().fellOutOfWorld())
            return false;

        return super.hurt(damageSource, f/ 2f);
    }

    public int getAttack() {
        return attack;
    }


    public float getLaserScreenAmp() {
        return laserScreenAmp;
    }

    public void setLaserScreenAmp(float laserScreenAmp) {
        this.laserScreenAmp = laserScreenAmp;
        if (this.laserScreenAmp > 1.0f) this.laserScreenAmp = 1.0f;
        if (this.laserScreenAmp < 0.0f) this.laserScreenAmp = 0.0f;
    }

    private void announceAttack(int attack) {
        switch (attack) {
            case 1: {
                ServerPlayNetworking.send((ServerPlayer) target, new AlertPlayerZregosBiteS2C(67));
                return;
            }
            case 2:
            case 3:
            case 4:
            case 5:
        }
    }

    private void handleRotation() {
        Vec3 vec3 = this.getDeltaMovement();

        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double horizontalDist = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (180F / Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, horizontalDist) * (180F / Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        double d = vec3.x;
        double e = vec3.y;
        double f = vec3.z;
        double horizontalDist = vec3.horizontalDistance();

        float targetYaw = (float)(Mth.atan2(d, f) * (180F / Math.PI));
        float targetPitch = (float)(Mth.atan2(e, horizontalDist) * (180F / Math.PI));

        this.setXRot(Mth.rotLerp(0.2F, this.xRotO, targetPitch));
        this.setYRot(Mth.rotLerp(0.2F, this.yRotO, targetYaw));

        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
            ZregosBody prevBody = null;
            for (int i = 0; i < MAX_SEGMENTS-1; i++){

                ZregosBody zregosBody = new ZregosBody(ModEntities.ZREGOS_BODY, serverLevelAccessor.getLevel());
                zregosBody.setBodyParent(prevBody == null ? getUUID() : prevBody.getUUID());
                zregosBody.moveTo(this.getX() + i * 5, this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                if (prevBody == null)
                    this.setBodyChild(zregosBody.getUUID());
                else
                   prevBody.setBodyChild(zregosBody.getUUID());
                zregosBody.setHead(this.getUUID());
                zregosBody.setIndex(i + 1);
                serverLevelAccessor.getLevel().addFreshEntity(zregosBody);

                prevBody = zregosBody;
            }
            //copy paste for za TAIL //80
        if (prevBody != null) {
            ZregosTail zregosTail = new ZregosTail(ModEntities.ZREGOS_TAIL, serverLevelAccessor.getLevel());
            zregosTail.setBodyParent(prevBody.getUUID());
            zregosTail.moveTo(this.getX() + (MAX_SEGMENTS-1) * 5, this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            prevBody.setBodyChild(zregosTail.getUUID());
            zregosTail.setHead(this.getUUID());
            zregosTail.setIndex(80);
            serverLevelAccessor.getLevel().addFreshEntity(zregosTail);
        }


        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
    }

    public static AttributeSupplier.Builder setAtributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, BASE_HP)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, BASE_DMG)
                .add(Attributes.ARMOR, BASE_AR)
                .add(Attributes.ATTACK_KNOCKBACK, 4);

    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Zregos> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("zregos.open", RawAnimation.begin().thenPlay("zregos.open"));

        controllers.add(controller);
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    public Player getTarget() {
        return this.target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(bodyChild, Optional.empty());
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        this.entityData.get(bodyChild).ifPresent(uuid1 -> {
            compoundTag.putUUID("BodyChild", uuid1);
        });
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

        if (compoundTag.hasUUID("BodyChild")) {
            UUID uuid = compoundTag.getUUID("BodyChild");
            this.entityData.set(bodyChild, Optional.of(uuid));
        }
    }

    public Monster getBodyChild() {
        Optional<UUID> optional = this.entityData.get(bodyChild);
        if (optional.isEmpty()) return null;

        if (level() instanceof ServerLevel serverLevel) {
            return (Monster) serverLevel.getEntity(optional.get());
        }

        return null;
    }

    public void setBodyChild(UUID uuid) {
        this.entityData.set(bodyChild, Optional.ofNullable(uuid));
    }

    public void spawnParticle(Entity entity, ResourceLocation id){
        try {
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setAttachedEntity(entity);
            float disp = 0.3f;
            float rx = (entity.getRandom().nextFloat() - .5f) * disp;
            float ry = (entity.getRandom().nextFloat() - .5f) * disp;
            float rz = (entity.getRandom().nextFloat() - .5f) * disp;
            emitter.setCount(1);
            emitter.setRate(100);
            Vector3fc initialDirection = laserDirection.add(rx, ry, rz).toVector3f();
            Vector3fc initialDirectionVariation = new Vector3f(1.0f, 1.0f, 1.0f);
            Vector3fc initialRoation = new Vector3f(0.0f, 0.0f, 0.0f);
            Vector3fc initialRoationVariation = new Vector3f(0.0f, 0.0f, 0.0f);


//            emitter.setParticleSettings(new ParticleSettings(10.0f, 1.2f, 2, 20, 1.0f,
//                    initialDirection, false, false, true, true, true));
            emitter.setParticleSettings(new ParticleSettings(10.0f, 1.0f, 2.0f, 1.0f, 20, 10,
                    initialDirection, true, initialDirectionVariation, initialRoation, false, initialRoationVariation, true, true, true));
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }

    public void makeIntangibleFor(float v) {
        noPhysics = true;
        intangibleTime = (int) (v * 20f);
    }

    public Vec3 getDashDirection() {
        return dashDirection;
    }

    public void setDashDirection(Vec3 dashDirection) {
        this.dashDirection = dashDirection;
    }
}
