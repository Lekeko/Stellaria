package com.keko.stellaria.stellarEvent.artha;

import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class StellarEvent {
    private int wave = 0;
    private int remainingEnemies = 0;
    private float enemiesChance[];
    private int enemiesPerWave[] = {15, 25, 35};
    private float enemiesChanceTotal[][] = {
            {0.8f, 0.15f, 0.05f},
            {0.6f, 0.25f, 0.15f},
            {0.4f, 0.35f, 0.25f}
    };
    private float clientTicker = 0;

    public float getClientTicker() {
        return clientTicker;
    }

    public void addClientTicker(float clientTicker) {
        this.clientTicker += clientTicker;
    }

    List<LivingEntity> waveEntities = new ArrayList<>();


    public float[] getEnemiesChance() {
        return enemiesChance;
    }

    private int intermision = 0;
    private int enemiesSpawnTimer = 0;
    private final int DEFAULT_SPAWN_TIMER = 20 * 12;

    public int getIntermision() {
        return intermision;
    }

    public void decreaseIntermision() {
        this.intermision -= 1;
    }

    public int getEnemiesSpawnTimer() {
        return enemiesSpawnTimer;
    }

    public void decreaseEnemiesSpawnTimer() {
        this.enemiesSpawnTimer -= 1;
    }

    public void setEnemiesSpawnTimer(int enemiesSpawnTimer) {
        this.enemiesSpawnTimer = enemiesSpawnTimer;
    }

    public int getRemainingEnemies() {
        return remainingEnemies;
    }

    public void setRemainingEnemies(int remainingEnemies) {
        this.remainingEnemies = remainingEnemies;
    }

    public int getWave() {
        return wave;
    }

    public StellarEvent(){
        wave = 1;
        remainingEnemies = enemiesPerWave[wave-1];
        enemiesChance = enemiesChanceTotal[wave-1];
        setEnemiesSpawnTimer(DEFAULT_SPAWN_TIMER);
        clientTicker = 0;
    }

    public int getDEFAULT_SPAWN_TIMER() {
        return DEFAULT_SPAWN_TIMER;
    }

    public boolean hasEnemiesToSpawn() {
        return remainingEnemies > 0;
    }

    public List<LivingEntity> getWaveEntities() {
        return waveEntities;
    }

    public void advanceWave() {
        if (wave >= 4) {
            wave = 0;
            return;
        }
        wave++;
        if (wave == 4){
            intermision = 10 * 20;
            return;
        }

        enemiesChance = enemiesChanceTotal[wave-1];
        remainingEnemies = enemiesPerWave[wave-1];
        intermision = 10 * 20;
    }

    public void clear() {
        wave = 0;
        for (LivingEntity livingEntity : waveEntities){
            livingEntity.discard();
        }
        waveEntities.clear();
    }

}
