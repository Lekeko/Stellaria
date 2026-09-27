package com.keko.stellaria.entity.boss.minions;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.boss.minions.einfarbig.Messier;
import com.keko.stellaria.entity.boss.minions.einfarbig.StarPhantom;
import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectile;
import com.keko.stellaria.helper.Vec3Helper;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class EinMinAttacks {
    //STAR PHANTOM

    public static void repulsePlayers(StarPhantom starPhantom){

    }

    public static void releaseStars(StarPhantom starPhantom, float multiplier){

    }

    public static void sliceAndPurge(StarPhantom starPhantom){

    }

    //MESSIER

    public static void starBurst(Messier messier){

    }

    public static void beams(Messier messier, ArrayList<Vec3> beamsList){

    }

    public static void spinDash(Messier messier){

    }

    //VOID DEALER

    public static void ripFabric(VoidDealer voidDealer){
        if (voidDealer.getTarget() == null) return;
        Vec3 dir = voidDealer.getTarget().position().subtract(voidDealer.position());
        float speed = 5.2f;
        Vec3 vel = dir.normalize().scale(speed);

        voidDealer.setDeltaMovement(vel);
        voidDealer.hurtMarked = true;

    }

    public static void shootCards(VoidDealer voidDealer){
        if (voidDealer.getTarget() == null) return;
        int numberOfCards = 7 + voidDealer.getRandom().nextIntBetweenInclusive(2,4);
        Vec3 dir = voidDealer.getTarget().position().subtract(voidDealer.position());
        float speed = 8.2f;
        float spread = 3.0f;

        Vec3 vel = dir.normalize().scale(speed);

        for (int i = 0; i < numberOfCards; i++){
            Vec3 randomOffset = Vec3Helper.getRandomVec3(voidDealer.getRandom(), spread);

            PlayingCardProjectile card = new PlayingCardProjectile(ModProjectiles.PLAYING_CARD_PROJECTILE, voidDealer.level());
            card.setPos(voidDealer.position());
            card.setOwner(voidDealer);
            card.setDeltaMovement(vel.add(randomOffset));

            voidDealer.level().addFreshEntity(card);
        }

    }

    public static void shootLaser(VoidDealer voidDealer){
        if (voidDealer.getTarget() == null) return;
    }

}
