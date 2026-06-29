package com.keko.stellaria.entity.decour.zregosShatter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class ZregosShatter extends Entity {
    private final int LIFESPAN = (int) (20 * 5.4);
    private float size = 0;
    private final int CRACK_NUMBER = 12;
    ArrayList<Vec3> crackList = new ArrayList<>();


    public ZregosShatter(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public int getLIFESPAN() {
        return LIFESPAN;
    }


    @Override
    public void tick() {
        if (level().isClientSide && crackList.isEmpty())
            initCracks(); //this hsit sound really wrong also im sorry i dont wanna copy rat but i need a space time thingy that breaks reality

        if (tickCount >= LIFESPAN && !level().isClientSide())
            discard();
        super.tick();
    }

    public ArrayList<Vec3> getCrackList() {
        return crackList;
    }

    private void initCracks() {
        for (int i = 0; i < CRACK_NUMBER; i++)
            crackList.add(new Vec3(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1));
    }

    public float getSize() {
        return size;
    }

    public void addSize(float size) {
        this.size += size;
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
