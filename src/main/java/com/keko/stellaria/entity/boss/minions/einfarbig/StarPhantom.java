package com.keko.stellaria.entity.boss.minions.einfarbig;

import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar.VoidStar;
import com.keko.stellaria.helper.UtilHelper;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.packet.PhantomTPPartS2C;
import com.keko.stellaria.sounds.ModSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class StarPhantom extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final int MAX_HP = 200;
    private int attack = 0;
    private float remainingAttackTime = 4 * 20;
    private int[] attackTimers =  {0, 60, 45, 95};
    private int changeTargetTimer = 0;
    private final int MAX_TARGET_TIEMR = 15 * 20;

    //Flying vars
    private Vec3 flyingDir = Vec3.ZERO;
    private final float FLIGHT_SPEED = 3.0f;

    private final float STAR_FIRE_RATE = 5.0f;
    private float starTime = STAR_FIRE_RATE;

    //SpinVars
    private final float UNTIL_STAR_BURST = 25;
    private final int MAX_STARS = 3;
    private int starCharges = MAX_STARS;

    //Slash Vars
    private final float UNTIL_SLASH_DMG = 70;
    private Vec3 strikeDir = Vec3.ZERO;

    //Teleport vars (nightmare)
    private final float MAX_TP_TIME = 40;
    private static EntityDataAccessor<Boolean> IS_TELEPORTING =
            SynchedEntityData.defineId(StarPhantom.class, EntityDataSerializers.BOOLEAN);
    private float teleportationTime = MAX_TP_TIME;

    private int reallyImportantTickCount = 0;

    private float damageTicker = 0;
    private boolean spawnednext = false;

    public float getDamageTicker() {
        return damageTicker;
    }

    public void decreaseDamageTicker(){
        this.damageTicker--;
    }

    public StarPhantom(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void checkDespawn() {

    }

    public void setFlyingDir(Vec3 flyingDir) {
        this.flyingDir = flyingDir;
    }

    public Vec3 getFlyingDir() {
        return flyingDir;
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
        if (getDamageTicker() > 0)
            decreaseDamageTicker();
    }

    private void serverTick() {
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
                return;
            }
        }

        if (isDeadOrDying())
            return;

        if (!getIsTeleporting()) {
            if (attack !=1)
                faceTarget();
            if (--remainingAttackTime <= 0) {
                teleportAndChooseAttack();
            }
            handleOngoingAttacks();
        } else if (--teleportationTime <= 0) {
            teleportNearby();
        }

    }

    private void handleOngoingAttacks() {
        switch (attack){
            case 1 -> {
                if (flyingDir != Vec3.ZERO){
                    setDeltaMovement(flyingDir.scale(FLIGHT_SPEED));
                    hurtMarked = true;
                }
                if (getTarget() == null) return;

                if (tickCount % 5 == 0){
                    VoidStar voidStar = new VoidStar(ModProjectiles.VOID_STAR, level());
                    voidStar.setPos(position());
                    voidStar.setTarget(getTarget());
                    Vec3 dir = Vec3Helper.getRandomVec3(getRandom(), 2.0f);
                    voidStar.setDeltaMovement(dir);
                    voidStar.setTrackingTime(40);
                    level().addFreshEntity(voidStar);
                }
            }
            case 2 -> {
                if (tickCount - reallyImportantTickCount > UNTIL_STAR_BURST && starCharges > 0){
                    for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
                        player.playNotifySound(ModSounds.STAR_PHANTOM_SHOOT, SoundSource.MASTER, 1.0f, 1.0f);
                    Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
                    Vec3 left = up.cross(getViewVector(1.0f));
                    Vec3 right = getViewVector(1.0f).cross(up);
                    float yDir = starCharges - 2;
                    yDir *= -1;
                    if (yDir == 0) yDir = 0.1f;
                    left = left.add(0.0f, yDir, 0.0f);
                    right = right.add(0.0f, yDir, 0.0f);
                    float speed = 5.0f;
                    Vec3 leftDir = left.scale(speed);
                    Vec3 rightDir = right.scale(speed);

                    VoidStar voidStar1 = new VoidStar(ModProjectiles.VOID_STAR, level());
                    voidStar1.setPos(position().add(leftDir).add(0.0f, 15.0f, 0.0f));
                    voidStar1.setTarget(getTarget());
                    voidStar1.setDeltaMovement(leftDir);
                    voidStar1.setTrackingTime(40);
                    level().addFreshEntity(voidStar1);

                    VoidStar voidStar2 = new VoidStar(ModProjectiles.VOID_STAR, level());
                    voidStar2.setPos(position().add(rightDir).add(0.0f, 15.0f, 0.0f));
                    voidStar2.setTarget(getTarget());
                    voidStar2.setDeltaMovement(rightDir);
                    voidStar2.setTrackingTime(40);
                    level().addFreshEntity(voidStar2);

                    starCharges--;


                }
            }
            case 3 -> {
                if (tickCount - reallyImportantTickCount == UNTIL_SLASH_DMG - 2){
                    strikeDir = getTarget().position();
                }
                if (tickCount - reallyImportantTickCount == UNTIL_SLASH_DMG){
                    for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
                        player.playNotifySound(ModSounds.STAR_PHANTOM_SLASH, SoundSource.MASTER, 1.0f, 1.0f);
                    for (Player player : level().players())
                        if (strikeDir.distanceTo(player.position()) < 3.0f)
                            player.hurt(level().damageSources().mobAttack(this), 60.0f);
                }
            }

        }
    }


    private void teleportAndChooseAttack() {
        attack = level().random.nextIntBetweenInclusive(1,3);

        setIsTeleporting(true);
        spawnTpParticles();
        teleportationTime = MAX_TP_TIME;
        Vec3 playerPosition = getTarget().position();
        float spreadXZ = 60.0f;


        Vec3 newTpPos = playerPosition.add(
                (10.0f + level().random.nextFloat() * spreadXZ) * (level().random.nextBoolean() ? -1 : 1),
                30,
                (10.0f + level().random.nextFloat() * spreadXZ) * (level().random.nextBoolean() ? -1 : 1)
        );
        teleportTo(newTpPos.x, newTpPos.y, newTpPos.z);

        ///////////////////////////////////////////
        flyingDir = Vec3.ZERO;
        starCharges = MAX_STARS;
        strikeDir = Vec3.ZERO;

    }

    private void beginAnimation() {
        switch (attack){
            case 1 -> {
                triggerAnim("controller", "star_phantom.fly");
            }
            case 2 -> {
                triggerAnim("controller", "star_phantom.spin_and_attack");
            }
            case 3 -> {
                triggerAnim("controller", "star_phantom.slice");
            }

        }
        handleAttacks();
    }

    private void handleAttacks() {
        switch (attack){
            case 1 -> {
                EinMinAttacks.fly(this);
            }
            case 2 -> {
                EinMinAttacks.releaseStars(this);
            }
            case 3 -> {
                EinMinAttacks.sliceAndPurge(this);
            }

        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return getIsTeleporting() || super.isInvulnerableTo(source);
    }

    private void teleportNearby() {
        spawnTpParticles();
        setIsTeleporting(false);
        remainingAttackTime = attackTimers[attack] > 0 ? attackTimers[attack] : 4 * 20;
        beginAnimation();
        for (ServerPlayer player : level().getServer().getPlayerList().getPlayers())
            player.playNotifySound(ModSounds.STAR_PHANTOM_TP, SoundSource.MASTER, 1.0f, 1.0f);

    }

    public void faceTarget() {
        if (getTarget() == null) return;

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

    @Override
    public void die(DamageSource damageSource) {
        if (!spawnednext && !level().isClientSide) {
            spawnednext = true;
            Messier messier = new Messier(ModEntities.MESSIER, level());
            messier.setPos(position().add(0.0f, 50.0f, 0.0f));
            level().addFreshEntity(messier);
            for (ServerPlayer player : level().getServer().getPlayerList().getPlayers()) {
                player.playNotifySound(ModSounds.MESSIER_SPAWN, SoundSource.MASTER, 1.0f, 1.0f);
            }
        }
        super.die(damageSource);
    }

    private void spawnTpParticles() {
        for (Player player : level ().players())
            ServerPlayNetworking.send((ServerPlayer) player, new PhantomTPPartS2C(position().toVector3f()));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenLoop("star_phantom.sleep"));
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<StarPhantom> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("star_phantom.spin_and_attack", RawAnimation.begin().thenPlay("star_phantom.spin_and_attack"));
        controller.triggerableAnim("star_phantom.fly", RawAnimation.begin().thenLoop("star_phantom.fly"));
        controller.triggerableAnim("star_phantom.slice", RawAnimation.begin().thenPlay("star_phantom.slice"));
        controller.triggerableAnim("star_phantom.sleep", RawAnimation.begin().thenPlay("star_phantom.sleep"));

        controllers.add(controller);
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }

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

    public void setIsTeleporting(boolean wannaTeleport) {
        this.entityData.set(IS_TELEPORTING, wannaTeleport);
    }

    public boolean getIsTeleporting() {
        return this.entityData.get(IS_TELEPORTING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_TELEPORTING, false);
    }

    public void resetStarCharges() {
        this.starCharges = MAX_STARS;
    }

    public void setReallyImportantTickCount(int reallyImportantTickCount) {
        this.reallyImportantTickCount = reallyImportantTickCount;
    }
}
