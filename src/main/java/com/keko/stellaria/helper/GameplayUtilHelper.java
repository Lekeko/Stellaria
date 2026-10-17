package com.keko.stellaria.helper;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GameplayUtilHelper {
    public static void createCustomExplosion(Level level, @Nullable Entity owner, Vec3 pos, float damage, float radius, SimpleParticleType particleType, int particleCount) {
        if (!(level instanceof ServerLevel serverLevel)) return;
;
        double radiusSqr = radius * radius;
        AABB box = SpaceHelpers.createEvenBox(pos, radius);

        for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, box)) {
            if (target == owner || !target.isAlive()) continue;
            if (target.position().add(0, target.getBbHeight() / 2, 0).distanceToSqr(pos) > radiusSqr) continue;

            target.hurt(serverLevel.damageSources().explosion(owner, owner), damage);

        }

        serverLevel.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0f, 1.0f);

        spawnExplosionParticles(serverLevel, pos, radius, particleType, particleCount);
    }

    public static void spawnExplosionParticles(ServerLevel level, Vec3 pos, float radius, SimpleParticleType particleType, int count) {
        double spread = radius / 2;
        for (ServerPlayer player : level.players()) {
            level.sendParticles(player, particleType, true, pos.x, pos.y, pos.z, count, spread, spread, spread, radius / 10.0f);
        }
    }
}
