package com.keko.stellaria.stellarEvent.ebony;

import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.entity.boss.einfarbig.EinfarbigStatue;
import com.keko.stellaria.helper.SpaceHelpers;
import com.keko.stellaria.helper.Vec3Helper;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.logging.Logger;

public class EbonyDimensionHandler {
    private static EinfarbigStatue einferbigStatue;
    private static boolean eventStarted = false;
    private static Vec3 posHeightOffset = new Vec3(0.0d, 40.0d, 0.0d);;


    public static void tick(Level level){
//        checkForForgottenStatue(level);
        checkForStatue(level);
    }

    public static Vec3 getOriginPos(Level level) {
        Vec3 pos = new Vec3(0.0d, level.getHeight() - 3, 0.0d);
        while (level.getBlockState(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)).is(Blocks.AIR))
            pos = pos.add(0.0d, -1.0d, 0.0d);

        pos = pos.add(posHeightOffset);

        return pos;
    }

    public static void checkForForgottenStatue(Level level) {
        Vec3 originPos = getOriginPos(level);

        for (EinfarbigStatue einfarbigStatue : level.getEntitiesOfClass(EinfarbigStatue.class, SpaceHelpers.createEvenBox(originPos, 2))){
            einfarbigStatue.discard();
        }
    }

    public static void checkForStatue(Level level) {
        Vec3 originPos = getOriginPos(level);
        if (!eventStarted && einferbigStatue == null) {
            einferbigStatue = new EinfarbigStatue(ModEntities.EINFARBIG_STATUE, level);
            einferbigStatue.setPos(originPos);
            level.addFreshEntity(einferbigStatue);
        }
    }

    public static void remove(Level levelEbony) {
        if (einferbigStatue != null) {
            einferbigStatue.discard();
            einferbigStatue = null;
        }
    }
}
