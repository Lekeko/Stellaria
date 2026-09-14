package com.keko.stellaria.vfx.voidVfx;

import com.keko.stellaria.Stellaria;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.data.module.init.LightmapParticleModuleData;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class VoidVfxHandler {
    static int tick = 0;
    
    public static void tick(){
        tick++;
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null)
            if (tick % 160 == 0 && Minecraft.getInstance().player.position().y < -60 && !Minecraft.getInstance().isPaused())
                spawnParticle(Minecraft.getInstance().player.position());

        renderShader();
    }

    private static void renderShader() {
    }

    public static void spawnParticle(Vec3 position){
        try {
            ResourceLocation id = Stellaria.id("big_spark");
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setPosition(position);
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
