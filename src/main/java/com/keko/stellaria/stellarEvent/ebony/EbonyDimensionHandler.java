package com.keko.stellaria.stellarEvent.ebony;

import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.boss.einfarbig.EinfarbigStatue;
import net.minecraft.world.level.Level;

public class EbonyDimensionHandler {
    private static EinfarbigStatue einferbigStatue;
    private static boolean eventStarted = false;

    public static void tick(Level level){
        checkForStatue(level);
    }

    private static void checkForStatue(Level level) {
        if (!eventStarted && einferbigStatue == null){
            einferbigStatue = new EinfarbigStatue(ModEntities.EINFARBIG_STATUE, level);
            einferbigStatue.setPos(0, 2000, 500);
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
