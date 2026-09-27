package com.keko.stellaria.helper;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class UtilHelper {

    public static Player getClosestPlayer(Entity source){
        Player player = null;
        double distance = Float.MAX_VALUE;
        for (Player player1 : source.level().players()){
            if (Vec3Helper.ETEdistance(source, player1) < distance){
                player = player1;
                distance = Vec3Helper.ETEdistance(source, player1);
            }
        }

        return player;
    }

    public static Color lerpColor(Color a, Color b, float t)
    {
        return new Color(
                (int) Mth.lerp(t, a.getRed(), b.getRed()),
                (int) Mth.lerp(t, a.getGreen(), b.getGreen()),
                (int) Mth.lerp(t, a.getBlue(), b.getBlue()),
                (int) Mth.lerp(t, a.getAlpha(), b.getAlpha())
        );
    }

    public static Player getPlayerTarget(Entity enemy, float maxDist){
        Player target = null;
        float finalDist = maxDist;
        if (maxDist < 0) finalDist = Float.MAX_VALUE;

        for (Player player : enemy.level().players()){
            float curDist = player.distanceTo(enemy);
            if (curDist < finalDist){
                target = player;
                finalDist = curDist;
            }
        }

        return target;
    }
}
