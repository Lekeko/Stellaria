package com.keko.stellaria.helper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Trail {

    private int stripLen;
    private final Map<UUID, ArrayList<Vec3>> entityPositions;
    private Entity entity;

    public Trail(int stripLen, Entity entity){
        this.stripLen = stripLen;
        this.entityPositions = new HashMap<>();
        this.entity = entity;
    }

    public Map<UUID, ArrayList<Vec3>> getEntityPositions() {
        return entityPositions;
    }

    public void update(float partialTick, Entity entity) {
        if (entity == null) return;

        ArrayList<Vec3> positions = entityPositions.computeIfAbsent(entity.getUUID(), k -> new ArrayList<>());
        Vec3 currentTickPos = entity.getPosition(partialTick);

        positions.addFirst(currentTickPos);

        if (positions.size() > stripLen) {
            positions.removeLast();
        }
    }


}
