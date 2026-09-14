package com.keko.stellaria.vfx.ebony;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.keko.stellaria.stellarEvent.StellariaSkyboxTypes;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.data.module.init.LightmapParticleModuleData;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EbonySkyboxAndVfxHandler {
    private static boolean enemiesActive = false;
    private static boolean bossActive = false;
    private static int bossPhase = 0;

    private static StellariaSkyboxTypes clientSkyType = StellariaSkyboxTypes.IDLE_EBONY;

    public static void renderTick(){
        if (Minecraft.getInstance().player == null) return;
        Player player = Minecraft.getInstance().player;

        if (!enemiesActive){
            if (bossActive)
                switch (bossPhase){
                    case 1 -> ClientShaderHandler.renderEiniphase1Skybox();
                    case 2 -> ClientShaderHandler.renderEiniphase2Skybox();
                }
            else {
                if (player.tickCount % 80 == 0 && !Minecraft.getInstance().isPaused())
                    spawnParticle(Minecraft.getInstance().player.position());
                ClientShaderHandler.renderEbonyidleSkybox();
            }
        }else {
            ClientShaderHandler.renderEbonyEnemiesSkybox();
        }
    }

    public static int getBossPhase() {
        return bossPhase;
    }

    public static StellariaSkyboxTypes getClientSkyType() {
        return clientSkyType;
    }

    public static boolean isBossActive() {
        return bossActive;
    }

    public static boolean isEnemiesActive() {
        return enemiesActive;
    }

    public static void setBossActive(boolean bossActive) {
        EbonySkyboxAndVfxHandler.bossActive = bossActive;
    }

    public static void setBossPhase(int bossPhase) {
        EbonySkyboxAndVfxHandler.bossPhase = bossPhase;
    }

    public static void setClientSkyType(StellariaSkyboxTypes clientSkyType) {
        EbonySkyboxAndVfxHandler.clientSkyType = clientSkyType;
    }

    public static void setEnemiesActive(boolean enemiesActive) {
        EbonySkyboxAndVfxHandler.enemiesActive = enemiesActive;
    }

    public static void spawnParticle(Vec3 position){
        try {
            ResourceLocation id = Stellaria.id("big_spark");
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setPosition(position);
            emitter.setCount(1);
            emitter.addModule(new LightmapParticleModuleData(LightTexture.FULL_BRIGHT));
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }


        try {
            ResourceLocation id = Stellaria.id("star_spark");
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setPosition(position);
            emitter.addModule(new LightmapParticleModuleData(LightTexture.FULL_BRIGHT));
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }
}
