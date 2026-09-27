package com.keko.stellaria.helper;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Vec3Helper {

    public static double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(value, max));
    }


    public static Vec3 clamp(Vec3 vec3, double xm, double xM, double ym, double yM, double zm, double zM){
        double x = clamp(xm, xM, vec3.x);
        double y = clamp(ym, yM, vec3.y);
        double z = clamp(zm, zM, vec3.z);

        return new Vec3(x,y,z);
    }

    public static Vec3 clampGeneral(Vec3 vec3, double min, double max){
        return clamp(vec3, min, max, min, max, min, max);
    }

    public static double ETEdistance(Entity e1, Entity e2){
        return Math.sqrt(Math.pow(e1.getX() - e2.getX(), 2) + Math.pow(e1.getY() - e2.getY(), 2) + Math.pow(e1.getZ() - e2.getZ(), 2));
    }
    public static double ETPdistance(Entity e, Vec3 p){
        return Math.sqrt(Math.pow(e.getX() - p.x(), 2) + Math.pow(e.getY() - p.y(), 2) + Math.pow(e.getZ() - p.z(), 2));
    }

    public static double getSpeedOnVel(Vec3 velocity){
        return Mth.sqrt((float) (velocity.x * velocity.x + velocity.y * velocity.y + velocity.z * velocity.z));
    }


    public static Vec3 getRandomVec3(RandomSource random, float spread) {
        return new Vec3(
                (random.nextFloat() - .5f) * spread,
                (random.nextFloat() - .5f) * spread,
                (random.nextFloat() - .5f) * spread
        );
    }
}
