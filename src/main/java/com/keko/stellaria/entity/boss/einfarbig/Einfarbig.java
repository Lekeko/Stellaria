package com.keko.stellaria.entity.boss.einfarbig;

import com.keko.stellaria.blocks.ModBlocks;
import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.BunnyHat;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.HoleVortex;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.VoidSword;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.VoidLaserWarn.VoidLaserWarn;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectile;
import com.keko.stellaria.helper.UtilHelper;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.packet.EiniTPPartS2C;
import com.keko.stellaria.packet.PhantomTPPartS2C;
import com.keko.stellaria.sounds.ModSounds;
import com.keko.stellaria.stellarEvent.artha.StellarEventHandler;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.DirectionalLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class Einfarbig extends Monster implements GeoEntity {
    private DirectionalLightData directionalLightData;
    LightRenderHandle<DirectionalLightData> handle;

    private int changeTargetTimer = 0;
    private final int MAX_TARGET_TIEMR = 15 * 20;

    private static final float MAX_HP = 1000;

    private final int MAX_CD = 1 * 20;
    private int atCd = MAX_CD;
    private final float MELEE_DMG = 30.0f;

    private int attack = 0;
    private int attatckTickCount = 1;
    private static EntityDataAccessor<Boolean> SHOULD_RENDER =
            SynchedEntityData.defineId(Einfarbig.class, EntityDataSerializers.BOOLEAN);
    private static EntityDataAccessor<Integer> PHASE =
            SynchedEntityData.defineId(Einfarbig.class, EntityDataSerializers.INT);
    //phase vars i hate with all my life
    private int phaseTimer = 60;
    private static EntityDataAccessor<Boolean> IS_CHANGIN =
            SynchedEntityData.defineId(Einfarbig.class, EntityDataSerializers.BOOLEAN);
    private PointLightData pointLightData;
    LightRenderHandle<PointLightData> handlePointLight;
    private float phaseLightBISR = 1.0f;

    //attack vars
    //card attack
    private final int MAX_CARD_BURST = 2;
    private int cardBurstNr = MAX_CARD_BURST;
    private final int CARD_BURST_SPACE_TIMER = 15;
    private int nextCardBurstTimer = 0;
    private final int CARD_NR = 20;

    //dash
    private int DASHES_NR = 4;
    private int curDash = 0;
    private Vec3 dashDirection = Vec3.ZERO;

    private enum DashState { PREPARE, HIDDEN, DASHING } //love this shi
    private DashState dashState = DashState.PREPARE;

    private final int PREPARE_TIMER = 20;
    private final int HIDDEN_TIMER = 20;
    private final int DASH_TIMER = 20;

    //HatBunnyAttack
    private final int HAT_SPAWN_TIMER = 13;


    //phase2 attacks

    //void lasers
    private final int LASER_RATE = 8;
    private final int MAX_LASERS = 20;
    private int curLaser = 0;

    //Swords
    private final int MAX_SWORDS = 20;
    private int swords = 0;

    //Movement vars

    private double orbitAngle;
    private double speed = 1.0d;

    private final ServerBossEvent bossBar;

    public Einfarbig(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.bossBar = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS));
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
    public void tick() {
        super.tick();
        this.noPhysics = true;
        this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());

        if (level().isClientSide)
            clientTick();
        else 
            serverTick();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }


    private void serverTick() {
        handleTargets();
        if (getTarget() != null){
            if (getIsChanging()){
                if (--phaseTimer > 0){
                    return;
                }else {
                    setIsChangin(false);
                    setPhase(2);
                    attack = 2;
                    handleAttacks();
                }
            }

            if (getHealth() < getMaxHealth() / 2.0f && getPhase() == 1){
                changePhase();
                return;
            }
            if (attack != 2)
                faceTarget();

            if (attack == 0) {
                triggerAnim("controller","einfarbig.idle");
                wanderAround();
                if (--atCd <= 0) {
                    evalAttack();
                }
            } else {
                handleOnGoingAttacks();
            }
        }

    }


    private void evalAttack() {
        if (getPhase() == 1){
            attack = random.nextIntBetweenInclusive(1, 3);
        }else {
            attack = random.nextIntBetweenInclusive(1, 4);
        }

        beginAnimation();
    }

    private void beginAnimation() {
        switch (attack){
            case 1 -> {
                triggerAnim("controller", "einfarbig.card_attack");
            }
            case 2 -> {
                triggerAnim("controller", "einfarbig.prepare_dashing");
            }
            case 3 -> {
                triggerAnim("controller", "einfarbig.hat_attack");
            }
            case 4 -> {
                triggerAnim("controller", "einfarbig.tp1");
            }

        }
        handleAttacks();
    }

    private void handleAttacks() {
        switch (attack){
            case 1 -> {
                cardBurstNr = MAX_CARD_BURST;
            }
            case 2 -> {
                attatckTickCount = tickCount;
                curDash = 0;
                dashState = DashState.PREPARE;
                setShouldRender(true);
            }
            case 3 -> {
                attatckTickCount = tickCount;
            }
            case 4 -> {
                attatckTickCount = tickCount;
                curLaser = 0;
            }
            case 5 -> {
                swords = 0;
            }
        }
    }

    private void handleOnGoingAttacks() {
        switch (attack){
            case 1 -> {
                if (cardBurstNr > 0 && tickCount - attatckTickCount > CARD_BURST_SPACE_TIMER){
                    if (getPhase() == 1){
                        for (int i = 0; i < CARD_NR; i++)
                            summonCards();
                    }else {
                        Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
                        Vec3 dir;
                        if (cardBurstNr == 2)
                            dir = getViewVector(1.0f).cross(up);
                        else
                            dir = up.cross(getViewVector(1.0f));
                        for (int i = 0; i < 5; i++){
                            Vec3 summonPos = position().add(dir.scale(i * 20)).add(0.0f, random.nextFloat() * 20, 0.0f);

                            HoleVortex holeVortex = new HoleVortex(ModProjectiles.HOLE_VORTEX, level());
                            holeVortex.setPos(summonPos);
                            holeVortex.setOwner(this);
                            level().addFreshEntity(holeVortex);
                        }
                        for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
                            player.playNotifySound(ModSounds.EINI_STARS, SoundSource.MASTER, 1.0f, 1.0f);


                    }
                    cardBurstNr--;
                    attatckTickCount = tickCount;
                }
                if (cardBurstNr == 0 &&  tickCount - attatckTickCount > CARD_BURST_SPACE_TIMER + 20){
                    finalizeAttack();
                }
            }
            case 2 -> {
                int elapsed = tickCount - attatckTickCount;

                switch (dashState) {
                    case PREPARE -> {
                        if (elapsed >= PREPARE_TIMER) {
                            goAway(20);
//                            getTarget().addDeltaMovement(new Vec3(0.0f, 3.0f, 0.0f));
                        }
                    }
                    case HIDDEN -> {
                        if (elapsed >= HIDDEN_TIMER) {
                            teleportNearbyTarget();
                            setShouldRender(true);
                            for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
                                player.playNotifySound(ModSounds.EINI_TP, SoundSource.MASTER, 1.0f, 1.0f);
                            spawnTpParticles(0);

                            if (curDash < DASHES_NR) {
                                curDash++;
                                dashDirection = getTarget().position().subtract(position()).multiply(1.0, 0.0, 1.0).normalize();
                                faceTarget();
                                triggerAnim("controller", "einfarbig.dash");
                                dashState = DashState.DASHING;
                                attatckTickCount = tickCount;
                            } else {
                                finalizeAttack();
                            }
                        }
                    }
                    case DASHING -> {
                        if (elapsed < DASH_TIMER) {
                            float speed = 4.2f;
                            damageTargetNearby();
                            setDeltaMovement(dashDirection.scale(speed));
                        } else {
                            goAway(0);
                        }
                    }
                }
            }
            case 3 -> {
                if (tickCount - attatckTickCount == HAT_SPAWN_TIMER){
                    if (getPhase() == 1){
                        BunnyHat bunnyHat = new BunnyHat(ModProjectiles.BUNNY_HAT, level());
                        bunnyHat.setPos(position().add(0.0f, 30.0f, 0.0f));
                        bunnyHat.setPhase(getPhase());
                        bunnyHat.setOwner(this);
                        level().addFreshEntity(bunnyHat);
                    }else {
                        for (int i = -1; i <= 1; i+=2)
                            for (int j = -1; j <= 1; j+=2)
                                for (int k = -1; k <= 1; k+=2){
                                    float dist = 20;

                                    BunnyHat bunnyHat = new BunnyHat(ModProjectiles.BUNNY_HAT, level());
                                    bunnyHat.setPos(position().add(0.0f, 10.0f, 0.0f)
                                            .add(new Vec3(i * dist, j*dist, k*dist)));
                                    bunnyHat.setPhase(getPhase());
                                    bunnyHat.setOwner(this);
                                    level().addFreshEntity(bunnyHat);
                            }
                    }

                }
                if (tickCount - attatckTickCount > HAT_SPAWN_TIMER + 20)
                    finalizeAttack();
            }
            case 4 -> {
                wanderAround();
                if (MAX_LASERS > curLaser){
                    if (tickCount - attatckTickCount > LASER_RATE + (curLaser == 0 ? 10 : 0)) {
                        setShouldRender(false);

                        curLaser++;
                        attatckTickCount = tickCount;
                        summonLaser();
                        if (curLaser >= MAX_LASERS) {
                            setShouldRender(true);
                            triggerAnim("controller", "einfarbig.tp2");
                        }
                    }
                }else {
                    if (tickCount - attatckTickCount > LASER_RATE + 20) {
                        finalizeAttack();
                    }
                }
            }
            case 5 ->{

            }

        }
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!level().isClientSide) {
            for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
                player.playNotifySound(ModSounds.EINI_DEFEAT, SoundSource.MASTER, 1.0f, 1.0f);
            StellarEventHandler.onBossDied((ServerLevel) level(), 2);
        }
        super.die(damageSource);

    }

    public void spawnSword(Vec3 pos) {
        Vec3 dir = getTarget().position().add(getTarget().getDeltaMovement()).subtract(pos).normalize();
        float distance = 30.0f;

        VoidSword voidSword = new VoidSword(ModProjectiles.VOID_SWORD, level());
        voidSword.setOwner(this);
        voidSword.setPos(pos);
        voidSword.setDirection(dir);

        level().addFreshEntity(voidSword);
    }

    private void summonLaser() {
        Vec3 playerPos = getTarget().position();
        Vec3 offset = Vec3Helper.getRandomVec3(random, 40.0f).multiply(1.0f, 0.0f, 1.0f);
        Vec3 spawnPos = playerPos.add(offset);

        while (!level().getBlockState(new BlockPos((int)spawnPos.x, (int)spawnPos.y, (int)spawnPos.z)).is(ModBlocks.VOID_STONE)){
            if (level().getBlockState(new BlockPos((int)spawnPos.x, (int)spawnPos.y, (int)spawnPos.z)).is(Blocks.VOID_AIR))
                break;
            spawnPos = spawnPos.subtract(0.0f, 1.0f, 0.0f);
        }

        VoidLaserWarn voidLaserWarn = new VoidLaserWarn(ModProjectiles.VOID_LASER_WARN, level());
        voidLaserWarn.setPos(spawnPos);
        voidLaserWarn.setNoGravity(true);
        voidLaserWarn.setOwner(this);

        level().addFreshEntity(voidLaserWarn);

        for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
            player.playNotifySound(ModSounds.EINI_LASER, SoundSource.MASTER, 1.0f, 1.0f);
    }

    private void summonCards(){
        Vec3 dir = getTarget().position().subtract(position()).normalize();
        float speed = 8.0f;
        float spread = 1.5f;
        Vec3 randomOffset = Vec3Helper.getRandomVec3(getRandom(), spread);
        Vec3 vel = dir.scale(speed);

        PlayingCardProjectile card = new PlayingCardProjectile(ModProjectiles.PLAYING_CARD_PROJECTILE, level());
        card.setPos(position());
        card.setDeltaMovement(vel.add(randomOffset));

        if (cardBurstNr == 1) {
            card.setPos(position().add(0.0f, 30.0f, 0.0f));
            card.setDeltaMovement(vel.add(randomOffset).subtract(0.0f, 4.0f, 0.0f));
        }

        card.setOwner(this);

        level().addFreshEntity(card);
    }

    private void summonStars(int i){

    }

    private void goAway(int i) {
        spawnTpParticles(i);
        setShouldRender(false);
        setDeltaMovement(Vec3.ZERO);
        dashState = DashState.HIDDEN;
        attatckTickCount = tickCount;
    }

    private void teleportNearbyTarget() {
        Vec3 playerPosition = getTarget().position();
        Vec3 playerDir = Vec3.directionFromRotation(0.0f, getTarget().getYRot());
        Vec3 newTpPos = playerPosition.add(playerDir.scale(30.0d));
        teleportTo(newTpPos.x, newTpPos.y + 7.0f, newTpPos.z);
    }



    private void finalizeAttack() {
        attack = 0;
        atCd = (int) (MAX_CD * (getHealth()/getMaxHealth()));

    }


    private void clientTick() {
        if (getPhase() == 2 && handle != null)
            handle.getLightData().setColor(1.0f, 1.0f, 1.0f);

        if (getIsChanging()){
            Vec3 pos =  position().add(getViewVector(1.0f));
            if (handlePointLight == null){
                pointLightData = new PointLightData()
                        .setPosition(pos.x ,pos.y, pos.z)
                        .setColor(1.0f,1.0f,1.0f)
                        .setRadius(phaseLightBISR)
                        .setBrightness(phaseLightBISR)
                        .setInscatteringStrength(phaseLightBISR);
                handlePointLight = VeilRenderSystem.renderer().getLightRenderer().addLight(pointLightData);
            } else {
                setPhaseLightBISR(Minecraft.getInstance().getTimer().getRealtimeDeltaTicks() * 5.0f);
                handlePointLight.getLightData()
                        .setPosition(pos.x ,pos.y, pos.z)
                        .setRadius(phaseLightBISR * 3)
                        .setBrightness(phaseLightBISR)
                        .setInscatteringStrength(phaseLightBISR * 10);
            }
        }else {
            if (handlePointLight != null && !getIsChanging()){
                Vec3 pos =  position().add(getViewVector(1.0f));
                setPhaseLightBISR(Minecraft.getInstance().getTimer().getRealtimeDeltaTicks() * -5.0f);
                handlePointLight.getLightData()
                        .setRadius(phaseLightBISR * 3)
                        .setBrightness(phaseLightBISR)
                        .setInscatteringStrength(phaseLightBISR * 10);
                if (phaseLightBISR < 1){
                    handlePointLight.free();
                    pointLightData = null;
                }
            }
        }

        if (isDeadOrDying()){
            if (handle != null){
                handle.free();
                directionalLightData = null;
            }
            if (handlePointLight != null){
                handlePointLight.free();
                pointLightData = null;
            }
        }
    }

    private void wanderAround() {

        double radius = 60.5f;
        double height = 20.5f;
        orbitAngle += 0.01f;
        Vec3 targetPos = getTarget().position().add(
                Math.cos(orbitAngle) * radius,
                height + Math.sin(tickCount * 0.01),
                Math.sin(orbitAngle) * radius
        );

        Vec3 dir = targetPos.subtract(position()).scale(0.4);
        if (dir.length() > speed)
            dir = dir.normalize().scale(speed);

        Vec3 vel = getDeltaMovement().lerp(dir, 0.15);

        setDeltaMovement(vel);
        move(MoverType.SELF, vel);

    }

    private void changePhase(){
        for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
            player.playNotifySound(ModSounds.EINI_PHASE, SoundSource.MASTER, 1.0f, 1.0f);
        finalizeAttack();
        setIsChangin(true);
        triggerAnim("controller", "einfarbig.phase_transition");
    }
    private void damageTargetNearby() {
        if (distanceTo(getTarget()) < 10.0f){
            getTarget().hurt(level().damageSources().mobAttack(this), MELEE_DMG);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (!getIsChanging())
            return super.hurt(damageSource, f);
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Einfarbig> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("einfarbig.idle", RawAnimation.begin().thenPlay("einfarbig.idle"));
        controller.triggerableAnim("einfarbig.idle2", RawAnimation.begin().thenPlay("einfarbig.idle2"));
        controller.triggerableAnim("einfarbig.card_attack", RawAnimation.begin().thenPlay("einfarbig.card_attack"));
        controller.triggerableAnim("einfarbig.prepare_dashing", RawAnimation.begin().thenPlay("einfarbig.prepare_dashing"));
        controller.triggerableAnim("einfarbig.dash", RawAnimation.begin().thenLoop("einfarbig.dash"));
        controller.triggerableAnim("einfarbig.hat_attack", RawAnimation.begin().thenPlay("einfarbig.hat_attack"));
        controller.triggerableAnim("einfarbig.tp1", RawAnimation.begin().thenPlay("einfarbig.tp1"));
        controller.triggerableAnim("einfarbig.tp2", RawAnimation.begin().thenPlay("einfarbig.tp2"));
        controller.triggerableAnim("einfarbig.phase_transition", RawAnimation.begin().thenPlay("einfarbig.phase_transition"));

        controllers.add(controller);
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("einfarbig.idle"));
    }
    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier.Builder setAtributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HP)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0);

    }

    public DirectionalLightData getDirectionalLightData() {
        return directionalLightData;
    }

    public void setDirectionalLightData(DirectionalLightData directionalLightData) {
        this.directionalLightData = directionalLightData;
    }

    public void removeLight() {
        if (handle != null) {
            handle.free();
            handle = null;
        }
    }

    public void addLight() {
        if (handle == null && !isRemoved()) {
            this.setDirectionalLightData(new DirectionalLightData()
                    .setBrightness(0.4f)
                    .setColor(255/255.0f, 196/255.0f, 248/255.0f)
            );
            handle = VeilRenderSystem.renderer().getLightRenderer().addLight(directionalLightData);
        }
    }

    @Override
    public void checkDespawn() {
        //nuh uhhhh
    }

    private void handleTargets() {
        boolean anyoneAlive = false;
        for (Player player : level().players()) {
            if (player.isAlive() && !player.isSpectator()) {
                anyoneAlive = true;
                break;
            }
        }

        if (!anyoneAlive) {
            discard();
            return;
        }

        if (getTarget() == null || --changeTargetTimer <= 0 || getTarget().isDeadOrDying()) {
            setTarget(UtilHelper.getPlayerTarget(this, -1));
            changeTargetTimer = MAX_TARGET_TIEMR;
            if (getTarget() == null) {
                discard();
            }
        }
    }
    private void faceTarget() {
        double dx = getTarget().getX() - getX();
        double dy = getTarget().getEyeY() - getEyeY();
        double dz = getTarget().getZ() - getZ();
        double horiz = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Mth.atan2(dz, dx) * (180.0f / Math.PI)) - 90.0f;
        float pitch = (float) -(Mth.atan2(dy, horiz) * (180.0f / Math.PI));

        setYRot(yaw);
        setXRot(pitch);
        setYHeadRot(yaw);
        this.yBodyRot = yaw;
        this.yRotO = yaw;
        this.yBodyRotO = yaw;
        this.yHeadRotO = yaw;
    }

    public void setPhaseLightBISR(float phaseLightBISR) {
        this.phaseLightBISR += phaseLightBISR;
    }

    public void setShouldRender(boolean shouldRender) {
        this.entityData.set(SHOULD_RENDER, shouldRender);
    }

    public boolean getShouldRender() {
        return this.entityData.get(SHOULD_RENDER);
    }

    public void setPhase(int phase) {
        this.entityData.set(PHASE, phase);
    }

    public int getPhase() {
        return this.entityData.get(PHASE);
    }

    public void setIsChangin(boolean c) {
        this.entityData.set(IS_CHANGIN, c);
    }

    public boolean getIsChanging() {
        return this.entityData.get(IS_CHANGIN);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHOULD_RENDER, true);
        builder.define(PHASE, 1);
        builder.define(IS_CHANGIN, false);
    }

    private void spawnTpParticles(int i) {
        for (Player player : level ().players()) {
            if (getPhase() == 1)
                ServerPlayNetworking.send((ServerPlayer) player, new EiniTPPartS2C(position().toVector3f().add(0.0f, i, 0.0f)));
            else
                ServerPlayNetworking.send((ServerPlayer) player, new PhantomTPPartS2C(position().toVector3f().add(0.0f, i, 0.0f)));
        }
    }
}
