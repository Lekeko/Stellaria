package com.keko.stellaria.stellarEvent;

import com.keko.stellaria.packet.BlackHoleShaderS2C;
import com.keko.stellaria.packet.IdleSkyboxS2C;
import com.keko.stellaria.packet.ZregosMinionsSkyboxS2C;
import com.keko.stellaria.shader.ClientShaderHandler;
import foundry.veil.api.client.util.Easing;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArthaDimensionHandler {


    public static void tick(Level level){
        Vec3 spherePos = new Vec3(200.5, 2000.0, 200.5);
        float sphereRadius = 30.0f;
        float maxDistFromCenter = 25.0f;
        float minDistFromCenter = 40.0f;
        float tranzitionLen = minDistFromCenter - maxDistFromCenter;


        for (Player player : level.players()){
            if (StellarEventHandler.getStellarEventInstance() == null) {
                ServerPlayNetworking.send((ServerPlayer) player, new IdleSkyboxS2C(0));

                float distance = Mth.sqrt((float) player.distanceToSqr(spherePos));
                if (distance < maxDistFromCenter){
                    StellarEventHandler.initiateStellarEvent(level);
                    //TODO Client Side to flash the player
                }else {
                    float distToMin = Mth.clamp(distance - maxDistFromCenter, 0.0f, tranzitionLen);
                    float tranzProg = 1.0f - distToMin/tranzitionLen;
                    tranzProg = Easing.EASE_OUT_CUBIC.ease(tranzProg);
                    ServerPlayNetworking.send((ServerPlayer) player, new BlackHoleShaderS2C(tranzProg, sphereRadius, spherePos.x, spherePos.y, spherePos.z));
                }

            }else {

                if (StellarEventHandler.getStellarEventInstance().getWave() < 4) {
                    ServerPlayNetworking.send((ServerPlayer) player, new ZregosMinionsSkyboxS2C(StellarEventHandler.getStellarEventInstance().getWave()/3.0f));
                }else {
                    if (StellarEventHandler.getBoss() != null && StellarEventHandler.getBoss().isDeadOrDying()){
                        //TODO Death anim for zregos death
                        StellarEventHandler.cancelEvent(level);
                    }
                }
            }
        }

        if (StellarEventHandler.getStellarEventInstance() != null){
            StellarEventHandler.tick(level);
        }
    }


    //Some client side shit

    private static StellariaSkyboxTypes clientSkyType = StellariaSkyboxTypes.IDLE;
    private static float amp = 0;
    private static float color = 0;
    private static float tranzProg = 0;
    private static float sphereRadius = 0;
    private static Vec3 spherePos = Vec3.ZERO;

    public static void setAmp(float amp) {
        ArthaDimensionHandler.amp = amp;
    }

    public static void setTranzProg(float tranzProg) {
        ArthaDimensionHandler.tranzProg = tranzProg;
    }

    public static void setSpherePos(Vec3 spherePos) {
        ArthaDimensionHandler.spherePos = spherePos;
    }

    public static void setSphereRadius(float sphereRadius) {
        ArthaDimensionHandler.sphereRadius = sphereRadius;
    }

    public static void setClientSkyType(StellariaSkyboxTypes clientSkyType) {
        ArthaDimensionHandler.clientSkyType = clientSkyType;
    }

    public static void setColor(float color) {
        ArthaDimensionHandler.color = color;
    }

    public static void handleSkybox() {
        switch (clientSkyType){
            case IDLE -> {
                ClientShaderHandler.renderIdleSky();
                ClientShaderHandler.renderBlackHole(tranzProg, sphereRadius, spherePos);
            }
            case MINIONS -> ClientShaderHandler.renderZregosMinionsSkybox(amp, color);
           // case BOSS -> int sixseven;
        }

    }

}
