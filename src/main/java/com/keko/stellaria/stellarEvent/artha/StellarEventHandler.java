package com.keko.stellaria.stellarEvent.artha;

import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.boss.minions.zregos.CosmicSpider;
import com.keko.stellaria.entity.boss.minions.zregos.Crystalius;
import com.keko.stellaria.entity.boss.minions.zregos.ZregosServant;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.entity.decour.zregosShatter.SpawnShatter;
import com.keko.stellaria.packet.BeginEndAnimS2C;
import com.keko.stellaria.packet.SendEnemySpawnTimerC2S;
import com.keko.stellaria.packet.StellarEventHudSwitchS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class StellarEventHandler {

    public static StellarEvent stellarEvent = null;
    public static LivingEntity boss = null;
    public static UUID bossUUID = null;
    public static boolean bossWasAlive = false;

    public static StellarEvent getStellarEventInstance() {
        return stellarEvent;
    }

    public static void initiateStellarEvent(Level level){
        if (stellarEvent != null)
            stellarEvent.clear();
        stellarEvent = new StellarEvent();
        if (boss != null) {
            boss.discard();
            boss = null;
        }

        for (Player player : level.players())
            ServerPlayNetworking.send((ServerPlayer) player, new StellarEventHudSwitchS2C(
                    0,
                    0,0,0,0

            ));
    }

    public static void cancelEvent(Level level){
        if (stellarEvent != null) {
            stellarEvent.clear();
            stellarEvent = null;
        }
        for (Player player : level.players())
            ServerPlayNetworking.send((ServerPlayer) player, new StellarEventHudSwitchS2C(
                    1,
                    0,0,0,0

            ));
    }

    public static void tick(Level level){
        if (!level.isClientSide){

            for (Player player : level.players())
                ServerPlayNetworking.send((ServerPlayer) player, new StellarEventHudSwitchS2C(
                        2,
                        stellarEvent.getWave(),
                        stellarEvent.getWaveEntities().size(),
                        stellarEvent.getRemainingEnemies(),
                        stellarEvent.getIntermision()

                ));

            if (stellarEvent.getWave() > 0){
                stellarEvent.getWaveEntities().removeIf(LivingEntity::isDeadOrDying);

                if (stellarEvent.getWave() == 4) {
                    if (bossUUID == null) {
                        spawnZregos(level);
                    }
                }


                if (stellarEvent.getIntermision() > 0) {
                    stellarEvent.decreaseIntermision();
                    return;
                }

                if (stellarEvent.getEnemiesSpawnTimer() > 0) {
                    stellarEvent.decreaseEnemiesSpawnTimer();
                    return;
                }

                if (stellarEvent.hasEnemiesToSpawn()) {
                    spawnEnemies(level);
                } else {
                    if (stellarEvent.getWaveEntities().isEmpty())
                        stellarEvent.advanceWave();
                }

                for (Player player : level.players()){
                    ServerPlayNetworking.send((ServerPlayer) player, new SendEnemySpawnTimerC2S(stellarEvent.getEnemiesSpawnTimer() / 20));
                }
            }
        }
    }

    private static void celebrateOrSmth(Level level) {
        System.out.println("wooow good job");
        for (Player player : level.players())
            ServerPlayNetworking.send((ServerPlayer) player, new BeginEndAnimS2C(0));
    }

    public static void onBossDied(ServerLevel level) {
        boss = null;
        bossUUID = null;
        bossWasAlive = false;
        celebrateOrSmth(level);
    }


    private static void spawnZregos(Level level) {
        Zregos zregos = new Zregos(ModEntities.ZREGOS, level);

        Vec3 spawnPosition = level.players().get(level.random.nextIntBetweenInclusive(0, level.players().size() - 1)).position();

        float dispersion = 100.0f;
        float yElevation = 5.0f;
        spawnPosition = spawnPosition.add(
                (level.random.nextFloat() - 0.5f) * dispersion + 30,
                (level.random.nextFloat() - 0.5f) * yElevation,
                (level.random.nextFloat() - 0.5f) * dispersion + 30
        );

        zregos.setPos(spawnPosition);

        SpawnShatter spawnShatter = new SpawnShatter(ModEntities.SPAWN_SHATTER, level);
        spawnShatter.setPos(spawnPosition);

        boss = zregos;
        ServerLevel serverLevel = (ServerLevel) level;
        zregos.finalizeSpawn(
                serverLevel,
                serverLevel.getCurrentDifficultyAt(zregos.blockPosition()),
                MobSpawnType.MOB_SUMMONED,
                null
        );
        level.addFreshEntity(zregos);
        level.addFreshEntity(spawnShatter);

        bossUUID = zregos.getUUID();
        boss = zregos;
        bossWasAlive = false;
    }


    private static void spawnEnemies(Level level) {
        int maxEnemies = 5;
        int minEnemies = 2;

        int nrOfEnemies = level.random.nextIntBetweenInclusive(minEnemies, maxEnemies);
        if (nrOfEnemies > stellarEvent.getRemainingEnemies())
            nrOfEnemies = stellarEvent.getRemainingEnemies();

        Vec3 spawnPosition = level.players().get(level.random.nextIntBetweenInclusive(0, level.players().size() - 1)).position();

        float dispersion = 80.0f;
        float yElevation = 5.0f;
        spawnPosition = spawnPosition.add(
                (level.random.nextFloat() - 0.5f) * dispersion + 30,
                (level.random.nextFloat() - 0.5f) * yElevation,
                (level.random.nextFloat() - 0.5f) * dispersion + 30
        );

        for (int i = 0; i < nrOfEnemies; i++){
            LivingEntity livingEntity = getRandomEnemy(level);
            float dispersionS = 20.0f;

            Vec3 spawnOffset = new Vec3(
                    (level.random.nextFloat() - 0.5f) * dispersionS,
                    (level.random.nextFloat() - 0.5f) * dispersionS,
                    (level.random.nextFloat() - 0.5f) * dispersionS
            );

            livingEntity.setPos(spawnPosition.add(spawnOffset));

            SpawnShatter spawnShatter = new SpawnShatter(ModEntities.SPAWN_SHATTER, level);
            spawnShatter.setPos(spawnPosition.add(spawnOffset));


            level.addFreshEntity(livingEntity);
            level.addFreshEntity(spawnShatter);

            stellarEvent.getWaveEntities().add(livingEntity);
        }
        stellarEvent.setEnemiesSpawnTimer(stellarEvent.getDEFAULT_SPAWN_TIMER() + level.random.nextIntBetweenInclusive(10, 20) - (maxEnemies - nrOfEnemies) * 10);//random bullshit go
        stellarEvent.setRemainingEnemies(stellarEvent.getRemainingEnemies() - nrOfEnemies);
    }

    private static LivingEntity getRandomEnemy(Level level) {
        float[] chances = stellarEvent.getEnemiesChance();
        float roll = level.random.nextFloat();

        int selectedIndex = chances.length - 1;
        float cumulative = 0.0f;

        for (int i = 0; i < chances.length; i++) {
            cumulative += chances[i];
            if (roll < cumulative) {
                selectedIndex = i;
                break;
            }
        }

        return switch (selectedIndex) {
            case 0 -> new Crystalius(ModEntities.CRYSTALIUS,level);
            case 1 -> new CosmicSpider(ModEntities.COSMIC_SPIDER,level);
            case 2 -> new ZregosServant(ModEntities.ZREGOS_SERVANT,level);
            default -> throw new IllegalStateException("No enemy for index: " + selectedIndex);
        };
    }

    public static LivingEntity getBoss() {
        return boss;
    }
}
