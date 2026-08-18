package com.keko.stellaria.entity.boss.zregos;

import com.keko.stellaria.effects.ModStatusEffects;
import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderNormal;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderShattered;
import com.keko.stellaria.entity.decour.zregosMace.ZregosMace;
import com.keko.stellaria.entity.decour.zregosShatter.ZregosShatter;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosBouldersChains;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosGhost;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosProjectile;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public abstract class ZregosAttack {

    public static void followPlayer(Zregos zregos, Vec3 target){
        Vec3 currentPos = zregos.position();
        Vec3 desiredDir = target.subtract(currentPos).subtract(0,1,0).normalize();
        double speed = 0.45f;

        if (zregos.getAttack() == 3){


            zregos.laserDirection = desiredDir;
            zregos.setDeltaMovement(zregos.getDeltaMovement().lerp(zregos.laserDirection.scale(1.8), 1));
            for (Player player : zregos.level().players()){
                if (Vec3Helper.ETEdistance(zregos, player) < 10){
                    player.addDeltaMovement(player.getPosition(1.0f).subtract(zregos.getPosition(1.0f)).normalize().scale(0.2d));
                    player.hurtMarked = true;
                }
            }

            return;
        }

        zregos.addDeltaMovement(target.subtract(zregos.position()).normalize().multiply(speed, speed/2.0d, speed));
    }



    public static void ghostDash(Zregos zregos, Player target, int dash) {
        int nrOGhosts = 4 + dash;
        Vec3 center = target.getPosition(1.0f).add(target.getDeltaMovement().scale(4.0f));

        for (int i = 0; i <= nrOGhosts; i++){
            float angle = (float) i /nrOGhosts * 360.0f;

            ZregosGhost zregosGhost = new ZregosGhost(ModProjectiles.ZREGOS_GHOST, zregos.level());
            zregosGhost.setRotation(angle);
            zregosGhost.setOwner(zregos);
            zregosGhost.setPos(center.add(0.0f, target.getDeltaMovement().y / 2.0f, 0.0f));

            if (i == 0) zregosGhost.setDamager(true);

            zregos.level().addFreshEntity(zregosGhost);
        }
    }

    public static void glideAndShoot(Zregos zregos, Player target) {
        float projectileSpeed = 8;

        for (int i = 0; i < 20; i++){

            ZregosProjectile zregosProjectile = new ZregosProjectile(ModProjectiles.ZREGOS_PROJECTILE, zregos.level());
            zregosProjectile.setPos(zregos.getPosition(1.0f));
            zregosProjectile.setDeltaMovement((zregos.getRandom().nextFloat() - .5f) * projectileSpeed,(zregos.getRandom().nextFloat() - .5f) * projectileSpeed,(zregos.getRandom().nextFloat() - .5f) * projectileSpeed);
            zregosProjectile.setTarget(target);
            zregosProjectile.setOwner(zregos);
            zregosProjectile.setDelay((int) (i * 1.5f));
            zregos.level().addFreshEntity(zregosProjectile);

        }

        ZregosShatter zregosShatter = new ZregosShatter(ModEntities.ZREGOS_SHATTER, zregos.level());
        zregosShatter.setDeltaMovement(zregos.getDeltaMovement().normalize());
        zregosShatter.setPos(zregos.getPosition(1.0f));
        zregos.level().addFreshEntity(zregosShatter);
        zregos.playSound(ModSounds.ZREGOS_SHATTER, 11110.0f, 1.0f);
    }


    public static void spawnBoulders(Zregos zregos, Player target){
        int baseBoulders = 3;
        float progress = 1.0f - zregos.getHealth()/zregos.getMaxHealth();
        int boulderMult = 2 * zregos.level().players().size();

        int nrOBoulders = (int) (baseBoulders + progress * boulderMult);
        float radius = 10 * nrOBoulders;

        Vec3 center = target.getPosition(1.0f);
        zregos.playSound(ModSounds.ZREGOS_BOULDERS_SUMMON, 1000.0f, 1.0f);

        for (int i = 0; i < nrOBoulders; i++){
            double angle = (2 * Math.PI * i) / nrOBoulders;

            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            Entity boulder;

            if (zregos.getHealth()/zregos.getMaxHealth() > 0.45f)
                boulder = new ZregosBoulderNormal(ModEntities.ZREGOS_BOULDER_NORMAL, zregos.level());
            else
                boulder = new ZregosBoulderShattered(ModEntities.ZREGOS_BOULDER_SHATTERED, zregos.level());


            boulder.setPos(x, center.y, z);
            zregos.level().addFreshEntity(boulder);
        }

    }

    public static void punish(Entity boulder, Player target) {
        target.addEffect(new MobEffectInstance(ModStatusEffects.CHAINED, 3 * 20, 1));
        int chains = 3;

        target.level().playSound(null, target.getOnPos(), SoundEvents.CHAIN_BREAK, SoundSource.HOSTILE, 10, 1.0f);
        for (int i = 0; i < chains; i++){
            ZregosBouldersChains zregosBouldersChains = new ZregosBouldersChains(ModProjectiles.ZREGOS_BOULDERS_CHAINS, boulder.level());
            zregosBouldersChains.setRotationX((boulder.getRandom().nextFloat() - .5f) * 2 * 360);
            zregosBouldersChains.setRotationY((boulder.getRandom().nextFloat() - .5f) * 2 * 360);
            zregosBouldersChains.setTarget(target);
            zregosBouldersChains.setPos(target.getPosition(1.0f));

            boulder.level().addFreshEntity(zregosBouldersChains);
        }
    }



    public static void finalAttack(Zregos zregos, Player target) {

    }

    public static void pierce(Zregos zregos, Player target) {
        if (zregos.getDashDirection() == null) {
            Vec3 targetPredictedPos = target.getPosition(1.0f).add(target.getDeltaMovement());
            Vec3 direction = targetPredictedPos.subtract(zregos.getPosition(1.0f)).normalize();
            double dashSize = Vec3Helper.ETEdistance(zregos, target);

            dashSize /= 5;
            dashSize = Mth.clamp(dashSize, 0.0f, 10.0f);
            zregos.setDashDirection(direction.scale(dashSize));

        }


        zregos.setDeltaMovement(zregos.getDashDirection());
        zregos.hurtMarked = true;

        zregos.makeIntangibleFor(1.4f);

    }

    public static void maceAttack(Zregos zregos) {


        int maceNr = 3 + zregos.level().random.nextIntBetweenInclusive(1,3);
        for (int i = 0 ; i < maceNr; i++){
            ZregosMace zregosMace = new ZregosMace(ModEntities.ZREGOS_MACE, zregos.level());
            zregosMace.setPos(zregos.position());
            float speed = 1;
            Vec3 vel = new Vec3(
                    zregos.level().random.nextFloat() - .5f,
                    zregos.level().random.nextFloat() - .5f,
                    zregos.level().random.nextFloat() - .5f
            ).scale(speed);

            zregosMace.setDeltaMovement(vel);
            zregosMace.setOwner(zregos);

            zregos.level().addFreshEntity(zregosMace);
        }

    }

    public static void glideAndDash(Zregos zregos, Player target) {
        zregos.setDeltaMovement(Vec3.ZERO);
        ((ServerLevel)zregos.level()).sendParticles((ServerPlayer) target, ParticleTypes.END_ROD,true, zregos.position().x, zregos.position().y, zregos.position().z, 51,1,1,1, 7);
        zregos.setDeltaMovement(
                (target.position()).subtract(zregos.position()).normalize().scale(Mth.clamp(zregos.distanceTo(target), 2.0f, 20.0f))
        );
    }
}
