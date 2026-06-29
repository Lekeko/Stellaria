package com.keko.stellaria.packet.networking;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.minions.zregos.ZregosServant;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.packet.*;
import com.keko.stellaria.renderHelpers.ZregosBite;
import com.keko.stellaria.screenVfx.screenShake.ScreenShakeHandler;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.keko.stellaria.sounds.ModSounds;
import com.keko.stellaria.stellarEvent.ArthaDimensionHandler;
import com.keko.stellaria.stellarEvent.ArthaHudHandler;
import com.keko.stellaria.stellarEvent.StellariaSkyboxTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class ModMessagesServer {


    public static void registerS2CPacket(){
        ClientPlayNetworking.registerGlobalReceiver(AlertPlayerZregosBiteS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                ZregosBite.setActive(true);
                context.client().player.playSound(SoundEvents.AMETHYST_CLUSTER_BREAK, 10, 1.5f);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ZServPartS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                ZregosServant zregosServant = (ZregosServant) context.client().level.getEntity(payload.id());
                ZregosServant.spawnParticle(zregosServant,Stellaria.id("star_burst"));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(RenderZregosLaserS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                try {
                    Zregos zregos = (Zregos) context.client().level.getEntity(payload.id());
                    if (payload.amp() != 0){
                        zregos.setLaserScreenAmp(zregos.getLaserScreenAmp() + Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 10f * payload.amp());
                        return;
                    }
                    if (payload.modifySky())
                        zregos.wantsToModifSky = true;
                    else {
                        if (!payload.active())
                            zregos.wantsToModifSky = false;
                        zregos.wantsToLaser = payload.active();
                    }
                }catch (Exception e) {
                    System.out.println("Couldn't find zregos to render laser");
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayZregosLaserSoundC2S.ID, (payload, context) -> {
            context.client().execute(() -> {
                context.client().player.playSound(ModSounds.ZREGOS_LASER, 10, 1.5f);

            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ShakeLaserS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                ScreenShakeHandler.beginShake(payload.amp(), 4);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ZregosMinionsSkyboxS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                ArthaDimensionHandler.setAmp(0.0f);
                ArthaDimensionHandler.setColor(payload.color());
                ArthaDimensionHandler.setClientSkyType(StellariaSkyboxTypes.MINIONS);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(IdleSkyboxS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                ArthaDimensionHandler.setClientSkyType(StellariaSkyboxTypes.IDLE);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BlackHoleShaderS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                ArthaDimensionHandler.setTranzProg(payload.tranzProg());
                ArthaDimensionHandler.setSphereRadius(payload.sphereRadius());
                ArthaDimensionHandler.setSpherePos(new Vec3(payload.x(), payload.y(), payload.z()));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(StellarEventHudSwitchS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                ArthaHudHandler.setAlive(payload.alive());
                ArthaHudHandler.setIntermision(payload.intermision());
                ArthaHudHandler.setToSpawn(payload.toSpawn());
                ArthaHudHandler.setWave(payload.wave());
                switch (payload.onOffIdle()){
                    case 0 -> ArthaHudHandler.beginTicker();
                    case 1 -> ArthaHudHandler.endTicker();
                }

            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BeginEndAnimS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                System.out.println("TIME TO GO");
                ArthaHudHandler.setTimeToGo(true);
            });
        });
    }
}
