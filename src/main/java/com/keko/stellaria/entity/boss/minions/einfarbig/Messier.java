package com.keko.stellaria.entity.boss.minions.einfarbig;

import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidLaser.VoidLaser;
import com.keko.stellaria.helper.MathHelpers;
import com.keko.stellaria.helper.UtilHelper;
import com.keko.stellaria.sounds.ModSounds;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.awt.Color;
import java.util.ArrayList;

public class Messier extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static PointLightData pointLightData;
    private static LightRenderHandle<PointLightData> handle;

    private static final int MAX_HP = 400;
    private float damageTicker = 0;

    private Player target = null;
    private int changeTargetTimer = 0;
    private final int MAX_TARGET_TIEMR = 15 * 20;

    //Attack vars
    private boolean startedAttack = false;
    private int actualAttackTimer = 0;
    private int attack = 0;
    private int attakTimers[] = {0, 20, 8};
    private int cd = 0;
    private final int MAX_CD = 40;

    private final float MELEE_DMG = 30.0f;

    //attacks sub vars
    private final int MAX_BEAMS = 8;
    private static final double ROT_SPEED = 0.5d;

    //Movement vars
    private double orbitAngle;
    private double speed = 1.0d;
    private boolean spawnednext = false;

    public Messier(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAtributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HP)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0);
    }

    @Override
    public @Nullable Player getTarget() {
        return target;
    }

    public float getDamageTicker() {
        return damageTicker;
    }

    public void decreaseDamageTicker() {
        this.damageTicker--;
    }

    public PointLightData getPointLightData() {
        return pointLightData;
    }

    public void setPointLightData(PointLightData pointLightData) {
        this.pointLightData = pointLightData;
    }

    public void removeLight() {
        if (handle != null) {
            handle.free();
            handle = null;
        }
    }

    public void addLight() {
        if (handle == null && !isRemoved()) {
            this.setPointLightData(new PointLightData()
                    .setBrightness(20.0f)
                    .setColor(1.0f, 1.0f, 1.0f)
                    .setRadius(30)
                    .setInscatteringStrength(444));
            handle = VeilRenderSystem.renderer().getLightRenderer().addLight(pointLightData);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (level().isClientSide)
            damageTicker = 10;
        return super.hurt(damageSource, f);
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

        if (pointLightData != null && handle != null) {
            Color colorWhite = new Color(255, 255, 255);
            Color colorRed = new Color(255, 0, 0);
            float t = getHealth() / getMaxHealth();

            Color curColor = MathHelpers.lerpColor(colorRed, colorWhite, t);

            handle.getLightData().setColor(curColor.getRed() / 255.0f, curColor.getGreen() / 255.0f, curColor.getBlue() / 255.0f);
            handle.getLightData().setPosition(position().x, position().y, position().z);
            if (isDeadOrDying()) removeLight();
        }
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

        if (target == null || --changeTargetTimer <= 0 || target.isDeadOrDying()) {
            target = UtilHelper.getPlayerTarget(this, -1);
            changeTargetTimer = MAX_TARGET_TIEMR;
            if (target == null) {
                return;
            }
        }

        if (isDeadOrDying())
            return;


        damageTargetNearby();

        this.hurtMarked = true;
        if (--cd > 0) return;

        if (!startedAttack) {
            evalAttack();
        } else {
            if (--actualAttackTimer <= 0) {
                switch (attack) {
                    case 1 -> {
                        EinMinAttacks.starBurst(this);
                    }
                    case 2 -> {
                        EinMinAttacks.beams(this);
                    }

                }

                attack = 0;
                startedAttack = false;
                cd = MAX_CD;
            }
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!spawnednext && !level().isClientSide) {
            spawnednext = true;
            Einfarbig einfarbig = new Einfarbig(ModEntities.EINFARBIG, level());
            einfarbig.setPos(position().add(0.0f, 5.0f, 0.0f));
            level().addFreshEntity(einfarbig);
            for (ServerPlayer player : level().getServer().getPlayerList().getPlayers()) {
                player.playNotifySound(ModSounds.EINIFARBIG_SPAWN, SoundSource.MASTER, 1.0f, 1.0f);
            }
        }
        super.die(damageSource);
    }


    private void evalAttack() {
        startedAttack = true;
        attack = random.nextIntBetweenInclusive(1, 2);
        actualAttackTimer = attakTimers[attack];

    }


    private void damageTargetNearby() {
        if (distanceTo(target) < 5.0f) {
            target.hurt(level().damageSources().mobAttack(this), MELEE_DMG);
        }
    }

    @Override
    public void onClientRemoval() {
        removeLight();
        super.onClientRemoval();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}