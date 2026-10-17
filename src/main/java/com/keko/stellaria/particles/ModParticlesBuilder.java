package com.keko.stellaria.particles;

import com.keko.stellaria.particles.zregos.WhiteBoomBig;
import com.keko.stellaria.particles.zregos.ZregosStarBurstNormal;
import com.keko.stellaria.particles.zregos.ZregosStarLaser;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticlesBuilder {
    public static void init(){
        ParticleFactoryRegistry.getInstance().register(ModParticles.ZREGOS_STAR_BURST_NORMAL, ZregosStarBurstNormal.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.ZREGOS_STAR_LASER, ZregosStarLaser.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.WHITE_BOOM_BIG, WhiteBoomBig.Factory::new);
    }
}
