package com.keko.stellaria.particles;

import com.keko.stellaria.Stellaria;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.data.ParticleSettings;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3fc;

public class QuasarParticlesHandler {

    public static void renderZregosMinionsParticlesDeath(Entity entity){
        try {
            ResourceLocation resourceLocation = Stellaria.id("zregos_minions_death_effect");
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(resourceLocation);
            emitter.setAttachedEntity(entity);
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }
}
