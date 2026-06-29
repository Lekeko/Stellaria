package com.keko.stellaria.helper;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpaceHelpers {
    public static AABB createEvenBox(Vec3 pos, double size){
        return new AABB(
                pos.x + size/2,pos.y + size/2,pos.z + size/2,
                pos.x - size/2,pos.y - size/2,pos.z - size/2
                );
    }
}
