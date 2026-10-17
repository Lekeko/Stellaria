package com.keko.stellaria.entity.boss.minions.einfarbig;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar.MessierStar;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectile;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidLaser.VoidLaser;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class EinMinAttacks {
    //STAR PHANTOM

    public static void fly(StarPhantom starPhantom){
        Vec3 dir = starPhantom.getTarget().position().subtract(starPhantom.position()).normalize().multiply(1.0f,0.0f,1.0f);
        starPhantom.setFlyingDir(dir);

        starPhantom.faceTarget();

    }

    public static void releaseStars(StarPhantom starPhantom){
        starPhantom.resetStarCharges();
        starPhantom.setReallyImportantTickCount(starPhantom.tickCount);

    }

    public static void sliceAndPurge(StarPhantom starPhantom){
        starPhantom.setReallyImportantTickCount(starPhantom.tickCount);
    }

    //MESSIER

    public static void starBurst(Messier messier){
        for (int i = 0; i < 4; i++){
            MessierStar star = new MessierStar(ModProjectiles.MESSIER_STAR, messier.level());
            star.setPos(messier.position().add(Vec3Helper.getRandomVec3(messier.level().random, 4)).add(0.0f, 10.0f, 0.0f));
            star.setTarget(messier.getTarget());
            star.setOrder(5);
            messier.level().addFreshEntity(star);
        }
    }

    public static void beams(Messier messier){
        for (int i = 0; i < 5; i++){
            VoidLaser voidLaser = new VoidLaser(ModProjectiles.VOID_LASER, messier.level());
            voidLaser.setPos(messier.position().add(0.0f, messier.getBbHeight() / 2.0f, 0.0f));
            voidLaser.setOwner(messier);
            messier.level().addFreshEntity(voidLaser);
        }
        for (ServerPlayer player : messier.level().getServer().getPlayerList().getPlayers())
            player.playNotifySound(ModSounds.MESSIER_LASERS, SoundSource.MASTER, 1.0f, 1.0f);
    }

    public static void spinDash(Messier messier){

    }

    //VOID DEALER

    public static void ripFabric(VoidDealer voidDealer){
        if (voidDealer.getTarget() == null) return;
        Vec3 dir = voidDealer.getTarget().position().subtract(voidDealer.position());
        float speed = 8.2f;
        Vec3 vel = dir.normalize().scale(speed);

        voidDealer.setDeltaMovement(vel);
        voidDealer.hurtMarked = true;

    }

    public static void shootCards(VoidDealer voidDealer){
        if (voidDealer.getTarget() == null) return;
        int numberOfCards = 7 + voidDealer.getRandom().nextIntBetweenInclusive(2,4);
        Vec3 dir = voidDealer.getTarget().position().subtract(voidDealer.position());
        float speed = 8.2f;
        float spread = 1.5f;

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
        VoidLaser voidLaser = new VoidLaser(ModProjectiles.VOID_LASER, voidDealer.level());
        voidLaser.setPos(voidDealer.position().add(0.0f, voidDealer.getBbHeight() / 2.0f, 0.0f));
        voidLaser.setOwner(voidDealer);
        voidDealer.level().addFreshEntity(voidLaser);
        for (ServerPlayer player : voidDealer.level().getServer().getPlayerList().getPlayers())
            player.playNotifySound(ModSounds.VOID_DEALER_LASER, SoundSource.MASTER, 1.0f, 1.0f);
    }

}
